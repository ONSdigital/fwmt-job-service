package uk.gov.ons.fwmt.job_service.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.ons.fwmt.job_service.data.dto.SampleSummaryDTO;
import uk.gov.ons.fwmt.job_service.exceptions.types.InvalidFileNameException;
import uk.gov.ons.fwmt.job_service.exceptions.types.MediaTypeNotSupportedException;
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

  @Test
  public void sampleREST() throws IOException, InvalidFileNameException, MediaTypeNotSupportedException {
    //Given
    when(jobService.validateSampleFile(any())).thenReturn(expectedSampleSummaryDTO);
    //When
    ResponseEntity<SampleSummaryDTO> result = jobServiceController.sampleREST(multipartFile, redirectAttributes);
    //
    assertEquals(expectedSampleSummaryDTO, result.getBody());
  }

  @Test(expected = MediaTypeNotSupportedException.class)
  public void shouldThrowMediaTypeException()
      throws IOException, InvalidFileNameException, MediaTypeNotSupportedException {
    //Given
    when(jobService.validateSampleFile(any())).thenThrow(new MediaTypeNotSupportedException("", ""));
    //When
    ResponseEntity<SampleSummaryDTO> result = jobServiceController.sampleREST(multipartFile, redirectAttributes);
    //
    assertEquals(expectedSampleSummaryDTO, result.getBody());
  }

  @Test(expected = IOException.class)
  public void shouldThrowIOException() throws IOException, InvalidFileNameException, MediaTypeNotSupportedException {
    //Given
    when(jobService.validateSampleFile(any())).thenThrow(new IOException());
    //When
    ResponseEntity<SampleSummaryDTO> result = jobServiceController.sampleREST(multipartFile, redirectAttributes);
    //
    assertEquals(expectedSampleSummaryDTO, result.getBody());
  }

  @Test(expected = InvalidFileNameException.class)
  public void shouldThrowInvalidFilenameException()
      throws IOException, InvalidFileNameException, MediaTypeNotSupportedException {
    //Given
    when(jobService.validateSampleFile(any())).thenThrow(new InvalidFileNameException("", ""));
    //When
    ResponseEntity<SampleSummaryDTO> result = jobServiceController.sampleREST(multipartFile, redirectAttributes);
    //
    assertEquals(expectedSampleSummaryDTO, result.getBody());
  }
}