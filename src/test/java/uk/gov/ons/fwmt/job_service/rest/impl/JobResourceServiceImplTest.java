package uk.gov.ons.fwmt.job_service.rest.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.job_service.exceptions.types.ResourceServiceMalfunctionException;
import uk.gov.ons.fwmt.job_service.rest.dto.JobDto;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JobResourceServiceImplTest {

  @InjectMocks private JobResourceServiceImpl jobResourceService;
  @Mock private RestTemplate restTemplate;
  @Mock private ResponseEntity<JobDto> jobDtoResponseEntity;
  @Mock private ResponseEntity<Void> voidResponseEntity;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void existsByTmJobId() {
    //Given
    String tmJobId = "testID";
    JobDto expectedJobDto = new JobDto(tmJobId, null);
    when(restTemplate.getForEntity(any(), eq(JobDto.class), eq(tmJobId))).thenReturn(jobDtoResponseEntity);
    when(jobDtoResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
    when(jobDtoResponseEntity.getBody()).thenReturn(expectedJobDto);

    //When
    Boolean result = jobResourceService.existsByTmJobId(tmJobId);

    //Then
    assertTrue(result);
  }

  @Test
  public void tmJobIDExists() {
    //Given
    String tmJobId = "testID";
    JobDto expectedJobDto = new JobDto(tmJobId, null);
    when(restTemplate.getForEntity(any(), eq(JobDto.class), eq(tmJobId))).thenReturn(jobDtoResponseEntity);
    when(jobDtoResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
    when(jobDtoResponseEntity.getBody()).thenReturn(expectedJobDto);

    //When
    Optional<JobDto> jobDto = jobResourceService.findByTmJobId(tmJobId);

    //Then
    assertTrue(jobDto.isPresent());
  }

  @Test
  public void existsByTmJobIdAndLastAuthNo() {
    //Given
    String tmJobId = "testID";
    String lastAuthNo = "lastAuth";
    JobDto expectedJobDto = new JobDto(tmJobId, lastAuthNo);
    when(restTemplate.getForEntity(any(), eq(JobDto.class), eq(tmJobId))).thenReturn(jobDtoResponseEntity);
    when(jobDtoResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
    when(jobDtoResponseEntity.getBody()).thenReturn(expectedJobDto);

    //When
    boolean result = jobResourceService.existsByTmJobIdAndLastAuthNo(tmJobId, lastAuthNo);

    //Then
    assertTrue(result);
  }

  @Test
  public void doesNotExistsByTmJobIdAndLastAuthNo() {
    //Given
    String tmJobId = "testID";
    String lastAuthNo = "lastAuth";
    JobDto expectedJobDto = new JobDto(tmJobId, lastAuthNo);
    when(restTemplate.getForEntity(any(), eq(JobDto.class), eq(tmJobId)))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    ResourceServiceMalfunctionException exception = null;

    //When
    try {
      jobResourceService.existsByTmJobIdAndLastAuthNo(tmJobId, lastAuthNo);
    } catch (ResourceServiceMalfunctionException e) {
      exception = e;
    }

    //Then
    verify(restTemplate).getForEntity(any(), eq(JobDto.class), eq(tmJobId));
    assertNotNull(exception);
    assertTrue(exception.getMessage().contains(HttpStatus.BAD_REQUEST.toString()));
  }

  @Test
  public void findByTmJobId() {
    //Given
    String tmJobId = "testID";
    JobDto expectedJobDto = new JobDto(tmJobId, null);
    when(restTemplate.getForEntity(any(), eq(JobDto.class), eq(tmJobId)))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    ResourceServiceMalfunctionException exception = null;

    //When
    try {
      Optional<JobDto> result = jobResourceService.findByTmJobId(tmJobId);
    } catch (ResourceServiceMalfunctionException e) {
      exception = e;
    }

    //Then
    verify(restTemplate).getForEntity(any(), eq(JobDto.class), eq(tmJobId));
    assertNotNull(exception);
    assertTrue(exception.getMessage().contains(HttpStatus.BAD_REQUEST.toString()));
  }

  @Test
  public void createJob() {
    //Given
    String tmJobId = "testID";
    String lastAuthNo = "lastAuth";
    JobDto jobDto = new JobDto(tmJobId, lastAuthNo);
    HttpEntity<JobDto> request = new HttpEntity<>(jobDto);
    when(restTemplate.postForEntity(any(), eq(request), eq(Void.class), eq(jobDto))).thenReturn(voidResponseEntity);
    when(voidResponseEntity.getStatusCode()).thenReturn(HttpStatus.CREATED);

    //When
    jobResourceService.createJob(jobDto);

    //Then
    verify(restTemplate).postForEntity(any(), eq(request), eq(Void.class), eq(jobDto));
  }

  @Test
  public void shouldFailToCreateJobAndThrowHttpClientErrorException() {
    //Given
    String tmJobId = "testID";
    String lastAuthNo = "lastAuth";
    JobDto jobDto = new JobDto(tmJobId, lastAuthNo);
    HttpEntity<JobDto> request = new HttpEntity<>(jobDto);
    when(restTemplate.postForEntity(any(), eq(request), eq(Void.class), eq(jobDto)))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    ResourceServiceMalfunctionException exception = null;

    //When
    try {
      jobResourceService.createJob(jobDto);
    } catch (ResourceServiceMalfunctionException e) {
      exception = e;
    }

    //Then
    verify(restTemplate).postForEntity(any(), eq(request), eq(Void.class), eq(jobDto));
    assertNotNull(exception);
    assertTrue(exception.getMessage().contains(HttpStatus.BAD_REQUEST.toString()));
  }

  @Test
  public void updateJob() {
    //Given
    String tmJobId = "testID";
    String lastAuthNo = "lastAuth";
    JobDto jobDto = new JobDto(tmJobId, lastAuthNo);
    HttpEntity<JobDto> request = new HttpEntity<>(jobDto);
    doNothing().when(restTemplate).put(anyString(), any());

    //When
    jobResourceService.updateJob(jobDto);

    //Then
    verify(restTemplate).put(anyString(), any());
  }

  @Test
  public void shouldFailToUpdateJobAndThrowHttpClientErrorException() {
    //Given
    String tmJobId = "testID";
    String lastAuthNo = "lastAuth";
    JobDto jobDto = new JobDto(tmJobId, lastAuthNo);
    HttpEntity<JobDto> request = new HttpEntity<>(jobDto);
    doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST)).when(restTemplate).put(anyString(), any());

    ResourceServiceMalfunctionException exception = null;

    //When
    try {
      jobResourceService.updateJob(jobDto);
    } catch (ResourceServiceMalfunctionException e) {
      exception = e;
    }

    //Then
    verify(restTemplate).put(anyString(), any());
    assertNotNull(exception);
    assertTrue(exception.getMessage().contains(HttpStatus.BAD_REQUEST.toString()));
  }
}