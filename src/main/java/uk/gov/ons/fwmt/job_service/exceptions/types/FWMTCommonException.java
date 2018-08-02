package uk.gov.ons.fwmt.job_service.exceptions.types;

import org.springframework.web.HttpMediaTypeNotSupportedException;

import lombok.Getter;
import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;
import uk.gov.ons.fwmt.job_service.rest.client.dto.UserDto;

public class FWMTCommonException extends RuntimeException {
  static public final long serialVersionUID = 0L;
  //Don't change this(JOB_FAILED_STRING) constant , this format is used in splunk to create reports.If changing, change accordingly the splunk report search string.
  public static final String JOB_FAILED_STRING = " could not be processed for job id: {} throwing error: {}";
  public static final String JOB_ENTRY_FAILED_STRING = "Job Entry could not be processed for job id: {} throwing error: {}";

  @Getter
  public final ExceptionCode code;

  protected static String makeMessage(ExceptionCode code, String message) {
    if (message != null) {
      return code.toString() + ": " + message;
    } else {
      return code.toString();
    }
  }

  public FWMTCommonException(ExceptionCode code) {
    super(makeMessage(code, null));
    this.code = code;
  }

  public FWMTCommonException(ExceptionCode code, String message) {
    super(makeMessage(code, message));
    this.code = code;
  }

  public FWMTCommonException(ExceptionCode code, String message, Throwable cause) {
    super(makeMessage(code, message), cause);
    this.code = code;
  }

  protected FWMTCommonException(ExceptionCode code, String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(makeMessage(code, message), cause, enableSuppression, writableStackTrace);
    this.code = code;
  }

  // 0001 - UNKNOWN

  public static FWMTCommonException makeUnknownException(Throwable cause) {
    return new FWMTCommonException(ExceptionCode.UNKNOWN, null, cause);
  }

  // 0002 - INVALID_FILE_NAME

  private static String makeInvalidFileNameExceptionMessage(String name, String reason) {
    return "'" + name + "' is not a valid file name: " + reason;
  }

  public static FWMTCommonException makeInvalidFileNameException(String name, String reason) {
    return new FWMTCommonException(ExceptionCode.INVALID_FILE_NAME,
        makeInvalidFileNameExceptionMessage(name, reason));
  }

  public static FWMTCommonException makeInvalidFileNameException(String name, String reason, Throwable cause) {
    return new FWMTCommonException(ExceptionCode.INVALID_FILE_NAME,
        makeInvalidFileNameExceptionMessage(name, reason), cause);
  }

  // 0003 - INVALID_MEDIA_TYPE

  private static String makeInvalidMediaTypeExceptionMessage(String expected, String received) {
    return "Expected any of '" + expected + "' but received '" + received + "'";
  }

  public static FWMTCommonException makeInvalidMediaTypeException(String expected, String received) {
    return new FWMTCommonException(ExceptionCode.INVALID_MEDIA_TYPE,
        makeInvalidMediaTypeExceptionMessage(expected, received));
  }

  public static FWMTCommonException makeInvalidMediaTypeException(HttpMediaTypeNotSupportedException exception) {
    return new FWMTCommonException(ExceptionCode.INVALID_MEDIA_TYPE,
        makeInvalidMediaTypeExceptionMessage(exception.getSupportedMediaTypes().toString(),
            exception.getContentType().toString()));
  }

  // 0004 - UNKNOWN_JOB_ID

  private static String makeUnknownJobIdExceptionMessage(String jobId) {
    return "Unknown job id " + jobId;
  }

  public static FWMTCommonException makeUnknownJobIdException(String jobId) {
    return new FWMTCommonException(ExceptionCode.UNKNOWN_JOB_ID,
        makeUnknownJobIdExceptionMessage(jobId));
  }

  // 0005 - UNKNOWN_USER_ID

  public static String makeUnknownUserIdException(String authNo) {
    return ExceptionCode.UNKNOWN_USER_ID.toString() + " : A user was not found on our system: authNo=" + authNo;
  }

  // 0006 - CSV_MISSING_COLUMN

  private static String makeCsvMissingColumnExceptionMessage(String fieldName) {
    return "A column was missing from a CSV file: fieldName=" + fieldName;
  }

  public static FWMTCommonException makeCsvMissingColumnException(String fieldName) {
    return new FWMTCommonException(ExceptionCode.CSV_MISSING_COLUMN,
        makeCsvMissingColumnExceptionMessage(fieldName));
  }

  // 0007 - CSV_INVALID_FIELD

  private static String makeCsvInvalidFieldExceptionMessage(String fieldName, String reason) {
    return "A field was invalid in a CSV file: fieldName=" + fieldName + " failed to parse: " + reason;
  }

  public static FWMTCommonException makeCsvInvalidFieldException(String fieldName, String reason) {
    return new FWMTCommonException(ExceptionCode.CSV_INVALID_FIELD,
        makeCsvInvalidFieldExceptionMessage(fieldName, reason));
  }

  // 0008 - CSV_OTHER

  private static String makeCsvOtherExceptionMessage(String reason) {
    return "An unexpected error occurred during exception parsing: " + reason;
  }

  public static FWMTCommonException makeCsvOtherException(String reason) {
    return new FWMTCommonException(ExceptionCode.CSV_OTHER,
        makeCsvOtherExceptionMessage(reason));
  }

  // 0009 - TM_UNAUTHENTICATED

  private static String makeTmUnauthenticatedExceptionMessage(String reason) {
    return "The TM server rejected our credentials: " + reason;
  }

