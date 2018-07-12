package uk.gov.ons.fwmt.job_service.service.impl;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateJobHeaderRequestMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.job_service.data.csv_parser.CSVParseResult;
import uk.gov.ons.fwmt.job_service.data.csv_parser.UnprocessedCSVRow;
import uk.gov.ons.fwmt.job_service.data.dto.SampleSummaryDTO;
import uk.gov.ons.fwmt.job_service.data.file_ingest.FileIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;
import uk.gov.ons.fwmt.job_service.exceptions.types.InvalidFileNameException;
import uk.gov.ons.fwmt.job_service.exceptions.types.MediaTypeNotSupportedException;
import uk.gov.ons.fwmt.job_service.rest.FieldPeriodResourceService;
import uk.gov.ons.fwmt.job_service.rest.JobResourceService;
import uk.gov.ons.fwmt.job_service.rest.UserResourceService;
import uk.gov.ons.fwmt.job_service.rest.dto.JobDto;
import uk.gov.ons.fwmt.job_service.rest.dto.UserDto;
import uk.gov.ons.fwmt.job_service.service.CSVParsingService;
import uk.gov.ons.fwmt.job_service.service.FileIngestService;
import uk.gov.ons.fwmt.job_service.service.JobService;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMJobConverterService;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class JobServiceImpl implements JobService {
  private FileIngestService fileIngestService;
  private CSVParsingService csvParsingService;
  private TMJobConverterService tmJobConverterService;
  private TMService tmService;
  private UserResourceService userResourceService;
  private JobResourceService jobResourceService;
  private FieldPeriodResourceService fieldPeriodResourceService;

  @Autowired
  public JobServiceImpl(
      FileIngestService fileIngestService,
      CSVParsingService csvParsingService,
      TMJobConverterService tmJobConverterService,
      TMService tmService,
      UserResourceService userResourceService,
      JobResourceService jobResourceService,
      FieldPeriodResourceService fieldPeriodResourceService) {
    this.fileIngestService = fileIngestService;
    this.csvParsingService = csvParsingService;
    this.tmJobConverterService = tmJobConverterService;
    this.tmService = tmService;
    this.userResourceService = userResourceService;
    this.jobResourceService = jobResourceService;
    this.fieldPeriodResourceService = fieldPeriodResourceService;
  }

  protected Optional<UnprocessedCSVRow> sendJobToUser(int row, LegacySampleIngest ingest, UserDto userDto) {
    log.debug("sendJobToUser: handling job {} for user {}", ingest.getTmJobId(), userDto.getTmUsername());
    String authno = userDto.getAuthNo();
    String username = userDto.getTmUsername();
    try {
      if (jobResourceService.existsByTmJobIdAndLastAuthNo(ingest.getTmJobId(), authno)) {
        log.debug("Job has been sent previously");
        return Optional.of(new UnprocessedCSVRow(row, "Job has been sent previously"));
      } else if (jobResourceService.existsByTmJobId(ingest.getTmJobId())) {
        log.debug("Job is a reallocation");
        final SendUpdateJobHeaderRequestMessage request = tmJobConverterService.updateJob(ingest, username);
        // TODO add error handling
        tmService.send(request);
        // update the last auth no in the database
        final Optional<JobDto> jobDto = jobResourceService.findByTmJobId(ingest.getTmJobId());
        jobDto.ifPresent(jobDto1 -> {
          jobDto1.setLastAuthNo(ingest.getAuth());
          jobResourceService.updateJob(jobDto1);
        });

      } else {
        switch (ingest.getLegacySampleSurveyType()) {
        case GFF:
          if (ingest.isGffReissue()) {
            log.debug("Job is a GFF reissue");
            // send the job to TM
            final SendCreateJobRequestMessage request = tmJobConverterService.createReissue(ingest, username);
            tmService.send(request);
            // save the job in the database
            jobResourceService.createJob(new JobDto(ingest.getTmJobId(), ingest.getAuth()));
          } else {
            log.debug("Job is a new GFF job");
            // send the job to TM
            final SendCreateJobRequestMessage request = tmJobConverterService.createJob(ingest, username);
            tmService.send(request);
            // save the job in the database
            jobResourceService.createJob(new JobDto(ingest.getTmJobId(), ingest.getAuth()));
          }
          break;
        case LFS:
          log.debug("Job is a new LFS job");
          // send the job to TM
          final SendCreateJobRequestMessage request = tmJobConverterService.createJob(ingest, username);
          tmService.send(request);
          // save the job in the database
          jobResourceService.createJob(new JobDto(ingest.getTmJobId(), ingest.getAuth()));
          break;
        default:
          throw new IllegalArgumentException("Unknown survey type");
        }
      }
      log.debug("Job sent successfully");
      return Optional.empty();
    } catch (Exception e) {
      log.error("Error while sending job", e);
      return Optional.of(new UnprocessedCSVRow(row, e.toString()));
    }
  }

  protected Optional<UserDto> findUser(LegacySampleIngest ingest) {
    log.debug("Handling entry with authno: " + ingest.getAuth());
    Optional<UserDto> userDto = userResourceService.findByAuthNo(ingest.getAuth());
    if (userDto.isPresent()) {
      log.debug("Found user by authno: " + userDto.toString());
      return userDto;
    }
    userDto = userResourceService.findByAlternateAuthNo(ingest.getAuth());
    if (userDto.isPresent()) {
      log.debug("Found user by alternate authno: " + userDto.toString());
      return userDto;
    } else {
      return Optional.empty();
    }
  }

  @Override
  public SampleSummaryDTO processSampleFile(MultipartFile file)
      throws IOException, InvalidFileNameException, MediaTypeNotSupportedException {
    log.debug("processSampleFile: handling file with name '{}'", file.getOriginalFilename());
    FileIngest fileIngest = fileIngestService.ingestSampleFile(file);
    Iterator<CSVParseResult<LegacySampleIngest>> csvRowIterator = csvParsingService
        .parseLegacySample(fileIngest.getReader(), fileIngest.getFilename().getTla());

    int parsed = 0;
    List<UnprocessedCSVRow> unprocessed = new ArrayList<>();

    while (csvRowIterator.hasNext()) {
      CSVParseResult<LegacySampleIngest> row = csvRowIterator.next();
      if (row.isError()) {
        String message = String.format("Row could not be parsed: row=%d, message=%s", row.getRow(), row.getErrorMessage());
        log.error(ExceptionCode.FWMT_JOB_SERVICE_0001 + " - " + message);
        unprocessed.add(new UnprocessedCSVRow(row.getRow(), message));
        continue;
      }
      final LegacySampleIngest ingest = row.getResult();
      final Optional<UserDto> user = findUser(ingest);
      if (!user.isPresent()) {
        String message = String.format("User did not exist in the gateway: authno=%s", ingest.getAuth());
        log.error(ExceptionCode.FWMT_JOB_SERVICE_0005 + " - " + message);
        unprocessed.add(new UnprocessedCSVRow(row.getRow(), message));
        continue;
      }
      if (!user.get().isActive()) {
        String message = String
            .format("User was not active: auth=%s, username=%s", ingest.getAuth(), user.get().getTmUsername());
        log.error(ExceptionCode.FWMT_JOB_SERVICE_0005 + " - " + message);
        unprocessed.add(new UnprocessedCSVRow(row.getRow(), message));
        continue;
      }
      final Optional<UnprocessedCSVRow> unprocessedCSVRow = sendJobToUser(row.getRow(), ingest, user.get());
      if (unprocessedCSVRow.isPresent()) {
        String message = String.format("Job could not be sent: tmJobId=%s", ingest.getTmJobId());
        log.error(ExceptionCode.FWMT_JOB_SERVICE_0004 + " - " + message);
        unprocessed.add(unprocessedCSVRow.get());
        continue;
      }
      parsed++;
    }

    // construct reply
    return new SampleSummaryDTO(file.getOriginalFilename(), parsed, unprocessed);
  }

}
