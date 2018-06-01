package uk.gov.ons.fwmt.job_service.rest.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.job_service.rest.UserResourceService;
import uk.gov.ons.fwmt.job_service.rest.dto.UserDto;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Slf4j
@Service
public class UserResourceServiceImpl implements UserResourceService {

    @Autowired
    private transient RestTemplate restTemplate;
    @Autowired
    private transient BasicAuthorizationInterceptor basicInterceptor;

    @PostConstruct
    private void initialize() {
        restTemplate.getInterceptors().add(basicInterceptor);
    }

    @Override
    public Optional<UserDto> findByAuthNo(String authNo) {
        log.info("findByAuthNo:{}", authNo);
        try {
            final ResponseEntity<UserDto> userDto = restTemplate.exchange("http://localhost:9095/users/auth/{authNo}", HttpMethod.GET, null, UserDto.class, authNo);
            if (userDto != null && userDto.getStatusCode().equals(HttpStatus.OK)) {
                return Optional.ofNullable(userDto.getBody());
            }
        } catch (HttpClientErrorException httpException) {
            //TODO log error
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> findByAlternateAuthNo(String authNo) {
        try {
            final ResponseEntity<UserDto> userDto = restTemplate.exchange("http://localhost:9095/users/alternative/{altAuthNo}", HttpMethod.GET, null, UserDto.class, authNo);
            //  final UserDto userDto = restTemplate.getForObject("http://localhost:9095/users/alternative", UserDto.class, "authNo");
            if (userDto != null && userDto.getStatusCode().equals(HttpStatus.OK)) {
                return Optional.ofNullable(userDto.getBody());
            }
        } catch (HttpClientErrorException httpException) {
            //TODO log error
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByAuthNoAndActive(String authNo, boolean active) {
        final Optional<UserDto> userDto = findByAuthNo(authNo);
        if (userDto.isPresent()) {
            return userDto.get().isActive() == active;
        }
        return false;
    }

    @Override
    public boolean existsByAlternateAuthNoAndActive(String alternativeAuthNo, boolean active) {
        final Optional<UserDto> userDto = findByAlternateAuthNo(alternativeAuthNo);
        if (userDto.isPresent()) {
            return userDto.get().isActive() == active;
        }
        return false;
    }

}
