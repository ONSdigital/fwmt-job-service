package uk.gov.ons.fwmt.job_service.service.impl;

import static uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException.JOB_ENTRY_FAILED_STRING;
import static uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException.JOB_FAILED_STRING;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateJobHeaderRequestMessage;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.fwmt.job_service.data.csv_parser.CSVParseResult;
import uk.gov.ons.fwmt.job_service.data.csv_parser.CSVParserBuilder;
import uk.gov.ons.fwmt.job_service.data.file_ingest.SampleFilenameComponents;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service.rest.client.FieldPeriodResourceServiceClient;
import uk.gov.ons.fwmt.job_service.rest.client.JobResourceServiceClient;
import uk.gov.ons.fwmt.job_service.rest.client.UserResourceServiceClient;
import uk.gov.ons.fwmt.job_service.rest.client.dto.JobDto;
import uk.gov.ons.fwmt.job_service.rest.client.dto.UserDto;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMJobConverterService;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMService;
import uk.gov.ons.fwmt.job_service.utils.SampleFileUtils;

@Slf4j
@Component
public class JobProcessor {

  @Autowired
  private UserResourceServiceClient userResourceServiceClient;

  @Autowired
  private TMJobConverterService tmJobConverterService;

  @Autowired
  private JobResourceServiceClient jobResourceServiceClient;

  @Autowired
  private TMService tmService;

  @Autowired
  private FieldPeriodResourceServiceClient fieldPeriodResourceServiceClient;

  @Async("processExecutor")
  public void processSampleFile(File file) throws IOException {
    SampleFilenameComponents filename = SampleFileUtils.buildSampleFilenameComponents(file);
    final Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);

    Iterator<CSVParseResult<LegacySampleIngest>> csvRowIterator = CSVParserBuilder
        .buildLegacySampleParserIterator(reader, filename.getTla(), fieldPeriodResourceServiceClient);

