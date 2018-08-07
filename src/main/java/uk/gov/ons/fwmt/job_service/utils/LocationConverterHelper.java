package uk.gov.ons.fwmt.job_service.utils;

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  */
/* Ordnance Survey Grid Reference functions                           (c) Chris Veness 2005-2017  */
/*                                                                                   MIT Licence  */
/* www.movable-type.co.uk/scripts/latlong-gridref.html                                            */
/* www.movable-type.co.uk/scripts/geodesy/docs/module-osgridref.html                              */
/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  */

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.Math.pow;

public class LocationConverterHelper {
  protected final static EllipsoidParameters AIRY1830_ELLIPSOID =
      new EllipsoidParameters(6377563.396, 6356256.909, 1 / 299.3249646);
  protected final static EllipsoidParameters WGS84_ELLIPSOID =
      new EllipsoidParameters(6377563.396, 6356256.909, 1 / 299.3249646);

  protected final static DatumParameters OSGB36_DATUM = new DatumParameters(AIRY1830_ELLIPSOID,
      -446.448, 125.157, -542.060, 20.4894, -0.1502, -0.2470, -0.8421);
  protected final static DatumParameters WGS84_DATUM = new DatumParameters(WGS84_ELLIPSOID,
      0, 0, 0, 0, 0, 0, 0);

  protected final static double NAT_GRID_SCALE_FACTOR = 0.9996012717;
  protected final static CartesianCoordinate NAT_GRID_ORIGIN_CARTESIAN = new CartesianCoordinate(-10000, 400000, 0);
  protected final static PolarCoordinate NAT_GRID_ORIGIN_POLAR = new PolarCoordinate(toRadians(49), toRadians(-2), 0);

