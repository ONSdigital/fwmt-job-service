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
  
  public void testGFFDate(LocalDate expected, String fieldPeriod) throws FWMTCommonException {
    LocalDate actual = csvParsingServiceImpl.convertToGFFDate(fieldPeriod);
    assertEquals(expected, actual);
  }

  public void testLFSDate(LocalDate expected, String fieldPeriod) throws FWMTCommonException {
    LocalDate actual = csvParsingServiceImpl.convertToLFSDate(fieldPeriod);
    assertEquals(expected, actual);
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith31Days() throws FWMTCommonException {
    testGFFDate(LocalDate.of(2018, 7, 31), "807");
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith30Days() throws FWMTCommonException {
    testGFFDate(LocalDate.of(2018, 6, 30), "806");
  }
  
  @Test
  public void fieldPeriodToDateGFFforMonthWith31DaysReissue() throws FWMTCommonException {
    testGFFDate(LocalDate.of(2018, 5, 31), "824");
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith30DaysReissue() throws FWMTCommonException {
    testGFFDate(LocalDate.of(2018, 6, 30), "825");
  }
  
  @Test
  public void fieldPeriodToDateGFFforMonthWith31Days19() throws FWMTCommonException {
    testGFFDate(LocalDate.of(2019, 7, 31), "907");
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith30Days19() throws FWMTCommonException {
    testGFFDate(LocalDate.of(2019, 6, 30), "906");
  }
  
  @Test
  public void fieldPeriodToDateGFFforMonthWith31DaysReissue19() throws FWMTCommonException {
    testGFFDate(LocalDate.of(2019, 5, 31), "924");
  }

  @Test
  public void fieldPeriodToDateGFFforMonthWith30DaysReissue19() throws FWMTCommonException {
    testGFFDate(LocalDate.of(2019, 6, 30), "925");
  }

  @Test
  public void fieldPeriodToDateLFSforQuater4Week11() throws FWMTCommonException {
    testLFSDate(LocalDate.of(2018, 12, 29), "84K");
  }

  @Test
  public void fieldPeriodToDateLFSforQuater2Week2() throws FWMTCommonException {
    testLFSDate(LocalDate.of(2018, 4, 28), "82B");
  }
  
  @Test
  public void fieldPeriodToDateLFSforQuater4Week112019() throws FWMTCommonException {
    testLFSDate(LocalDate.of(2019, 12, 28), "94K");
  }

  @Test
  public void fieldPeriodToDateLFSforQuater2Week22019() throws FWMTCommonException {
    testLFSDate(LocalDate.of(2019, 4, 27), "92B");
  }
}