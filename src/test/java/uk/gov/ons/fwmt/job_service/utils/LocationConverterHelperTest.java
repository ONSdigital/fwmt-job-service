package uk.gov.ons.fwmt.job_service.utils;

import org.junit.Test;
import uk.gov.ons.fwmt.job_service.utils.LocationConverterHelper.PolarCoordinate;

import static uk.gov.ons.fwmt.job_service.utils.LocationConverterHelper.*;

public class LocationConverterHelperTest {
  @Test
  public void testing() {
//    System.out.println(latLongToOsGridRef(new PolarCoordinate(WGS84_DATUM, 52.6575, 1.7178, 0)));
    System.out.println(osGridRefToLatLong(new CartesianCoordinate(651530, 313130, 0)));
  }
}