  protected static double round(double value, int places) {
    if (places < 0)
      throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

  protected static double toRadians(double input) {
    return input * (Math.PI / 180);
  }

  protected static double toDegrees(double input) {
    return input * (180 / Math.PI);
  }

  protected static CartesianCoordinate toCartesian(LatLong input) {
    double phi = toRadians(input.lat);
    double lambda = toRadians(input.lon);
    double h = 0; // height above ellipsoid - not currently used
    double a = input.datum.ellipsoid.a, f = input.datum.ellipsoid.f;

    double sin_phi = Math.sin(phi);
    double cos_phi = Math.cos(phi);
    double sin_lambda = Math.sin(lambda);
    double cos_lambda = Math.cos(lambda);

    // eSq: 1st eccentricity squared ≡ (a²-b²)/a²
    double eSq = 2 * f - pow(f, 2);
    // nu: radius of curvature in prime vertical
    double nu = a / Math.sqrt(1 - eSq * sin_phi * sin_phi);

    double x = (nu + h) * cos_phi * cos_lambda;
    double y = (nu + h) * cos_phi * sin_lambda;
    double z = (nu * (1 - eSq) + h) * sin_phi;

    return new CartesianCoordinate(x, y, z);
  }

  protected static LatLong toLatLong(CartesianCoordinate input, DatumParameters datum) {
    double x = input.x;
    double y = input.y;
    double z = input.z;
    double a = datum.ellipsoid.a;
    double b = datum.ellipsoid.b;
    double f = datum.ellipsoid.f;

    double e2 = 2 * f - pow(f, 2);   // 1st eccentricity squared ≡ (a²-b²)/a²
    double epsilon2 = e2 / (1 - e2); // 2nd eccentricity squared ≡ (a²-b²)/b²
    double p = Math.sqrt(pow(x, 2) + pow(y, 2)); // distance from minor axis
    double R = Math.sqrt(pow(p, 2) + pow(z, 2)); // polar radius

    // parametric latitude (Bowring eqn 17, replacing tanβ = z·a / p·b)
    double tan_beta = (b * z) / (a * p) * (1 + epsilon2 * b / R);
    double sin_beta = tan_beta / Math.sqrt(1 + tan_beta * tan_beta);
    double cos_beta = sin_beta / tan_beta;

    // geodetic latitude (Bowring eqn 18: tanφ = z+ε²bsin³β / p−e²cos³β)
    double phi = Double.isNaN(cos_beta) ?
        0 :
        Math.atan2(z + epsilon2 * b * pow(sin_beta, 3), p - e2 * a * pow(cos_beta, 3));

    // longitude
    double lambda = Math.atan2(y, x);

    // height above ellipsoid (Bowring eqn 7) [not currently used]
    // nu: length of the normal terminated by the minor axis
    double nu = a / Math.sqrt(1 - e2 * pow(Math.sin(phi), 2));
    double h = p * Math.cos(phi) + z * Math.sin(phi) - (pow(a, 2) / nu);

    return new LatLong(datum, toDegrees(phi), toDegrees(lambda));
  }

  protected static CartesianCoordinate helmertTransform(CartesianCoordinate input, DatumParameters datum) {
    double rx = toRadians(datum.rx / 3600);
    double ry = toRadians(datum.ry / 3600);
    double rz = toRadians(datum.rz / 3600);

    double x_prime = datum.tx + ((1 + datum.scale) * input.x) - (rz * input.y) + (datum.ry * input.z);
    double y_prime = datum.ty + (rz * input.x) + ((1 + datum.scale) * input.y) - (datum.rx * input.z);
    double z_prime = datum.tz - (ry * input.x) + (datum.rx * input.y) + ((1 + datum.scale) * input.z);
    return new CartesianCoordinate(x_prime, y_prime, z_prime);
  }

//  protected static LatLong convertLatLong(LatLong input, DatumParameters source, DatumParameters destination) {
//
//  }

  protected static CartesianCoordinate latLongToOsGridRef(LatLong input) {
    double psi = toRadians(input.lat);
    double lambda = toRadians(input.lon);

    // elipsoid parameters
    double a = AIRY1830_ELLIPSOID.a;
    double b = AIRY1830_ELLIPSOID.b;

    double F0 = NAT_GRID_SCALE_FACTOR;

    // origin
    double psi_0 = NAT_GRID_ORIGIN_POLAR.psi;
    double lambda_0 = NAT_GRID_ORIGIN_POLAR.lambda;
    double northings_origin = NAT_GRID_ORIGIN_CARTESIAN.x;
    double eastings_origin = NAT_GRID_ORIGIN_CARTESIAN.y;

    // e2: eccentricity squared
    double e2 = 1 - pow(b, 2) / pow(a, 2);
    double n = (a - b) / (a + b);

    double cos_psi = Math.cos(psi);
    double sin_psi = Math.sin(psi);
    double tan_psi = Math.tan(psi);

    double v = a * F0 / Math.sqrt(1 - e2 * pow(sin_psi, 2));
    double rho = a * F0 * (1 - e2) / pow(1 - e2 * pow(sin_psi, 2), 1.5);
    double eta_2 = v / rho - 1;

    double Ma = (1 + n + (5 / 4) * pow(n, 2) + (5 / 4) * pow(n, 3)) * (psi - psi_0);
    double Mb = (3 * n + 3 * n * n + (21 / 8) * pow(n, 3)) * Math.sin(psi - psi_0) * Math.cos(psi + psi_0);
    double Mc =
        ((15 / 8) * pow(n, 2) + (15 / 8) * pow(n, 3)) * Math.sin(2 * (psi - psi_0)) * Math.cos(2 * (psi + psi_0));
    double Md = (35 / 24) * pow(n, 3) * Math.sin(3 * (psi - psi_0)) * Math.cos(3 * (psi + psi_0));
    double M = b * F0 * (Ma - Mb + Mc - Md);

    double I = M + northings_origin;
    double II = (v / 2) * sin_psi * cos_psi;
    double III = (v / 24) * sin_psi * pow(cos_psi, 3) * (5 - pow(tan_psi, 2) + 9 * eta_2);
    double IIIA = (v / 720) * sin_psi * pow(cos_psi, 5) * (61 - 58 * pow(tan_psi, 2) + pow(tan_psi, 4));
    double IV = v * cos_psi;
    double V = (v / 6) * pow(cos_psi, 3) * (v / rho - pow(tan_psi, 2));
    double VI =
        (v / 120) * pow(cos_psi, 5) * (5 - 18 * pow(tan_psi, 2) + pow(tan_psi, 4) + 14 * eta_2 - 58 * pow(tan_psi, 2)
            * eta_2);

    double delta_lambda = lambda - lambda_0;

    double northings = I + II * pow(delta_lambda, 2) + III * pow(delta_lambda, 4) + IIIA * pow(delta_lambda, 6);
    double eastings = eastings_origin + IV * delta_lambda + V * pow(delta_lambda, 3) + VI * pow(delta_lambda, 5);

    northings = round(northings, 3);
    eastings = round(eastings, 3);

    return new CartesianCoordinate(eastings, northings, 0);
  }

  //  private CartesianCoordinate polarToCartesian(PolarCoordinate input) {
  //
  //  }

  //  private PolarCoordinate cartesianToPolar(CartesianCoordinate input) {
  //
  //  }

  protected static LatLong osGridRefToLatLong(CartesianCoordinate input) {
    double eastings = input.x;
    double northings = input.y;

    // elipsoid parameters
    double a = AIRY1830_ELLIPSOID.a;
    double b = AIRY1830_ELLIPSOID.b;

    double F0 = NAT_GRID_SCALE_FACTOR;

    // origin
    double psi_0 = NAT_GRID_ORIGIN_POLAR.psi;
    double lambda_0 = NAT_GRID_ORIGIN_POLAR.lambda;
    double northings_origin = NAT_GRID_ORIGIN_CARTESIAN.x;
    double eastings_origin = NAT_GRID_ORIGIN_CARTESIAN.y;

    // e2: eccintricity squared
    double e2 = 1 - pow(b, 2) / pow(a, 2);
    double n = (a - b) / (a + b);

    double psi = psi_0;
    double M = 0;
    do {
      psi = (northings - northings_origin - M) / (a * F0) + psi;

      double Ma = (1 + n + (5 / 4) * pow(n, 2) + (5 / 4) * pow(n, 3)) * (psi - psi_0);
      double Mb = (3 * n + 3 * n * n + (21 / 8) * pow(n, 3)) * Math.sin(psi - psi_0) * Math.cos(psi + psi_0);
      double Mc =
          ((15 / 8) * pow(n, 2) + (15 / 8) * pow(n, 3)) * Math.sin(2 * (psi - psi_0)) * Math.cos(2 * (psi + psi_0));
      double Md = (35 / 24) * pow(n, 3) * Math.sin(3 * (psi - psi_0)) * Math.cos(3 * (psi + psi_0));
      M = b * F0 * (Ma - Mb + Mc - Md);              // meridional arc
    } while (northings - northings_origin - M >= 0.00001);  // ie until < 0.01mm

    double sin_psi = Math.sin(psi);
    // nu: transverse radius of curvature
    double nu = a * F0 / Math.sqrt(1 - e2 * pow(sin_psi, 2));
    // rho = meridional radius of curvature
    double rho = a * F0 * (1 - e2) / Math.pow(1 - e2 * pow(sin_psi, 2), 1.5);
    double eta_2 = nu / rho - 1;

    double tan_psi = Math.tan(psi);
    double sec_psi = 1 / Math.cos(psi);
    double VII = tan_psi / (2 * rho * nu);
    double VIII = tan_psi / (24 * rho * pow(nu, 3)) * (5 + 3 * pow(tan_psi, 2) + eta_2 - 9 * pow(tan_psi, 2) * eta_2);
    double IX = tan_psi / (720 * rho * pow(nu, 5)) * (61 + 90 * pow(tan_psi, 2) + 45 * pow(tan_psi, 4));
    double X = sec_psi / nu;
    double XI = sec_psi / (6 * pow(nu, 3)) * (nu / rho + 2 * pow(tan_psi, 2));
    double XII = sec_psi / (120 * pow(nu, 5)) * (5 + 28 * pow(tan_psi, 2) + 24 * pow(tan_psi, 4));
    double XIIA =
        sec_psi / (5040 * pow(nu, 7)) * (61 + 662 * pow(tan_psi, 2) + 1320 * pow(tan_psi, 4) + 720 * pow(tan_psi, 6));

    double dE = (eastings - eastings_origin);
    psi = psi - VII * pow(dE, 2) + VIII * pow(dE, 4) - IX * pow(dE, 6);
    double lambda = lambda_0 + X * dE - XI * pow(dE, 3) + XII * pow(dE, 5) - XIIA * pow(dE, 7);

    return new LatLong(WGS84_DATUM, toDegrees(psi), toDegrees(lambda));
  }

  @Data
  @AllArgsConstructor
  private static class CartesianCoordinate {
    double x, y, z;
  }

  @Data
  @AllArgsConstructor
  private static class PolarCoordinate {
    double psi, lambda, h;
  }

  @Data
  @AllArgsConstructor
  private static class LatLong {
    DatumParameters datum;
    double lat, lon;
  }

  @Data
  @AllArgsConstructor
  private static class EllipsoidParameters {
    double a, b, f;
  }

  @Data
  @AllArgsConstructor
  private static class DatumParameters {
    EllipsoidParameters ellipsoid;
    double tx, ty, tz, scale, rx, ry, rz;
  }

}
