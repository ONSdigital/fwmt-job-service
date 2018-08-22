package uk.gov.ons.fwmt.job_service.utils;

/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  */
/* Ordnance Survey Grid Reference functions                           (c) Chris Veness 2005-2017  */
/*                                                                                   MIT Licence  */
/* www.movable-type.co.uk/scripts/latlong-gridref.html                                            */
/* www.movable-type.co.uk/scripts/geodesy/docs/module-osgridref.html                              */
/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.Math.pow;

public class LocationConverterHelper {
  protected final static EllipsoidParameters AIRY1830_ELLIPSOID =
      new EllipsoidParameters(6377563.396, 6356256.909, 1 / 299.3249646);
  protected final static EllipsoidParameters WGS84_ELLIPSOID =
      new EllipsoidParameters(6378137, 6356752.314245, 1 / 298.257223563);

  protected final static DatumParameters OSGB36_DATUM = new DatumParameters(AIRY1830_ELLIPSOID,
      -446.448, 125.157, -542.060, 20.4894, -0.1502, -0.2470, -0.8421);
  protected final static DatumParameters WGS84_DATUM = new DatumParameters(WGS84_ELLIPSOID,
      0, 0, 0, 0, 0, 0, 0);

  protected final static double NAT_GRID_SCALE_FACTOR = 0.9996012717;
  protected final static CartesianCoordinate NAT_GRID_ORIGIN_CARTESIAN = new CartesianCoordinate(-100000, 400000, 0);
  protected final static PolarCoordinate NAT_GRID_ORIGIN_POLAR =
      new PolarCoordinate(WGS84_DATUM, toRadians(49), toRadians(-2), 0);

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

