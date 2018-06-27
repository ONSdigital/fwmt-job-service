package uk.gov.ons.fwmt.job_service.controller.tm_endpoint;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.WebServiceAdapterOutputRequest;

import org.junit.Test;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import static org.junit.Assert.assertNotNull;

public class GenericOutgoingWsTest {

  GenericOutgoingWs genericOutgoingWs = new GenericOutgoingWs();

  @Test
  public void sendAdapterOutput() {
    //Given

    //When
    QName qname = new QName("request");
    JAXBElement<WebServiceAdapterOutputRequest> jaxbElement = new JAXBElement<WebServiceAdapterOutputRequest>(qname, WebServiceAdapterOutputRequest.class, new WebServiceAdapterOutputRequest());
    JAXBElement<SendMessageResponse> result = genericOutgoingWs.request(jaxbElement);

    //Then
    assertNotNull(result);
  }
}