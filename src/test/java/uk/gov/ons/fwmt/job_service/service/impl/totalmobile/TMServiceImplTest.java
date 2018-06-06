package uk.gov.ons.fwmt.job_service.service.impl.totalmobile;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.xml.bind.JAXBElement;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class TMServiceImplTest {

  @InjectMocks private TMServiceImpl tmServiceImpl;
  @Mock private ObjectFactory objectFactory;
  @Mock private WebServiceTemplate webServiceTemplate;
  @Mock private JAXBElement<Object> jaxbElement;

  @Before
  public void setUp() throws Exception {
    tmServiceImpl = new TMServiceImpl("https://ons.totalmobile.co.uk",
        "messageQueuePath",
        "messageQueuePackage",
        "expectedNamespace",
        "username", "password");
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected=IllegalArgumentException.class)
  public void sOAPLookupReceivesAMessageThatDoesNotMatchTM() {
    //When
    tmServiceImpl.lookupSOAPAction(Object.class);
  }

  @Test
  public void sOAPLookupReceivesARequestMessage() {
    //Given
    String expectedResult = "expectedNamespaceQuery";

    //When
    String result = tmServiceImpl.lookupSOAPAction(QueryMessagesRequest.class);

    //Then
    assertEquals(expectedResult, result);
  }

  @Test
  public void sOAPLookupReceivesNonMappedRequestMessage(){
    //Given
    String expectedResult = "expectedNamespaceSendAddJobTasksRequestMessage";

    //When
    String result = tmServiceImpl.lookupSOAPAction(SendAddJobTasksRequestMessage.class);

    //Then
    assertEquals(expectedResult,result);
  }

  @Test
  public void shouldTestIfCreateSendMessageRequestIsCalled() {
    //Given
    SendMessageRequest sendMessageRequest = new SendMessageRequest();

    //When
    tmServiceImpl.jaxbWrap(sendMessageRequest);

    //Then
    Mockito.verify(objectFactory).createSendMessageRequest(sendMessageRequest);
  }

  @Test
  public void shouldTestIfCreateQueryMessageRequestIsCalled() {
    //Given
    QueryMessagesRequest queryMessagesRequest = new QueryMessagesRequest();

    //When
    tmServiceImpl.jaxbWrap(queryMessagesRequest);

    //Then
    Mockito.verify(objectFactory).createQueryMessagesRequest(any());
  }

  @Test
  public void shouldHandleAnyOtherObject() {
    //Given
    DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest();

    //When
    Object result = tmServiceImpl.jaxbWrap(deleteMessageRequest);

    //Then
    Assert.assertTrue(result instanceof DeleteMessageRequest);
  }

  @Test
  public void sendsAResponseToBeUnwrapped() {
    //Given
    SendCreateJobRequestMessageResponse response = new SendCreateJobRequestMessageResponse();

    //When
    Object result = tmServiceImpl.jaxbUnwrap(response);

    //Then
    assertEquals(response,result);
  }

  @Test
  public void sendsAJAXBElementToBeUnwrapped() {
    //Given
    SendCreateJobRequestMessageResponse response = new SendCreateJobRequestMessageResponse();
    when(jaxbElement.getValue()).thenReturn(response);

    //When
    Object result = tmServiceImpl.jaxbUnwrap(jaxbElement);

    //Then
    assertEquals(response,result);
  }

  @Test
  public void shouldReturnSuccessfulResponseWhenReceivesPermittedResponse() {
    //Given
    QueryMessagesRequest queryMessagesRequest = new QueryMessagesRequest();
    when(webServiceTemplate.marshalSendAndReceive(any(),any(),any())).thenReturn(jaxbElement);
    QueryMessagesResponse queryMessagesResponse = new QueryMessagesResponse();
    when(jaxbElement.getValue()).thenReturn(queryMessagesResponse);

    //When
    QueryMessagesResponse result = tmServiceImpl.send(queryMessagesRequest);

    //Then
    assertEquals(queryMessagesResponse, result);
  }

  @Test(expected=IllegalArgumentException.class)
  public void shouldThrowExceptionWhenReceivedResponseWIsNotInPermittedResponses() {
    //Given
    QueryMessagesRequest queryMessagesRequest = new QueryMessagesRequest();
    when(webServiceTemplate.marshalSendAndReceive(any(),any(),any())).thenReturn(jaxbElement);
    when(jaxbElement.getValue()).thenReturn(new Object());

    //When
    tmServiceImpl.send(queryMessagesRequest);
  }
}