package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class FWMTCommonException extends Exception {
  static final long serialVersionUID = 0L;

  protected static String makeMessage(ExceptionCode code, String message) {
    if (message != null) {
      return code.toString() + message;
    } else {
      return code.toString();
    }
  }

  public FWMTCommonException(ExceptionCode code) {
    super(code.toString());
  }

  public FWMTCommonException(ExceptionCode code, String message) {
    super(makeMessage(code, message));
  }

  public FWMTCommonException(ExceptionCode code, String message, Throwable cause) {
    super(makeMessage(code, message), cause);
  }

  protected FWMTCommonException(ExceptionCode code, String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(makeMessage(code, message), cause, enableSuppression, writableStackTrace);
  }
}
