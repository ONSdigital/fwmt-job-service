package uk.gov.ons.fwmt.job_service.rest.client.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.fwmt.job_service.rest.client.FieldPeriodResourceServiceClient;
import uk.gov.ons.fwmt.job_service.rest.client.ResourceRESTHelper;
import uk.gov.ons.fwmt.job_service.rest.client.dto.FieldPeriodDto;

@Slf4j
@Service
public class FieldPeriodResourceServiceClientImpl implements FieldPeriodResourceServiceClient {
  private transient RestTemplate restTemplate;

  private transient String findUrl;

  @Autowired
  public FieldPeriodResourceServiceClientImpl(
      RestTemplate restTemplate,
      @Value("${service.resource.baseUrl}") String baseUrl,
      @Value("${service.resource.operation.fieldPeriods.find.path}") String findPath) {
    this.restTemplate = restTemplate;
    this.findUrl = baseUrl + findPath;
  }

  @Override
  public Optional<FieldPeriodDto> findByFieldPeriod(final String fieldPeriod) {
    log.debug("Start: fieldPeriod={}", fieldPeriod);
    final Optional<FieldPeriodDto> jobDto = ResourceRESTHelper
        .get(restTemplate, findUrl, FieldPeriodDto.class, fieldPeriod);
    if (jobDto.isPresent()) {
      log.debug("Found: {}", jobDto.get());
    } else {
      log.debug("Not found");
    }
    return jobDto;
  }
}
