package uk.gov.ons.fwmt.job_service.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.job_service.data.csv_parser.CSVParseResult;
import uk.gov.ons.fwmt.job_service.data.dto.SampleSummaryDTO;
import uk.gov.ons.fwmt.job_service.data.file_ingest.FileIngest;
import uk.gov.ons.fwmt.job_service.data.file_ingest.Filename;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.exceptions.types.InvalidFileNameException;
import uk.gov.ons.fwmt.job_service.exceptions.types.MediaTypeNotSupportedException;
import uk.gov.ons.fwmt.job_service.service.CSVParsingService;
import uk.gov.ons.fwmt.job_service.service.FileIngestService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobServiceImplTest {

  @InjectMocks private JobServiceImpl jobServiceImpl;
  @Mock FileIngestService fileIngestService;
  @Mock CSVParsingService csvParsingService;
  @Mock LegacySampleIngest legacySampleIngest;
  @Mock FileIngest fileIngest;
  @Mock MultipartFile multipartFile;
  @Mock Filename filename;


  @Test
  public void sendJobToUser() {
  }

  @Test
  public void findUser() {
  }

  @Test
  public void ShouldReturnUnprocessedRowWhenErrorMessageInCSVParsing() throws InvalidFileNameException, MediaTypeNotSupportedException, IOException {
    int expectedProcessedRows = 0;
    int expectedUnprocessedRows = 1;
    String expectedErrorMessage= "Row could not be parsed: TestError";
    String expectedFileName = "TestFile";

    //given
    when(multipartFile.getOriginalFilename()).thenReturn("TestFile");
    when(fileIngestService.ingestSampleFile(any())).thenReturn(fileIngest);
    when(fileIngest.getFilename()).thenReturn(filename);
    when(csvParsingService.parseLegacySample(any(),any())).thenReturn(csvExpectedResult());

    //When
    SampleSummaryDTO result = jobServiceImpl.processSampleFile(multipartFile);

    //Then
    assertEquals(expectedProcessedRows, result.getProcessedRows(), 0);
    assertEquals(expectedUnprocessedRows, result.getUnprocessedRows().size());
    assertEquals(expectedErrorMessage, result.getUnprocessedRows().get(0).getMessage());
    assertEquals(expectedUnprocessedRows, result.getUnprocessedRows().get(0).getRow());
    assertEquals(expectedFileName, result.getFilename());
  }

  private Iterator<CSVParseResult<LegacySampleIngest>> csvExpectedResult() {
    int testRows = 1;
    String errorMessage = "TestError";

    final List<CSVParseResult<LegacySampleIngest>> csvParseResults = new ArrayList<>();
    csvParseResults.add(CSVParseResult.withError(testRows, errorMessage));
    return csvParseResults.iterator();
  }
}