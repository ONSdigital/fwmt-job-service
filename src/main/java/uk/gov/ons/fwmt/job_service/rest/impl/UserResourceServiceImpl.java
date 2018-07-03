package uk.gov.ons.fwmt.job_service.rest.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.job_service.rest.UserResourceService;
import uk.gov.ons.fwmt.job_service.rest.dto.UserDto;

import java.util.Optional;

@Slf4j
@Service
public class UserResourceServiceImpl implements UserResourceService {
  private transient RestTemplate restTemplate;

  private transient String findUrl;
  private transient String findAltUrl;

  @Autowired
  public UserResourceServiceImpl(
      RestTemplate restTemplate,
      @Value("${service.resource.baseUrl}") String baseUrl,
      @Value("${service.resource.operation.users.find.path}") String findPath,
      @Value("${service.resource.operation.users.findAlt.path}") String findAltPath) {
    this.restTemplate = restTemplate;
    this.findUrl = baseUrl + findPath;
    this.findAltUrl = baseUrl + findAltPath;
  }

  @Override
  public Optional<UserDto> findByAuthNo(String authNo) {
    log.info("findByAuthNo: {}", authNo);
    try {
      final ResponseEntity<UserDto> responseEntity = restTemplate.getForEntity(findUrl, UserDto.class, authNo);
      if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
        // return a successful result
        return Optional.ofNullable(responseEntity.getBody());
      }
      // any success that doesn't have a 200 is an unexpected error
      log.error("findByAuthNo: returned with a non-200 code: authNo={}, code={}", authNo,
          responseEntity.getStatusCodeValue());
      return Optional.empty();
    } catch (HttpClientErrorException httpException) {
      if (httpException.getStatusCode() == HttpStatus.NOT_FOUND) {
        // a 404, which occurs when we can't find an authNo
        log.debug("findByAuthNo: authNo not found", httpException);
        return Optional.empty();
      }
      // any other unexpected error
      log.error(String.format("findByAuthNo: error communicating with the resource service: authNo=%s", authNo),
          httpException);
      return Optional.empty();
    }
  }

  @Override
  public Optional<UserDto> findByAlternateAuthNo(String authNo) {
    log.info("findByAlternateAuthNo: {}", authNo);
    try {
      final ResponseEntity<UserDto> responseEntity = restTemplate.getForEntity(findAltUrl, UserDto.class, authNo);
      if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
        // return a successful result
        return Optional.ofNullable(responseEntity.getBody());
      }
      // any success that doesn't have a 200 is an unexpected error
      log.error("findByAlternateAuthNo: returned with a non-200 code: authNo={}, code={}", authNo,
          responseEntity.getStatusCodeValue());
      return Optional.empty();
    } catch (HttpClientErrorException httpException) {
      if (httpException.getStatusCode() == HttpStatus.NOT_FOUND) {
        // a 404, which occurs when we can't find an authNo
        log.debug("findByAlternateAuthNo: authNo not found", httpException);
        return Optional.empty();
      }
      // any other unexpected error
      log.error(String.format("findByAlternateAuthNo: error communicating with the resource service: authNo=%s", authNo),
          httpException);
      return Optional.empty();
    }
  }

  @Override
  public boolean existsByAuthNoAndActive(String authNo, boolean active) {
    log.info("existsByAuthNoAndActive: {}, {}", authNo, active);
    final Optional<UserDto> userDto = findByAuthNo(authNo);
    return userDto.filter(userDto1 -> userDto1.isActive() == active).isPresent();
  }

  @Override
  public boolean existsByAlternateAuthNoAndActive(String authNo, boolean active) {
    log.info("existsByAlternativeAuthNoAndActive: {}, {}", authNo, active);
    final Optional<UserDto> userDto = findByAlternateAuthNo(authNo);
    return userDto.filter(userDto1 -> userDto1.isActive() == active).isPresent();
  }

}
