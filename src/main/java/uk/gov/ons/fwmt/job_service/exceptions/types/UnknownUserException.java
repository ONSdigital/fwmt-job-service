package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class UnknownUserException extends FWMTCommonException {
  static public final long serialVersionUID = 0L;

  public UnknownUserException(String authNo) {
    super(ExceptionCode.UNKNOWN_USER_ID, "A user was not found on our system, authNo: " + authNo);
  }
}
