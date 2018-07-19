package uk.gov.ons.fwmt.job_service.rest.client;

import java.util.Optional;

import uk.gov.ons.fwmt.job_service.rest.client.dto.UserDto;

public interface UserResourceServiceClient {

    Optional<UserDto> findByAuthNo(String authNo);
    Optional<UserDto> findByAlternateAuthNo(String authNo);
    boolean existsByAuthNoAndActive(String authNo, boolean active);
    boolean existsByAlternateAuthNoAndActive(String alternativeAuthNo, boolean active);
}
