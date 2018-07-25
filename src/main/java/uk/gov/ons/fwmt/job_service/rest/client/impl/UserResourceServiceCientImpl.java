package uk.gov.ons.fwmt.job_service.rest.client.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.fwmt.job_service.rest.client.ResourceRESTHelper;
import uk.gov.ons.fwmt.job_service.rest.client.UserResourceServiceClient;
import uk.gov.ons.fwmt.job_service.rest.client.dto.UserDto;

@Slf4j
@Service
public class UserResourceServiceCientImpl implements UserResourceServiceClient {
  private transient RestTemplate restTemplate;

  private transient String findUrl;
  private transient String findAltUrl;

  @Autowired
  public UserResourceServiceCientImpl(
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
    log.debug("Start: authNo={}", authNo);
    Optional<UserDto> userDto = ResourceRESTHelper.get(restTemplate, findUrl, UserDto.class, authNo);
    if (userDto.isPresent()) {
      log.debug("Found: {}", userDto.get());
    } else {
      log.debug("Not found");
    }
    return userDto;
  }

  @Override
  public Optional<UserDto> findByAlternateAuthNo(String authNo) {
    log.debug("Start: authNo={}", authNo);
    Optional<UserDto> userDto = ResourceRESTHelper.get(restTemplate, findAltUrl, UserDto.class, authNo);
    if (userDto.isPresent()) {
      log.debug("Found: {}", userDto.get());
    } else {
      log.debug("Not found");
    }
    return userDto;
  }

  @Override
  public Optional<UserDto> findByEitherAuthNo(String authNo) {
    log.debug("Start: authNo={}" + authNo);
    Optional<UserDto> userDto = findByAuthNo(authNo);
    if (userDto.isPresent()) {
      return userDto;
    }
    userDto = findByAlternateAuthNo(authNo);
    return userDto;
  }

  @Override
  public boolean existsByAuthNoAndActive(String authNo, boolean active) {
    log.debug("Start: authNo={},active={}", authNo, active);
    final Optional<UserDto> userDto = findByAuthNo(authNo);
    boolean result = userDto.filter(userDto1 -> userDto1.isActive() == active).isPresent();
    if (result) {
      log.debug("Found");
    } else {
      log.debug("Not found");
    }
    return result;
  }

  @Override
  public boolean existsByAlternateAuthNoAndActive(String authNo, boolean active) {
    log.debug("Start: authNo={},active={}", authNo, active);
    final Optional<UserDto> userDto = findByAlternateAuthNo(authNo);
    boolean result = userDto.filter(userDto1 -> userDto1.isActive() == active).isPresent();
    if (result) {
      log.debug("Found");
    } else {
      log.debug("Not found");
    }
    return result;
  }

  @Override
  public boolean existsByEitherAuthNoAndActive(String authNo, boolean active) {
    log.debug("Start: authNo={},active={}", authNo, active);
    final Optional<UserDto> userDto = findByEitherAuthNo(authNo);
    boolean result = userDto.filter(userDto1 -> userDto1.isActive() == active).isPresent();
    if (result) {
      log.debug("Found");
    } else {
      log.debug("Not found");
    }
    return result;
  }
}
