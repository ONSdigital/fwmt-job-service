package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class TMInaccessibleException extends FWMTCommonException {
  static public final long serialVersionUID = 0L;

  public TMInaccessibleException() {
    super(ExceptionCode.TM_INACCESSIBLE, "The resource service could not be contacted");
  }

  public TMInaccessibleException(Throwable cause) {
    super(ExceptionCode.TM_INACCESSIBLE, "The resource service could not be contacted", cause);
  }

  public TMInaccessibleException(String message) {
    super(ExceptionCode.TM_INACCESSIBLE,
        String.format("The resource service could not be contacted: %s", message));
  }

  public TMInaccessibleException(String message, Throwable cause) {
    super(ExceptionCode.TM_INACCESSIBLE,
        String.format("The resource service could not be contacted: %s", message), cause);
  }
}
