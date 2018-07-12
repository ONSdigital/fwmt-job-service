package uk.gov.ons.fwmt.job_service.exceptions.types;

import org.springframework.web.HttpMediaTypeNotSupportedException;
import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;

public class MediaTypeNotSupportedException extends FWMTCommonException {
  static public final long serialVersionUID = 0L;

  public MediaTypeNotSupportedException(String expected, String given) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0003, "Expected '" + expected + "' but was given '" + given + "'");
  }

  public MediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
    super(ExceptionCode.FWMT_JOB_SERVICE_0003,
        "Expected any of " + exception.getSupportedMediaTypes().toString() + " but received '" + exception
            .getContentType().toString() + "'");
  }
}
