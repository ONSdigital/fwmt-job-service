package uk.gov.ons.fwmt.job_service.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleGFFDataIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.utilities.TestIngestBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import static uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType.GFF;
import static uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType.LFS;

@RunWith(MockitoJUnitRunner.class)
public class CSVParsingServiceImplTest {

  @InjectMocks CSVParsingServiceImpl csvParsingServiceImpl;

  @Test
  public void setFromCSVColumnAnnotations() throws IOException {
    //Given
    File testFile = new File("src/test/resources/sampledata/unit_tests/sample_GFF_2018-05-17T15-34-00Z.csv");
    Reader reader = new InputStreamReader(new FileInputStream(testFile));

    CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

    LegacySampleIngest testIngestData = new TestIngestBuilder().ingestBuild();
    testIngestData.setGffData(new LegacySampleGFFDataIngest());
    testIngestData.setLegacySampleSurveyType(GFF);

    //When
    csvParsingServiceImpl.setFromCSVColumnAnnotations(testIngestData, csvParser.iterator().next(),"GFF");
  }

  @Test(expected = NoSuchElementException.class)
  public void inputCSVHasNoRecords() throws IOException {
    //Given
    File testFile = new File("src/test/resources/sampledata/unit_tests/headersOnly.csv");
    Reader reader = new InputStreamReader(new FileInputStream(testFile));

    CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

    LegacySampleIngest testIngestData = new TestIngestBuilder().ingestBuild();
    testIngestData.setGffData(new LegacySampleGFFDataIngest());
    testIngestData.setLegacySampleSurveyType(GFF);

    //When
    csvParsingServiceImpl.setFromCSVColumnAnnotations(testIngestData, csvParser.iterator().next(),"GFF");
  }

  @Test(expected = IllegalArgumentException.class)
  public void notAValidPivot() throws IOException {
    //Given
    File testFile = new File("src/test/resources/sampledata/unit_tests/sample_GFF_2018-05-17T15-34-00Z.csv");
    Reader reader = new InputStreamReader(new FileInputStream(testFile));

    CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

    LegacySampleIngest testIngestData = new TestIngestBuilder().ingestBuild();
    testIngestData.setGffData(new LegacySampleGFFDataIngest());
    testIngestData.setLegacySampleSurveyType(GFF);

    //When
    csvParsingServiceImpl.setFromCSVColumnAnnotations(testIngestData, csvParser.iterator().next(),"hdsjf");
  }

  @Test(expected = IllegalArgumentException.class)
  public void missingMandatoryColumnsInCSV() throws IOException {
    //Given
    File testFile = new File("src/test/resources/sampledata/unit_tests/missingColumns.csv");
    Reader reader = new InputStreamReader(new FileInputStream(testFile));

    CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

    LegacySampleIngest testIngestData = new TestIngestBuilder().ingestBuild();
    testIngestData.setGffData(new LegacySampleGFFDataIngest());
    testIngestData.setLegacySampleSurveyType(GFF);

    //When
    csvParsingServiceImpl.setFromCSVColumnAnnotations(testIngestData, csvParser.iterator().next(),"GFF");
  }


  @Test
  public void constructTmGFFJobId() throws IOException {
    //Given
    File testFile = new File("src/test/resources/sampledata/unit_tests/sample_GFF_2018-05-17T15-34-00Z.csv");
    Reader reader = new InputStreamReader(new FileInputStream(testFile));

    CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

    //When
    String result = csvParsingServiceImpl.constructTmJobId(csvParser.iterator().next(),GFF);

    //Then
    assertNotNull(result);
  }

  @Test
  public void constructTmLFSJobId() throws IOException {
    //Given
    File testFile = new File("src/test/resources/sampledata/unit_tests/sample_LFS_2018-05-17T15-34-00Z.csv");
    Reader reader = new InputStreamReader(new FileInputStream(testFile));

    CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

    //When
    String result = csvParsingServiceImpl.constructTmJobId(csvParser.iterator().next(),LFS);

    //Then
    assertNotNull(result);
  }

  @Test
  public void convertToGFFDate() {
  }

  @Test
  public void convertToLFSDate() {
  }

  @Test
  public void parseLegacySample() {
  }
}