package uk.gov.ons.fwmt.job_service.service;

import uk.gov.ons.fwmt.job_service.data.csv_parser.CSVParseResult;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public interface CSVParsingService {
  Iterator<CSVParseResult<LegacySampleIngest>> parseLegacySample(Reader reader, LegacySampleSurveyType legacySampleSurveyType)
      throws IOException;
}
