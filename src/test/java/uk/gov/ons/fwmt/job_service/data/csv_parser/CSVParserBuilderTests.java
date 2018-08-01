package uk.gov.ons.fwmt.job_service.data.csv_parser;

import static org.junit.Assert.assertNotNull;
import static uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType.GFF;
import static uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType.LFS;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.gov.ons.fwmt.job_service.rest.client.FieldPeriodResourceServiceClient;

@RunWith(MockitoJUnitRunner.class)
public class CSVParserBuilderTests {
  @Mock private FieldPeriodResourceServiceClient fieldPeriodResourceServiceClient;

  @Test
  public void parseGFFLegacySample() throws IOException {
    //Given
    File testFile = new File("src/test/resources/sampledata/unit_tests/sample_GFF_2018-05-17T15-34-00Z.csv");
    Reader reader = new InputStreamReader(new FileInputStream(testFile));

    //When
    //Then
    assertNotNull(CSVParserBuilder.buildLegacySampleParserIterator(reader, GFF, fieldPeriodResourceServiceClient));
  }

  @Test
  public void parseLFSLegacySample() throws IOException {
    //Given
    File testFile = new File("src/test/resources/sampledata/unit_tests/sample_LFS_2018-05-17T15-34-00Z.csv");
    Reader reader = new InputStreamReader(new FileInputStream(testFile));

    //When
    //Then
    assertNotNull(CSVParserBuilder.buildLegacySampleParserIterator(reader, LFS, fieldPeriodResourceServiceClient));
  }

}