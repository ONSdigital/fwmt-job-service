package uk.gov.ons.fwmt.job_service.service.impl.totalmobile;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.DeleteMessageRequest;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.ObjectFactory;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.QueryMessagesRequest;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendMessageRequest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
@TestPropertySource(properties = {
    "${totalmobile.url=url}",
    "${totalmobile.message-queue-path=messageQueuePath}",
    "${totalmobile.message-queue-package=messageQueuePackage}",
    "${totalmobile.message-queue-namespace=namespace}",
    "${totalmobile.username=username}",
    "${totalmobile.password=password}"})
@Ignore
public class TMServiceImplTest {

  @Mock TMServiceImpl tmServiceImpl;
  @Mock ObjectFactory objectFactory;

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