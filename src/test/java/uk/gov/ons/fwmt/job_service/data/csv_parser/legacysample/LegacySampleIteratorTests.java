package uk.gov.ons.fwmt.job_service.data.csv_parser.legacysample;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleGFFDataIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleLFSDataIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service.rest.client.FieldPeriodResourceServiceClient;
import uk.gov.ons.fwmt.job_service.rest.client.dto.FieldPeriodDto;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LegacySampleUtils.class, LegacySampleAnnotationProcessor.class})
public class LegacySampleIteratorTests {
  @Mock
  private FieldPeriodResourceServiceClient fieldPeriodResourceServiceClient;

  @Test
  public void givenSampleIsLFS_whenIngesting_verifyItsParsedAsLFS() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Reader reader = mock(Reader.class);
    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(""));

    LegacySampleIterator legacySampleIterator = new LegacySampleIterator(csvParser, LegacySampleSurveyType.LFS,fieldPeriodResourceServiceClient);
    LegacySampleIterator spyLegacySampleIterator = spy(legacySampleIterator);

    doNothing().when(spyLegacySampleIterator).parseLegacySampleLFSData(any(LegacySampleIngest.class), any(CSVRecord.class));
    doNothing().when(spyLegacySampleIterator).parseLegacySampleCommonData(any(LegacySampleIngest.class), any(CSVRecord.class));

    CSVRecord csvRecord = mock(CSVRecord.class);
    spyLegacySampleIterator.ingest(csvRecord);

    verify(spyLegacySampleIterator, times(1)).parseLegacySampleLFSData(any(LegacySampleIngest.class), any(CSVRecord.class));
    verify(spyLegacySampleIterator, times(0)).parseLegacySampleGFFData(any(LegacySampleIngest.class), any(CSVRecord.class));
  }

  @Test
  public void givenSampleIsGFF_whenIngesting_verifyItsParsedAsGFF() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Reader reader = mock(Reader.class);
    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(""));

    LegacySampleIterator legacySampleIterator = new LegacySampleIterator(csvParser, LegacySampleSurveyType.GFF, fieldPeriodResourceServiceClient);

    LegacySampleIterator spyLegacySampleIterator = spy(legacySampleIterator);

    doNothing().when(spyLegacySampleIterator).parseLegacySampleGFFData(any(LegacySampleIngest.class), any(CSVRecord.class));
    doNothing().when(spyLegacySampleIterator).parseLegacySampleCommonData(any(LegacySampleIngest.class), any(CSVRecord.class));

    CSVRecord csvRecord = mock(CSVRecord.class);
    spyLegacySampleIterator.ingest(csvRecord);

    verify(spyLegacySampleIterator, times(0)).parseLegacySampleLFSData(any(LegacySampleIngest.class), any(CSVRecord.class));
    verify(spyLegacySampleIterator, times(1)).parseLegacySampleGFFData(any(LegacySampleIngest.class), any(CSVRecord.class));
  }

  @Test
  public void givenSampleIsLFS_whenIngesting_verifyItsParsesCommon() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Reader reader = mock(Reader.class);
    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(""));

    LegacySampleIterator legacySampleIterator = new LegacySampleIterator(csvParser, LegacySampleSurveyType.LFS, fieldPeriodResourceServiceClient);

    LegacySampleIterator spyLegacySampleIterator = spy(legacySampleIterator);

    doNothing().when(spyLegacySampleIterator).parseLegacySampleLFSData(any(LegacySampleIngest.class), any(CSVRecord.class));
    doNothing().when(spyLegacySampleIterator).parseLegacySampleCommonData(any(LegacySampleIngest.class), any(CSVRecord.class));

    CSVRecord csvRecord = mock(CSVRecord.class);
    spyLegacySampleIterator.ingest(csvRecord);

    verify(spyLegacySampleIterator, times(1)).parseLegacySampleCommonData(any(LegacySampleIngest.class), any(CSVRecord.class));
  }

  @Test
  public void givenSampleIsGFF_whenIngesting_verifyItsParsesCommon() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Reader reader = mock(Reader.class);
    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(""));

    LegacySampleIterator legacySampleIterator = new LegacySampleIterator(csvParser, LegacySampleSurveyType.GFF, fieldPeriodResourceServiceClient);

    LegacySampleIterator spyLegacySampleIterator = spy(legacySampleIterator);

    doNothing().when(spyLegacySampleIterator).parseLegacySampleGFFData(any(LegacySampleIngest.class), any(CSVRecord.class));
    doNothing().when(spyLegacySampleIterator).parseLegacySampleCommonData(any(LegacySampleIngest.class), any(CSVRecord.class));

    CSVRecord csvRecord = mock(CSVRecord.class);
    spyLegacySampleIterator.ingest(csvRecord);

    verify(spyLegacySampleIterator, times(1)).parseLegacySampleCommonData(any(LegacySampleIngest.class), any(CSVRecord.class));
  }

  @Test
  public void givenAllSurveyTypes_whenIngesting_verifyIllegalArgumentIsNeverThrown() throws IOException {
    Reader reader = mock(Reader.class);
    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(""));

    for (LegacySampleSurveyType surveyType : LegacySampleSurveyType.values()) {
      try {
        LegacySampleIterator legacySampleIterator = new LegacySampleIterator(csvParser, surveyType, fieldPeriodResourceServiceClient);

        LegacySampleIterator spyLegacySampleIterator = spy(legacySampleIterator);

        doNothing().when(spyLegacySampleIterator).parseLegacySampleLFSData(any(LegacySampleIngest.class), any(CSVRecord.class));
        doNothing().when(spyLegacySampleIterator).parseLegacySampleGFFData(any(LegacySampleIngest.class), any(CSVRecord.class));
        doNothing().when(spyLegacySampleIterator).parseLegacySampleCommonData(any(LegacySampleIngest.class), any(CSVRecord.class));

        CSVRecord csvRecord = mock(CSVRecord.class);
        spyLegacySampleIterator.ingest(csvRecord);
      } catch (Exception e) {
        fail("No Exception expected");
      }
    }
  }

  @Test
  public void givenAcsvRecord_whenParsesCommon_verifyDataIsCopied() throws IOException {
    Reader reader = mock(Reader.class);
    CSVRecord csvRecord = mock(CSVRecord.class);
    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(""));
    LegacySampleIngest lsi = LegacySampleIngest.builder()
        .osGridRef("9876,5432")
        .build();
    LegacySampleIterator legacySampleIterator = new LegacySampleIterator(csvParser, LegacySampleSurveyType.GFF, fieldPeriodResourceServiceClient);

    PowerMockito.mockStatic(LegacySampleUtils.class);
    when(LegacySampleUtils.constructTmJobId(any(CSVRecord.class), any(LegacySampleSurveyType.class))) .thenReturn("1234");

    legacySampleIterator.parseLegacySampleCommonData(lsi, csvRecord);

    assertEquals("1234", lsi.getTmJobId());
    assertEquals("9876.0", lsi.getGeoX().toString());
    assertEquals("5432.0", lsi.getGeoY().toString());
  }

  @Test
  public void givenAMalformedOSGridRef_whenParsesCommon_verifyExceptionIsThrown() throws IOException {
    Reader reader = mock(Reader.class);
    CSVRecord csvRecord = mock(CSVRecord.class);
    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(""));
    LegacySampleIngest lsi = LegacySampleIngest.builder().build();
    LegacySampleIterator legacySampleIterator = new LegacySampleIterator(csvParser, LegacySampleSurveyType.GFF, fieldPeriodResourceServiceClient);

    PowerMockito.mockStatic(LegacySampleUtils.class);
    when(LegacySampleUtils.constructTmJobId(any(CSVRecord.class), any(LegacySampleSurveyType.class))).thenReturn("1234");

    legacySampleIterator.parseLegacySampleCommonData(lsi, csvRecord);

    assertTrue(null==lsi.getGeoX());
    assertTrue(null==lsi.getGeoY());
  }

  @Test
  public void givenANoOSGridRef_whenParsesCommon_verifyGeoFieldsAreBlank() throws IOException {
    String expectedExceptionMessage = "FWMT_JOB_SERVICE_0007-CSV_INVALID_FIELD: A field was invalid in a CSV file: fieldName=OSGridRef failed to parse: Did not match the expected format of 'X,Y'";
    Reader reader = mock(Reader.class);
    CSVRecord csvRecord = mock(CSVRecord.class);
    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(""));
    LegacySampleIngest lsi = LegacySampleIngest.builder().osGridRef("9876--5432").build();
    LegacySampleIterator legacySampleIterator = new LegacySampleIterator(csvParser, LegacySampleSurveyType.GFF, fieldPeriodResourceServiceClient);

    PowerMockito.mockStatic(LegacySampleUtils.class);
    when(LegacySampleUtils.constructTmJobId(any(CSVRecord.class), any(LegacySampleSurveyType.class))) .thenReturn("1234");

    try {
      legacySampleIterator.parseLegacySampleCommonData(lsi, csvRecord);
      fail("FWMTCommonException expected");
    } catch (FWMTCommonException e) {
      assertEquals(expectedExceptionMessage, e.getMessage());
    }
  }
  
  @Test
  public void givenAcsvRecord_whenParseLegacySampleGFFData_verifyDataIsCopied() throws Exception {
    Reader reader = mock(Reader.class);
    CSVRecord csvRecord = mock(CSVRecord.class);
    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(""));
    LegacySampleIngest lsi = LegacySampleIngest.builder().stage("112").tla("LCF").build();
    LocalDate now = LocalDate.now();
    FieldPeriodDto fieldPeriodDto = new FieldPeriodDto();
    fieldPeriodDto.setEndDate(now);
    LegacySampleGFFDataIngest legacySampleGFFDataIngest = new LegacySampleGFFDataIngest();

    LegacySampleIngest expectedLSI = LegacySampleIngest.builder()
        .dueDate(now)
        .calculatedDueDate(String.valueOf(now))
        .legacySampleSurveyType(LegacySampleSurveyType.GFF)
        .gffData(legacySampleGFFDataIngest)
        .tla("LCF")
        .stage("112")
        .build();
    
    LegacySampleIterator legacySampleIterator = new LegacySampleIterator(csvParser, LegacySampleSurveyType.GFF, fieldPeriodResourceServiceClient);

    PowerMockito.mockStatic(LegacySampleAnnotationProcessor.class);
    PowerMockito.doNothing().when(LegacySampleAnnotationProcessor.class, "process", any(LegacySampleIngest.class), any(CSVRecord.class), eq("GFF"));
    PowerMockito.doNothing().when(LegacySampleAnnotationProcessor.class, "process", any(LegacySampleIngest.class), any(CSVRecord.class), eq(null));

    PowerMockito.mockStatic(LegacySampleUtils.class);
    when(LegacySampleUtils.convertToFieldPeriodDate(lsi.getStage(), fieldPeriodResourceServiceClient))
        .thenReturn(fieldPeriodDto);

    legacySampleIterator.parseLegacySampleGFFData(lsi, csvRecord);

    assertEquals(expectedLSI, lsi);
  }
  
  @Test
  public void givenAcsvRecord_whenParseLegacySampleLFSData_verifyDataIsCopied() throws Exception {
    Reader reader = mock(Reader.class);
    CSVRecord csvRecord = mock(CSVRecord.class);

    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(""));
    LegacySampleIngest lsi = LegacySampleIngest.builder().stage("Old Vic").build();
    LegacySampleIterator legacySampleIterator = new LegacySampleIterator(csvParser, LegacySampleSurveyType.LFS, fieldPeriodResourceServiceClient);
    FieldPeriodDto fieldPeriodDto = new FieldPeriodDto();
    LocalDate now = LocalDate.now();
    fieldPeriodDto.setEndDate(now);
    LegacySampleLFSDataIngest legacySampleLFSDataIngest = new LegacySampleLFSDataIngest();

    LegacySampleIngest expectedLSI = LegacySampleIngest.builder()
        .dueDate(now)
        .calculatedDueDate(String.valueOf(now))
        .legacySampleSurveyType(LegacySampleSurveyType.LFS)
        .lfsData(legacySampleLFSDataIngest)
        .stage("Old Vic")
        .build();
    
    PowerMockito.mockStatic(LegacySampleAnnotationProcessor.class);
    PowerMockito.doNothing().when(LegacySampleAnnotationProcessor.class, "process", any(LegacySampleIngest.class), any(CSVRecord.class), eq("LFS"));
    PowerMockito.doNothing().when(LegacySampleAnnotationProcessor.class, "process", any(LegacySampleIngest.class), any(CSVRecord.class), eq(null));

    PowerMockito.mockStatic(LegacySampleUtils.class);
    when(LegacySampleUtils.convertToFieldPeriodDate(lsi.getStage(), fieldPeriodResourceServiceClient))
        .thenReturn(fieldPeriodDto);
    when(LegacySampleUtils.checkSetLookingForWorkIndicator(lsi,csvRecord)).thenReturn(legacySampleLFSDataIngest);
    legacySampleIterator.parseLegacySampleLFSData(lsi, csvRecord);

    assertEquals(expectedLSI, lsi);
  }
}
