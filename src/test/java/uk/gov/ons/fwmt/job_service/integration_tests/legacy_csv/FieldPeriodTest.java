package uk.gov.ons.fwmt.job_service.integration_tests.legacy_csv;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service.service.impl.CSVParsingServiceImpl;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class FieldPeriodTest {

  @Autowired
  private CSVParsingServiceImpl csvParsingServiceImpl;

  @Test
  public void fieldPeriodToDateGFFforMonthWith31Days() throws FWMTCommonException {
    String fieldPeriod = "807";
    int year = 2018;
    int month = 7;
    int day = 31;
    LocalDate expected = LocalDate.of(year,month,day);
    LocalDate actual = csvParsingServiceImpl.convertToGFFDate(fieldPeriod);
    assertEquals(expected, actual);
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith30Days() throws FWMTCommonException {
    String fieldPeriod = "806";
    int year = 2018;
    int month = 6;
    int day = 30;
    LocalDate expected = LocalDate.of(year,month,day);
    LocalDate actual = csvParsingServiceImpl.convertToGFFDate(fieldPeriod);
    assertEquals(expected, actual);
  }
  
  @Test
  public void fieldPeriodToDateGFFforMonthWith31DaysReissue() throws FWMTCommonException {
    String fieldPeriod = "824";
    int year = 2018;
    int month = 5;
    int day = 31;
    LocalDate expected = LocalDate.of(year,month,day);
    LocalDate actual = csvParsingServiceImpl.convertToGFFDate(fieldPeriod);
    assertEquals(expected, actual);
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith30DaysReissue() throws FWMTCommonException {
    String fieldPeriod = "825";
    int year = 2018;
    int month = 6;
    int day = 30;
    LocalDate expected = LocalDate.of(year,month,day);
    LocalDate actual = csvParsingServiceImpl.convertToGFFDate(fieldPeriod);
    assertEquals(expected, actual);
  }
  
  @Test
  public void fieldPeriodToDateGFFforMonthWith31Days19() throws FWMTCommonException {
    String fieldPeriod = "907";
    int year = 2019;
    int month = 7;
    int day = 31;
    LocalDate expected = LocalDate.of(year,month,day);
    LocalDate actual = csvParsingServiceImpl.convertToGFFDate(fieldPeriod);
    assertEquals(expected, actual);
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith30Days19() throws FWMTCommonException {
    String fieldPeriod = "906";
    int year = 2018;
    int month = 6;
    int day = 30;
    LocalDate expected = LocalDate.of(year,month,day);
    LocalDate actual = csvParsingServiceImpl.convertToGFFDate(fieldPeriod);
    assertEquals(expected, actual);
  }
  
  @Test
  public void fieldPeriodToDateGFFforMonthWith31DaysReissue19() throws FWMTCommonException {
    String fieldPeriod = "924";
    int year = 2019;
    int month = 5;
    int day = 31;
    LocalDate expected = LocalDate.of(year,month,day);
    LocalDate actual = csvParsingServiceImpl.convertToGFFDate(fieldPeriod);
    assertEquals(expected, actual);
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith30DaysReissue19() throws FWMTCommonException {
    String fieldPeriod = "925";
    int year = 2019;
    int month = 6;
    int day = 30;
    LocalDate expected = LocalDate.of(year,month,day);
    LocalDate actual = csvParsingServiceImpl.convertToGFFDate(fieldPeriod);
    assertEquals(expected, actual);
  }

  @Test
  public void fieldPeriodToDateLFSforQuater4Week11() throws FWMTCommonException {
    String fieldPeriod = "84K";
    int year = 2018;
    int month = 12;
    int day = 19;
    LocalDate expected = LocalDate.of(year,month,day);
    LocalDate actual = csvParsingServiceImpl.convertToLFSDate(fieldPeriod);
    assertEquals(expected, actual);
  }

  @Test
  public void fieldPeriodToDateLFSforQuater2Week2() throws FWMTCommonException {
    String fieldPeriod = "82B";
    int year = 2018;
    int month = 4;
    int day = 28;
    LocalDate expected = LocalDate.of(year,month,day);
    LocalDate actual = csvParsingServiceImpl.convertToLFSDate(fieldPeriod);
    assertEquals(expected, actual);
  }
  
  @Test
  public void fieldPeriodToDateLFSforQuater4Week112019() throws FWMTCommonException {
    String fieldPeriod = "94K";
    int year = 2019;
    int month = 12;
    int day = 28;
    LocalDate expected = LocalDate.of(year,month,day);
    LocalDate actual = csvParsingServiceImpl.convertToLFSDate(fieldPeriod);
    assertEquals(expected, actual);
  }

  @Test
  public void fieldPeriodToDateLFSforQuater2Week22019() throws FWMTCommonException {
    String fieldPeriod = "92B";
    int year = 2018;
    int month = 7;
    int day = 31;
    LocalDate expected = LocalDate.of(year,month,day);
    LocalDate actual = csvParsingServiceImpl.convertToLFSDate(fieldPeriod);
    assertEquals(expected, actual);
  }
}