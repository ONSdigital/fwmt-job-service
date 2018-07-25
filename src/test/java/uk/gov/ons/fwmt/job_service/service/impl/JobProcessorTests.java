package uk.gov.ons.fwmt.job_service.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.rest.client.UserResourceServiceClient;
import uk.gov.ons.fwmt.job_service.rest.client.dto.UserDto;

@RunWith(MockitoJUnitRunner.class)
public class JobProcessorTests {
  @InjectMocks private JobProcessor jobProcessor;
  @Mock private UserResourceServiceClient userResourceServiceClient;
  
  
  @Test
  public void givenALegacySampleIngestWithAnExistingUser_findUserShouldReturnAUserBasedOnAuthNo() {
    UserDto authUserDto = UserDto.builder().authNo("auth").tmUsername("I is Auth").build();
    when(userResourceServiceClient.findByAuthNo("auth")).thenReturn(Optional.of(authUserDto));

    LegacySampleIngest lsi = LegacySampleIngest.builder().auth("auth").build();
    Optional<UserDto> user = jobProcessor.findUser(lsi);
    assertEquals(authUserDto, user.get());
  }  
  
  @Test
  public void givenALegacySampleIngestWithAnAlternateExistingUser_findUserShouldFindByAltAuth_AndReturnAUser() {
    UserDto altAuthUserDto = UserDto.builder().authNo("alt-auth").tmUsername("I is Alt Auth").build();
    when(userResourceServiceClient.findByAuthNo("alt-auth")).thenReturn(Optional.empty());
    when(userResourceServiceClient.findByAlternateAuthNo("alt-auth")).thenReturn(Optional.of(altAuthUserDto));

    LegacySampleIngest lsi = LegacySampleIngest.builder().auth("alt-auth").build();
    Optional<UserDto> user = jobProcessor.findUser(lsi);
    assertEquals(altAuthUserDto, user.get());
  }
  
  @Test
  public void givenALegacySampleIngestWithAnNonExistingUser_findUserShouldNotReturnAUser() {
    when(userResourceServiceClient.findByAuthNo("non-existing")).thenReturn(Optional.empty());
    when(userResourceServiceClient.findByAlternateAuthNo("non-existing")).thenReturn(Optional.empty());

    LegacySampleIngest lsi = LegacySampleIngest.builder().auth("non-existing").build();
    Optional<UserDto> user = jobProcessor.findUser(lsi);
    assertTrue(!user.isPresent());
  }
  

}
