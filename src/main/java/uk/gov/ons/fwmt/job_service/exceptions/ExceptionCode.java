package uk.gov.ons.fwmt.job_service.exceptions;

import lombok.Getter;

public enum ExceptionCode {
  // indicates an unexpected exception, usually a runtime exception
  UNKNOWN(1),

  INVALID_FILE_NAME(2),

  INVALID_MEDIA_TYPE(3),

  UNKNOWN_JOB_ID(4),

  UNKNOWN_USER_ID(5),

  CSV_MISSING_COLUMN(6),

  CSV_INVALID_FIELD(7),

  CSV_OTHER(8),

  // indicates that totalmobile rejected our credentials
  TM_UNAUTHENTICATED(9),

  // indicates that totalmobile could not be contacted
  TM_INACCESSIBLE(10),

  // indicates that TM could be accessed, but did not behave as expected
  // this includes strange HTTP responses
  TM_MALFUNCTION(11),

  // indicates that the resource service has rejected our credentials
  RESOURCE_SERVICE_UNAUTHENTICATED(12),

  // indicates that the resource service could not be contacted
  RESOURCE_SERVICE_INACCESSIBLE(13),

  // indicates that the resource service could be accessed, but did not behave as expected
  // this includes strange HTTP responses
  RESOURCE_SERVICE_MALFUNCTION(14),

  UNKNOWN_FIELD_PERIOD(15),

  UNABLE_TO_SAVE_FILE(16),

  BAD_USER_STATE(17);

  @Getter
  private final int value;

  @Getter
  private final String code;

  ExceptionCode(int value) {
    this.value = value;
    this.code = "FWMT_JOB_SERVICE_" + String.format("%04d", this.getValue());
  }

  public String toString() {
    return this.getCode() + "-" + this.name();
  }
}
