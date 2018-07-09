package uk.gov.ons.fwmt.job_service.rest.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.job_service.exceptions.types.ResourceServiceInaccessibleException;
import uk.gov.ons.fwmt.job_service.exceptions.types.ResourceServiceMalfunctionException;
import uk.gov.ons.fwmt.job_service.rest.JobResourceService;
import uk.gov.ons.fwmt.job_service.rest.dto.JobDto;

import java.util.Optional;

@Slf4j
@Service
public class JobResourceServiceImpl implements JobResourceService {
  private transient RestTemplate restTemplate;

  private transient String findUrl;
  private transient String createUrl;
  private transient String updateUrl;

  @Autowired
  public JobResourceServiceImpl(
      RestTemplate restTemplate,
      @Value("${service.resource.baseUrl}") String baseUrl,
      @Value("${service.resource.operation.jobs.find.path}") String findPath,
      @Value("${service.resource.operation.jobs.create.path}") String createPath,
      @Value("${service.resource.operation.jobs.update.path}") String updatePath) {
    this.restTemplate = restTemplate;
    this.findUrl = baseUrl + findPath;
    this.createUrl = baseUrl + createPath;
    this.updateUrl = baseUrl + updatePath;
  }

  @Override
  public boolean existsByTmJobId(String tmJobId) {
    final Optional<JobDto> jobDto = findByTmJobId(tmJobId);
    return jobDto.isPresent();
  }

  @Override
  public boolean existsByTmJobIdAndLastAuthNo(String tmJobId, String lastAuthNo) {
    final Optional<JobDto> jobDto = findByTmJobId(tmJobId);
    return jobDto.map(jobDto1 -> jobDto1.getLastAuthNo().equals(lastAuthNo)).orElse(false);
  }

  @Override
  public Optional<JobDto> findByTmJobId(String tmJobId) {
    log.info("JobResourceService.findByTmJobId entered: tmJobId={}", tmJobId);
    Optional<JobDto> jobDto = RestCommon.get(restTemplate, findUrl, JobDto.class, tmJobId);
    if (jobDto.isPresent()) {
      log.info("JobResourceService.findByTmJobId found: {}", jobDto.get());
    } else {
      log.info("JobResourceService.findByTmJobId not found");
    }
    return jobDto;
  }

  @Override
  public boolean createJob(JobDto jobDto) {
    log.info("CreateJob: {}", jobDto.toString());
//    RestCommon.post(restTemplate, createUrl, new HttpEntity<>(jobDto), Void.class, jobDto);
    try {
      final HttpEntity<JobDto> request = new HttpEntity<>(jobDto);
      final ResponseEntity responseEntity = restTemplate.postForEntity(createUrl, request, Void.class, jobDto);
      return responseEntity.getStatusCode().equals(HttpStatus.CREATED);
    } catch (HttpClientErrorException httpClientErrorException) {
      log.error("An error occurred while communicating with the resource service", httpClientErrorException);
      return false;
    }
  }

  // TODO can we use the restTemplate.put method?
  @Override
  public boolean updateJob(JobDto jobDto) {
    log.info("UpdateJob: {}", jobDto.toString());
    //    RestCommon.update(restTemplate, createUrl, new HttpEntity<>(jobDto), Void.class, jobDto);
    try {
      final HttpEntity<JobDto> request = new HttpEntity<>(jobDto);
      final ResponseEntity jobDtoResponseEntity = restTemplate.exchange(updateUrl, HttpMethod.PUT, request, Void.class);
      return jobDtoResponseEntity.getStatusCode().equals(HttpStatus.OK);
    } catch (HttpClientErrorException httpClientErrorException) {
      log.error("An error occurred while communicating with the resource service", httpClientErrorException);
    }
    return false;
  }

}
