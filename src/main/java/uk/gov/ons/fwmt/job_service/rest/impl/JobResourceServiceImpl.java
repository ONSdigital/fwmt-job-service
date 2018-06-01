package uk.gov.ons.fwmt.job_service.rest.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.job_service.rest.JobResourceService;
import uk.gov.ons.fwmt.job_service.rest.dto.JobDto;
import uk.gov.ons.fwmt.job_service.rest.dto.UserDto;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class JobResourceServiceImpl implements JobResourceService {

    @Autowired
    private transient RestTemplate restTemplate;
    @Autowired
    private transient BasicAuthorizationInterceptor basicInterceptor;

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
        final ResponseEntity<JobDto> jobDtoResponseEntity = restTemplate.exchange("http://localhost:9095/jobs/{authNo}", HttpMethod.GET, null, JobDto.class, tmJobId);
        if (jobDtoResponseEntity != null && jobDtoResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return Optional.of(jobDtoResponseEntity.getBody());
        }
        return Optional.of(null);
    }

    @Override
    public boolean createJob(JobDto jobDto) {
        final HttpEntity<JobDto> request = new HttpEntity<>(jobDto);
        final ResponseEntity jobDtoResponseEntity = restTemplate.exchange("http://localhost:9095/jobs/", HttpMethod.POST, request, Void.class);

        return jobDtoResponseEntity.getStatusCode().equals(HttpStatus.CREATED);
    }

    @Override
    public boolean updateJob(JobDto jobDto) {
        final HttpEntity<JobDto> request = new HttpEntity<>(jobDto);
        final ResponseEntity jobDtoResponseEntity = restTemplate.exchange("http://localhost:9095/jobs/", HttpMethod.PUT, request, Void.class);
        return jobDtoResponseEntity.getStatusCode().equals(HttpStatus.OK);
    }

}
