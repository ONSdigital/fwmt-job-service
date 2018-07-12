package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class TMInaccessibleException extends FWMTCommonRuntimeException {
  static public final long serialVersionUID = 0L;

  public TMInaccessibleException() {
    super(ExceptionCode.FWMT_JOB_SERVICE_0010, "The resource service could not be contacted");
  }

  public TMInaccessibleException(Throwable cause) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0010, "The resource service could not be contacted", cause);
  }

  public TMInaccessibleException(String message) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0010,
        String.format("The resource service could not be contacted: %s", message));
  }

  public TMInaccessibleException(String message, Throwable cause) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0010,
        String.format("The resource service could not be contacted: %s", message), cause);
  }
}