    while (csvRowIterator.hasNext()) {
      CSVParseResult<LegacySampleIngest> row = csvRowIterator.next();
      if (row.isError()) {
        log.error("Job Entry could not be processed", FWMTCommonException.makeCsvOtherException(row.getErrorMessage()));
        continue;
      }

      final LegacySampleIngest ingest = row.getResult();
      boolean isExistingJob = jobResourceServiceClient.existsByTmJobId(ingest.getTmJobId());
      final Optional<UserDto> user = findUser(ingest);

      if (rowIsValid(row, ingest, isExistingJob, user)) {
        log.debug("Processing row: " + row.getRow());
        processRow(row, ingest, isExistingJob, user.get());
      }
    }
  }

  protected boolean rowIsValid(CSVParseResult<LegacySampleIngest> row, LegacySampleIngest ingest, boolean isExistingJob,
      Optional<UserDto> user) {
    //Don't change the jobtype string, this string is used in splunk report. if changing change splunk search query as well.
    String jobType = findJobType(isExistingJob);

    if (!user.isPresent()) {
      log.error(jobType + JOB_FAILED_STRING, ingest.getTmJobId(),
          FWMTCommonException.makeUnknownUserIdException(ingest.getAuth()));
      return false;
    }

    if (!user.get().isActive()) {
      log.error(jobType + JOB_FAILED_STRING, ingest.getTmJobId(),
          FWMTCommonException.makeBadUserStateException(user.get(), "User was inactive"));
      return false;
    }

    return true;
  }

  protected String findJobType(boolean isExistingJob) {
    return isExistingJob ? "Reallocation" : "Allocation";
  }

  protected void processRow(CSVParseResult<LegacySampleIngest> row, LegacySampleIngest ingest, boolean isReallocation,
      UserDto user) {
    try {
      Optional<JobDto> oJob = jobResourceServiceClient.findByTmJobId(ingest.getTmJobId());
      if (oJob.isPresent()) {
        JobDto jobDto = oJob.get();
        if (ingestIsLatestTransaction(ingest, jobDto)) {
          if (isUsersTheSame(ingest, jobDto)) {
            updateLegacyLastUpdated(ingest, jobDto);
          } else {
            sendJobToUser(row.getRow(), ingest, user, true);
          }
        } else {
          log.warn("Not latest transaction");
        }
      } else {
        sendJobToUser(row.getRow(), ingest, user, isReallocation);
      }
    } catch (Exception e) {
      log.error(findJobType(isReallocation) + JOB_FAILED_STRING, ingest.getTmJobId(), ExceptionCode.UNKNOWN.toString(),
          FWMTCommonException.makeUnknownException(e));
    }
  }

  protected boolean isUsersTheSame(LegacySampleIngest ingest, JobDto jobDto) {
    return ingest.getAuth().equals(jobDto.getLastAuthNo());
  }

  protected boolean ingestIsLatestTransaction(LegacySampleIngest ingest, JobDto jobDto) {
    if (jobDto.getLastUpdated() == null)
      return true;
    LocalDateTime ingestDateTime = getIngestLastUpdateAsLocalDateTime(ingest);
    return ingestDateTime.isAfter(jobDto.getLastUpdated());
  }

  protected void updateLegacyLastUpdated(LegacySampleIngest ingest, JobDto jobDto) {
    LocalDateTime ingestDateTime = getIngestLastUpdateAsLocalDateTime(ingest);
    jobDto.setLastUpdated(ingestDateTime);
    jobResourceServiceClient.updateJob(jobDto);
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

  protected void sendJobToUser(int row, LegacySampleIngest ingest, UserDto userDto, boolean isReallocation) {
    if (jobResourceServiceClient.existsByTmJobIdAndLastAuthNo(ingest.getTmJobId(), userDto.getAuthNo())) {
      log.error(JOB_ENTRY_FAILED_STRING, ingest.getTmJobId(), "Job has been sent previously");
      return;
    }

    if (isReallocation) {
      processReallocation(ingest, userDto);
    } else {
      processBySurveyType(ingest, userDto, row);
    }
  }

  protected void processReallocation(LegacySampleIngest ingest, UserDto userDto) {
    final SendUpdateJobHeaderRequestMessage request = tmJobConverterService.updateJob(ingest, userDto.getTmUsername());
    log.info("Reallocating job with ID {} to user {}", ingest.getTmJobId(), userDto.toString());
    tmService.send(request);
    final Optional<JobDto> jobDto = jobResourceServiceClient.findByTmJobId(ingest.getTmJobId());

    jobDto.ifPresent(jobDto1 -> {
      LocalDateTime lastUpdateParsed = getIngestLastUpdateAsLocalDateTime(ingest);
      jobDto1.setLastUpdated(lastUpdateParsed);
      jobDto1.setLastAuthNo(ingest.getAuth());
      jobResourceServiceClient.updateJob(jobDto1);
    });
  }

  protected LocalDateTime getIngestLastUpdateAsLocalDateTime(LegacySampleIngest ingest) {
    LocalDateTime lastUpdateParsed = null;

    String lastUpdate = ingest.getLastUpdated().replace(" ", "T");
    lastUpdateParsed = LocalDateTime.parse(lastUpdate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    return lastUpdateParsed;
  }

  protected void processBySurveyType(LegacySampleIngest ingest, UserDto userDto, int row) {
    try {
      switch (ingest.getLegacySampleSurveyType()) {
      case GFF:
        processGFFSample(ingest, userDto);
        break;
      case LFS:
        processLFSSample(ingest, userDto);
        break;
      default:
        throw new IllegalArgumentException("Unknown survey type");
      }
    } catch (Exception e) {
      log.error(JOB_ENTRY_FAILED_STRING, ingest.getTmJobId(), "Last updated column cannot be parsed");
      throw e;
    }
  }

  protected void processLFSSample(LegacySampleIngest ingest, UserDto userDto) {
    LocalDateTime lastUpdateParsed = getIngestLastUpdateAsLocalDateTime(ingest);
    final SendCreateJobRequestMessage request = tmJobConverterService.createJob(ingest, userDto.getTmUsername());
    log.info("Creating LFS job with ID {} to user {}", ingest.getTmJobId(), userDto.toString());
    tmService.send(request);
    jobResourceServiceClient.createJob(new JobDto(ingest.getTmJobId(), ingest.getAuth(), lastUpdateParsed));
  }

  protected void processGFFSample(LegacySampleIngest ingest, UserDto userDto) {
    LocalDateTime lastUpdateParsed = getIngestLastUpdateAsLocalDateTime(ingest);
    SendCreateJobRequestMessage request = null;
    if (ingest.isGffReissue()) {
      request = tmJobConverterService.createReissue(ingest, userDto.getTmUsername());
      log.info("Reissuing GFF job with ID {} to user {}", ingest.getTmJobId(), userDto.toString());
    } else {
      request = tmJobConverterService.createJob(ingest, userDto.getTmUsername());
      log.info("Creating GFF job with ID {} to user {}", ingest.getTmJobId(), userDto.toString());
    }
    tmService.send(request);
    jobResourceServiceClient.createJob(new JobDto(ingest.getTmJobId(), ingest.getAuth(), lastUpdateParsed));
  }

}
