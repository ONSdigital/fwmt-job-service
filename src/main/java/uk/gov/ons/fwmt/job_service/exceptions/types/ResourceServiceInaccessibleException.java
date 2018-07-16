package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class ResourceServiceInaccessibleException extends FWMTCommonException {
  static public final long serialVersionUID = 0L;

  public ResourceServiceInaccessibleException() {
    super(ExceptionCode.RESOURCE_SERVICE_INACCESSIBLE, "The resource service could not be contacted");
  }

  public ResourceServiceInaccessibleException(Throwable cause) {
    super(ExceptionCode.RESOURCE_SERVICE_INACCESSIBLE, "The resource service could not be contacted", cause);
  }

  public ResourceServiceInaccessibleException(String message) {
    super(ExceptionCode.RESOURCE_SERVICE_INACCESSIBLE,
        String.format("The resource service could not be contacted: %s", message));
  }

  public ResourceServiceInaccessibleException(String message, Throwable cause) {
    super(ExceptionCode.RESOURCE_SERVICE_INACCESSIBLE,
        String.format("The resource service could not be contacted: %s", message), cause);
  }
}
