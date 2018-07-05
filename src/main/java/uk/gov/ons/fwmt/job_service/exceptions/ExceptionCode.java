package uk.gov.ons.fwmt.job_service.exceptions;

import lombok.Getter;

public enum ExceptionCode {
  FWMT_JOB_SERVICE_0001("UNKNOWN"),

  FWMT_JOB_SERVICE_0002("INVALID_FILE_NAME"),

  FWMT_JOB_SERVICE_0003("INVALID_MEDIA_TYPE"),

  FWMT_JOB_SERVICE_0004("UNKNOWN_JOB_ID"),

  FWMT_JOB_SERVICE_0005("UNKNOWN_USER_ID"),

  FWMT_JOB_SERVICE_0006("CSV_MISSING_COLUMN"),

  FWMT_JOB_SERVICE_0007("CSV_INVALID_FIELD"),

  FWMT_JOB_SERVICE_0008("CSV_OTHER"),

  // indicates that totalmobile rejected our credentials
  FWMT_JOB_SERVICE_0009("TM_UNAUTHENTICATED"),

  // indicates that totalmobile could not be contacted
  FWMT_JOB_SERVICE_0010("TM_INACCESSIBLE"),

  FWMT_JOB_SERVICE_0011("UNKNOWN_FIELD_PERIOD"),

  // indicates that the resource service could not be contacted
  FWMT_JOB_SERVICE_0012("RESOURCE_SERVICE_INACCESSIBLE"),

  // indicates that the resource service could be accessed, but did not behave as expected
  // this includes strange HTTP responses
  FWMT_JOB_SERVICE_0013("RESOURCE_SERVICE_MALFUNCTION");

  @Getter
  private final String description;
  
  ExceptionCode(String description) {
    this.description = description;
  }

  public String toString() {
    return this.name() + " " + this.description;
  }
}
