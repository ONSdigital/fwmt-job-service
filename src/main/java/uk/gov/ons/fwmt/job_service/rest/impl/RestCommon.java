package uk.gov.ons.fwmt.job_service.rest.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.job_service.exceptions.types.ResourceServiceMalfunctionException;

import java.util.Optional;

@Slf4j
public class RestCommon {
  public static <T> Optional<T> get(RestTemplate restTemplate, String url, Class<T> klass, Object... parameters) {
    try {
      final ResponseEntity<T> responseEntity = restTemplate.getForEntity(url, klass, parameters);
      if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
        // return a successful result
        log.info(String.format("Entity %s found", klass.getSimpleName()));
        return Optional.ofNullable(responseEntity.getBody());
      }
      // any success that doesn't have a 200 is an unexpected error
      ResourceServiceMalfunctionException malfunctionException = new ResourceServiceMalfunctionException(
          String.format("Unexpected HTTP code: %d", responseEntity.getStatusCode().value()));
      log.error(malfunctionException.toString(), malfunctionException);
      // TODO do we throw?
      // throw malfunctionException;
      return Optional.ofNullable(responseEntity.getBody());
    } catch (HttpClientErrorException httpException) {
      if (httpException.getStatusCode() == HttpStatus.NOT_FOUND) {
        // a 404, which occurs when we can't find an authNo
        log.info(String.format("Entity %s not found", klass.getSimpleName()));
        return Optional.empty();
      }
      // any other unexpected error
      ResourceServiceMalfunctionException malfunctionException = new ResourceServiceMalfunctionException(
          String.format("Unexpected HTTP code: %d", httpException.getStatusCode().value()), httpException);
      log.error(malfunctionException.toString(), malfunctionException);
      // TODO do we throw?
      // throw malfunctionException;
      return Optional.empty();
    }
  }
}
