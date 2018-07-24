package uk.gov.ons.fwmt.job_service.exceptions.handler;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import uk.gov.ons.fwmt.job_service.data.dto.GatewayCommonErrorDTO;

public class RestExceptionHandlerTest {

  RestExceptionHandler restExceptionHandler = new RestExceptionHandler();

  @Test
  public void makeCommonError() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    Throwable exception = new Throwable();
    ResponseEntity<GatewayCommonErrorDTO> result = restExceptionHandler.makeCommonError(request,exception,HttpStatus.BAD_REQUEST,"","");
  }

  @Test
  public void handleAnyException() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    Exception exception = new Exception();
    ResponseEntity<GatewayCommonErrorDTO> result = restExceptionHandler.handleAnyException(request,exception);
  }

  @Test
  public void handleContentTypeException() {
    MockHttpServletRequest request = new MockHttpServletRequest();
//    HttpMediaTypeNotSupportedException exception = new HttpMediaTypeNotSupportedException();
  }

  @Test
  public void handleInvalidFileNameException() {
    MockHttpServletRequest request = new MockHttpServletRequest();
//    InvalidFileNameException exception = new InvalidFileNameException();
  }

  @Test
  public void handleInternalServerError() {
    MockHttpServletRequest request = new MockMultipartHttpServletRequest();
//    UnknownUserException exception = new UnknownUserException();
  }

}