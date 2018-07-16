package uk.gov.ons.fwmt.job_service.exceptions.types;

import org.springframework.web.HttpMediaTypeNotSupportedException;
import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class MediaTypeNotSupportedException extends FWMTCommonException {
  static public final long serialVersionUID = 0L;

  public MediaTypeNotSupportedException(String expected, String given) {
    super(ExceptionCode.INVALID_MEDIA_TYPE, "Expected '" + expected + "' but was given '" + given + "'");
  }

  public MediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
    super(ExceptionCode.INVALID_MEDIA_TYPE,
        "Expected any of " + exception.getSupportedMediaTypes().toString() + " but received '" + exception
            .getContentType().toString() + "'");
  }
}
