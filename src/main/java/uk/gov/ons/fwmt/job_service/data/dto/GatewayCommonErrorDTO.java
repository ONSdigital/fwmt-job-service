package uk.gov.ons.fwmt.job_service.data.dto;

import lombok.Data;

@Data
public class GatewayCommonErrorDTO {
  public String error;
  public String exception;
  public String message;
  public String path;
  public int status;
  public String timestamp;
}
