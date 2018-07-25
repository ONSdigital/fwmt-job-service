package uk.gov.ons.fwmt.job_service.rest.client.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FieldPeriodDto {
    LocalDate startDate;
    LocalDate endDate;
    String fieldPeriod;
}
