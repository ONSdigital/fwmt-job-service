package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class TMMalfunctionException extends FWMTCommonException {
  static public final long serialVersionUID = 0L;

  public TMMalfunctionException() {
    super(ExceptionCode.TM_MALFUNCTION, "The resource service malfunctioned");
  }

  public TMMalfunctionException(Throwable cause) {
    super(ExceptionCode.TM_MALFUNCTION, "The resource service malfunctioned", cause);
  }

  public TMMalfunctionException(String message) {
    super(ExceptionCode.TM_MALFUNCTION,
        String.format("The resource service malfunctioned: %s", message));
  }

  public TMMalfunctionException(String message, Throwable cause) {
    super(ExceptionCode.TM_MALFUNCTION,
        String.format("The resource service malfunctioned: %s", message), cause);
  }
}
