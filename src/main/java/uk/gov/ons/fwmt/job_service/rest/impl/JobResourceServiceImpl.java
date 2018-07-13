package uk.gov.ons.fwmt.job_service.rest.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    log.debug("existsByTmJobId entered: tmJobId={}", tmJobId);
    final Optional<JobDto> jobDto = ResourceRESTHelper.get(restTemplate, findUrl, JobDto.class, tmJobId);
    if (jobDto.isPresent()) {
      log.debug("existsByTmJobId: JobDto found");
    } else {
      log.debug("existsByTmJobId: JobDto not found");
    }
    return jobDto.isPresent();
  }

  @Override
  public boolean existsByTmJobIdAndLastAuthNo(String tmJobId, String lastAuthNo) {
    log.debug("existsByTmJobIdAndLastAuthNo entered: tmJobId={},lastAuthNo={}", tmJobId, lastAuthNo);
    final Optional<JobDto> jobDto = ResourceRESTHelper.get(restTemplate, findUrl, JobDto.class, tmJobId);
    boolean result = jobDto.map(jobDto1 -> jobDto1.getLastAuthNo().equals(lastAuthNo)).orElse(false);
    if (result) {
      log.debug("existsByTmJobIdAndLastAuthNo: JobDto found");
    } else {
      log.debug("existsByTmJobIdAndLastAuthNo: JobDto not found");
    }
    return result;
  }

  @Override
  public Optional<JobDto> findByTmJobId(String tmJobId) {
    log.debug("findByTmJobId entered: tmJobId={}", tmJobId);
    final Optional<JobDto> jobDto = ResourceRESTHelper.get(restTemplate, findUrl, JobDto.class, tmJobId);
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
    ResourceRESTHelper.post(restTemplate, createUrl, new HttpEntity<>(jobDto), Void.class, jobDto);
    log.debug("createJob posted");
  }

  @Override
  public void updateJob(JobDto jobDto) {
    log.debug("updateJob entered: jobDto.tmJobId={}", jobDto.getTmJobId());
    ResourceRESTHelper.put(restTemplate, createUrl, new HttpEntity<>(jobDto));
    log.debug("updateJob updated");
  }

  @Override
  public boolean sendCSV(MultipartFile file) throws HttpClientErrorException, FileNotFoundException, IOException{
    log.debug("sendCSV");
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
    } catch (HttpClientErrorException HttpClientErrorException) {
      log.error("An error occurred while communicating with the resource service", HttpClientErrorException);
      throw HttpClientErrorException;
    } catch (FileNotFoundException e) {
      log.error("File cannot be found", e);
      throw e;
    } catch (IOException e) {
      log.error("File could not be converted", e);
      throw e;
    }
  }


  private File convertFile(MultipartFile file) throws IOException{
    File convFile = new File (file.getOriginalFilename());
    try (FileOutputStream fos = new FileOutputStream(convFile)) {
      fos.write(file.getBytes());
    }
    return convFile;
  }
}
