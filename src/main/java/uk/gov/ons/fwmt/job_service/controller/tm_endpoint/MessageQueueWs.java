package uk.gov.ons.fwmt.job_service.controller.tm_endpoint;

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
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.bind.JAXBElement;

@Slf4j
@Endpoint
public class MessageQueueWs {
  private static final String NAMESPACE_URI = "http://schemas.consiliumtechnologies.com/services/mobile/2007/07/messaging";

  private ObjectFactory objectFactory = new ObjectFactory();

  private void stub(String messageType) {
    log.debug("Found message of type {}", messageType);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendMessageRequest")
  @ResponsePayload
  public JAXBElement<SendMessageResponse> sendMessage(@RequestPayload JAXBElement<SendMessageRequest> request) {
    stub("SendMessage");
    SendMessageResponse response = new SendMessageResponse();
    return objectFactory.createSendMessageResponse(response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "TransformAndSendRequest")
  @ResponsePayload
  public JAXBElement<TransformAndSendResponse> transformAndSendMessage(
      @RequestPayload JAXBElement<TransformAndSendRequest> request) {
    stub("TransformAndSendMessage");
    TransformAndSendResponse response = new TransformAndSendResponse();
    return objectFactory.createTransformAndSendResponse(response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "QueryMessagesRequest")
  @ResponsePayload
  public JAXBElement<QueryMessagesResponse> query(@RequestPayload JAXBElement<QueryMessagesRequest> request) {
    stub("Query");
    QueryMessagesResponse response = new QueryMessagesResponse();
    return objectFactory.createQueryMessagesResponse(response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetMessageRequest")
  @ResponsePayload
  public JAXBElement<GetMessageResponse> get(@RequestPayload JAXBElement<GetMessageRequest> request) {
    stub("Get");
    GetMessageResponse response = new GetMessageResponse();
    return objectFactory.createGetMessageResponse(response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteMessageRequest")
  @ResponsePayload
  public JAXBElement<DeleteMessageResponse> delete(@RequestPayload JAXBElement<DeleteMessageRequest> request) {
    stub("Delete");
    DeleteMessageResponse response = new DeleteMessageResponse();
    return objectFactory.createDeleteMessageResponse(response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RetryMessageRequest")
  @ResponsePayload
  public JAXBElement<RetryMessageResponse> retry(@RequestPayload JAXBElement<RetryMessageRequest> request) {
    stub("Retry");
    RetryMessageResponse response = new RetryMessageResponse();
    return objectFactory.createRetryMessageResponse(response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ResetMessageRequest")
  @ResponsePayload
  public JAXBElement<ResetMessageResponse> reset(@RequestPayload JAXBElement<ResetMessageRequest> request) {
    stub("Reset");
    ResetMessageResponse response = new ResetMessageResponse();
    return objectFactory.createResetMessageResponse(response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendCreateVisitRequestMessage")
  @ResponsePayload
  public SendCreateVisitRequestMessageResponse sendCreateVisitRequestMessage(
      @RequestPayload SendCreateVisitRequestMessage request) {
    stub("SendCreateVisitRequestMessage");
    return new SendCreateVisitRequestMessageResponse();
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendForceRecallVisitRequestMessage")
  @ResponsePayload
  public SendForceRecallVisitRequestMessageResponse sendForceRecallVisitRequestMessage(
      @RequestPayload SendForceRecallVisitRequestMessage request) {
    stub("SendForceRecallVisitRequestMessage");
    return new SendForceRecallVisitRequestMessageResponse();
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendAddVisitTasksRequestMessage")
  @ResponsePayload
  public SendAddVisitTasksRequestMessageResponse sendAddVisitTasksRequestMessage(
      @RequestPayload SendAddVisitTasksRequestMessage request) {
    stub("SendAddVisitTasksRequestMessage");
    return new SendAddVisitTasksRequestMessageResponse();
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendUpdateVisitScheduleRequestMessage")
  @ResponsePayload
  public SendUpdateVisitScheduleRequestMessageResponse sendUpdateVisitScheduleRequestMessage(
      @RequestPayload SendUpdateVisitScheduleRequestMessage request) {
    stub("SendUpdateVisitScheduleRequestMessage");
    return new SendUpdateVisitScheduleRequestMessageResponse();
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendUpdateVisitHeaderRequestMessage")
  @ResponsePayload
  public SendUpdateVisitHeaderRequestMessageResponse sendUpdateVisitHeaderRequestMessage(
      @RequestPayload SendUpdateVisitHeaderRequestMessage request) {
    stub("SendUpdateVisitHeaderRequestMessage");
    return new SendUpdateVisitHeaderRequestMessageResponse();
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendCreateBulletinRequestMessage")
  @ResponsePayload
  public SendCreateBulletinRequestMessageResponse sendCreateBulletinRequestMessage(
      @RequestPayload SendCreateBulletinRequestMessage request) {
    stub("SendCreateBulletinRequestMessage");
    return new SendCreateBulletinRequestMessageResponse();
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendDeleteBulletinRequestMessage")
  @ResponsePayload
  public SendDeleteBulletinRequestMessageResponse sendDeleteBulletinRequestMessage(
      @RequestPayload SendDeleteBulletinRequestMessage request) {
    stub("SendDeleteBulletinRequestMessage");
    return new SendDeleteBulletinRequestMessageResponse();
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendGenerateFolioContentRequestMessage")
  @ResponsePayload
  public SendGenerateFolioContentRequestMessageResponse sendGenerateFolioContentRequestMessage(
      @RequestPayload SendGenerateFolioContentRequestMessage request) {
    stub("SendGenerateFolioContentRequestMessage");
    return new SendGenerateFolioContentRequestMessageResponse();
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendAddFolioContentRequestMessage")
  @ResponsePayload
  public SendAddFolioContentRequestMessageResponse sendAddFolioContentRequestMessage(
      @RequestPayload SendAddFolioContentRequestMessage request) {
    stub("SendAddFolioContentRequestMessage");
    return new SendAddFolioContentRequestMessageResponse();
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendCreateReferralRequestMessage")
  @ResponsePayload
  public SendCreateReferralRequestMessageResponse sendCreateReferralRequestMessage(
      @RequestPayload SendCreateReferralRequestMessage request) {
    stub("SendCreateReferralRequestMessage");
    return new SendCreateReferralRequestMessageResponse();
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendCreatePatientRequestMessage")
  @ResponsePayload
  public SendCreatePatientRequestMessageResponse sendCreatePatientRequestMessage(
      @RequestPayload SendCreatePatientRequestMessage request) {
    stub("SendCreatePatientRequestMessage");
    return new SendCreatePatientRequestMessageResponse();
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendUpdateReferralRequestMessage")
  @ResponsePayload
  public SendUpdateReferralRequestMessageResponse sendUpdateReferralRequestMessage(
      @RequestPayload SendUpdateReferralRequestMessage request) {
    stub("SendUpdateReferralRequestMessage");
    return new SendUpdateReferralRequestMessageResponse();
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendCreateAppointmentRequestMessage")
  @ResponsePayload
  public SendCreateAppointmentRequestMessageResponse sendCreateAppointmentRequestMessage(
      @RequestPayload SendCreateAppointmentRequestMessage request) {
    stub("SendCreateAppointmentRequestMessage");
    return new SendCreateAppointmentRequestMessageResponse();
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendDischargeReferralRequestMessage")
  @ResponsePayload
  public SendDischargeReferralRequestMessageResponse sendDischargeReferralRequestMessage(
      @RequestPayload SendDischargeReferralRequestMessage request) {
    stub("SendDischargeReferralRequestMessage");
    return new SendDischargeReferralRequestMessageResponse();
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendCreateJobRequestMessage")
  @ResponsePayload
  public SendCreateJobRequestMessageResponse sendCreateJobRequestMessage(
      @RequestPayload SendCreateJobRequestMessage request) {
    stub("SendCreateJobRequestMessage");
    return new SendCreateJobRequestMessageResponse();
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendDeleteJobRequestMessage")
  @ResponsePayload
  public SendDeleteJobRequestMessageResponse sendDeleteJobRequestMessage(
      @RequestPayload SendDeleteJobRequestMessage request) {
    stub("SendDeleteJobRequestMessage");
    return new SendDeleteJobRequestMessageResponse();
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendAddJobTasksRequestMessage")
  @ResponsePayload
  public SendAddJobTasksRequestMessageResponse sendAddJobTasksRequestMessage(
      @RequestPayload SendAddJobTasksRequestMessage request) {
    stub("SendAddJobTasksRequestMessage");
    return new SendAddJobTasksRequestMessageResponse();
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendSaveAvailabilityRequestMessage")
  @ResponsePayload
  public SendSaveAvailabilityRequestMessageResponse sendSaveAvailabilityRequestMessage(
      @RequestPayload SendSaveAvailabilityRequestMessage request) {
    stub("SendSaveAvailabilityRequestMessage");
    return new SendSaveAvailabilityRequestMessageResponse();
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SendUpdateJobHeaderRequestMessage")
  @ResponsePayload
  public SendUpdateJobHeaderRequestMessageResponse sendUpdateJobHeaderRequestMessage(
      @RequestPayload SendUpdateJobHeaderRequestMessage request) {
    stub("SendUpdateJobHeaderRequestMessage");
    return new SendUpdateJobHeaderRequestMessageResponse();
  }

}
