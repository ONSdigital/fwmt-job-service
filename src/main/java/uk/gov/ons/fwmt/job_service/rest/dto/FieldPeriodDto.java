package uk.gov.ons.fwmt.job_service.rest.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FieldPeriodDto {
    LocalDate startDate;
    LocalDate endDate;
    String fieldPeriod;
}
