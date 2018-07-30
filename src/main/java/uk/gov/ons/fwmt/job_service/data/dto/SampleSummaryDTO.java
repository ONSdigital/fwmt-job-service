package uk.gov.ons.fwmt.job_service.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import uk.gov.ons.fwmt.job_service.data.csv_parser.UnprocessedCSVRow;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class SampleSummaryDTO {
  private final String filename;
  private final int processedRows;
  private final List<UnprocessedCSVRow> unprocessedRows;

}
