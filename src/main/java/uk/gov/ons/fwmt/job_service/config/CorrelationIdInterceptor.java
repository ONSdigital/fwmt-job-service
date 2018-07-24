package uk.gov.ons.fwmt.job_service.config;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static uk.gov.ons.fwmt.job_service.config.CorrelationIdFilter.CID_KEY;

public class CorrelationIdInterceptor implements ClientHttpRequestInterceptor {
  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
      throws IOException {
    HttpHeaders headers = request.getHeaders();
    String correlationId = MDC.get(CID_KEY);
    if (correlationId != null) {
      headers.add(CorrelationIdFilter.CORRELATION_ID_HEADER, correlationId);
    }
    return execution.execute(request, body);
  }
}
