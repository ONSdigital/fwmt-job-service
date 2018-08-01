package uk.gov.ons.fwmt.job_service.data.csv_parser.legacysample;

import static org.junit.Assert.assertEquals;
import static uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType.GFF;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.NoSuchElementException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleGFFDataIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service.utilities.TestIngestBuilder;

@RunWith(MockitoJUnitRunner.class)
public class LegacySampleAnnotationProcessorTests {
  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void setFromCSVColumnAnnotations() throws IOException, FWMTCommonException {
    //Given
    File testFile = new File("src/test/resources/sampledata/unit_tests/sample_GFF_2018-05-17T15-34-00Z.csv");
    Reader reader = new InputStreamReader(new FileInputStream(testFile));

    CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

    LegacySampleIngest testIngestData = new LegacySampleIngest();

    //When
    LegacySampleAnnotationProcessor.process(testIngestData, csvParser.iterator().next(), "GFF");

    //Then
    assertEquals("tla_1", testIngestData.getTla());
    assertEquals("testLcfIncentive1", testIngestData.getLcfIncentive());
  }

  @Test(expected = NoSuchElementException.class)
  public void inputCSVHasNoRecords() throws IOException, FWMTCommonException {
    //Given
    File testFile = new File("src/test/resources/sampledata/unit_tests/headersOnly.csv");
    Reader reader = new InputStreamReader(new FileInputStream(testFile));

    CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

    LegacySampleIngest testIngestData = new TestIngestBuilder().ingestBuild();
    testIngestData.setGffData(new LegacySampleGFFDataIngest());
    testIngestData.setLegacySampleSurveyType(GFF);

    //When
    LegacySampleAnnotationProcessor.process(testIngestData, csvParser.iterator().next(), "GFF");
  }

  @Test
  public void notAValidPivot() throws IOException, FWMTCommonException {
    //Given
    File testFile = new File("src/test/resources/sampledata/unit_tests/sample_GFF_2018-05-17T15-34-00Z.csv");
    Reader reader = new InputStreamReader(new FileInputStream(testFile));

    CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

    LegacySampleIngest testIngestData = new TestIngestBuilder().ingestBuild();
    testIngestData.setGffData(new LegacySampleGFFDataIngest());
    testIngestData.setLegacySampleSurveyType(GFF);

    expectedException.expect(FWMTCommonException.class);
    expectedException.expectMessage(ExceptionCode.CSV_OTHER.getCode());

    //When
    LegacySampleAnnotationProcessor.process(testIngestData, csvParser.iterator().next(), "hdsjf");
  }

  @Test
  public void missingMandatoryColumnsInCSV() throws IOException, FWMTCommonException {
    //Given
    File testFile = new File("src/test/resources/sampledata/unit_tests/missingColumns.csv");
    Reader reader = new InputStreamReader(new FileInputStream(testFile));

    CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

    LegacySampleIngest testIngestData = new TestIngestBuilder().ingestBuild();
    testIngestData.setGffData(new LegacySampleGFFDataIngest());
    testIngestData.setLegacySampleSurveyType(GFF);

    expectedException.expect(FWMTCommonException.class);
    expectedException.expectMessage(ExceptionCode.CSV_MISSING_COLUMN.getCode());

    //When
    LegacySampleAnnotationProcessor.process(testIngestData, csvParser.iterator().next(), "GFF");
  }

}
