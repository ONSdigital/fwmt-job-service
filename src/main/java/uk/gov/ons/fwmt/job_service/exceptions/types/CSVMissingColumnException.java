package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class CSVMissingColumnException extends FWMTCommonException {
  static public final long serialVersionUID = 0L;

  private static String makeMessage(String fieldName) {
    return "The CSV was missing the column '" + fieldName + "'";
  }

  public CSVMissingColumnException(String fieldName) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0006, makeMessage(fieldName));
  }

  public CSVMissingColumnException(String fieldName, Throwable cause) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0006, makeMessage(fieldName), cause);
  }
}
