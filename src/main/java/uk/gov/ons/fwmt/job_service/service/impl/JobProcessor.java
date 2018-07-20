package uk.gov.ons.fwmt.job_service.service.impl;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateJobHeaderRequestMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.job_service.data.csv_parser.CSVParseResult;
import uk.gov.ons.fwmt.job_service.data.csv_parser.UnprocessedCSVRow;
import uk.gov.ons.fwmt.job_service.data.file_ingest.FileIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service.rest.JobResourceService;
import uk.gov.ons.fwmt.job_service.rest.UserResourceService;
import uk.gov.ons.fwmt.job_service.rest.dto.JobDto;
import uk.gov.ons.fwmt.job_service.rest.dto.UserDto;
import uk.gov.ons.fwmt.job_service.service.CSVParsingService;
import uk.gov.ons.fwmt.job_service.service.FileIngestService;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMJobConverterService;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Optional;

@Slf4j
@Component
public class JobProcessor {

  private FileIngestService fileIngestService;

  private CSVParsingService csvParsingService;

  private UserResourceService userResourceService;

  private TMJobConverterService tmJobConverterService;

  private JobResourceService jobResourceService;

  private TMService tmService;

  @Autowired
  public JobProcessor(FileIngestService fileIngestService,
      CSVParsingService csvParsingService,
      UserResourceService userResourceService,
      TMJobConverterService tmJobConverterService,
      JobResourceService jobResourceService,
      TMService tmService
  ) {
    this.fileIngestService = fileIngestService;
    this.csvParsingService = csvParsingService;
    this.userResourceService = userResourceService;
    this.tmJobConverterService = tmJobConverterService;
    this.jobResourceService = jobResourceService;
    this.tmService = tmService;
  }

  @Async("processExecutor")
  public void processSampleFile(File file)
      throws IOException {

    FileIngest fileIngest = fileIngestService.ingestSampleFile(file);
    Iterator<CSVParseResult<LegacySampleIngest>> csvRowIterator = csvParsingService
        .parseLegacySample(fileIngest.getReader(), fileIngest.getFilename().getTla());

    while (csvRowIterator.hasNext()) {
      CSVParseResult<LegacySampleIngest> row = csvRowIterator.next();

      if (row.isError()) {
        log.error("Entry could not be processed", FWMTCommonException.makeCsvOtherException(row.getErrorMessage()));
        continue;
      }

      final LegacySampleIngest ingest = row.getResult();
      final Optional<UserDto> user = userResourceService.findByEitherAuthNo(ingest.getAuth());

      boolean isReallocation = jobResourceService.existsByTmJobId(ingest.getTmJobId());
      String jobType = isReallocation ? "Reallocation" : "Allocation";

      if (!user.isPresent()) {
        log.error(jobType + " could not be processed",
            FWMTCommonException.makeUnknownUserIdException(ingest.getAuth()));
        continue;
      }

      if (!user.get().isActive()) {
        log.error(jobType + " could not be processed",
            FWMTCommonException.makeBadUserStateException(user.get(), "User was inactive"));
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

  protected Optional<UnprocessedCSVRow> sendJobToUser(int row, LegacySampleIngest ingest, UserDto userDto) {
    String authno = userDto.getAuthNo();
    String username = userDto.getTmUsername();
    try {
      if (jobResourceService.existsByTmJobIdAndLastAuthNo(ingest.getTmJobId(), authno)) {
        return Optional.of(new UnprocessedCSVRow(row, "Job has been sent previously"));
      } else if (jobResourceService.existsByTmJobId(ingest.getTmJobId())) {
        final SendUpdateJobHeaderRequestMessage request = tmJobConverterService.updateJob(ingest, username);
          log.info("Reallocating job with ID {} to user {}", ingest.getTmJobId(), userDto.toString());
        // TODO add error handling
        tmService.send(request);
        final Optional<JobDto> jobDto = jobResourceService.findByTmJobId(ingest.getTmJobId());
        jobDto.ifPresent(jobDto1 -> {
          jobDto1.setLastAuthNo(ingest.getAuth());
          jobResourceService.updateJob(jobDto1);
        });

      } else {
        switch (ingest.getLegacySampleSurveyType()) {
        case GFF:
          if (ingest.isGffReissue()) {
            final SendCreateJobRequestMessage request = tmJobConverterService.createReissue(ingest, username);
              log.info("Reissuing GFF job with ID {} to user {}", ingest.getTmJobId(), userDto.toString());
            tmService.send(request);
            jobResourceService.createJob(new JobDto(ingest.getTmJobId(), ingest.getAuth(),LocalDateTime.parse(ingest.getLastUpdated(),DateTimeFormatter.ISO_LOCAL_DATE_TIME)));));
          } else {
            final SendCreateJobRequestMessage request = tmJobConverterService.createJob(ingest, username);
            tmService.send(request);
            jobResourceService.createJob(new JobDto(ingest.getTmJobId(), ingest.getAuth(),LocalDateTime.parse(ingest.getLastUpdated(),DateTimeFormatter.ISO_LOCAL_DATE_TIME)));));
          }
          break;
        case LFS:
          final SendCreateJobRequestMessage request = tmJobConverterService.createJob(ingest, username);
          log.info("Reissuing GFF job with ID {} to user {}", ingest.getTmJobId(), userDto.toString());
          tmService.send(request);
          jobResourceService.createJob(new JobDto(ingest.getTmJobId(), ingest.getAuth(),LocalDateTime.parse(ingest.getLastUpdated(),DateTimeFormatter.ISO_LOCAL_DATE_TIME)));));
          break;
        default:
          throw new IllegalArgumentException("Unknown survey type");
        }
      }
    }
    return Optional.empty();
  }
}
