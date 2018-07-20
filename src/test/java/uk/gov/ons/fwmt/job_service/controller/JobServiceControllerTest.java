package uk.gov.ons.fwmt.job_service.controller;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.ons.fwmt.job_service.data.dto.SampleSummaryDTO;
import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service.service.impl.JobServiceImpl;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobServiceControllerTest {

  @InjectMocks JobServiceController jobServiceController;
  @Mock private JobServiceImpl jobService;
  @Mock private MultipartFile multipartFile;
  @Mock private RedirectAttributes redirectAttributes;
  @Mock private SampleSummaryDTO expectedSampleSummaryDTO;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void sampleREST() throws IOException {
    //Given
    when(jobService.processSampleFile(any())).thenReturn(expectedSampleSummaryDTO);

    //When
    ResponseEntity<SampleSummaryDTO> result = jobServiceController.sampleREST(multipartFile, redirectAttributes);

    //
    assertEquals(expectedSampleSummaryDTO, result.getBody());
  }

  @Test
  public void shouldThrowMediaTypeException() throws IOException {
    //Given
    when(jobService.processSampleFile(any())).thenThrow(FWMTCommonException.makeInvalidMediaTypeException("", ""));

    expectedException.expect(FWMTCommonException.class);
    expectedException.expectMessage(ExceptionCode.INVALID_MEDIA_TYPE.getCode());

    //When
    ResponseEntity<SampleSummaryDTO> result = jobServiceController.sampleREST(multipartFile, redirectAttributes);

    //
    assertEquals(expectedSampleSummaryDTO, result.getBody());
  }

  @Test
  public void shouldThrowIOException() throws IOException {
    //Given
    when(jobService.processSampleFile(any())).thenThrow(new IOException());

    expectedException.expect(IOException.class);

    //When
    ResponseEntity<SampleSummaryDTO> result = jobServiceController.sampleREST(multipartFile, redirectAttributes);

    //
    assertEquals(expectedSampleSummaryDTO, result.getBody());
  }

  @Test
  public void shouldThrowInvalidFilenameException() throws IOException {
    //Given
    when(jobService.processSampleFile(any())).thenThrow(FWMTCommonException.makeInvalidFileNameException("", ""));

    expectedException.expect(FWMTCommonException.class);
    expectedException.expectMessage(ExceptionCode.INVALID_FILE_NAME.getCode());

    //When
    ResponseEntity<SampleSummaryDTO> result = jobServiceController.sampleREST(multipartFile, redirectAttributes);

    //
    assertEquals(expectedSampleSummaryDTO, result.getBody());
  }
}