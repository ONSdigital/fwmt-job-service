package uk.gov.ons.fwmt.job_service.error;

public class MediaTypeNotSupportedException extends FWMTCommonException {
  static final long serialVersionUID = 0L;

  public MediaTypeNotSupportedException(String expected, String given) {
    super(ExceptionCode.INVALID_MEDIA_TYPE, "Expected '" + expected + "' but was given '" + given + "'");
  }
}
