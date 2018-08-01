package uk.gov.ons.fwmt.job_service.data.csv_parser;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType;

@Slf4j
public class CSVParserBuilder {
  public static Iterator<CSVParseResult<LegacySampleIngest>> buildLegacySampleParserIterator(Reader reader,
      LegacySampleSurveyType legacySampleSurveyType) throws IOException {
    log.debug("Began parsing file: surveyType={}", legacySampleSurveyType);
    CSVParser parser = getCSVFormat().parse(reader);
    Iterator<CSVParseResult<LegacySampleIngest>> i = new LegacySampleIterator<LegacySampleIngest>(parser, legacySampleSurveyType);
    return i;
  }
  
  private static CSVFormat getCSVFormat() {
    return CSVFormat.DEFAULT.withHeader();
  }

}
