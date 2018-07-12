package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class ResourceServiceUnauthenticatedException extends FWMTCommonRuntimeException {
  static public final long serialVersionUID = 0L;

  public ResourceServiceUnauthenticatedException() {
    super(ExceptionCode.FWMT_JOB_SERVICE_0012, "The resource service rejected our credentials");
  }

  public ResourceServiceUnauthenticatedException(Throwable cause) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0012, "The resource service rejected our credentials", cause);
  }

  public ResourceServiceUnauthenticatedException(String message) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0012,
        String.format("The resource service rejected out credentials: %s", message));
  }

  public ResourceServiceUnauthenticatedException(String message, Throwable cause) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0012,
        String.format("The resource service rejected our credentials: %s", message), cause);
  }
}
