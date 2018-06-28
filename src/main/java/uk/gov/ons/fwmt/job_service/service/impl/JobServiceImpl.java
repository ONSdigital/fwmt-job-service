package uk.gov.ons.fwmt.job_service.service.impl;

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
import uk.gov.ons.fwmt.job_service.service.CSVParsingService;
import uk.gov.ons.fwmt.job_service.service.FileIngestService;
import uk.gov.ons.fwmt.job_service.service.JobService;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMJobConverterService;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
  private JobProcessServiceImpl jobProcessService;

  @Autowired
  public JobServiceImpl(
      FileIngestService fileIngestService,
      CSVParsingService csvParsingService,
      TMJobConverterService tmJobConverterService,
      TMService tmService,
      UserResourceService userResourceService,
      JobResourceService jobResourceService,
      FieldPeriodResourceService fieldPeriodResourceService,
      JobProcessServiceImpl jobProcessService) {
    this.fileIngestService = fileIngestService;
    this.csvParsingService = csvParsingService;
    this.tmJobConverterService = tmJobConverterService;
    this.tmService = tmService;
    this.userResourceService = userResourceService;
    this.jobResourceService = jobResourceService;
    this.fieldPeriodResourceService = fieldPeriodResourceService;
    this.jobProcessService = jobProcessService;
  }

  @Override
  public SampleSummaryDTO validateSampleFile(MultipartFile file)
          throws IOException, InvalidFileNameException, MediaTypeNotSupportedException{
    FileIngest fileIngest = fileIngestService.ingestSampleFile(file);
    Iterator<CSVParseResult<LegacySampleIngest>> csvRowIterator = csvParsingService.parseLegacySample(fileIngest.getReader(), fileIngest.getFilename().getTla());

    int parsed = 0;
    List<UnprocessedCSVRow> unprocessed = new ArrayList<>();


    while (csvRowIterator.hasNext()) {
      CSVParseResult<LegacySampleIngest> row = csvRowIterator.next();
      if (row.isError()) {
        log.error(ExceptionCode.FWMT_JOB_SERVICE_0001 + " - Entry could not be processed");
        unprocessed.add(new UnprocessedCSVRow(row.getRow(), "Row could not be parsed: " + row.getErrorMessage()));
        continue;
      }
      parsed++;
    }

    jobProcessService.processSampleFile(file);

    System.out.println("sending message");

    // construct reply
    return new SampleSummaryDTO(file.getOriginalFilename(), parsed, unprocessed);
  }

}
