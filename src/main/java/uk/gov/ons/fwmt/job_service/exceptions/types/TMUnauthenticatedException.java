package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class TMUnauthenticatedException extends FWMTCommonException {
  static public final long serialVersionUID = 0L;

  public TMUnauthenticatedException() {
    super(ExceptionCode.TM_UNAUTHENTICATED, "The resource service rejected our credentials");
  }

  public TMUnauthenticatedException(Throwable cause) {
    super(ExceptionCode.TM_UNAUTHENTICATED, "The resource service rejected our credentials", cause);
  }

  public TMUnauthenticatedException(String message) {
    super(ExceptionCode.TM_UNAUTHENTICATED,
        String.format("The resource service rejected our credentials: %s", message));
  }

  public TMUnauthenticatedException(String message, Throwable cause) {
    super(ExceptionCode.TM_UNAUTHENTICATED,
        String.format("The resource service rejected our credentials: %s", message), cause);
  }
}
