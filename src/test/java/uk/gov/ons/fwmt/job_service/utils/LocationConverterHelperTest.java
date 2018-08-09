package uk.gov.ons.fwmt.job_service.utils;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.ons.fwmt.job_service.utils.LocationConverterHelper.PolarCoordinate;

import static uk.gov.ons.fwmt.job_service.utils.LocationConverterHelper.CartesianCoordinate;
import static uk.gov.ons.fwmt.job_service.utils.LocationConverterHelper.WGS84_DATUM;
import static uk.gov.ons.fwmt.job_service.utils.LocationConverterHelper.convertLatLong;
import static uk.gov.ons.fwmt.job_service.utils.LocationConverterHelper.osGridRefToLatLong;

public class LocationConverterHelperTest {
  public void assertCartesianCoordinatesApproximate(CartesianCoordinate expected, CartesianCoordinate actual,
      double delta) {
    Assert.assertEquals("x coordinate", expected.x, actual.x, delta);
    Assert.assertEquals("y coordinate", expected.y, actual.y, delta);
    Assert.assertEquals("z coordinate", expected.z, actual.z, delta);
  }

  public void assertPolarCoordinatesApproximate(PolarCoordinate expected, PolarCoordinate actual, double delta) {
    PolarCoordinate expected_normalized = convertLatLong(expected, WGS84_DATUM);
    PolarCoordinate actual_normalized = convertLatLong(actual, WGS84_DATUM);
    Assert.assertEquals("lambda coordinate", expected_normalized.lambda, actual_normalized.lambda, delta);
    Assert.assertEquals("phi coordinate", expected_normalized.phi, actual_normalized.phi, delta);
    Assert.assertEquals("h coordinate", expected_normalized.h, actual_normalized.h, delta);
  }

  @Test
  public void gridRefToWGS84() {
    // 33 Blethin Close, Cardif
    PolarCoordinate expected = new PolarCoordinate(WGS84_DATUM, 51.50503055555556, -3.2379722222222225, 0);
    PolarCoordinate actual = osGridRefToLatLong(new CartesianCoordinate(314174, 179182, 0));
    assertPolarCoordinatesApproximate(expected, actual, 0.00001);

    // 25 Greenway
    assertPolarCoordinatesApproximate(new PolarCoordinate(WGS84_DATUM, 52.2927568,-1.2104724, 0),
        osGridRefToLatLong(new CartesianCoordinate(454011, 266380, 0)), 0.001);

    // 36 Cadogan Road
    System.out.println(osGridRefToLatLong(new CartesianCoordinate(327180, 669452, 0)));
//    assertPolarCoordinatesApproximate(new PolarCoordinate(WGS84_DATUM, 52.2927568,-1.2104724, 0),
//        osGridRefToLatLong(new CartesianCoordinate(454011, 266380, 0)), 0.00001);
  }
}
