package uk.gov.ons.fwmt.job_service.exceptions;

import lombok.Getter;

public enum ExceptionCode {
  // indicates an unexpected exception, usually a runtime exception
  FWMT_JOB_SERVICE_0001("UNKNOWN"),

  // used in InvalidFileNameException
  FWMT_JOB_SERVICE_0002("INVALID_FILE_NAME"),

  // used in MediaTypeNotSupportedException
  FWMT_JOB_SERVICE_0003("INVALID_MEDIA_TYPE"),

  // unused?
  FWMT_JOB_SERVICE_0004("UNKNOWN_JOB_ID"),

  // used in UnknownUserException
  FWMT_JOB_SERVICE_0005("UNKNOWN_USER_ID"),

  // used in CSVMissingColumnException
  FWMT_JOB_SERVICE_0006("CSV_MISSING_COLUMN"),

  // used in CSVInvalidFieldException
  FWMT_JOB_SERVICE_0007("CSV_INVALID_FIELD"),

  // used in CSVOtherException
  FWMT_JOB_SERVICE_0008("CSV_OTHER"),

  // indicates that totalmobile rejected our credentials
  FWMT_JOB_SERVICE_0009("TM_UNAUTHENTICATED"),

  // indicates that totalmobile could not be contacted
  FWMT_JOB_SERVICE_0010("TM_INACCESSIBLE"),

  // indicates that TM could be accessed, but did not behave as expected
  // this includes strange HTTP responses
  FWMT_JOB_SERVICE_0011("TM_MALFUNCTION"),

  // indicates that the resource service has rejected our credentials
  FWMT_JOB_SERVICE_0012("RESOURCE_SERVICE_UNAUTHENTICATED"),

  // indicates that the resource service could not be contacted
  FWMT_JOB_SERVICE_0013("RESOURCE_SERVICE_INACCESSIBLE"),

  // indicates that the resource service could be accessed, but did not behave as expected
  // this includes strange HTTP responses
  FWMT_JOB_SERVICE_0014("RESOURCE_SERVICE_MALFUNCTION"),

  FWMT_JOB_SERVICE_0015("UNKNOWN_FIELD_PERIOD"),

  FWMT_JOB_SERVICE_0016("UNABLE_TO_SAVE_FILE");

  @Getter
  private final String description;
  
  ExceptionCode(String description) {
    this.description = description;
  }

  public String toString() {
    return this.name() + " " + this.description;
  }
}
