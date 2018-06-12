package uk.gov.ons.fwmt.job_service.rest.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.job_service.rest.FieldPeriodResourceService;
import uk.gov.ons.fwmt.job_service.rest.dto.FieldPeriodDto;

import java.util.Optional;

@Slf4j
@Service
public class FieldPeriodResourceServiceImpl implements FieldPeriodResourceService {
  private transient RestTemplate restTemplate;

  private transient String findUrl;

  @Autowired
  public FieldPeriodResourceServiceImpl(
      RestTemplate restTemplate,
      @Value("${service.resource.baseUrl}") String baseUrl,
      @Value("${service.resource.operation.fieldPeriods.find.path}") String findPath) {
    this.restTemplate = restTemplate;
    this.findUrl = baseUrl + findPath;
  }

  @Override
  public Optional<FieldPeriodDto> findByFieldPeriod(final String fieldPeriod) {
    log.info("findByFieldPeriod: {}", fieldPeriod);
    try {
      final ResponseEntity<FieldPeriodDto> responseEntity = restTemplate
          .getForEntity(findUrl, FieldPeriodDto.class, fieldPeriod);
      if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
        return Optional.ofNullable(responseEntity.getBody());
      }
    } catch (HttpClientErrorException httpException) {
      log.error("An error occurred while communicating with the resource service", httpException);
    }
    return Optional.empty();
  }
}
