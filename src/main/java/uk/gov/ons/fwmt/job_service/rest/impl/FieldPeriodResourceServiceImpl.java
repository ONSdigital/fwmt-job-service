package uk.gov.ons.fwmt.job_service.rest.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
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

    @PostConstruct
    private void initialize() {
        restTemplate.getInterceptors().add(basicInterceptor);
    }

    @Override
    public boolean existsByFieldPeriod(String fieldPeriod) {
        return false;
    }

    @Override
    public Optional<FieldPeriodDto> findByFieldPeriod(String fieldPeriod) {
        final ParameterizedTypeReference<List<FieldPeriodDto>> typeRef = new ParameterizedTypeReference<List<FieldPeriodDto>>() {};
        final ResponseEntity<List<FieldPeriodDto>> fiListResponseEntity = restTemplate.exchange("http://localhost:9095/fieldperiods", HttpMethod.GET, null, typeRef);
        if (fiListResponseEntity != null && fiListResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            List<FieldPeriodDto> fieldPeriodDtos  = fiListResponseEntity.getBody();
            return fieldPeriodDtos.stream().filter(fieldPeriodDto -> fieldPeriodDto.getFieldPeriod().equals(fieldPeriod)).findAny();
        }
        return Optional.of(null);
    }
}
