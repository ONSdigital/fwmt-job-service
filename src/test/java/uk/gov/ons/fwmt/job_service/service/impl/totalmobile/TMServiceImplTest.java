package uk.gov.ons.fwmt.job_service.service.impl.totalmobile;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.DeleteMessageRequest;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.ObjectFactory;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.QueryMessagesRequest;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendMessageRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;

public class TMServiceImplTest {

  @InjectMocks TMServiceImpl tmServiceImpl;
  @Mock ObjectFactory objectFactory;

  @Before
  public void setup() throws Exception {
    tmServiceImpl = new TMServiceImpl(null, null, null, null, "username", "password");
    MockitoAnnotations.initMocks(this);

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
  public void jaxbUnwrap() {
  }

  @Test
  public void send() {
  }
}