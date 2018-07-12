package uk.gov.ons.fwmt.job_service.rest.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
    log.debug("existsByTmJobId entered: tmJobId={}", tmJobId);
    final Optional<JobDto> jobDto = RestCommon.get(restTemplate, findUrl, JobDto.class, tmJobId);
    if (jobDto.isPresent()) {
      log.debug("existsByTmJobId found");
    } else {
      log.debug("existsByTmJobId not found");
    }
    return jobDto.isPresent();
  }

  @Override
  public boolean existsByTmJobIdAndLastAuthNo(String tmJobId, String lastAuthNo) {
    log.debug("existsByTmJobIdAndLastAuthNo entered: tmJobId={},lastAuthNo={}", tmJobId, lastAuthNo);
    final Optional<JobDto> jobDto = RestCommon.get(restTemplate, findUrl, JobDto.class, tmJobId);
    boolean result = jobDto.map(jobDto1 -> jobDto1.getLastAuthNo().equals(lastAuthNo)).orElse(false);
    if (result) {
      log.debug("existsByTmJobIdAndLastAuthNo found");
    } else {
      log.debug("existsByTmJobIdAndLastAuthNo not found");
    }
    return result;
  }

  @Override
  public Optional<JobDto> findByTmJobId(String tmJobId) {
    log.debug("findByTmJobId entered: tmJobId={}", tmJobId);
    final Optional<JobDto> jobDto = RestCommon.get(restTemplate, findUrl, JobDto.class, tmJobId);
    if (jobDto.isPresent()) {
      log.debug("findByTmJobId found: {}", jobDto.get());
    } else {
      log.debug("findByTmJobId not found");
    }
    return jobDto;
  }

  @Override
  public void createJob(JobDto jobDto) {
    log.debug("createJob entered: jobDto.tmJobId={}", jobDto.getTmJobId());
    RestCommon.post(restTemplate, createUrl, new HttpEntity<>(jobDto), Void.class, jobDto);
    log.debug("createJob posted");
  }

  @Override
  public void updateJob(JobDto jobDto) {
    log.debug("updateJob entered: jobDto.tmJobId={}", jobDto.getTmJobId());
    RestCommon.put(restTemplate, createUrl, new HttpEntity<>(jobDto));
    log.debug("updateJob updated");
  }

}
