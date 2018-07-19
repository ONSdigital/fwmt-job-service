package uk.gov.ons.fwmt.job_service.rest.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.job_service.rest.dto.JobDto;

import java.io.File;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JobResourceServiceImplTest {

  @InjectMocks private JobResourceServiceImpl jobResourceService;
  @Mock private RestTemplate restTemplate;
  @Mock private ResponseEntity responseEntity;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void existsByTmJobId() {
    //Given
    String tmJobId = "testID";
    JobDto expectedJobDto = new JobDto(tmJobId, null);
    when(restTemplate.getForEntity(any(), eq(JobDto.class), eq(tmJobId))).thenReturn(responseEntity);
    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
    when(responseEntity.getBody()).thenReturn(expectedJobDto);

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
    when(restTemplate.getForEntity(any(), eq(JobDto.class), eq(tmJobId))).thenReturn(responseEntity);
    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
    when(responseEntity.getBody()).thenReturn(expectedJobDto);

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
    when(restTemplate.getForEntity(any(), eq(JobDto.class), eq(tmJobId))).thenReturn(responseEntity);
    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
    when(responseEntity.getBody()).thenReturn(expectedJobDto);

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

    //When
    boolean result = jobResourceService.existsByTmJobIdAndLastAuthNo(tmJobId, lastAuthNo);

    //Then
    assertFalse(result);
    verify(restTemplate).getForEntity(any(), eq(JobDto.class), eq(tmJobId));
  }

  @Test
  public void findByTmJobId() {
    //Given
    String tmJobId = "testID";
    JobDto expectedJobDto = new JobDto(tmJobId, null);
    when(restTemplate.getForEntity(any(), eq(JobDto.class), eq(tmJobId)))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    //When
    Optional<JobDto> result = jobResourceService.findByTmJobId(tmJobId);

    //Then
    assertFalse(result.isPresent());
    verify(restTemplate).getForEntity(any(), eq(JobDto.class), eq(tmJobId));
  }

  @Test
  public void createJob() {
    //Given
    String tmJobId = "testID";
    String lastAuthNo = "lastAuth";
    JobDto jobDto = new JobDto(tmJobId, lastAuthNo);
    HttpEntity<JobDto> request = new HttpEntity<>(jobDto);
    when(restTemplate.postForEntity(any(), eq(request), eq(Void.class), eq(jobDto))).thenReturn(responseEntity);
    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.CREATED);

    //When
    Boolean result = jobResourceService.createJob(jobDto);

    //Then
    verify(restTemplate).postForEntity(any(), eq(request), eq(Void.class), eq(jobDto));
    assertTrue(result);
  }

  @Test
  public void shouldFailToCreatJobAndThrowHttpClientErrorException() {
    //Given
    String tmJobId = "testID";
    String lastAuthNo = "lastAuth";
    JobDto jobDto = new JobDto(tmJobId, lastAuthNo);
    HttpEntity<JobDto> request = new HttpEntity<>(jobDto);
    when(restTemplate.postForEntity(any(), eq(request), eq(Void.class), eq(jobDto)))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    //When
    Boolean result = jobResourceService.createJob(jobDto);

    //Then
    verify(restTemplate).postForEntity(any(), eq(request), eq(Void.class), eq(jobDto));
    assertFalse(result);
  }

  @Test
  public void updateJob() {
    //Given
    String tmJobId = "testID";
    String lastAuthNo = "lastAuth";
    JobDto jobDto = new JobDto(tmJobId, lastAuthNo);
    HttpEntity<JobDto> request = new HttpEntity<>(jobDto);
    when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(Void.class))).thenReturn(responseEntity);
    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);

    //When
    Boolean result = jobResourceService.updateJob(jobDto);

    //Then
    verify(restTemplate).exchange(anyString(), eq(HttpMethod.PUT), eq(request), eq(Void.class));
    assertTrue(result);
  }

  @Test
  public void shouldFailToUpdateJobAndThrowHttpClientErrorException() {
    //Given
    String tmJobId = "testID";
    String lastAuthNo = "lastAuth";
    JobDto jobDto = new JobDto(tmJobId, lastAuthNo);
    HttpEntity<JobDto> request = new HttpEntity<>(jobDto);
    when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), eq(request), eq(Void.class)))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    //When
    Boolean result = jobResourceService.updateJob(jobDto);

    //Then
    verify(restTemplate).exchange(anyString(), eq(HttpMethod.PUT), eq(request), eq(Void.class));
    assertFalse(result);
  }

  @Test
  public void sendCSV() {
    File file = new File("bla");
    when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class))).thenReturn(responseEntity);
    jobResourceService.sendCSV(file, true);
    verify(restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class));
  }

  @Test
  public void sendCSV4xxError() {
    File file = new File("bla");
    when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class)))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
    jobResourceService.sendCSV(file, true);
    verify(restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class));
  }

}