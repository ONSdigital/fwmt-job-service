package uk.gov.ons.fwmt.job_service.rest.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.job_service.rest.JobResourceService;
import uk.gov.ons.fwmt.job_service.rest.dto.JobDto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class JobResourceServiceImpl implements JobResourceService {
  private transient RestTemplate restTemplate;

  private transient String findUrl;
  private transient String createUrl;
  private transient String updateUrl;
  private transient String sendCSVUrl;

  @Autowired
  public JobResourceServiceImpl(
      RestTemplate restTemplate,
      @Value("${service.resource.baseUrl}") String baseUrl,
      @Value("${service.resource.operation.jobs.find.path}") String findPath,
      @Value("${service.resource.operation.jobs.create.path}") String createPath,
      @Value("${service.resource.operation.jobs.update.path}") String updatePath,
      @Value("${service.resource.operation.jobs.sendcsv.path}") String sendCSVPath) {
    this.restTemplate = restTemplate;
    this.findUrl = baseUrl + findPath;
    this.createUrl = baseUrl + createPath;
    this.updateUrl = baseUrl + updatePath;
    this.sendCSVUrl = baseUrl + sendCSVPath;
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
    log.info("findByTmJobId: {}", tmJobId);
    try {
      final ResponseEntity<JobDto> jobDtoResponseEntity = restTemplate.getForEntity(findUrl, JobDto.class, tmJobId);
      if (jobDtoResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
        return Optional.ofNullable(jobDtoResponseEntity.getBody());
      }
    } catch (HttpClientErrorException httpClientErrorException) {
      log.error("An error occurred while communicating with the resource service", httpClientErrorException);
    }
    return Optional.empty();
  }

  @Override
  public boolean createJob(JobDto jobDto) {
    log.info("CreateJob: {}", jobDto.toString());
    try {
      final HttpEntity<JobDto> request = new HttpEntity<>(jobDto);
      final ResponseEntity responseEntity = restTemplate.postForEntity(createUrl, request, Void.class, jobDto);
      return responseEntity.getStatusCode().equals(HttpStatus.CREATED);
    } catch (HttpClientErrorException httpClientErrorException) {
      log.error("An error occurred while communicating with the resource service", httpClientErrorException);
    }
    return false;
  }

  // TODO can we use the restTemplate.put method?
  @Override
  public boolean updateJob(JobDto jobDto) {
    log.info("UpdateJob: {}", jobDto.toString());
    try {
      final HttpEntity<JobDto> request = new HttpEntity<>(jobDto);
      final ResponseEntity jobDtoResponseEntity = restTemplate.exchange(updateUrl, HttpMethod.PUT, request, Void.class);
      return jobDtoResponseEntity.getStatusCode().equals(HttpStatus.OK);
    } catch (HttpClientErrorException httpClientErrorException) {
      log.error("An error occurred while communicating with the resource service", httpClientErrorException);
    }
    return false;
  }

  @Override
  public boolean sendCSV(MultipartFile file) {

    log.info("SendCSV");
    try {

      File convFile = convertFile(file);
      Resource fileConvert = new FileSystemResource(convFile);

      MultiValueMap<String,Object> bodyMap = new LinkedMultiValueMap<>();
      bodyMap.add("file",fileConvert);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);

      final HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(bodyMap, headers);
      final ResponseEntity<String> sendCSVResponseEntity = restTemplate.exchange(sendCSVUrl, HttpMethod.POST, request, String.class);
      return sendCSVResponseEntity.getStatusCode().equals(HttpStatus.OK);
    } catch (HttpClientErrorException httpClientErrorException) {
      log.error("An error occurred while communicating with the resource service", httpClientErrorException);
    } catch (FileNotFoundException e) {
      log.error("File cannot be found", e);
    } catch (IOException e) {
      log.error("File could not be converted", e);
    }
    return false;
  }


  private File convertFile(MultipartFile file) throws IOException{

    File convFile = new File (file.getOriginalFilename());
    FileOutputStream fos = new FileOutputStream(convFile);
    try {
      if(!convFile.createNewFile()) {
        fos.write(file.getBytes());
      }else {
        return null;
      }
    } finally {
      fos.close();
    }
    return convFile;
  }
}
