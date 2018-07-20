package uk.gov.ons.fwmt.job_service.rest;

import uk.gov.ons.fwmt.job_service.rest.dto.UserDto;

import java.util.Optional;

public interface UserResourceService {
    Optional<UserDto> findByAuthNo(String authNo);
    Optional<UserDto> findByAlternateAuthNo(String authNo);
    Optional<UserDto> findByEitherAuthNo(String authNo);
    boolean existsByAuthNoAndActive(String authNo, boolean active);
    boolean existsByAlternateAuthNoAndActive(String alternativeAuthNo, boolean active);
    boolean existsByEitherAuthNoAndActive(String authNo, boolean active);
}
