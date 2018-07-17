package uk.gov.ons.fwmt.job_service.exceptions;

import lombok.Getter;

public enum ExceptionCode {
  // indicates an unexpected exception, usually a runtime exception
  UNKNOWN(1, "UNKNOWN"),

  INVALID_FILE_NAME(2, "INVALID_FILE_NAME"),

  INVALID_MEDIA_TYPE(3, "INVALID_MEDIA_TYPE"),

  UNKNOWN_JOB_ID(4, "UNKNOWN_JOB_ID"),

  UNKNOWN_USER_ID(5, "UNKNOWN_USER_ID"),

  CSV_MISSING_COLUMN(6, "CSV_MISSING_COLUMN"),

  CSV_INVALID_FIELD(7, "CSV_INVALID_FIELD"),

  CSV_OTHER(8, "CSV_OTHER"),

  // indicates that totalmobile rejected our credentials
  TM_UNAUTHENTICATED(9, "TM_UNAUTHENTICATED"),

  // indicates that totalmobile could not be contacted
  TM_INACCESSIBLE(10, "TM_INACCESSIBLE"),

  // indicates that TM could be accessed, but did not behave as expected
  // this includes strange HTTP responses
  TM_MALFUNCTION(11, "TM_MALFUNCTION"),

  // indicates that the resource service has rejected our credentials
  RESOURCE_SERVICE_UNAUTHENTICATED(12, "RESOURCE_SERVICE_UNAUTHENTICATED"),

  // indicates that the resource service could not be contacted
  RESOURCE_SERVICE_INACCESSIBLE(13, "RESOURCE_SERVICE_INACCESSIBLE"),

  // indicates that the resource service could be accessed, but did not behave as expected
  // this includes strange HTTP responses
  RESOURCE_SERVICE_MALFUNCTION(14, "RESOURCE_SERVICE_MALFUNCTION"),

  UNKNOWN_FIELD_PERIOD(15, "UNKNOWN_FIELD_PERIOD"),

  UNABLE_TO_SAVE_FILE(16, "UNABLE_TO_SAVE_FILE");

  @Getter
  private final int errorCode;

  @Getter
  private final String description;

  @Getter
  private final String prefixedName;

  ExceptionCode(int errorCode, String description) {
    this.errorCode = errorCode;
    this.description = description;
    this.prefixedName = "FWMT_JOB_SERVICE_" + String.format("%04d", this.getErrorCode()) + " " + this.getDescription();
  }

  public String toString() {
    return this.getPrefixedName();
  }
}
