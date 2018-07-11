package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class CSVInvalidFieldException extends FWMTCommonException {
  static final long serialVersionUID = 0L;

  public CSVInvalidFieldException(String message) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0007, message);
  }

  public CSVInvalidFieldException(String message, Throwable cause) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0007, message, cause);
  }
}
