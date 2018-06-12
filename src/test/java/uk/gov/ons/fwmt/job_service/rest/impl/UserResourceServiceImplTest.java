package uk.gov.ons.fwmt.job_service.rest.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.job_service.rest.dto.UserDto;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceServiceImplTest {

  @InjectMocks private UserResourceServiceImpl userResourceService;
  @Mock private RestTemplate restTemplate;
  @Mock private ResponseEntity<UserDto> responseEntity;

  @Test
  public void findByAuthNo() {
    //Given
    String testAuthNo = "1111";
    UserDto expectedUserDto = new UserDto();

    when(restTemplate.getForEntity(any(), eq(UserDto.class), eq(testAuthNo))).thenReturn(responseEntity);
    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
    when(responseEntity.getBody()).thenReturn(expectedUserDto);

    //When
    Optional<UserDto> result = userResourceService.findByAuthNo(testAuthNo);

    //Then
    assertTrue(result.isPresent());
    verify(restTemplate).getForEntity(any(), eq(UserDto.class), eq(testAuthNo));
  }

  @Test
  public void authNoNotFound() {
    //Given
    String testAuthNo = "1111";
    when(restTemplate.getForEntity(any(), eq(UserDto.class), eq(testAuthNo)))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    //When
    Optional<UserDto> result = userResourceService.findByAuthNo(testAuthNo);

    //Then
    assertFalse(result.isPresent());
    verify(restTemplate).getForEntity(any(), eq(UserDto.class), eq(testAuthNo));
  }

  @Test
  public void findByAlternateAuthNo() {
    //Given
    String testAltAuth = "2222";
    UserDto expectedUserDto = new UserDto();
    when(restTemplate.getForEntity(any(), eq(UserDto.class), eq(testAltAuth))).thenReturn(responseEntity);
    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
    when(responseEntity.getBody()).thenReturn(expectedUserDto);

    //When
    Optional<UserDto> result = userResourceService.findByAlternateAuthNo(testAltAuth);

    //Then
    assertTrue(result.isPresent());
    verify(restTemplate).getForEntity(any(), eq(UserDto.class), eq(testAltAuth));
  }

  @Test
  public void cannotFindByAlternateAuthNo() {
    //Given
    String testAltAuth = "2222";
    when(restTemplate.getForEntity(any(), eq(UserDto.class), eq(testAltAuth)))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    //When
    Optional<UserDto> result = userResourceService.findByAlternateAuthNo(testAltAuth);

    //Then
    assertFalse(result.isPresent());
    verify(restTemplate).getForEntity(any(), eq(UserDto.class), eq(testAltAuth));
  }

  @Test
  public void existsByAuthNoAndActive() {
    //Given
    String testAuthNo = "1111";
    Boolean isActive = true;
    UserDto expectedUserDto = new UserDto();
    expectedUserDto.setActive(true);
    when(restTemplate.getForEntity(any(), eq(UserDto.class), eq(testAuthNo))).thenReturn(responseEntity);
    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
    when(responseEntity.getBody()).thenReturn(expectedUserDto);

    //When
    Boolean result = userResourceService.existsByAuthNoAndActive(testAuthNo, isActive);

    //Then
    assertTrue(result);
    verify(restTemplate).getForEntity(any(), eq(UserDto.class), eq(testAuthNo));
  }

  @Test

  public void userIsNotActive() {
    //Given
    String testAuthNo = "1111";
    Boolean isActive = false;
    when(restTemplate.getForEntity(any(), eq(UserDto.class), eq(testAuthNo)))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    //When
    Boolean result = userResourceService.existsByAuthNoAndActive(testAuthNo, isActive);

    //Then
    assertFalse(result);
    verify(restTemplate).getForEntity(any(), eq(UserDto.class), eq(testAuthNo));
  }

  @Test
  public void userDoesNotExist() {
    //Given
    String testAuthNo = "1111";
    Boolean isActive = true;
    when(restTemplate.getForEntity(any(), eq(UserDto.class), eq(testAuthNo)))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    //When
    Boolean result = userResourceService.existsByAuthNoAndActive(testAuthNo, isActive);

    //Then
    assertFalse(result);
    verify(restTemplate).getForEntity(any(), eq(UserDto.class), eq(testAuthNo));
  }

  @Test
  public void existsByAlternateAuthNoAndActive() {
    //Given
    String testAlthNo = "1111";
    Boolean isActive = true;
    UserDto expectedUserDto = new UserDto();
    expectedUserDto.setActive(true);
    when(restTemplate.getForEntity(any(), eq(UserDto.class), eq(testAlthNo))).thenReturn(responseEntity);
    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
    when(responseEntity.getBody()).thenReturn(expectedUserDto);

    //When
    Boolean result = userResourceService.existsByAlternateAuthNoAndActive(testAlthNo, isActive);

    //Then
    assertTrue(result);
    verify(restTemplate).getForEntity(any(), eq(UserDto.class), eq(testAlthNo));
  }

  @Test
  public void altAuthNoUserNotActive() {
    //Given
    String testAlthNo = "1111";
    Boolean isActive = false;
    when(restTemplate.getForEntity(any(), eq(UserDto.class), eq(testAlthNo)))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    //When
    Boolean result = userResourceService.existsByAlternateAuthNoAndActive(testAlthNo, isActive);

    //Then
    assertFalse(result);
    verify(restTemplate).getForEntity(any(), eq(UserDto.class), eq(testAlthNo));
  }

  @Test
  public void altAuthNoUserNotExist() {
    //Given
    String testAlthNo = "1111";
    Boolean isActive = true;
    when(restTemplate.getForEntity(any(), eq(UserDto.class), eq(testAlthNo)))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    //When
    Boolean result = userResourceService.existsByAlternateAuthNoAndActive(testAlthNo, isActive);

    //Then
    assertFalse(result);
    verify(restTemplate).getForEntity(any(), eq(UserDto.class), eq(testAlthNo));
  }
}