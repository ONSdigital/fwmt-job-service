package uk.gov.ons.fwmt.job_service.rest;

import uk.gov.ons.fwmt.job_service.exceptions.types.ResourceServiceInaccessibleException;
import uk.gov.ons.fwmt.job_service.rest.dto.FieldPeriodDto;

import java.util.Optional;

public interface FieldPeriodResourceService {
    Optional<FieldPeriodDto> findByFieldPeriod(String fieldPeriod);
}
