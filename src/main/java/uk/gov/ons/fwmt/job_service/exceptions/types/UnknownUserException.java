package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class UnknownUserException extends FWMTCommonException {
  static public final long serialVersionUID = 0L;

  public UnknownUserException(String authNo) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0005, "A user was not found on our system, authNo: " + authNo);
  }
}
