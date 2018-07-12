package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class TMUnauthenticatedException extends FWMTCommonRuntimeException {
  static public final long serialVersionUID = 0L;

  public TMUnauthenticatedException() {
    super(ExceptionCode.FWMT_JOB_SERVICE_0009, "The resource service rejected our credentials");
  }

  public TMUnauthenticatedException(Throwable cause) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0009, "The resource service rejected our credentials", cause);
  }

  public TMUnauthenticatedException(String message) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0009,
        String.format("The resource service rejected our credentials: %s", message));
  }

  public TMUnauthenticatedException(String message, Throwable cause) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0009,
        String.format("The resource service rejected our credentials: %s", message), cause);
  }
}