  public static FWMTCommonException makeTmUnauthenticatedException(String reason) {
    return new FWMTCommonException(ExceptionCode.TM_UNAUTHENTICATED,
        makeTmUnauthenticatedExceptionMessage(reason));
  }

  public static FWMTCommonException makeTmUnauthenticatedException(String reason, Throwable cause) {
    return new FWMTCommonException(ExceptionCode.TM_UNAUTHENTICATED, makeTmUnauthenticatedExceptionMessage(reason),
        cause);
  }

  // 0010 - TM_INACCESSIBLE

  private static String makeTmInaccessibleExceptionMessage(String reason) {
    return "The TM server was inaccessible: " + reason;
  }

  public static FWMTCommonException makeTmInaccessibleException(String reason) {
    return new FWMTCommonException(ExceptionCode.TM_INACCESSIBLE,
        makeTmInaccessibleExceptionMessage(reason));
  }

  public static FWMTCommonException makeTmInaccessibleException(String reason, Throwable cause) {
    return new FWMTCommonException(ExceptionCode.TM_INACCESSIBLE, makeTmInaccessibleExceptionMessage(reason),
        cause);
  }

  // 0011 - TM_MALFUNCTION

  private static String makeTmMalfunctionExceptionMessage(String reason) {
    return "The TM server acted unexpectedly: " + reason;
  }

  public static FWMTCommonException makeTmMalfunctionException(String reason) {
    return new FWMTCommonException(ExceptionCode.TM_MALFUNCTION,
        makeTmMalfunctionExceptionMessage(reason));
  }

  public static FWMTCommonException makeTmMalfunctionException(String reason, Throwable cause) {
    return new FWMTCommonException(ExceptionCode.TM_MALFUNCTION, makeTmMalfunctionExceptionMessage(reason),
        cause);
  }

  // 0012 - RESOURCE_SERVICE_UNAUTHENTICATED

  private static String makeResourceServiceUnauthenticatedExceptionMessage(String reason) {
    return "The resource server rejected our credentials: " + reason;
  }

  public static FWMTCommonException makeResourceServiceUnauthenticatedException(String reason) {
    return new FWMTCommonException(ExceptionCode.RESOURCE_SERVICE_UNAUTHENTICATED,
        makeResourceServiceUnauthenticatedExceptionMessage(reason));
  }

  public static FWMTCommonException makeResourceServiceUnauthenticatedException(String reason, Throwable cause) {
    return new FWMTCommonException(ExceptionCode.RESOURCE_SERVICE_UNAUTHENTICATED,
        makeResourceServiceUnauthenticatedExceptionMessage(reason),
        cause);
  }

  // 0013 - RESOURCE_SERVICE_INACCESSIBLE

  private static String makeResourceServiceInaccessibleExceptionMessage(String reason) {
    return "The resource server was inaccessible: " + reason;
  }

  public static FWMTCommonException makeResourceServiceInaccessibleException(String reason) {
    return new FWMTCommonException(ExceptionCode.RESOURCE_SERVICE_INACCESSIBLE,
        makeResourceServiceInaccessibleExceptionMessage(reason));
  }

  public static FWMTCommonException makeResourceServiceInaccessibleException(String reason, Throwable cause) {
    return new FWMTCommonException(ExceptionCode.RESOURCE_SERVICE_INACCESSIBLE,
        makeResourceServiceInaccessibleExceptionMessage(reason),
        cause);
  }

  // 0014 - RESOURCE_SERVICE_MALFUNCTION

  private static String makeResourceServiceMalfunctionExceptionMessage(String reason) {
    return "The resource server acted unexpectedly: " + reason;
  }

  public static FWMTCommonException makeResourceServiceMalfunctionException(String reason) {
    return new FWMTCommonException(ExceptionCode.RESOURCE_SERVICE_MALFUNCTION,
        makeResourceServiceMalfunctionExceptionMessage(reason));
  }

  public static FWMTCommonException makeResourceServiceMalfunctionException(String reason, Throwable cause) {
    return new FWMTCommonException(ExceptionCode.RESOURCE_SERVICE_MALFUNCTION,
        makeResourceServiceMalfunctionExceptionMessage(reason), cause);
  }

  // 0015 - UNKNOWN_FIELD_PERIOD

  private static String makeUnknownFieldPeriodExceptionMessage(String fieldPeriod) {
    return "An unknown field period was requested: fieldPeriod=" + fieldPeriod;
  }

  public static FWMTCommonException makeUnknownFieldPeriodException(String fieldPeriod) {
    return new FWMTCommonException(ExceptionCode.UNKNOWN_FIELD_PERIOD,
        makeUnknownFieldPeriodExceptionMessage(fieldPeriod));
  }

  // 0016 - UNABLE_TO_SAVE_FILE

  private static String makeUnableToSaveFileExceptionMessage(String reason) {
    return "A file could not be stored: " + reason;
  }

  public static FWMTCommonException makeUnableToSaveFileException(String reason) {
    return new FWMTCommonException(ExceptionCode.UNABLE_TO_SAVE_FILE,
        makeUnableToSaveFileExceptionMessage(reason));
  }

  public static FWMTCommonException makeUnableToSaveFileException(String reason, Throwable cause) {
    return new FWMTCommonException(ExceptionCode.UNABLE_TO_SAVE_FILE, makeUnableToSaveFileExceptionMessage(reason),
        cause);
  }

  // 0017 - BAD_USER_STATE
  public static String makeBadUserStateException(UserDto user, String reason) {
    return ExceptionCode.BAD_USER_STATE.toString() +  "User=" + user.toString() + " was in a bad state because: " + reason;
  }
}
