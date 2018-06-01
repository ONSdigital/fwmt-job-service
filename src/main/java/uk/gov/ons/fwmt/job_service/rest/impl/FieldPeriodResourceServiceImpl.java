package uk.gov.ons.fwmt.job_service.rest.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.job_service.rest.FieldPeriodResourceService;
import uk.gov.ons.fwmt.job_service.rest.dto.FieldPeriodDto;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class FieldPeriodResourceServiceImpl implements FieldPeriodResourceService {

    @Autowired
    private transient RestTemplate restTemplate;
    @Autowired
    private transient BasicAuthorizationInterceptor basicInterceptor;
    @Value("${service.resource.fieldPeriod.operation.find.fieldPeriodUrl}")
    private transient String findURL;

    @PostConstruct
    private void initialize() {
        restTemplate.getInterceptors().add(basicInterceptor);
    }

    @Override
    public boolean existsByFieldPeriod(final String fieldPeriod) {
        return false;
    }

    @Override
    public Optional<FieldPeriodDto> findByFieldPeriod(final String fieldPeriod) {
        try {
            final ParameterizedTypeReference<List<FieldPeriodDto>> typeRef = new ParameterizedTypeReference<List<FieldPeriodDto>>() {
            };
            final ResponseEntity<List<FieldPeriodDto>> fiListResponseEntity = restTemplate.exchange(findURL, HttpMethod.GET, null, typeRef);
            if (fiListResponseEntity != null && fiListResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                final List<FieldPeriodDto> fieldPeriodDtos = fiListResponseEntity.getBody();
                return fieldPeriodDtos.stream().filter(fieldPeriodDto -> fieldPeriodDto.getFieldPeriod().equals(fieldPeriod)).findAny();
            }
            return Optional.empty();
        } catch(HttpClientErrorException ht) {
            //TODO log error
        }
        return Optional.empty();
    }
}
