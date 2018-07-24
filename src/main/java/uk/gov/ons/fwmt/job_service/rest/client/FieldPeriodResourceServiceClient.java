package uk.gov.ons.fwmt.job_service.rest.client;

import java.util.Optional;

import uk.gov.ons.fwmt.job_service.rest.client.dto.FieldPeriodDto;

public interface FieldPeriodResourceServiceClient {
    Optional<FieldPeriodDto> findByFieldPeriod(String fieldPeriod);
}
