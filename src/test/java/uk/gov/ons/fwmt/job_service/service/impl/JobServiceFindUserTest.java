package uk.gov.ons.fwmt.job_service.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.rest.UserResourceService;
import uk.gov.ons.fwmt.job_service.rest.dto.UserDto;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobServiceFindUserTest {
  @Mock private UserResourceService mockUserResourceService;
  @InjectMocks private JobServiceImpl jobService;

  // The user database for this tests contains:
  //  A blank user under authno 2222
  //  A blank user under alternate authno 3333
  @Before
  public void setup() {
    when(mockUserResourceService.findByAuthNo(any())).thenReturn(Optional.empty());
    when(mockUserResourceService.findByAlternateAuthNo(any())).thenReturn(Optional.empty());
    when(mockUserResourceService.findByAuthNo("1111")).thenReturn(Optional.empty());
    when(mockUserResourceService.findByAlternateAuthNo("1111")).thenReturn(Optional.empty());
    UserDto user2222 = new UserDto();
    user2222.setAuthNo("2222");
    when(mockUserResourceService.findByAuthNo("2222")).thenReturn(Optional.of(user2222));
    when(mockUserResourceService.findByAlternateAuthNo("2222")).thenReturn(Optional.empty());
    UserDto user3333 = new UserDto();
    user3333.setAuthNo("3333");
    when(mockUserResourceService.findByAuthNo("3333")).thenReturn(Optional.empty());
    when(mockUserResourceService.findByAlternateAuthNo("3333")).thenReturn(Optional.of(user3333));
  }

  private LegacySampleIngest makeExampleIngest_authNoOnly(String authNo) {
    LegacySampleIngest ingest = new LegacySampleIngest();
    ingest.setAuth(authNo);
    return ingest;
  }

  @Test
  public void findUser_notPresent() {
    Optional<UserDto> user = jobService.findUser(makeExampleIngest_authNoOnly("1111"));
    assertFalse(user.isPresent());
  }

  @Test
  public void findUser_presentByAuthNo() {
    Optional<UserDto> user = jobService.findUser(makeExampleIngest_authNoOnly("2222"));
    assertTrue(user.isPresent());
  }

  @Test
  public void findUser_presentByAltAuthNo() {
    Optional<UserDto> user = jobService.findUser(makeExampleIngest_authNoOnly("3333"));
    assertTrue(user.isPresent());
  }
}
