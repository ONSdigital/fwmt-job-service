package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class TMMalfunctionException extends FWMTCommonRuntimeException {
  static public final long serialVersionUID = 0L;

  public TMMalfunctionException() {
    super(ExceptionCode.FWMT_JOB_SERVICE_0011, "The resource service malfunctioned");
  }

  public TMMalfunctionException(Throwable cause) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0011, "The resource service malfunctioned", cause);
  }

  public TMMalfunctionException(String message) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0011,
        String.format("The resource service malfunctioned: %s", message));
  }

  public TMMalfunctionException(String message, Throwable cause) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0011,
        String.format("The resource service malfunctioned: %s", message), cause);
  }
}
