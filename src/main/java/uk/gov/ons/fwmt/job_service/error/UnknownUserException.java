package uk.gov.ons.fwmt.job_service.error;

public class UnknownUserException extends FWMTCommonException {
  static final long serialVersionUID = 0L;

  public UnknownUserException(String authNo) {
    super(ExceptionCode.UNKNOWN_USER_ID, "A user was not found on our system, authNo: " + authNo);
  }
}
