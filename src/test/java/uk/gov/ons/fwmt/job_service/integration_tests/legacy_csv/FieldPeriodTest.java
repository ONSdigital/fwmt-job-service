package uk.gov.ons.fwmt.job_service.integration_tests.legacy_csv;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import uk.gov.ons.fwmt.job_service.service.impl.CSVParsingServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class FieldPeriodTest {

  @Autowired
  private CSVParsingServiceImpl csvParsingServiceImpl;
  
  void testGFFDate(LocalDate expected, String fieldPeriod) {
    LocalDate actual = csvParsingServiceImpl.convertToGFFDate(fieldPeriod);
    assertEquals(expected, actual);
  }

  void testLFSDate(LocalDate expected, String fieldPeriod) {
    LocalDate actual = csvParsingServiceImpl.convertToLFSDate(fieldPeriod);
    assertEquals(expected, actual);
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith31Days() {
    testGFFDate(LocalDate.of(2018, 7, 31), "807");
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith30Days() {
    testGFFDate(LocalDate.of(2018, 6, 30), "806");
  }
  
  @Test
  public void fieldPeriodToDateGFFforMonthWith31DaysReissue() {
    testGFFDate(LocalDate.of(2018, 5, 31), "824");
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith30DaysReissue() {
    testGFFDate(LocalDate.of(2018, 6, 30), "825");
  }
  
  @Test
  public void fieldPeriodToDateGFFforMonthWith31Days19() {
    testGFFDate(LocalDate.of(2019, 7, 31), "907");
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith30Days19() {
    testGFFDate(LocalDate.of(2019, 6, 30), "906");
  }
  
  @Test
  public void fieldPeriodToDateGFFforMonthWith31DaysReissue19() {
    testGFFDate(LocalDate.of(2019, 5, 31), "924");
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith30DaysReissue19() {
    testGFFDate(LocalDate.of(2019, 6, 30), "925");
  }

  @Test
  public void fieldPeriodToDateLFSforQuater4Week11() {
    testLFSDate(LocalDate.of(2018, 12, 29), "84K");
  }

  @Test
  public void fieldPeriodToDateLFSforQuater2Week2() {
    testLFSDate(LocalDate.of(2018, 4, 28), "82B");
  }
  
  @Test
  public void fieldPeriodToDateLFSforQuater4Week112019() {
    testLFSDate(LocalDate.of(2019, 12, 28), "94K");
  }

  @Test
  public void fieldPeriodToDateLFSforQuater2Week22019() {
    testLFSDate(LocalDate.of(2019, 4, 27), "92B");
  }
}