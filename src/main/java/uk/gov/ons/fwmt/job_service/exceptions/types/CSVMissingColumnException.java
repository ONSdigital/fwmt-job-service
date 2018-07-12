package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class CSVMissingColumnException extends FWMTCommonException {
  static final long serialVersionUID = 0L;

  public CSVMissingColumnException(String message) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0006, message);
  }

  public CSVMissingColumnException(String message, Throwable cause) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0006, message, cause);
  }
}
