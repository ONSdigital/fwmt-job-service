package uk.gov.ons.fwmt.job_service.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class CorrelationIdInterceptor implements ClientHttpRequestInterceptor {
  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
      throws IOException {
    HttpHeaders headers = request.getHeaders();
    headers.add(CorrelationIdFilter.CORRELATION_ID_HEADER, CorrelationIdHandler.getId());
    return execution.execute(request, body);
  }
}
