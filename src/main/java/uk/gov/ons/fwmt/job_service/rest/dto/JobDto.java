package uk.gov.ons.fwmt.job_service.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobDto {

  private String tmJobId;

  private String lastAuthNo;

  private LocalDateTime lastUpdated;

}