package uk.gov.ons.fwmt.job_service.rest.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.job_service.exceptions.types.ResourceServiceInaccessibleException;
import uk.gov.ons.fwmt.job_service.exceptions.types.ResourceServiceMalfunctionException;

import java.util.Optional;

@Slf4j
public class RestCommon {
  public static <T> Optional<T> get(RestTemplate restTemplate, String url, Class<T> klass, Object... parameters) {
    try {
      final ResponseEntity<T> responseEntity = restTemplate.getForEntity(url, klass, parameters);
      if (responseEntity.getStatusCode().is2xxSuccessful()) {
        // return a successful result
        log.info(String.format("GET successful, received %s", klass.getSimpleName()));
        return Optional.ofNullable(responseEntity.getBody());
      }
      // any success that doesn't have a 200 is an unexpected error
      ResourceServiceMalfunctionException malfunctionException = new ResourceServiceMalfunctionException(
          String.format("Unexpected HTTP code: %d", responseEntity.getStatusCode().value()));
      log.info("GET failed with non-2xx status code");
      throw malfunctionException;
    } catch (HttpClientErrorException httpException) {
      if (httpException.getStatusCode() == HttpStatus.NOT_FOUND) {
        // a 404, which occurs when we can't find the entity
        log.info(String.format("GET failed with entity %s not found", klass.getSimpleName()));
        return Optional.empty();
      }
      // any other unexpected error
      ResourceServiceMalfunctionException malfunctionException = new ResourceServiceMalfunctionException(
          String.format("Unexpected HTTP code: %d", httpException.getStatusCode().value()), httpException);
      log.info("GET failed with HTTP exception");
      throw malfunctionException;
    } catch (ResourceAccessException accessException) {
      // unable to contact the resource service
      ResourceServiceInaccessibleException inaccessibleException = new ResourceServiceInaccessibleException(
          "Unable to contact the server", accessException);
      log.info("GET failed with network exception");
      throw inaccessibleException;
    }
  }

  public static <T> Optional<T> post(RestTemplate restTemplate, String url, Object request, Class<T> klass,
      Object... parameters) {
    try {
      final ResponseEntity<T> responseEntity = restTemplate.postForEntity(url, request, klass, parameters);
      if (responseEntity.getStatusCode().is2xxSuccessful()) {
        // return a successful result
        log.info(String.format("POST successful, received %s", klass.getSimpleName()));
        return Optional.ofNullable(responseEntity.getBody());
      }
      // any success that doesn't have a 2xx is an unexpected error
      ResourceServiceMalfunctionException malfunctionException = new ResourceServiceMalfunctionException(
          String.format("Unexpected HTTP code: %d", responseEntity.getStatusCode().value()));
      log.info("POST failed with non-2xx status code");
      throw malfunctionException;
    } catch (HttpClientErrorException httpException) {
      // any other unexpected error
      ResourceServiceMalfunctionException malfunctionException = new ResourceServiceMalfunctionException(
          String.format("Unexpected HTTP code: %d", httpException.getStatusCode().value()), httpException);
      log.info("POST failed with HTTP exception");
      throw malfunctionException;
    } catch (ResourceAccessException accessException) {
      // unable to contact the resource service
      ResourceServiceInaccessibleException inaccessibleException = new ResourceServiceInaccessibleException(
          "Unable to contact the server", accessException);
      log.info("POST failed with network exception");
      throw inaccessibleException;
    }
  }

  public static <T> void put(RestTemplate restTemplate, String url, Object request, Object... parameters) {
    try {
      restTemplate.put(url, request, parameters);
      // any non-exception is a success
      log.info("PUT successful");
    } catch (HttpClientErrorException httpException) {
      // any other unexpected error
      ResourceServiceMalfunctionException malfunctionException = new ResourceServiceMalfunctionException(
          String.format("Unexpected HTTP code: %d", httpException.getStatusCode().value()), httpException);
      log.info("PUT failed with HTTP exception");
      throw malfunctionException;
    } catch (ResourceAccessException accessException) {
      // unable to contact the resource service
      ResourceServiceInaccessibleException inaccessibleException = new ResourceServiceInaccessibleException(
          "Unable to contact the server", accessException);
      log.info("PUT failed with network exception");
      throw inaccessibleException;
    }
  }
}
