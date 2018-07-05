package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class ResourceServiceInaccessibleException extends FWMTCommonRuntimeException {
  public ResourceServiceInaccessibleException() {
    super(ExceptionCode.FWMT_JOB_SERVICE_0012, "The resource service could not be contacted");
  }

  public ResourceServiceInaccessibleException(Throwable cause) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0012, "The resource service could not be contacted", cause);
  }

  public ResourceServiceInaccessibleException(String message) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0012,
        String.format("The resource service could not be contacted: %s", message));
  }

  public ResourceServiceInaccessibleException(String message, Throwable cause) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0012,
        String.format("The resource service could not be contacted: %s", message), cause);
  }
}
