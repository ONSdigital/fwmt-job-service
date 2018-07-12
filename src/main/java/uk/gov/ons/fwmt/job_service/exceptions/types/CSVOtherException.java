package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class CSVOtherException extends FWMTCommonException {
  static public final long serialVersionUID = 0L;

  public CSVOtherException(String message) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0008, message);
  }

  public CSVOtherException(String message, Throwable cause) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0008, message, cause);
  }
}
