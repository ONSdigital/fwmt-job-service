package uk.gov.ons.fwmt.job_service.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

  private String authNo;

  private String alternateAuthNo;

  private boolean active;

  private String tmUsername;


}
