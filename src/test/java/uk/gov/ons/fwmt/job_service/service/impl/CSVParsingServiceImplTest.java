package uk.gov.ons.fwmt.job_service.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleGFFDataIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service.rest.FieldPeriodResourceService;
import uk.gov.ons.fwmt.job_service.rest.dto.FieldPeriodDto;
import uk.gov.ons.fwmt.job_service.utilities.TestIngestBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType.GFF;
import static uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType.LFS;

@RunWith(MockitoJUnitRunner.class)
public class CSVParsingServiceImplTest {

  @InjectMocks private CSVParsingServiceImpl csvParsingServiceImpl;
  @Mock private FieldPeriodResourceService fieldPeriodResourceService;
  @Mock private FieldPeriodDto fieldPeriodDto;
  @Rule public ExpectedException expectedException = ExpectedException.none();

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
    csvParsingServiceImpl.setFromCSVColumnAnnotations(testIngestData, csvParser.iterator().next(), "GFF");
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
  public void convertToGFFDate() throws FWMTCommonException {
    //Given
    String stage = "807";
    int year = 2018;
    int month = 7;
    int day = 1;
    LocalDate date = LocalDate.of(year,month,day);
    when(fieldPeriodResourceService.findByFieldPeriod(any())).thenReturn(Optional.of(fieldPeriodDto));
    when(fieldPeriodDto.getEndDate()).thenReturn(date);

    //When
    LocalDate result = csvParsingServiceImpl.convertToGFFDate(stage);

    //Then
    assertEquals(date,result);
  }

  @Test
  public void convertToGFFDateShouldThrowExceptionWhenStageMissing() throws FWMTCommonException {
    //Given
    when(fieldPeriodResourceService.findByFieldPeriod(any())).thenReturn(Optional.empty());
    expectedException.expect(FWMTCommonException.class);
    expectedException.expectMessage(ExceptionCode.FWMT_JOB_SERVICE_0011.toString());

    //When
    csvParsingServiceImpl.convertToGFFDate(anyString());
  }

  @Test
  public void convertToLFSDate() throws FWMTCommonException {
    //Given
    String fieldPeriod = "807";
    int year = 2018;
    int month = 7;
    int day = 1;
    LocalDate date = LocalDate.of(year,month,day);
    when(fieldPeriodResourceService.findByFieldPeriod(any())).thenReturn(Optional.of(fieldPeriodDto));
    when(fieldPeriodDto.getEndDate()).thenReturn(date);

    //When
    LocalDate result = csvParsingServiceImpl.convertToLFSDate(fieldPeriod);

    //Then
    assertEquals(date,result);
  }

  @Test
  public void convertToLFSDateShouldThrowExceptionWhenFieldPeriodMissing() throws FWMTCommonException {
    //Given
    when(fieldPeriodResourceService.findByFieldPeriod(any())).thenReturn(Optional.empty());
    expectedException.expect(FWMTCommonException.class);
    expectedException.expectMessage(ExceptionCode.FWMT_JOB_SERVICE_0011.toString());

    //When
    csvParsingServiceImpl.convertToLFSDate(anyString());
  }

  @Test
  public void parseGFFLegacySample() throws IOException {
    //Given
    File testFile = new File("src/test/resources/sampledata/unit_tests/sample_GFF_2018-05-17T15-34-00Z.csv");
    Reader reader = new InputStreamReader(new FileInputStream(testFile));

    //When
    //Then
    assertNotNull(csvParsingServiceImpl.parseLegacySample(reader,GFF));
  }

  @Test
  public void parseLFSLegacySample() throws IOException {
    //Given
    File testFile = new File("src/test/resources/sampledata/unit_tests/sample_LFS_2018-05-17T15-34-00Z.csv");
    Reader reader = new InputStreamReader(new FileInputStream(testFile));

    //When
    //Then
    assertNotNull(csvParsingServiceImpl.parseLegacySample(reader,LFS));
  }
}