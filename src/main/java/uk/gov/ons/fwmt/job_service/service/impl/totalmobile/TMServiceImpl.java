package uk.gov.ons.fwmt.job_service.service.impl.totalmobile;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.DeleteMessageRequest;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.DeleteMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.GetMessageRequest;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.GetMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.ObjectFactory;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.QueryMessagesRequest;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.QueryMessagesResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.ResetMessageRequest;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.ResetMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.RetryMessageRequest;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.RetryMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendAddFolioContentRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendAddFolioContentRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendAddJobTasksRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendAddJobTasksRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendAddVisitTasksRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendAddVisitTasksRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateAppointmentRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateAppointmentRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateBulletinRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateBulletinRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreatePatientRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreatePatientRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateReferralRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateReferralRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateVisitRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateVisitRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDeleteBulletinRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDeleteBulletinRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDeleteJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDeleteJobRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDischargeReferralRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDischargeReferralRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendForceRecallVisitRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendForceRecallVisitRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendGenerateFolioContentRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendGenerateFolioContentRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendMessageRequest;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendSaveAvailabilityRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendSaveAvailabilityRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateJobHeaderRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateJobHeaderRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateReferralRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateReferralRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateVisitHeaderRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateVisitHeaderRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateVisitScheduleRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateVisitScheduleRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.TransformAndSendRequest;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.TransformAndSendResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import uk.gov.ons.fwmt.job_service.exceptions.types.TMMalfunctionException;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMService;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This service handles all of the interactions between this legacy_gateway and TotalMobile
 * This interaction largely consists of sending messages in SOAP format
 */
@Slf4j
@Service
public class TMServiceImpl extends WebServiceGatewaySupport implements TMService {
  // A lookup detailing the instances where the message name does not translate easily into a SOAP action
  // Normally, we assume that the SOAP action is equal to the class name with the word 'Response' at the end removed
  private static final Map<Class<?>, String> messageActionMap;

  // A list of all classes denoting valid TM messages
  private static Class<?>[] knownRequestTypes = {
      SendMessageRequest.class, TransformAndSendRequest.class, QueryMessagesRequest.class, GetMessageRequest.class,
      DeleteMessageRequest.class, RetryMessageRequest.class, ResetMessageRequest.class,
      SendCreateVisitRequestMessage.class, SendForceRecallVisitRequestMessage.class,
      SendAddVisitTasksRequestMessage.class, SendUpdateVisitScheduleRequestMessage.class,
      SendUpdateVisitHeaderRequestMessage.class, SendCreateBulletinRequestMessage.class,
      SendDeleteBulletinRequestMessage.class, SendGenerateFolioContentRequestMessage.class,
      SendAddFolioContentRequestMessage.class, SendCreateReferralRequestMessage.class,
      SendCreatePatientRequestMessage.class, SendUpdateReferralRequestMessage.class,
      SendCreateAppointmentRequestMessage.class, SendDischargeReferralRequestMessage.class,
      SendCreateJobRequestMessage.class, SendDeleteJobRequestMessage.class,
      SendAddJobTasksRequestMessage.class, SendSaveAvailabilityRequestMessage.class,
      SendUpdateJobHeaderRequestMessage.class};
  private static Class<?>[] knownResponseTypes = {
      SendMessageResponse.class, TransformAndSendResponse.class, QueryMessagesResponse.class, GetMessageResponse.class,
      DeleteMessageResponse.class, RetryMessageResponse.class, ResetMessageResponse.class,
      SendCreateVisitRequestMessageResponse.class, SendForceRecallVisitRequestMessageResponse.class,
      SendAddVisitTasksRequestMessageResponse.class, SendUpdateVisitScheduleRequestMessageResponse.class,
      SendUpdateVisitHeaderRequestMessageResponse.class, SendCreateBulletinRequestMessageResponse.class,
      SendDeleteBulletinRequestMessageResponse.class, SendGenerateFolioContentRequestMessageResponse.class,
      SendAddFolioContentRequestMessageResponse.class, SendCreateReferralRequestMessageResponse.class,
      SendCreatePatientRequestMessageResponse.class, SendUpdateReferralRequestMessageResponse.class,
      SendCreateAppointmentRequestMessageResponse.class, SendDischargeReferralRequestMessageResponse.class,
      SendCreateJobRequestMessageResponse.class, SendDeleteJobRequestMessageResponse.class,
      SendAddJobTasksRequestMessageResponse.class, SendSaveAvailabilityRequestMessageResponse.class,
      SendUpdateJobHeaderRequestMessageResponse.class};

