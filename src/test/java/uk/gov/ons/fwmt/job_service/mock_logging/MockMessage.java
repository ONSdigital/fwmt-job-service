package uk.gov.ons.fwmt.job_service.mock_logging;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MockMessage {

  public String endpoint;

  public LocalDateTime requestTimestamp;
  public String requestRawHeaders;
  public String requestRawHtml;
  public Object requestMessageParsed;

  public LocalDateTime responseTimestamp;
  public String responseRawHeaders;
  public String responseRawHtml;
  public Object responseMessageParsed;
  public boolean isFault;

}
