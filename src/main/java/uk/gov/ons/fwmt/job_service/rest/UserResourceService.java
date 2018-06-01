package uk.gov.ons.fwmt.job_service.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.job_service.rest.dto.UserDto;

public interface UserResourceService {

    UserDto findByAuthNo(String authNo);
    UserDto findByAlternateAuthNo(String authNo);
    boolean existsByAuthNoAndActive(String authNo, boolean active);
    boolean existsByAlternateAuthNoAndActive(String alternativeAuthNo, boolean active);
}
