package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class FWMTCommonRuntimeException extends RuntimeException {
  static public final long serialVersionUID = 0L;

  protected static String makeMessage(ExceptionCode code, String message) {
    if (message != null) {
      return code.toString() + ": " + message;
    } else {
      return code.toString();
    }
  }

  public FWMTCommonRuntimeException(ExceptionCode code) {
    super(code.toString());
  }

  public FWMTCommonRuntimeException(ExceptionCode code, String message) {
    super(makeMessage(code, message));
  }

  public FWMTCommonRuntimeException(ExceptionCode code, String message, Throwable cause) {
    super(makeMessage(code, message), cause);
  }

  protected FWMTCommonRuntimeException(ExceptionCode code, String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(makeMessage(code, message), cause, enableSuppression, writableStackTrace);
  }
}