  static {
    messageActionMap = new HashMap<>();
    messageActionMap.put(SendMessageRequest.class, "SendMessage");
    messageActionMap.put(TransformAndSendRequest.class, "TransformAndSendMessage");
    messageActionMap.put(QueryMessagesRequest.class, "Query");
    messageActionMap.put(GetMessageRequest.class, "Get");
    messageActionMap.put(DeleteMessageRequest.class, "Delete");
    messageActionMap.put(RetryMessageRequest.class, "Retry");
    messageActionMap.put(ResetMessageRequest.class, "Reset");
  }

  private final String messageQueueUrl;
  private final String namespace;
  ObjectFactory objectFactory;

  @Autowired
  public TMServiceImpl(
      @Value("${totalmobile.url}") String url,
      @Value("${totalmobile.message-queue-path}") String messageQueuePath,
      @Value("${totalmobile.message-queue-package}") String messageQueuePackage,
      @Value("${totalmobile.message-queue-namespace}") String namespace,
      @Value("${totalmobile.username}") String username,
      @Value("${totalmobile.password}") String password) throws Exception {
    this.messageQueueUrl = url + messageQueuePath;
    this.namespace = namespace;

    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setContextPath(messageQueuePackage);
    this.setMarshaller(marshaller);
    this.setUnmarshaller(marshaller);

    HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender();
    messageSender.setCredentials(new UsernamePasswordCredentials(username, password));
    messageSender.afterPropertiesSet();
    this.setMessageSender(messageSender);

    ClientInterceptor[] interceptors = { new ClientInterceptor() {
      @Override public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        log.trace(messageContext.getRequest().toString());
        return true;
      }

      @Override public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        log.trace(messageContext.getRequest().toString());
        return true;
      }

      @Override public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        log.trace(messageContext.getRequest().toString());
        return true;
      }

      @Override public void afterCompletion(MessageContext messageContext, Exception ex)
          throws WebServiceClientException { }
    } };
    this.setInterceptors(interceptors);

    this.objectFactory = new ObjectFactory();
  }

  /**
   * Translates a class name into the SOAP action expected by TotalMobile
   * <p>
   * If we have a specific mapping, we use such
   * If we do not, we use the general case of removing the word 'Response'
   */
  protected String lookupSOAPAction(Class<?> cl) {
    if (!Arrays.asList(knownRequestTypes).contains(cl)) {
      throw new IllegalArgumentException("Message passed that does not match any TotalMobile message");
    }
    String className = cl.getSimpleName();
    String action = namespace + messageActionMap.getOrDefault(cl, className);
    log.debug("lookupSOAPAction found action: {}", action);
    return action;
  }

  // TODO can we assert that we return 'JAXBElement<?> or T'?
  // TODO expand this to cover all messages that do not contain the needed @XMLRootElement
  protected <T> Object jaxbWrap(T value) {
    if (value instanceof SendMessageRequest) {
      return objectFactory.createSendMessageRequest((SendMessageRequest) value);
    } else if (value instanceof QueryMessagesRequest) {
      return objectFactory.createQueryMessagesRequest((QueryMessagesRequest) value);
    } else {
      return value;
    }
  }

  // TODO can we assert that we return 'JAXBElement<?> or T'?
  // TODO expand this to cover all messages that do not contain the needed @XMLRootElement
  protected <T> Object jaxbUnwrap(T value) {
    if (value instanceof JAXBElement) {
      return ((JAXBElement) value).getValue();
    } else {
      return value;
    }
  }

  public <I, O> O send(I message) {
    log.debug("send: Began sending message of class {}", message.getClass().getSimpleName());
    String soapAction = lookupSOAPAction(message.getClass());
    Object wrapped = jaxbWrap(message);
    @SuppressWarnings("unchecked")
    O response = (O) jaxbUnwrap(getWebServiceTemplate()
        .marshalSendAndReceive(messageQueueUrl, wrapped, new SoapActionCallback(soapAction)));
    if (!Arrays.asList(knownResponseTypes).contains(response.getClass())) {
      log.error("Message received from TM that does not match any TotalMobile message", response);
      throw new TMMalfunctionException("Message received from TM that does not match any TotalMobile message");
    }
    log.debug("send: successfully sent message and received a response of class {}",
        response.getClass().getSimpleName());
    return response;
  }

}
