package uk.gov.ons.fwmt.job_service.data.csv_parser.legacysample;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleLFSDataIngest;
import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service.rest.client.FieldPeriodResourceServiceClient;
import uk.gov.ons.fwmt.job_service.rest.client.dto.FieldPeriodDto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType.GFF;
import static uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType.LFS;

@RunWith(MockitoJUnitRunner.class)
public class LegacySampleUtilsTests {
  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Mock private FieldPeriodResourceServiceClient fieldPeriodResourceServiceClient;
  
  @Mock private FieldPeriodDto fieldPeriodDto;
  
  @Test
  public void constructTmGFFJobId() throws IOException {
    //Given
    File testFile = new File("src/test/resources/sampledata/unit_tests/sample_GFF_2018-05-17T15-34-00Z.csv");
    Reader reader = new InputStreamReader(new FileInputStream(testFile));

    CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

    //When
    String result = LegacySampleUtils.constructTmJobId(csvParser.iterator().next(), GFF);

    //Then
    assertThat(result, is("tla_1-quota_1-addressno_1-801"));
  }

  @Test
  public void constructTmLFSJobId() throws IOException {
    //Given
    File testFile = new File("src/test/resources/sampledata/unit_tests/sample_LFS_2018-05-17T15-34-00Z.csv");
    Reader reader = new InputStreamReader(new FileInputStream(testFile));

    CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

    //When
    String result = LegacySampleUtils.constructTmJobId(csvParser.iterator().next(), LFS);

    //Then
    assertNotNull(result);
    assertThat(result, is("quota_1 week_1 w1yr_1 qrtr_1 addr_1 wavfnd_1 hhld_1 chklet_1 - 81K"));
  }

  @Test
  public void convertToGFFDate() throws FWMTCommonException {
    //Given
    String stage = "807";
    int year = 2018;
    int month = 7;
    int day = 1;
    LocalDate date = LocalDate.of(year, month, day);
    when(fieldPeriodResourceServiceClient.findByFieldPeriod(any())).thenReturn(Optional.of(fieldPeriodDto));
    when(fieldPeriodDto.getEndDate()).thenReturn(date);

    //When
    LocalDate result = LegacySampleUtils.convertToFieldPeriodDate(stage, fieldPeriodResourceServiceClient);

    //Then
    assertEquals(date, result);
  }

  @Test
  public void convertToGFFDateShouldThrowExceptionWhenStageMissing() throws FWMTCommonException {
    //Given
    when(fieldPeriodResourceServiceClient.findByFieldPeriod(any())).thenReturn(Optional.empty());
    expectedException.expect(FWMTCommonException.class);
    expectedException.expectMessage(ExceptionCode.UNKNOWN_FIELD_PERIOD.toString());

    //When
    LegacySampleUtils.convertToFieldPeriodDate(anyString(), fieldPeriodResourceServiceClient);
  }

  @Test
  public void convertToLFSDate() throws FWMTCommonException {
    //Given
    String fieldPeriod = "807";
    int year = 2018;
    int month = 7;
    int day = 1;
    LocalDate date = LocalDate.of(year, month, day);
    when(fieldPeriodResourceServiceClient.findByFieldPeriod(any())).thenReturn(Optional.of(fieldPeriodDto));
    when(fieldPeriodDto.getEndDate()).thenReturn(date);

    //When
    LocalDate result = LegacySampleUtils.convertToFieldPeriodDate(fieldPeriod, fieldPeriodResourceServiceClient);

    //Then
    assertEquals(date, result);
  }

  @Test
  public void convertToLFSDateShouldThrowExceptionWhenFieldPeriodMissing() throws FWMTCommonException {
    //Given
    when(fieldPeriodResourceServiceClient.findByFieldPeriod(any())).thenReturn(Optional.empty());
    expectedException.expect(FWMTCommonException.class);
    expectedException.expectMessage(ExceptionCode.UNKNOWN_FIELD_PERIOD.toString());

    //When
    LegacySampleUtils.convertToFieldPeriodDate(anyString(), fieldPeriodResourceServiceClient);
  }
  
  @Test
  public void constructTmLFSReIssueJobId() throws IOException {
    //Given
    String expectedResult = "quota_1 week_1 w1yr_1 qrtr_1 addr_1 wavfnd_1 hhld_1 chklet_1 - 81K [R2]";
    File testFile = new File("src/test/resources/sampledata/unit_tests/reissueTest.csv");
    Reader reader = new InputStreamReader(new FileInputStream(testFile));

    CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

    //When
    String result = LegacySampleUtils.constructTmJobId(csvParser.iterator().next(), LFS);

    //Then
    assertNotNull(result);
    assertEquals(expectedResult,result);
  }

  @Test
  public void checkSetLookingForWorkIndicatorTest() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException{

    File testFile = new File("src/test/resources/sampledata/unit_tests/sample_LFS_2018-09-01T01-04-01Z.csv");
    Reader reader = new InputStreamReader(new FileInputStream(testFile));
    CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

    LegacySampleIngest instance = new LegacySampleIngest();
    instance.setLfsData(new LegacySampleLFSDataIngest());
    LegacySampleLFSDataIngest lfsData;
    CSVRecord record = csvParser.iterator().next();

    lfsData = LegacySampleUtils.checkSetLookingForWorkIndicator(instance,record);

    assertEquals("W",lfsData.getRespondentWorkIndicator1());
    assertEquals("N",lfsData.getRespondentLookingForWork1());
    assertEquals("W",lfsData.getRespondentWorkIndicator2());
    assertEquals("N",lfsData.getRespondentLookingForWork2());
    assertEquals("W",lfsData.getRespondentWorkIndicator3());
    assertEquals("N",lfsData.getRespondentLookingForWork3());
    assertEquals("W",lfsData.getRespondentWorkIndicator4());
    assertEquals("N",lfsData.getRespondentLookingForWork4());
    assertEquals("N",lfsData.getRespondentWorkIndicator5());
    assertEquals("L",lfsData.getRespondentLookingForWork5());
    assertEquals("N",lfsData.getRespondentWorkIndicator6());
    assertEquals("L",lfsData.getRespondentLookingForWork6());
    assertEquals("N",lfsData.getRespondentWorkIndicator7());
    assertEquals("N",lfsData.getRespondentLookingForWork7());
    assertEquals("W",lfsData.getRespondentWorkIndicator8());
    assertEquals("L",lfsData.getRespondentLookingForWork8());
    assertEquals("W",lfsData.getRespondentWorkIndicator16());
    assertEquals("L",lfsData.getRespondentLookingForWork16());
    
  }

}
