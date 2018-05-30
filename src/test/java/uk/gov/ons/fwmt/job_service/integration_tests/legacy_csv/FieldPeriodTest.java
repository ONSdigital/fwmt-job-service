package uk.gov.ons.fwmt.job_service.integration_tests.legacy_csv;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import uk.gov.ons.fwmt.job_service.service.impl.CSVParsingServiceImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FieldPeriodTest {
  void testGFFDate(LocalDateTime expected, String fieldPeriod) {
    LocalDateTime actual = CSVParsingServiceImpl.convertToGFFDate(fieldPeriod);
    assertEquals(expected, actual);
  }

  void testLFSDate(LocalDateTime expected, String fieldPeriod) {
    LocalDateTime actual = convertToLocalDateTime(CSVParsingServiceImpl.convertToLFSDate(fieldPeriod));
    assertEquals(expected, actual);
  }

  public LocalDateTime convertToLocalDateTime(Date dateToConvert) {
    return dateToConvert.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith31Days() {
    testGFFDate(LocalDateTime.of(2018, 7, 31, 23, 59, 59), "807");
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith30Days() {
    testGFFDate(LocalDateTime.of(2018, 6, 30, 23, 59, 59), "806");
  }
  
  @Test
  public void fieldPeriodToDateGFFforMonthWith31DaysReissue() {
    testGFFDate(LocalDateTime.of(2018, 5, 31, 23, 59, 59), "824");
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith30DaysReissue() {
    testGFFDate(LocalDateTime.of(2018, 6, 30, 23, 59, 59), "825");
  }
  
  @Test
  public void fieldPeriodToDateGFFforMonthWith31Days19() {
    testGFFDate(LocalDateTime.of(2019, 7, 31, 23, 59, 59), "907");
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith30Days19() {
    testGFFDate(LocalDateTime.of(2019, 6, 30, 23, 59, 59), "906");
  }
  
  @Test
  public void fieldPeriodToDateGFFforMonthWith31DaysReissue19() {
    testGFFDate(LocalDateTime.of(2019, 5, 31, 23, 59, 59), "924");
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith30DaysReissue19() {
    testGFFDate(LocalDateTime.of(2019, 6, 30, 23, 59, 59), "925");
  }

  @Test
  public void fieldPeriodToDateLFSforQuater4Week11() {
    testLFSDate(LocalDateTime.of(2018, 12, 29, 23, 59, 59), "84K");
  }

  @Test
  public void fieldPeriodToDateLFSforQuater2Week2() {
    testLFSDate(LocalDateTime.of(2018, 4, 28, 23, 59, 59), "82B");
  }
  
  @Test
  public void fieldPeriodToDateLFSforQuater4Week112019() {
    testLFSDate(LocalDateTime.of(2019, 12, 28, 23, 59, 59), "94K");
  }

  @Test
  public void fieldPeriodToDateLFSforQuater2Week22019() {
    testLFSDate(LocalDateTime.of(2019, 4, 27, 23, 59, 59), "92B");
  }
}