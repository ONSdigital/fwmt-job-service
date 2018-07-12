package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class UnknownException extends FWMTCommonException {
  static public final long serialVersionUID = 0L;

  public UnknownException(Exception cause) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0001, null, cause);
  }
}
