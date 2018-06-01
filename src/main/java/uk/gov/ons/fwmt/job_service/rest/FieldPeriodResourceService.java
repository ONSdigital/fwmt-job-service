package uk.gov.ons.fwmt.job_service.rest;

import uk.gov.ons.fwmt.job_service.rest.dto.FieldPeriodDto;

import java.util.List;
import java.util.Optional;

public interface FieldPeriodResourceService {
    boolean existsByFieldPeriod(String fieldPeriod);
    Optional<FieldPeriodDto> findByFieldPeriod(String fieldPeriod);
}
