package uk.gov.ons.fwmt.job_service.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import uk.gov.ons.fwmt.job_service.data.csv_parser.CSVParseResult;
import uk.gov.ons.fwmt.job_service.data.dto.SampleSummaryDTO;
import uk.gov.ons.fwmt.job_service.data.file_ingest.SampleFilenameComponents;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType;
import uk.gov.ons.fwmt.job_service.utils.SampleFileUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SampleFileUtils.class)
public class FileIngestServiceImplTest {
  @InjectMocks private FileIngestServiceImpl fileIngestServiceImpl;
  @Mock private CSVParsingServiceImpl csvParsingServiceImpl;
  
  @Test 
  public void givenEmptyCSV_whenValitadateCSVRows_checkSampleSummaryDTOIndicatesThatItWasEmpty(){
    final String expectedFilename = "emptyCSV.csv";
    List<CSVParseResult<LegacySampleIngest>> emptyCsv = Collections.emptyList();

    SampleSummaryDTO dto = fileIngestServiceImpl.valitadateCSVRows(expectedFilename, emptyCsv.iterator());
    
    assertEquals(expectedFilename, dto.getFilename());
    assertEquals(0, dto.getProcessedRows());
    assertEquals(0, dto.getUnprocessedRows().size());
  }
  
  @Test 
  public void givenCSVWithNoErrors_whenValitadateCSVRows_checkSampleSummaryDTOIndicatesThatAllRowsWerProcessed(){
    final String expectedFilename = "validCSV.csv";
    List<CSVParseResult<LegacySampleIngest>> validCsv = new ArrayList<>();
    validCsv.add(CSVParseResult.withResult(0,LegacySampleIngest.builder().build()));
    validCsv.add(CSVParseResult.withResult(1,LegacySampleIngest.builder().build()));
    
    SampleSummaryDTO dto = fileIngestServiceImpl.valitadateCSVRows(expectedFilename, validCsv.iterator());
    
    assertEquals(expectedFilename, dto.getFilename());
    assertEquals(2, dto.getProcessedRows());
    assertEquals(0, dto.getUnprocessedRows().size());
  }
  
  @Test 
  public void givenCSVWithOneErrors_whenValitadateCSVRows_checkSampleSummaryDTOIndicatesThatTheErroredRowIsNotProcessed(){
    final String expectedFilename = "withErrorCSV.csv";
    final String errorMessage = "Row 1 has Error";
    List<CSVParseResult<LegacySampleIngest>> validCsv = new ArrayList<>();
    validCsv.add(CSVParseResult.withResult(0,LegacySampleIngest.builder().build()));
    validCsv.add(CSVParseResult.withError(1, errorMessage));
    validCsv.add(CSVParseResult.withResult(2,LegacySampleIngest.builder().build()));
    
    SampleSummaryDTO dto = fileIngestServiceImpl.valitadateCSVRows(expectedFilename, validCsv.iterator());
    
    assertEquals(expectedFilename, dto.getFilename());
    assertEquals(2, dto.getProcessedRows());
    assertEquals(1, dto.getUnprocessedRows().size());
    assertTrue(dto.getUnprocessedRows().get(0).getMessage().contains(errorMessage));
    assertEquals(1, dto.getUnprocessedRows().get(0).getRow());
  }
  
  
  @Test
  public void givenAnEmptyFile_whenValidateSampleIsInvoke_confirmSampleSummaryDTOcontainsfilenameAndNoUnprocessedRows() throws InvalidFileNameException, IOException{
    SampleFilenameComponents sfc = SampleFilenameComponents.builder().tla(LegacySampleSurveyType.GFF).build();
    File sampleFile = File.createTempFile("sample","csv");
    List<CSVParseResult<LegacySampleIngest>> emptyCsv = Collections.emptyList();
    Iterator<CSVParseResult<LegacySampleIngest>> i = emptyCsv.iterator();
    
    PowerMockito.mockStatic(SampleFileUtils.class);    
    when(SampleFileUtils.buildSampleFilenameComponents(any(File.class))).thenReturn(sfc);
    when(csvParsingServiceImpl.parseLegacySample(any(Reader.class), any(LegacySampleSurveyType.class))).thenReturn(i);

    SampleSummaryDTO dto = fileIngestServiceImpl.validateSampleFile(sampleFile);
    
    assertEquals(sampleFile.getName(), dto.getFilename());
    assertEquals(0, dto.getProcessedRows());
    assertEquals(0, dto.getUnprocessedRows().size());
  }
}