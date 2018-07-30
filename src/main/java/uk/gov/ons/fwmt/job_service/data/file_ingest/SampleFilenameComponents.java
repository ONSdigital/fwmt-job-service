package uk.gov.ons.fwmt.job_service.data.file_ingest;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType;

/**
 * This class describes a filename as seen by the FileIngesterService, split into constituent parts
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder 
public class SampleFilenameComponents {
  private String endpoint;
  private LegacySampleSurveyType tla;
  private LocalDateTime timestamp;
}
