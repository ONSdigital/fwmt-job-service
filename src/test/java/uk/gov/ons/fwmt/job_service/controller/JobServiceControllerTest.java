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

  @Test
  public void sampleREST() throws IOException {
    //Given
    when(jobService.processSampleFile(any())).thenReturn(expectedSampleSummaryDTO);
    //When
    ResponseEntity<SampleSummaryDTO> result = jobServiceController.sampleREST(multipartFile, redirectAttributes);
    //
    assertEquals(expectedSampleSummaryDTO, result.getBody());
  }

  // this normally throws MediaTypeNotSupportedException
  // TODO add checks to make sure that this throws the right exception
  @Test(expected = FWMTCommonException.class)
  public void shouldThrowMediaTypeException() throws IOException {
    //Given
    when(jobService.processSampleFile(any())).thenThrow(FWMTCommonException.makeInvalidMediaTypeException("", ""));
    //When
    ResponseEntity<SampleSummaryDTO> result = jobServiceController.sampleREST(multipartFile, redirectAttributes);
    //
    assertEquals(expectedSampleSummaryDTO, result.getBody());
  }

  @Test(expected = IOException.class)
  public void shouldThrowIOException() throws IOException {
    //Given
    when(jobService.processSampleFile(any())).thenThrow(new IOException());
    //When
    ResponseEntity<SampleSummaryDTO> result = jobServiceController.sampleREST(multipartFile, redirectAttributes);
    //
    assertEquals(expectedSampleSummaryDTO, result.getBody());
  }

  // this normally throws InvalidFileNameException
  // TODO add checks to make sure that this throws the right exception
  @Test(expected = FWMTCommonException.class)
  public void shouldThrowInvalidFilenameException() throws IOException {
    //Given
    when(jobService.processSampleFile(any())).thenThrow(FWMTCommonException.makeInvalidFileNameException("", ""));
    //When
    ResponseEntity<SampleSummaryDTO> result = jobServiceController.sampleREST(multipartFile, redirectAttributes);
    //
    assertEquals(expectedSampleSummaryDTO, result.getBody());
  }
}