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
import uk.gov.ons.fwmt.job_service.rest.dto.FieldPeriodDto;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class FieldPeriodResourceServiceImplTest {

  @InjectMocks private FieldPeriodResourceServiceImpl fieldPeriodResourceService;
  @Mock RestTemplate restTemplate;
  @Mock ResponseEntity<FieldPeriodDto> responseEntity;

  @Test
  public void findByFieldPeriod() {
    //Given
    String fieldPeriod = "807";
    FieldPeriodDto expectedFPDto = new FieldPeriodDto();
    when(restTemplate.exchange(any(),any(),any(),eq(FieldPeriodDto.class),eq(fieldPeriod))).thenReturn(responseEntity);
    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
    when(responseEntity.getBody()).thenReturn(expectedFPDto);

    //When
    Optional<FieldPeriodDto> result = fieldPeriodResourceService.findByFieldPeriod(fieldPeriod);

    //Then
    assertTrue(result.isPresent());
    assertEquals(expectedFPDto,result.get());
  }

  @Test
  public void findByFieldPeriodAndThrowHttpClientErrorException() {
    //Given
    String fieldPeriod = "807";
    FieldPeriodDto expectedFPDto = new FieldPeriodDto();
    when(restTemplate.exchange(any(),any(),any(),eq(FieldPeriodDto.class),eq(fieldPeriod))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    //When
    Optional<FieldPeriodDto> result = fieldPeriodResourceService.findByFieldPeriod(fieldPeriod);

    //Then
    assertFalse(result.isPresent());
  }
}