package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class CSVInvalidFieldException extends FWMTCommonException {
  static public final long serialVersionUID = 0L;

  private static String makeMessage(String reason) {
    return "The CSV contained an invalid field: " + reason;
  }

  public CSVInvalidFieldException(String reason) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0007, makeMessage(reason));
  }

  public CSVInvalidFieldException(String reason, Throwable cause) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0007, makeMessage(reason), cause);
  }
}
