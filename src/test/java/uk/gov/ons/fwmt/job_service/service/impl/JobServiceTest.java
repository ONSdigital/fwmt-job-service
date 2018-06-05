package uk.gov.ons.fwmt.job_service.service.impl;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import uk.gov.ons.fwmt.job_service.rest.dto.UserDto;
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
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobServiceTest {
  @Mock private FileIngestService mockFileIngestService;
  @Mock private CSVParsingService mockCsvParsingService;
  @Mock private TMJobConverterService mockTmJobConverterService;
  @Mock private TMService mockTmService;
  @Mock private UserResourceService mockUserResourceService;
  @Mock private JobResourceService mockJobResourceService;
  @Mock private FieldPeriodResourceService mockFieldPeriodResourceService;

  @InjectMocks private JobServiceImpl jobService;

  @Mock FileIngest fileIngest;
  @Mock MultipartFile multipartFile;
  @Mock Filename filename;

  private LegacySampleIngest makeExampleIngest_authNoOnly(String authNo) {
    LegacySampleIngest ingest = new LegacySampleIngest();
    ingest.setAuth(authNo);
    return ingest;
  }

  public void findUser_setupDB() {

  }

  @Test
  public void findUser_notPresent() {
    Optional<UserDto> user = jobService.findUser(makeExampleIngest_authNoOnly("1111"));
    assertFalse(user.isPresent());
  }

  @Test
  public void findUser_presentByAuthNo() {

  }

  @Test
  public void findUser_presentByAltAuthNo() {

  }

  @Test
  public void ShouldReturnUnprocessedRowWhenErrorMessageInCSVParsing() throws InvalidFileNameException,
      MediaTypeNotSupportedException, IOException {
    int expectedProcessedRows = 0;
    int expectedUnprocessedRows = 1;
    String expectedErrorMessage= "Row could not be parsed: TestError";
    String expectedFileName = "TestFile";


    //given
    when(multipartFile.getOriginalFilename()).thenReturn("TestFile");
    when(mockFileIngestService.ingestSampleFile(any())).thenReturn(fileIngest);
    when(fileIngest.getFilename()).thenReturn(filename);
    when(mockCsvParsingService.parseLegacySample(any(),any())).thenReturn(csvExpectedResult());

    //When
    SampleSummaryDTO result = jobService.processSampleFile(multipartFile);

    //Then
    assertEquals(expectedProcessedRows, result.getProcessedRows(), 0);
    assertEquals(expectedUnprocessedRows, result.getUnprocessedRows().size());
    assertEquals(expectedErrorMessage, result.getUnprocessedRows().get(0).getMessage());
    assertEquals(expectedUnprocessedRows, result.getUnprocessedRows().get(0).getRow());
    assertEquals(expectedFileName, result.getFilename());
  }

  @Test
  public void ShouldReturnAuthNoWhenUserDoesNotExistInTM() throws InvalidFileNameException, MediaTypeNotSupportedException, IOException {
    int expectedProcessedRows = 0;
    int expectedUnprocessedRows = 1;
    String expectedErrorMessage= "User did not exist in the gateway: expectedAuth";
    String expectedFileName = "TestFile";


    //given
    when(multipartFile.getOriginalFilename()).thenReturn("TestFile");
    when(mockFileIngestService.ingestSampleFile(any())).thenReturn(fileIngest);
    when(fileIngest.getFilename()).thenReturn(filename);
    when(mockCsvParsingService.parseLegacySample(any(),any())).thenReturn(csvExpectedResultSuccess());
    when(mockUserResourceService.findByAuthNo(any())).thenReturn(Optional.empty());
    when(mockUserResourceService.findByAlternateAuthNo(any())).thenReturn(Optional.empty());

    //When
    SampleSummaryDTO result = jobService.processSampleFile(multipartFile);

    //Then
    assertEquals(expectedProcessedRows,result.getProcessedRows());
    assertEquals(expectedUnprocessedRows,result.getUnprocessedRows().size());
    assertEquals(expectedFileName,result.getFilename());
    assertEquals(expectedErrorMessage,result.getUnprocessedRows().get(0).getMessage());
    assertEquals(expectedUnprocessedRows,result.getUnprocessedRows().get(0).getRow());
  }

  private Iterator<CSVParseResult<LegacySampleIngest>> csvExpectedResult() {
    int testRows = 1;
    String errorMessage = "TestError";

    final List<CSVParseResult<LegacySampleIngest>> csvParseResults = new ArrayList<>();
    csvParseResults.add(CSVParseResult.withError(testRows, errorMessage));
    return csvParseResults.iterator();
  }

  private Iterator<CSVParseResult<LegacySampleIngest>> csvExpectedResultSuccess () {
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
