package uk.gov.ons.fwmt.job_service.exceptions;

public enum ExceptionCode {
  FWMT_JOB_SERVICE_0001("UNKNOWN"),
  FWMT_JOB_SERVICE_0002("INVALID_FILE_NAME"),
  FWMT_JOB_SERVICE_0003("INVALID_MEDIA_TYPE"),
  FWMT_JOB_SERVICE_0004("UNKNOWN_JOB_ID"),
  FWMT_JOB_SERVICE_0005("UNKNOWN_USER_ID"),
  FWMT_JOB_SERVICE_0006("CSV_MISSING_COLUMN"),
  FWMT_JOB_SERVICE_0007("CSV_INVALID_FIELD"),
  FWMT_JOB_SERVICE_0008("CSV_OTHER"),
  FWMT_JOB_SERVICE_0009("TM_UNAUTHENTICATED"),
  FWMT_JOB_SERVICE_0010("TM_INACCESSIBLE");
  
  private final String error;
  
  ExceptionCode(String error) {
    this.error = error;
  }
  
  public String getError() {
    return error;
  }
}
