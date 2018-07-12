package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class ResourceServiceMalfunctionException extends FWMTCommonRuntimeException {
  static public final long serialVersionUID = 0L;

  public ResourceServiceMalfunctionException() {
    super(ExceptionCode.FWMT_JOB_SERVICE_0014, "The resource service malfunctioned");
  }

  public ResourceServiceMalfunctionException(Throwable cause) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0014, "The resource service malfunctioned", cause);
  }

  public ResourceServiceMalfunctionException(String message) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0014,
        String.format("The resource service malfunctioned: %s", message));
  }

  public ResourceServiceMalfunctionException(String message, Throwable cause) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0014,
        String.format("The resource service malfunctioned: %s", message), cause);
  }
}
