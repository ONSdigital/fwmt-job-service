package uk.gov.ons.fwmt.job_service.rest.client;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class ResourceRESTHelperTest {

  //GET//////////////////////////////
  
  @Test
  public void whenSendingASuccessfulGET_confirmBodyIsReturned() {
    String expectedBody = "The Rock";
    String url = "http://nowwhere.com";
    RestTemplate mockRestTemplate = mock(RestTemplate.class);
    ResponseEntity<String> mockResposeEntity = mock(ResponseEntity.class);

    when(mockRestTemplate.getForEntity(url, String.class)).thenReturn(mockResposeEntity);
    when(mockResposeEntity.getStatusCode()).thenReturn(HttpStatus.ACCEPTED);
    when(mockResposeEntity.getBody()).thenReturn(expectedBody);

    Optional<String> optional = ResourceRESTHelper.get(mockRestTemplate, url, String.class);

    assertEquals(expectedBody, optional.get());
  }

  @Test
  public void whenSendingGETToAnUnkownAddress_confirmBodyReturned() {
    String body = "The Rock";
    String url = "http://nowwhere.com";
    RestTemplate mockRestTemplate = mock(RestTemplate.class);
    ResponseEntity<String> mockResposeEntity = mock(ResponseEntity.class);

    when(mockRestTemplate.getForEntity(url, String.class)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
    when(mockResposeEntity.getBody()).thenReturn(body);

    Optional<String> optional = ResourceRESTHelper.get(mockRestTemplate, url, String.class);

    assertTrue(!optional.isPresent());
  }

  @Test
  public void whenSendingGETReturnsUnknownHTTPS_STATUS_confirmFWMTCommonExceptionIsThrown() {
    String expectedMessage = "FWMT_JOB_SERVICE_0014-RESOURCE_SERVICE_MALFUNCTION: The resource server acted unexpectedly: Unexpected HTTP code: 300";
    String body = "The Rock";
    String url = "http://nowwhere.com";
    RestTemplate mockRestTemplate = mock(RestTemplate.class);
    ResponseEntity<String> mockResposeEntity = mock(ResponseEntity.class);

    when(mockRestTemplate.getForEntity(url, String.class)).thenReturn(mockResposeEntity);
    when(mockResposeEntity.getStatusCode()).thenReturn(HttpStatus.MULTIPLE_CHOICES);

    String msg = "";
    try{
      ResourceRESTHelper.get(mockRestTemplate, url, String.class);
    }catch (Exception e) {
      msg = e.getMessage();
    }
    assertEquals(expectedMessage, msg);
  }

  @Test
  public void whenSendingGET_andServerCannotBeAccessed_confirmFWMTCommonExceptionIsThrown() {
    String expectedMessage = "FWMT_JOB_SERVICE_0013-RESOURCE_SERVICE_INACCESSIBLE: The resource server was inaccessible: Unable to contact the server";
    String body = "The Rock";
    String url = "http://nowwhere.com";
    RestTemplate mockRestTemplate = mock(RestTemplate.class);

    when(mockRestTemplate.getForEntity(url, String.class)).thenThrow(new ResourceAccessException(""));

    String msg = "";
    try{
      ResourceRESTHelper.get(mockRestTemplate, url, String.class);
    }catch (Exception e) {
      msg = e.getMessage();
    }
    assertEquals(expectedMessage, msg);
  }
  
  
  //POST//////////////////////////////

  @Test
  public void whenSendingASuccessfulPOST_confirmBodyIsReturned() {
    String expectedBody = "The Rock";
    String url = "http://nowwhere.com";
    RestTemplate mockRestTemplate = mock(RestTemplate.class);
    ResponseEntity<String> mockResposeEntity = mock(ResponseEntity.class);

    when(mockRestTemplate.postForEntity(url, expectedBody, String.class)).thenReturn(mockResposeEntity);
    when(mockResposeEntity.getStatusCode()).thenReturn(HttpStatus.ACCEPTED);
    when(mockResposeEntity.getBody()).thenReturn(expectedBody);

    Optional<String> optional = ResourceRESTHelper.post(mockRestTemplate, url, expectedBody, String.class);

    assertEquals(expectedBody, optional.get());
  }

  @Test
  public void whenSendingPOSTToAnUnkownAddress_confirmFWMTCommonExceptionIsThrown() {
    String expectedMessage = "FWMT_JOB_SERVICE_0014-RESOURCE_SERVICE_MALFUNCTION: The resource server acted unexpectedly: Unexpected HTTP code: 404";
    String body = "The Rock";
    String url = "http://nowwhere.com";
    RestTemplate mockRestTemplate = mock(RestTemplate.class);
    ResponseEntity<String> mockResposeEntity = mock(ResponseEntity.class);

    when(mockRestTemplate.postForEntity(url, body, String.class)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
    
    String msg = "";
    try{
      ResourceRESTHelper.post(mockRestTemplate, url, body, String.class);
    }catch (Exception e) {
      msg = e.getMessage();
    }
    assertEquals(expectedMessage, msg);
  }

  @Test
  public void whenSendingPOSTReturnsUnknownHTTPS_STATUS_confirmFWMTCommonExceptionIsThrown() {
    String expectedMessage = "FWMT_JOB_SERVICE_0014-RESOURCE_SERVICE_MALFUNCTION: The resource server acted unexpectedly: Unexpected HTTP code: 300";
    String body = "The Rock";
    String url = "http://nowwhere.com";
    RestTemplate mockRestTemplate = mock(RestTemplate.class);
    ResponseEntity<String> mockResposeEntity = mock(ResponseEntity.class);

    when(mockRestTemplate.postForEntity(url, body, String.class)).thenReturn(mockResposeEntity);
    when(mockResposeEntity.getStatusCode()).thenReturn(HttpStatus.MULTIPLE_CHOICES);

    String msg = "";
    try{
      ResourceRESTHelper.post(mockRestTemplate, url, body, String.class);
    }catch (Exception e) {
      msg = e.getMessage();
    }
    assertEquals(expectedMessage, msg);
  }

  @Test
  public void whenSendingPOST_andServerCannotBeAccessed_confirmFWMTCommonExceptionIsThrown() {
    String expectedMessage = "FWMT_JOB_SERVICE_0013-RESOURCE_SERVICE_INACCESSIBLE: The resource server was inaccessible: Unable to contact the server";
    String body = "The Rock";
    String url = "http://nowwhere.com";
    RestTemplate mockRestTemplate = mock(RestTemplate.class);

    when(mockRestTemplate.postForEntity(url, body, String.class)).thenThrow(new ResourceAccessException(""));

    String msg = "";
    try{
      ResourceRESTHelper.post(mockRestTemplate, url, body, String.class);
    }catch (Exception e) {
      msg = e.getMessage();
    }
    assertEquals(expectedMessage, msg);
  }
  
  //PUT//////////////////////////////

  @Test
  public void whenSendingASuccessfulPUT_confirmNoExceptionIsThrown() {
    String expectedBody = "The Rock";
    String url = "http://nowwhere.com";
    RestTemplate mockRestTemplate = mock(RestTemplate.class);
    
    doNothing().when(mockRestTemplate).put(url, expectedBody, String.class);
    
    try{
      ResourceRESTHelper.put(mockRestTemplate, url, expectedBody);
    }catch (Exception e) {
      fail("Should not thorw exception");
    }
  }

  @Test
  public void whenSendingPUTToAnUnkownAddress_confirmFWMTCommonExceptionIsThrown() {
    String expectedMessage = "FWMT_JOB_SERVICE_0014-RESOURCE_SERVICE_MALFUNCTION: The resource server acted unexpectedly: Unexpected HTTP code: 404";
    String body = "The Rock";
    String url = "http://nowwhere.com";
    RestTemplate mockRestTemplate = mock(RestTemplate.class);

    doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND)).when(mockRestTemplate).put(url, body);
    
    String msg = "";
    try{
      ResourceRESTHelper.put(mockRestTemplate, url, body);
    }catch (Exception e) {
      msg = e.getMessage();
    }
    assertEquals(expectedMessage, msg);
  }

  @Test
  public void whenSendingPUT_andServerCannotBeAccessed_confirmFWMTCommonExceptionIsThrown() {
    String expectedMessage = "FWMT_JOB_SERVICE_0013-RESOURCE_SERVICE_INACCESSIBLE: The resource server was inaccessible: Unable to contact the server";
    String body = "The Rock";
    String url = "http://nowwhere.com";
    RestTemplate mockRestTemplate = mock(RestTemplate.class);

    doThrow(new ResourceAccessException("")).when(mockRestTemplate).put(url, body);

    String msg = "";
    try{
      ResourceRESTHelper.put(mockRestTemplate, url, body);
    }catch (Exception e) {
      msg = e.getMessage();
    }
    assertEquals(expectedMessage, msg);
  }
 
}
