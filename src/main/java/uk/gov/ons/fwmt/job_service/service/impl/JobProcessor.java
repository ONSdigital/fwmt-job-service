package uk.gov.ons.fwmt.job_service.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateJobHeaderRequestMessage;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.fwmt.job_service.data.csv_parser.CSVParseResult;
import uk.gov.ons.fwmt.job_service.data.csv_parser.UnprocessedCSVRow;
import uk.gov.ons.fwmt.job_service.data.file_ingest.SampleFilenameComponents;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service.rest.client.JobResourceServiceClient;
import uk.gov.ons.fwmt.job_service.rest.client.UserResourceServiceClient;
import uk.gov.ons.fwmt.job_service.rest.client.dto.JobDto;
import uk.gov.ons.fwmt.job_service.rest.client.dto.UserDto;
import uk.gov.ons.fwmt.job_service.service.CSVParsingService;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMJobConverterService;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMService;
import uk.gov.ons.fwmt.job_service.utils.SampleFileUtils;

@Slf4j
@Component
public class JobProcessor { 

  @Autowired
  private CSVParsingService csvParsingService;

  @Autowired
  private UserResourceServiceClient userResourceServiceClient;

  @Autowired
  private TMJobConverterService tmJobConverterService;

  @Autowired
  private JobResourceServiceClient jobResourceServiceClient;

  @Autowired
  private TMService tmService;
  
  public JobProcessor(CSVParsingService csvParsingService,
      UserResourceServiceClient userResourceServiceClient,
      TMJobConverterService tmJobConverterService,
      JobResourceServiceClient jobResourceServiceClient,
      TMService tmService
      ){
        this.csvParsingService = csvParsingService;
        this.userResourceServiceClient = userResourceServiceClient;
        this.tmJobConverterService = tmJobConverterService;
        this.jobResourceServiceClient = jobResourceServiceClient;
        this.tmService = tmService;
  }
  
  @Async("processExecutor")
  public void processSampleFile(File file)
      throws IOException{
    
    SampleFilenameComponents filename = SampleFileUtils.buildSampleFilenameComponents(file);
    final Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);

    Iterator<CSVParseResult<LegacySampleIngest>> csvRowIterator = csvParsingService
     .parseLegacySample(reader, filename.getTla());
    
    while (csvRowIterator.hasNext()) {
      CSVParseResult<LegacySampleIngest> row = csvRowIterator.next();
      if (row.isError()) {
        log.error("Entry could not be processed", FWMTCommonException.makeCsvOtherException(row.getErrorMessage()));
        continue;
      }
      final LegacySampleIngest ingest = row.getResult();
      final Optional<UserDto> user = findUser(ingest);

      boolean isReallocation = jobResourceServiceClient.existsByTmJobId(ingest.getTmJobId());
      String jobType = isReallocation ? "Reallocation" : "Allocation";
      if (!user.isPresent()) {
        log.error(jobType + " could not be processed", FWMTCommonException.makeUnknownUserIdException(ingest.getAuth()));        
        continue;
      }
      
      if (!user.get().isActive()) {
        log.error(jobType + " could not be processed", FWMTCommonException.makeBadUserStateException(user.get(), "User was inactive"));
        continue;
      }
      try {
        final Optional<UnprocessedCSVRow> unprocessedCSVRow = sendJobToUser(row.getRow(), ingest, user.get(),
            isReallocation);
        unprocessedCSVRow.ifPresent(unprocessedCSVRow1 -> log.error("Entry could not be processed",
            FWMTCommonException.makeCsvOtherException(unprocessedCSVRow1.getMessage())));
      } catch (Exception e) {
        log.error(jobType + " could not be processed", FWMTCommonException.makeUnknownException(e));
      }
    }
  }

  protected Optional<UnprocessedCSVRow> sendJobToUser(int row, LegacySampleIngest ingest, UserDto userDto,
      boolean isReallocation) {
    String authNo = userDto.getAuthNo();
    String username = userDto.getTmUsername();
    if (jobResourceServiceClient.existsByTmJobIdAndLastAuthNo(ingest.getTmJobId(), authNo)) {
      return Optional.of(new UnprocessedCSVRow(row, "Job has been sent previously"));
    } else if (isReallocation) {
      final SendUpdateJobHeaderRequestMessage request = tmJobConverterService.updateJob(ingest, username);
      log.info("Reallocating job with ID {} to user {}", ingest.getTmJobId(), userDto.toString());
      // TODO add error handling
      tmService.send(request);
      final Optional<JobDto> jobDto = jobResourceServiceClient.findByTmJobId(ingest.getTmJobId());
      jobDto.ifPresent(jobDto1 -> {
        jobDto1.setLastAuthNo(ingest.getAuth());
        jobResourceServiceClient.updateJob(jobDto1);
      });
    } else {
      switch (ingest.getLegacySampleSurveyType()) {
      case GFF:
        if (ingest.isGffReissue()) {
          final SendCreateJobRequestMessage request = tmJobConverterService.createReissue(ingest, username);
          log.info("Reissuing GFF job with ID {} to user {}", ingest.getTmJobId(), userDto.toString());
          tmService.send(request);
          jobResourceServiceClient.createJob(new JobDto(ingest.getTmJobId(), ingest.getAuth()));
        } else {
          final SendCreateJobRequestMessage request = tmJobConverterService.createJob(ingest, username);
          log.info("Sending GFF job with ID {} to user {}", ingest.getTmJobId(), userDto.toString());
          tmService.send(request);
          jobResourceServiceClient.createJob(new JobDto(ingest.getTmJobId(), ingest.getAuth()));
        }
        break;
      case LFS:
        final SendCreateJobRequestMessage request = tmJobConverterService.createJob(ingest, username);
        log.info("Sending LFS job with ID {} to user {}", ingest.getTmJobId(), userDto.toString());
        tmService.send(request);
        jobResourceServiceClient.createJob(new JobDto(ingest.getTmJobId(), ingest.getAuth()));
        break;
      default:
        throw new IllegalArgumentException("Unknown survey type");
      }
    }
    return Optional.empty();
  }

  protected Optional<UserDto> findUser(LegacySampleIngest ingest) {
    log.debug("Handling entry with authno: " + ingest.getAuth());
    Optional<UserDto> userDto = userResourceServiceClient.findByAuthNo(ingest.getAuth());
    if (userDto.isPresent()) {
      log.debug("Found user by authno: " + userDto.toString());
      return userDto;
    }
    userDto = userResourceServiceClient.findByAlternateAuthNo(ingest.getAuth());
    if (userDto.isPresent()) {
      log.debug("Found user by alternate authno: " + userDto.toString());
      return userDto;
    } else {
      return Optional.empty();
    }
  }
}
