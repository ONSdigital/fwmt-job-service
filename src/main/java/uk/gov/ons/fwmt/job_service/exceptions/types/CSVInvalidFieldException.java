package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class CSVInvalidFieldException extends FWMTCommonException {
  static public final long serialVersionUID = 0L;

  private static String makeMessage(String reason) {
    return "The CSV contained an invalid field: " + reason;
  }

  public CSVInvalidFieldException(String reason) {
    super(ExceptionCode.CSV_INVALID_FIELD, makeMessage(reason));
  }

  public CSVInvalidFieldException(String reason, Throwable cause) {
    super(ExceptionCode.CSV_INVALID_FIELD, makeMessage(reason), cause);
  }
}
