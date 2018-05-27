package uk.gov.ons.fwmt.job_service.exceptions.types;

import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class MediaTypeNotSupportedException extends FWMTCommonException {
  static final long serialVersionUID = 0L;

  public MediaTypeNotSupportedException(String expected, String given) {
    super(ExceptionCode.INVALID_MEDIA_TYPE, "Expected '" + expected + "' but was given '" + given + "'");
  }
}
