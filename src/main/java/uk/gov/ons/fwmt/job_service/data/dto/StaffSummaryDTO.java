package uk.gov.ons.fwmt.job_service.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StaffSummaryDTO {

  private String filename;
  private int rows;

}
