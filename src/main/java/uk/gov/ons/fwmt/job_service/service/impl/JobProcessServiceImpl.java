package uk.gov.ons.fwmt.job_service.service.impl;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateJobHeaderRequestMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.job_service.data.csv_parser.CSVParseResult;
import uk.gov.ons.fwmt.job_service.data.csv_parser.UnprocessedCSVRow;
import uk.gov.ons.fwmt.job_service.data.file_ingest.FileIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;
import uk.gov.ons.fwmt.job_service.exceptions.types.InvalidFileNameException;
import uk.gov.ons.fwmt.job_service.exceptions.types.MediaTypeNotSupportedException;
import uk.gov.ons.fwmt.job_service.rest.JobResourceService;
import uk.gov.ons.fwmt.job_service.rest.UserResourceService;
import uk.gov.ons.fwmt.job_service.rest.dto.JobDto;
import uk.gov.ons.fwmt.job_service.rest.dto.UserDto;
import uk.gov.ons.fwmt.job_service.service.CSVParsingService;
import uk.gov.ons.fwmt.job_service.service.FileIngestService;
import uk.gov.ons.fwmt.job_service.service.JobProcessService;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMJobConverterService;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMService;

import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

@Slf4j
@Service
public class JobProcessServiceImpl implements JobProcessService {


    @Autowired
    private FileIngestService fileIngestService;

    @Autowired
    private CSVParsingService csvParsingService;

    @Autowired
    private UserResourceService userResourceService;

    @Autowired
    private TMJobConverterService tmJobConverterService;

    @Autowired
    private JobResourceService jobResourceService;

    @Autowired
    private TMService tmService;

    protected Optional<UnprocessedCSVRow> sendJobToUser(int row, LegacySampleIngest ingest, UserDto userDto) {
        String authno = userDto.getAuthNo();
        String username = userDto.getTmUsername();
        try {
            if (jobResourceService.existsByTmJobIdAndLastAuthNo(ingest.getTmJobId(), authno)) {
                log.info("Job has been sent previously");
                return Optional.of(new UnprocessedCSVRow(row, "Job has been sent previously"));
            } else if (jobResourceService.existsByTmJobId(ingest.getTmJobId())) {
                log.info("Job is a reallocation");
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
                            log.info("Job is a GFF reissue");
                            // send the job to TM
                            final SendCreateJobRequestMessage request = tmJobConverterService.createReissue(ingest, username);
                            tmService.send(request);
                            // save the job in the database
                            jobResourceService.createJob(new JobDto(ingest.getTmJobId(), ingest.getAuth()));
                        } else {
                            log.info("Job is a new GFF job");
                            // send the job to TM
                            final SendCreateJobRequestMessage request = tmJobConverterService.createJob(ingest, username);
                            tmService.send(request);
                            // save the job in the database
                            jobResourceService.createJob(new JobDto(ingest.getTmJobId(), ingest.getAuth()));
                        }
                        break;
                    case LFS:
                        log.info("Job is a new LFS job");
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
            log.info("Job sent successfully");
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error while sending job", e);
            return Optional.of(new UnprocessedCSVRow(row, e.toString()));
        }
    }


    protected Optional<UserDto> findUser(LegacySampleIngest ingest) {
        log.info("Handling entry with authno: " + ingest.getAuth());
        Optional<UserDto> userDto = userResourceService.findByAuthNo(ingest.getAuth());
        if (userDto.isPresent()) {
            log.info("Found user by authno: " + userDto.toString());
            return userDto;
        }
        userDto = userResourceService.findByAlternateAuthNo(ingest.getAuth());
        if (userDto.isPresent()) {
            log.info("Found user by alternate authno: " + userDto.toString());
            return userDto;
        } else {
            return Optional.empty();
        }
    }


    @Async("processExecutor")
    public void processSampleFile(MultipartFile file)
            throws IOException, InvalidFileNameException, MediaTypeNotSupportedException {

        FileIngest fileIngest = fileIngestService.ingestSampleFile(file);
        Iterator<CSVParseResult<LegacySampleIngest>> csvRowIterator = csvParsingService.parseLegacySample(fileIngest.getReader(), fileIngest.getFilename().getTla());

        //List<UnprocessedCSVRow> unprocessed = new ArrayList<>();
        while (csvRowIterator.hasNext()) {
            CSVParseResult<LegacySampleIngest> row = csvRowIterator.next();
            final LegacySampleIngest ingest = row.getResult();
            final Optional<UserDto> user = findUser(ingest);
            if (!user.isPresent()) {
                log.error(ExceptionCode.FWMT_JOB_SERVICE_0005 + " - User did not exist in the gateway");
                //unprocessed.add(new UnprocessedCSVRow(row.getRow(), "User did not exist in the gateway: " + ingest.getAuth()));
                continue;
            }
            if (!user.get().isActive()) {
                log.error(ExceptionCode.FWMT_JOB_SERVICE_0005 + " - User was not active");
                //unprocessed.add(new UnprocessedCSVRow(row.getRow(), "User was not active: " + ingest.getAuth()));
                continue;
            }
            final Optional<UnprocessedCSVRow> unprocessedCSVRow = sendJobToUser(row.getRow(), ingest, user.get());
            if (unprocessedCSVRow.isPresent()) {
                log.error(ExceptionCode.FWMT_JOB_SERVICE_0004 + " - Job could not be sent");
                //unprocessed.add(unprocessedCSVRow.get());
                continue;
            }
        }
    }
}
