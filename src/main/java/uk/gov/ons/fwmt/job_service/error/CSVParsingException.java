package uk.gov.ons.fwmt.job_service.error;

import lombok.Getter;

import java.util.List;

public class CSVParsingException extends FWMTCommonException {
  static final long serialVersionUID = 0L;

  @Getter private final List<String> rows;

  public CSVParsingException(String message, List<String> rows) {
    super(ExceptionCode.CSV_OTHER, message);
    this.rows = rows;
  }

  public CSVParsingException(String message, List<String> rows, Throwable cause) {
    super(ExceptionCode.CSV_OTHER, message, cause);
    this.rows = rows;
  }
}