  protected static CartesianCoordinate toCartesian(PolarCoordinate input) {
    double phi = toRadians(input.phi);
    double lambda = toRadians(input.lambda);
    double h = 0; // height above ellipsoid - not currently used
    double a = input.datum.ellipsoid.a;
    double f = input.datum.ellipsoid.f;

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

  protected static PolarCoordinate toLatLong(CartesianCoordinate input, DatumParameters datum) {
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

    return new PolarCoordinate(datum, toDegrees(phi), toDegrees(lambda), 0);
  }

  protected static CartesianCoordinate helmertTransform(CartesianCoordinate input, DatumParameters datum) {
    // normalise parts-per-million to (s+1)
    double scale = datum.scale / 1e6 + 1;

    // normalize arcseconds to radians
    double rx = toRadians(datum.rx / 3600);
    double ry = toRadians(datum.ry / 3600);
    double rz = toRadians(datum.rz / 3600);

    // apply the transformation
    double x_prime = datum.tx + input.x * scale - input.y * rz + input.z * ry;
    double y_prime = datum.ty + input.x * rz + input.y * scale - input.z * rx;
    double z_prime = datum.tz - input.x * ry + input.y * rx + input.z * scale;

    return new CartesianCoordinate(x_prime, y_prime, z_prime);
  }

  protected static PolarCoordinate convertLatLong(PolarCoordinate input, DatumParameters target) {
    DatumParameters transformation;
    if (input.datum == target) {
      return input;
    } else if (input.datum == WGS84_DATUM) {
      // converting from WGS 84
      transformation = target;
    } else if (target == WGS84_DATUM) {
      // converting to WGS 84; use inverse transform
      transformation = input.datum.inverse;
    } else {
      // neither this.datum nor toDatum are WGS84: convert the input to WGS84 first
      input = convertLatLong(input, WGS84_DATUM);
      transformation = target;
    }

    CartesianCoordinate oldCartesian = toCartesian(input);
    CartesianCoordinate newCartesian = helmertTransform(oldCartesian, transformation);
    return toLatLong(newCartesian, target);
  }

  protected static CartesianCoordinate latLongToOsGridRef(PolarCoordinate input) {
    PolarCoordinate translated = convertLatLong(input, OSGB36_DATUM);

    double phi = toRadians(translated.phi);
    double lambda = toRadians(translated.lambda);

    // elipsoid parameters
    double a = AIRY1830_ELLIPSOID.a;
    double b = AIRY1830_ELLIPSOID.b;

    double F0 = NAT_GRID_SCALE_FACTOR;

    // origin
    double phi_0 = NAT_GRID_ORIGIN_POLAR.phi;
    double lambda_0 = NAT_GRID_ORIGIN_POLAR.lambda;
    double northings_origin = NAT_GRID_ORIGIN_CARTESIAN.x;
    double eastings_origin = NAT_GRID_ORIGIN_CARTESIAN.y;

    // e2: eccentricity squared
    double e2 = 1 - pow(b, 2) / pow(a, 2);
    double n = (a - b) / (a + b);

    double cos_phi = Math.cos(phi);
    double sin_phi = Math.sin(phi);
    double tan_phi = Math.tan(phi);

    double v = a * F0 / Math.sqrt(1 - e2 * pow(sin_phi, 2));
    double rho = a * F0 * (1 - e2) / pow(1 - e2 * pow(sin_phi, 2), 1.5);
    double eta_2 = v / rho - 1;

    double Ma = (1 + n + (5.0 / 4.0) * pow(n, 2) + (5.0 / 4.0) * pow(n, 3)) * (phi - phi_0);
    double Mb = (3 * n + 3 * n * n + (21.0 / 8.0) * pow(n, 3)) * Math.sin(phi - phi_0) * Math.cos(phi + phi_0);
    double Mc =
        ((15.0 / 8.0) * pow(n, 2) + (15.0 / 8.0) * pow(n, 3)) * Math.sin(2 * (phi - phi_0)) * Math.cos(2 * (phi + phi_0));
    double Md = (35.0 / 24.0) * pow(n, 3) * Math.sin(3 * (phi - phi_0)) * Math.cos(3 * (phi + phi_0));
    double M = b * F0 * (Ma - Mb + Mc - Md);

    double I = M + northings_origin;
    double II = (v / 2.0) * sin_phi * cos_phi;
    double III = (v / 24.0) * sin_phi * pow(cos_phi, 3) * (5 - pow(tan_phi, 2) + 9 * eta_2);
    double IIIA = (v / 720.0) * sin_phi * pow(cos_phi, 5) * (61 - 58 * pow(tan_phi, 2) + pow(tan_phi, 4));
    double IV = v * cos_phi;
    double V = (v / 6.0) * pow(cos_phi, 3) * (v / rho - pow(tan_phi, 2));
    double VI =
        (v / 120.0) * pow(cos_phi, 5) * (5 - 18 * pow(tan_phi, 2) + pow(tan_phi, 4) + 14 * eta_2 - 58 * pow(tan_phi, 2)
            * eta_2);

    double delta_lambda = lambda - lambda_0;

    double northings = I + II * pow(delta_lambda, 2) + III * pow(delta_lambda, 4) + IIIA * pow(delta_lambda, 6);
    double eastings = eastings_origin + IV * delta_lambda + V * pow(delta_lambda, 3) + VI * pow(delta_lambda, 5);

    northings = round(northings, 3);
    eastings = round(eastings, 3);

    return new CartesianCoordinate(eastings, northings, 0);
  }

  protected static PolarCoordinate osGridRefToLatLong(CartesianCoordinate input) {
    double eastings = input.x;
    double northings = input.y;

    // elipsoid parameters
    double a = AIRY1830_ELLIPSOID.a;
    double b = AIRY1830_ELLIPSOID.b;

    double F0 = NAT_GRID_SCALE_FACTOR;

    // origin
    double phi_0 = NAT_GRID_ORIGIN_POLAR.phi;
    double lambda_0 = NAT_GRID_ORIGIN_POLAR.lambda;
    double northings_origin = NAT_GRID_ORIGIN_CARTESIAN.x;
    double eastings_origin = NAT_GRID_ORIGIN_CARTESIAN.y;

    // e2: eccintricity squared
    double e2 = 1 - pow(b, 2) / pow(a, 2);
    double n = (a - b) / (a + b);

    double phi = phi_0;
    double M = 0;
    do {
      phi = (northings - northings_origin - M) / (a * F0) + phi;

      double Ma = (1 + n + (5.0 / 4.0) * pow(n, 2) + (5.0 / 4.0) * pow(n, 3)) * (phi - phi_0);
      double Mb = (3 * n + 3 * n * n + (21.0 / 8.0) * pow(n, 3)) * Math.sin(phi - phi_0) * Math.cos(phi + phi_0);
      double Mc =
          ((15.0 / 8.0) * pow(n, 2) + (15.0 / 8.0) * pow(n, 3)) * Math.sin(2 * (phi - phi_0)) * Math.cos(2 * (phi + phi_0));
      double Md = (35.0 / 24.0) * pow(n, 3) * Math.sin(3 * (phi - phi_0)) * Math.cos(3 * (phi + phi_0));
      M = b * F0 * (Ma - Mb + Mc - Md);              // meridional arc
    } while (northings - northings_origin - M >= 0.00001);  // ie until < 0.01mm

    double sin_phi = Math.sin(phi);
    // nu: transverse radius of curvature
    double nu = a * F0 / Math.sqrt(1 - e2 * pow(sin_phi, 2));
    // rho = meridional radius of curvature
    double rho = a * F0 * (1 - e2) / Math.pow(1 - e2 * pow(sin_phi, 2), 1.5);
    double eta_2 = nu / rho - 1;

    double tan_phi = Math.tan(phi);
    double sec_phi = 1 / Math.cos(phi);
    double VII = tan_phi / (2 * rho * nu);
    double VIII = tan_phi / (24 * rho * pow(nu, 3)) * (5 + 3 * pow(tan_phi, 2) + eta_2 - 9 * pow(tan_phi, 2) * eta_2);
    double IX = tan_phi / (720 * rho * pow(nu, 5)) * (61 + 90 * pow(tan_phi, 2) + 45 * pow(tan_phi, 4));
    double X = sec_phi / nu;
    double XI = sec_phi / (6 * pow(nu, 3)) * (nu / rho + 2 * pow(tan_phi, 2));
    double XII = sec_phi / (120 * pow(nu, 5)) * (5 + 28 * pow(tan_phi, 2) + 24 * pow(tan_phi, 4));
    double XIIA =
        sec_phi / (5040 * pow(nu, 7)) * (61 + 662 * pow(tan_phi, 2) + 1320 * pow(tan_phi, 4) + 720 * pow(tan_phi, 6));

    double dE = (eastings - eastings_origin);
    phi = phi - VII * pow(dE, 2) + VIII * pow(dE, 4) - IX * pow(dE, 6);
    double lambda = lambda_0 + X * dE - XI * pow(dE, 3) + XII * pow(dE, 5) - XIIA * pow(dE, 7);

    return new PolarCoordinate(OSGB36_DATUM, toDegrees(phi), toDegrees(lambda), 0);
  }

  @Data
  @AllArgsConstructor
  public static class CartesianCoordinate {
    double x, y, z;
  }

  @Data
  @AllArgsConstructor
  public static class PolarCoordinate {
    DatumParameters datum;
    double phi, lambda, h;
  }

  @Data
  @AllArgsConstructor
  public static class EllipsoidParameters {
    double a, b, f;
  }

  @Data
  @ToString(exclude = "inverse")
  private static class DatumParameters {
    EllipsoidParameters ellipsoid;
    double tx, ty, tz, scale, rx, ry, rz;
    DatumParameters inverse;

    private DatumParameters(EllipsoidParameters ellipsoid,
        double tx, double ty, double tz, double scale, double rx, double ry, double rz, DatumParameters inverse) {
      this.ellipsoid = ellipsoid;
      this.tx = tx;
      this.ty = ty;
      this.tz = tz;
      this.scale = scale;
      this.rx = rx;
      this.ry = ry;
      this.rz = rz;
      this.inverse = inverse;
    }

    public DatumParameters(EllipsoidParameters ellipsoid,
        double tx, double ty, double tz, double scale, double rx, double ry, double rz) {
      this(ellipsoid, tx, ty, tz, scale, rx, ry, rz, null);
      this.inverse = new DatumParameters(ellipsoid, -tx, -ty, -tz, -scale, -rx, -ry, -rz, this);
    }

  }

}
