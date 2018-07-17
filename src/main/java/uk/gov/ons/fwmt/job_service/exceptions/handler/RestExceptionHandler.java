package uk.gov.ons.fwmt.job_service.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uk.gov.ons.fwmt.job_service.data.dto.GatewayCommonErrorDTO;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {
  public ResponseEntity<GatewayCommonErrorDTO> makeCommonError(HttpServletRequest request, Throwable exception,
      HttpStatus status, String error, String message) {
    GatewayCommonErrorDTO errorDTO = new GatewayCommonErrorDTO();
    errorDTO.setError(error);
    errorDTO.setException(exception.getClass().getName());
    errorDTO.setMessage(message);
    errorDTO.setPath(request.getContextPath());
    errorDTO.setStatus(status.value());
    errorDTO.setTimestamp(LocalTime.now().toString());
    return new ResponseEntity<>(errorDTO, status);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<GatewayCommonErrorDTO> handleAnyException(HttpServletRequest request, Exception exception) {
    FWMTCommonException unknownException = FWMTCommonException.makeUnknownException(exception);
    log.error(unknownException.toString(), unknownException);
    return makeCommonError(request, exception, HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error", "Unknown error");
  }

  @ExceptionHandler(FWMTCommonException.class)
  public ResponseEntity<GatewayCommonErrorDTO> handleFWMTException(HttpServletRequest request,
      FWMTCommonException exception) {
    log.error(exception.toString(), exception);
    switch (exception.getCode()) {
    case INVALID_MEDIA_TYPE:
      return makeCommonError(request, exception, HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Invalid content type",
          exception.getMessage());
    case INVALID_FILE_NAME:
      return makeCommonError(request, exception, HttpStatus.BAD_REQUEST, "Invalid CSV File Name", exception.toString());
    case UNKNOWN_USER_ID:
      return makeCommonError(request, exception, HttpStatus.INTERNAL_SERVER_ERROR, "Unknown user",
          exception.toString());
    default:
      return handleAnyException(request, exception);
    }
  }
}
