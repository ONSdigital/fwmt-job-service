package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class InvalidFileNameException extends FWMTCommonException {
  static public final long serialVersionUID = 0L;

  private static String makeMessage(String name, String reason) {
    if (reason != null) {
      return "'" + name + "' is not a valid file name: " + reason;
    } else {
      return "'" + name + "' is not a valid file name";
    }
  }

  public InvalidFileNameException(String name) {
    super(ExceptionCode.INVALID_FILE_NAME, makeMessage(name, null));
  }

  public InvalidFileNameException(String name, String reason) {
    super(ExceptionCode.INVALID_FILE_NAME, makeMessage(name, reason));
  }

  public InvalidFileNameException(String name, String reason, Exception cause) {
    super(ExceptionCode.INVALID_FILE_NAME, makeMessage(name, reason), cause);
  }
}
