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
import uk.gov.ons.fwmt.job_service.rest.UserResourceService;
import uk.gov.ons.fwmt.job_service.service.CSVParsingService;
import uk.gov.ons.fwmt.job_service.service.FileIngestService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobServiceImplTest {

  @InjectMocks private JobServiceImpl jobServiceImpl;
  @Mock private FileIngestService fileIngestService;
  @Mock private CSVParsingService csvParsingService;
  @Mock private UserResourceService userResourceService;
  @Mock private FileIngest fileIngest;
  @Mock private MultipartFile multipartFile;
  @Mock private Filename filename;

  @Test
  public void shouldReturnUnprocessedRowWhenErrorMessageInCSVParsing()
      throws InvalidFileNameException, MediaTypeNotSupportedException, IOException {
    int expectedProcessedRows = 0;
    int expectedUnprocessedRows = 1;
    String expectedErrorMessage = "Row could not be parsed: row=1, message=TestError";
    String expectedFileName = "TestFile";

    //given
    when(multipartFile.getOriginalFilename()).thenReturn("TestFile");
    when(fileIngestService.ingestSampleFile(any())).thenReturn(fileIngest);
    when(fileIngest.getFilename()).thenReturn(filename);
    when(csvParsingService.parseLegacySample(any(), any())).thenReturn(csvExpectedResult());

    //When
    SampleSummaryDTO result = jobServiceImpl.processSampleFile(multipartFile);

    //Then
    assertEquals(expectedProcessedRows, result.getProcessedRows(), 0);
    assertEquals(expectedUnprocessedRows, result.getUnprocessedRows().size());
    assertEquals(expectedErrorMessage, result.getUnprocessedRows().get(0).getMessage());
    assertEquals(expectedUnprocessedRows, result.getUnprocessedRows().get(0).getRow());
    assertEquals(expectedFileName, result.getFilename());
  }

  @Test
  public void shouldReturnAuthNoWhenUserDoesNotExistInTM()
      throws InvalidFileNameException, MediaTypeNotSupportedException, IOException {
    int expectedProcessedRows = 0;
    int expectedUnprocessedRows = 1;
    String expectedErrorMessage = "User did not exist in the gateway: authno=expectedAuth";
    String expectedFileName = "TestFile";

    //given
    when(multipartFile.getOriginalFilename()).thenReturn("TestFile");
    when(fileIngestService.ingestSampleFile(any())).thenReturn(fileIngest);
    when(fileIngest.getFilename()).thenReturn(filename);
    when(csvParsingService.parseLegacySample(any(), any())).thenReturn(csvExpectedResultSuccess());
    when(userResourceService.findByAuthNo(any())).thenReturn(Optional.empty());
    when(userResourceService.findByAlternateAuthNo(any())).thenReturn(Optional.empty());

    //When
    SampleSummaryDTO result = jobServiceImpl.processSampleFile(multipartFile);

    //Then
    assertEquals(expectedProcessedRows, result.getProcessedRows());
    assertEquals(expectedUnprocessedRows, result.getUnprocessedRows().size());
    assertEquals(expectedFileName, result.getFilename());
    assertEquals(expectedErrorMessage, result.getUnprocessedRows().get(0).getMessage());
    assertEquals(expectedUnprocessedRows, result.getUnprocessedRows().get(0).getRow());
  }

  private Iterator<CSVParseResult<LegacySampleIngest>> csvExpectedResult() {
    int testRows = 1;
    String errorMessage = "TestError";

    final List<CSVParseResult<LegacySampleIngest>> csvParseResults = new ArrayList<>();
    csvParseResults.add(CSVParseResult.withError(testRows, errorMessage));
    return csvParseResults.iterator();
  }

  private Iterator<CSVParseResult<LegacySampleIngest>> csvExpectedResultSuccess() {
    int testRows = 1;

    final List<CSVParseResult<LegacySampleIngest>> csvParseResults = new ArrayList<>();
    csvParseResults.add(CSVParseResult.withResult(testRows, createSampleLegacyIngest()));
    return csvParseResults.iterator();
  }

  private LegacySampleIngest createSampleLegacyIngest() {
    LegacySampleIngest legacySampleIngest = new LegacySampleIngest();
    legacySampleIngest.setAuth("expectedAuth");
    return legacySampleIngest;
  }
}