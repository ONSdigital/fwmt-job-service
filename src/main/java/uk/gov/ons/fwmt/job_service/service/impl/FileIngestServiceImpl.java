package uk.gov.ons.fwmt.job_service.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.fwmt.job_service.data.csv_parser.CSVParseResult;
import uk.gov.ons.fwmt.job_service.data.csv_parser.UnprocessedCSVRow;
import uk.gov.ons.fwmt.job_service.data.dto.SampleSummaryDTO;
import uk.gov.ons.fwmt.job_service.data.file_ingest.SampleFilenameComponents;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;
import uk.gov.ons.fwmt.job_service.service.CSVParsingService;
import uk.gov.ons.fwmt.job_service.service.FileIngestService;
import uk.gov.ons.fwmt.job_service.utils.SampleFileUtils;

/**
 * This service handles the checking of file attributes, media types and filenames
 * Then, the service hands off control to the CSVParsingService
 */
@Slf4j
@Service
public class FileIngestServiceImpl implements FileIngestService {
  private CSVParsingService csvParsingService;
  
  @Autowired
  public FileIngestServiceImpl(CSVParsingService csvParsingService) {
    this.csvParsingService = csvParsingService;
  }
  
  public SampleSummaryDTO validateSampleFile(File file) throws IOException{
    SampleFilenameComponents filename = SampleFileUtils.buildSampleFilenameComponents(file);
    final Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
    
    Iterator<CSVParseResult<LegacySampleIngest>> csvRowIterator = csvParsingService.parseLegacySample(reader, filename.getTla());
    int parsed = 0;
    List<UnprocessedCSVRow> unprocessed = new ArrayList<>();

    while (csvRowIterator.hasNext()) {
      CSVParseResult<LegacySampleIngest> row = csvRowIterator.next();
      if (row.isError()) {
        log.error(ExceptionCode.UNKNOWN + " - Entry could not be processed");
        unprocessed.add(new UnprocessedCSVRow(row.getRow(), "Row could not be parsed: " + row.getErrorMessage()));
        continue;
      }
      parsed++;
    }

    return new SampleSummaryDTO(file.getName(), parsed, unprocessed);
  }

}