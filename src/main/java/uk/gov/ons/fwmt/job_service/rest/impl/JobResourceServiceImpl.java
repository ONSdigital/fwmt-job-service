package uk.gov.ons.fwmt.job_service.rest.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;
import uk.gov.ons.fwmt.job_service.rest.JobResourceService;
import uk.gov.ons.fwmt.job_service.rest.dto.JobDto;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Slf4j
@Service
public class JobResourceServiceImpl implements JobResourceService {

    @Autowired
    private transient RestTemplate restTemplate;
    @Autowired
    private transient BasicAuthorizationInterceptor basicInterceptor;
    @Value("${service.resource.jobs.operation.find.jobUrl}")
    private transient String findURL;
    @Value("${service.resource.jobs.operation.create.url}")
    private transient String createURL;
    @Value("${service.resource.jobs.operation.update.url}")
    private transient String updateUrl;

    @PostConstruct
    private void initialize() {
        restTemplate.getInterceptors().add(basicInterceptor);
    }

    @Override
    public boolean existsByTmJobId(String tmJobId) {
        final Optional<JobDto> jobDto = findByTmJobId(tmJobId);
        return jobDto.isPresent();
    }

    @Override
    public boolean existsByTmJobIdAndLastAuthNo(String tmJobId, String lastAuthNo) {
        final Optional<JobDto> jobDto = findByTmJobId(tmJobId);
        if(jobDto.isPresent()) {
            return jobDto.get().getLastAuthNo().equals(lastAuthNo);
        }
        return false;
    }

    @Override
    public Optional<JobDto> findByTmJobId(String tmJobId) {
        try {
            final ResponseEntity<JobDto> jobDtoResponseEntity = restTemplate.exchange(findURL, HttpMethod.GET, null, JobDto.class, tmJobId);
            if (jobDtoResponseEntity != null && jobDtoResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                return Optional.ofNullable(jobDtoResponseEntity.getBody());
            }
        } catch(HttpClientErrorException httpClientErrorException) {
            //TODO log error correctly
            log.error("findByTmJobId failed with error code: {}",httpClientErrorException.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean createJob(JobDto jobDto) {
        try {
            final HttpEntity<JobDto> request = new HttpEntity<>(jobDto);
            log.info("CreateJob :{}",jobDto.toString());
            final ResponseEntity jobDtoResponseEntity = restTemplate.exchange(createURL, HttpMethod.POST, request, Void.class);
            return jobDtoResponseEntity.getStatusCode().equals(HttpStatus.CREATED);
        } catch (HttpClientErrorException httpClientErrorException) {
            //TODO log error correctly
            log.error("createJob failed with error code: {}",httpClientErrorException.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateJob(JobDto jobDto) {
        try {
            final HttpEntity<JobDto> request = new HttpEntity<>(jobDto);
            log.info("UpdateJob :{}",jobDto.toString());
            final ResponseEntity jobDtoResponseEntity = restTemplate.exchange(updateUrl, HttpMethod.PUT, request, Void.class);
            return jobDtoResponseEntity.getStatusCode().equals(HttpStatus.OK);
        } catch(HttpClientErrorException httpClientErrorException) {
            //TODO log error
            log.error("updateJob failed with error code: {}",httpClientErrorException.getMessage());
        }
        return false;
    }

}
