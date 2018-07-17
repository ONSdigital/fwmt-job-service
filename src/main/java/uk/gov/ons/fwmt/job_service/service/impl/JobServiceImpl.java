package uk.gov.ons.fwmt.job_service.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.fwmt.job_service.data.csv_parser.CSVParseResult;
import uk.gov.ons.fwmt.job_service.data.csv_parser.UnprocessedCSVRow;
import uk.gov.ons.fwmt.job_service.data.dto.SampleSummaryDTO;
import uk.gov.ons.fwmt.job_service.data.file_ingest.FileIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;
import uk.gov.ons.fwmt.job_service.exceptions.types.InvalidFileNameException;
import uk.gov.ons.fwmt.job_service.exceptions.types.MediaTypeNotSupportedException;
import uk.gov.ons.fwmt.job_service.rest.JobResourceService;
import uk.gov.ons.fwmt.job_service.service.CSVParsingService;
import uk.gov.ons.fwmt.job_service.service.FileIngestService;
import uk.gov.ons.fwmt.job_service.service.JobService;

@Slf4j
@Service
public class JobServiceImpl implements JobService {
  private FileIngestService fileIngestService;
  private CSVParsingService csvParsingService;
  private JobResourceService jobResourceService;
  private JobProcessor jobProcessor;

  @Autowired
  public JobServiceImpl(
      FileIngestService fileIngestService,
      CSVParsingService csvParsingService,
      JobProcessor jobProcessService,
      JobResourceService jobResourceService) {
    this.fileIngestService = fileIngestService;
    this.csvParsingService = csvParsingService;
    this.jobResourceService = jobResourceService;
    this.jobProcessor = jobProcessService;
  }

  @Override
  public SampleSummaryDTO processSampleFile(MultipartFile file)
          throws IOException, InvalidFileNameException, MediaTypeNotSupportedException{
    File f = convertFile(file);
    SampleSummaryDTO sampleSummaryDTO = validateSampleFile(f);
    boolean valid = sampleSummaryDTO.getUnprocessedRows().isEmpty();
    jobResourceService.sendCSV(file, valid);

    // This is an async call
    jobProcessor.processSampleFile(f);
    return sampleSummaryDTO;
  }
  
  private SampleSummaryDTO validateSampleFile(File file) throws InvalidFileNameException, MediaTypeNotSupportedException, IOException{
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

    return new SampleSummaryDTO(file.getName(), parsed, unprocessed);
  }

  private File convertFile(MultipartFile file) throws IOException{
    File convFile = new File (file.getOriginalFilename());
    FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(convFile));
    return convFile;
  }
}
