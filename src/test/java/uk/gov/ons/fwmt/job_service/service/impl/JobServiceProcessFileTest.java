package uk.gov.ons.fwmt.job_service.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.job_service.data.csv_parser.CSVParseResult;
import uk.gov.ons.fwmt.job_service.data.dto.SampleSummaryDTO;
import uk.gov.ons.fwmt.job_service.data.file_ingest.FileIngest;
import uk.gov.ons.fwmt.job_service.data.file_ingest.Filename;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.exceptions.types.InvalidFileNameException;
import uk.gov.ons.fwmt.job_service.exceptions.types.MediaTypeNotSupportedException;
import uk.gov.ons.fwmt.job_service.rest.FieldPeriodResourceService;
import uk.gov.ons.fwmt.job_service.rest.JobResourceService;
import uk.gov.ons.fwmt.job_service.rest.UserResourceService;
import uk.gov.ons.fwmt.job_service.service.CSVParsingService;
import uk.gov.ons.fwmt.job_service.service.FileIngestService;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMJobConverterService;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobServiceProcessFileTest {
  @Mock FileIngest fileIngest;
  @Mock Filename filename;

  @Mock private FileIngestService mockFileIngestService;
  @Mock private CSVParsingService mockCsvParsingService;

  @Mock private TMJobConverterService mockTmJobConverterService;
  @Mock private TMService mockTmService;
  @Mock private UserResourceService mockUserResourceService;
  @Mock private JobResourceService mockJobResourceService;
  @Mock private FieldPeriodResourceService mockFieldPeriodResourceService;
  @InjectMocks private JobServiceImpl jobService;

  @Before
  public void setup() {
    when(mockUserResourceService.findByAuthNo(any())).thenReturn(Optional.empty());
    when(mockUserResourceService.findByAlternateAuthNo(any())).thenReturn(Optional.empty());
  }

  @Test
  public void ShouldReturnUnprocessedRowWhenErrorMessageInCSVParsing() throws InvalidFileNameException,
      MediaTypeNotSupportedException, IOException {
    String expectedErrorMessage = "Row could not be parsed: TestError";
    String fileName = "TestFile";

    //Given
    MultipartFile mockMultipartFile = new MockMultipartFile(fileName, fileName, "text/csv", new byte[] {});
    when(mockFileIngestService.ingestSampleFile(any())).thenReturn(fileIngest);
    when(fileIngest.getFilename()).thenReturn(filename);
    when(mockCsvParsingService.parseLegacySample(any(), any())).thenReturn(csvExpectedResult());

    //When
    SampleSummaryDTO result = jobService.processSampleFile(mockMultipartFile);

    //Then
    assertEquals(0, result.getProcessedRows());
    assertEquals(1, result.getUnprocessedRows().size());
    assertEquals(expectedErrorMessage, result.getUnprocessedRows().get(0).getMessage());
    assertEquals(1, result.getUnprocessedRows().get(0).getRow());
    assertEquals(fileName, result.getFilename());
  }

  @Test
  public void ShouldReturnAuthNoWhenUserDoesNotExistInTM()
      throws InvalidFileNameException, MediaTypeNotSupportedException, IOException {
    String expectedErrorMessage = "User did not exist in the gateway: expectedAuth";
    String fileName = "TestFile";

    //Given
    MultipartFile mockMultipartFile = new MockMultipartFile(fileName, fileName, "text/csv", new byte[] {});
    when(mockFileIngestService.ingestSampleFile(any())).thenReturn(fileIngest);
    when(fileIngest.getFilename()).thenReturn(filename);
    when(mockCsvParsingService.parseLegacySample(any(), any())).thenReturn(csvExpectedResultSuccess());

    //When
    SampleSummaryDTO result = jobService.processSampleFile(mockMultipartFile);

    //Then
    assertEquals(0, result.getProcessedRows());
    assertEquals(1, result.getUnprocessedRows().size());
    assertEquals(fileName, result.getFilename());
    assertEquals(expectedErrorMessage, result.getUnprocessedRows().get(0).getMessage());
    assertEquals(1, result.getUnprocessedRows().get(0).getRow());
  }

  private Iterator<CSVParseResult<LegacySampleIngest>> csvExpectedResult() {
    final List<CSVParseResult<LegacySampleIngest>> csvParseResults = new ArrayList<>();
    csvParseResults.add(CSVParseResult.withError(1, "TestError"));
    return csvParseResults.iterator();
  }

  private Iterator<CSVParseResult<LegacySampleIngest>> csvExpectedResultSuccess() {
    final List<CSVParseResult<LegacySampleIngest>> csvParseResults = new ArrayList<>();
    csvParseResults.add(CSVParseResult.withResult(1, createSampleLegacyIngest()));
    return csvParseResults.iterator();
  }

  private LegacySampleIngest createSampleLegacyIngest() {
    LegacySampleIngest legacySampleIngest = new LegacySampleIngest();
    legacySampleIngest.setAuth("expectedAuth");
    return legacySampleIngest;
  }
}
