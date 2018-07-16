package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class CSVOtherException extends FWMTCommonException {
  static public final long serialVersionUID = 0L;

  public CSVOtherException(String message) {
    super(ExceptionCode.CSV_OTHER, message);
  }

  public CSVOtherException(String message, Throwable cause) {
    super(ExceptionCode.CSV_OTHER, message, cause);
  }
}
