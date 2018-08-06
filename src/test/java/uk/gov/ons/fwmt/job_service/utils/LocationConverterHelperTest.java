package uk.gov.ons.fwmt.job_service.utils;

import org.junit.Test;

public class LocationConverterHelperTest {
  @Test
  public void testing() {
    System.out.println(LocationConvertorHelper.latLongToOsGridRef(52.6575, 1.7178));
  }
}
