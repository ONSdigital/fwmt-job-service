package uk.gov.ons.fwmt.job_service.rest.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FieldPeriodDto {
    Date startDate;
    Date endDate;
    String fieldPeriod;
}
