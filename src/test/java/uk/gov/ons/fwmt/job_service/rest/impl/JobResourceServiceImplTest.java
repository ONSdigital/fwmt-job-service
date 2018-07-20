package uk.gov.ons.fwmt.job_service.rest.impl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service.rest.dto.JobDto;

import java.io.File;
import java.util.Optional;

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

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

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
    when(restTemplate.getForEntity(any(), eq(JobDto.class), eq(tmJobId)))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    expectedException.expect(FWMTCommonException.class);
    expectedException.expectMessage(ExceptionCode.RESOURCE_SERVICE_MALFUNCTION.getCode());
    expectedException.expectMessage(HttpStatus.BAD_REQUEST.toString());

    //When
    jobResourceService.existsByTmJobIdAndLastAuthNo(tmJobId, lastAuthNo);

    //Then
    verify(restTemplate).getForEntity(any(), eq(JobDto.class), eq(tmJobId));
  }

  @Test
  public void findByTmJobId() {
    //Given
    String tmJobId = "testID";
    when(restTemplate.getForEntity(any(), eq(JobDto.class), eq(tmJobId)))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    expectedException.expect(FWMTCommonException.class);
    expectedException.expectMessage(ExceptionCode.RESOURCE_SERVICE_MALFUNCTION.getCode());
    expectedException.expectMessage(HttpStatus.BAD_REQUEST.toString());

    //When
    jobResourceService.findByTmJobId(tmJobId);

    //Then
    verify(restTemplate).getForEntity(any(), eq(JobDto.class), eq(tmJobId));
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

    expectedException.expect(FWMTCommonException.class);
    expectedException.expectMessage(ExceptionCode.RESOURCE_SERVICE_MALFUNCTION.getCode());
    expectedException.expectMessage(HttpStatus.BAD_REQUEST.toString());

    //When
    jobResourceService.createJob(jobDto);

    //Then
    verify(restTemplate).postForEntity(any(), eq(request), eq(Void.class), eq(jobDto));
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
    doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST)).when(restTemplate).put(anyString(), any());

    expectedException.expect(FWMTCommonException.class);
    expectedException.expectMessage(ExceptionCode.RESOURCE_SERVICE_MALFUNCTION.getCode());
    expectedException.expectMessage(HttpStatus.BAD_REQUEST.toString());

    //When
    jobResourceService.updateJob(jobDto);

    //Then
    verify(restTemplate).put(anyString(), any());
  }

  @Test
  public void storeCSV() {
    File file = new File("bla");
    when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class))).thenReturn(responseEntity);
    jobResourceService.storeCSV(file, true);
    verify(restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class));
  }

  @Test
  public void storeCSV4xxError() {
    File file = new File("bla");
    when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class)))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
    jobResourceService.storeCSV(file, true);
    verify(restTemplate).exchange(anyString(), eq(HttpMethod.POST), any(), eq(String.class));
  }

}
