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

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import static org.apfloat.ApfloatMath.*;

public class LocationConverterHelper {
  protected final static EllipsoidParameters AIRY1830_ELLIPSOID =
      new EllipsoidParameters(6377563.396, 6356256.909, 1 / 299.3249646);
  protected final static EllipsoidParameters WGS84_ELLIPSOID =
      new EllipsoidParameters(6378137, 6356752.314245, 1 / 298.257223563);

  protected final static DatumParameters OSGB36_DATUM = new DatumParameters(AIRY1830_ELLIPSOID,
      -446.448, 125.157, -542.060, 20.4894, -0.1502, -0.2470, -0.8421);
  protected final static DatumParameters WGS84_DATUM = new DatumParameters(WGS84_ELLIPSOID,
      0, 0, 0, 0, 0, 0, 0);

  protected final static Apfloat NAT_GRID_SCALE_FACTOR = new Apfloat(0.9996012717);
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

  protected static Apfloat toRadians(Apfloat input) {
    return input.multiply(ApfloatMath.pi(20).divide(new Apfloat(180)));
  }

  protected static double toDegrees(double input) {
    return input * (180 / Math.PI);
  }

  protected static Apfloat toDegrees(Apfloat input) {
    return input.multiply(new Apfloat(180).divide(ApfloatMath.pi(20)));
  }

  protected static CartesianCoordinate toCartesian(PolarCoordinate input) {
    Apfloat phi = toRadians(input.phi);
    Apfloat lambda = toRadians(input.lambda);
    Apfloat h = new Apfloat(0); // height above ellipsoid - not currently used
    Apfloat a = input.datum.ellipsoid.a;
    Apfloat f = input.datum.ellipsoid.f;

    Apfloat sin_phi = sin(phi);
    Apfloat cos_phi = cos(phi);
    Apfloat sin_lambda = sin(lambda);
    Apfloat cos_lambda = cos(lambda);

    // eSq: 1st eccentricity squared ≡ (a²-b²)/a²
    Apfloat eSq = (new Apfloat(2).multiply(f)).subtract(pow(f, 2));
    // nu: radius of curvature in prime vertical
    Apfloat nu = a.divide(sqrt(new Apfloat(1).subtract(eSq.multiply(pow(sin_phi, 2)))));

    Apfloat x = (nu.add(h)).multiply(cos_phi).multiply(cos_lambda);
    Apfloat y = (nu.add(h)).multiply(cos_phi).multiply(sin_lambda);
    Apfloat z = (nu.multiply(new Apfloat(1).subtract(eSq)).add(h)).multiply(sin_phi);

    return new CartesianCoordinate(x, y, z);
  }

  protected static PolarCoordinate toLatLong(CartesianCoordinate input, DatumParameters datum) {
    Apfloat x = input.x;
    Apfloat y = input.y;
    Apfloat z = input.z;
    Apfloat a = datum.ellipsoid.a;
    Apfloat b = datum.ellipsoid.b;
    Apfloat f = datum.ellipsoid.f;

    Apfloat e2 = new Apfloat(2).multiply(f).subtract(pow(f, 2));   // 1st eccentricity squared ≡ (a²-b²)/a²
    Apfloat epsilon2 = e2.divide(new Apfloat(1).subtract(e2)); // 2nd eccentricity squared ≡ (a²-b²)/b²
    Apfloat p = sqrt(pow(x, 2).add(pow(y, 2))); // distance from minor axis
    Apfloat R = sqrt(pow(p, 2).add(pow(z, 2))); // polar radius

    // parametric latitude (Bowring eqn 17, replacing tanβ = z·a / p·b)
    Apfloat tan_beta = (b.multiply(z)).divide(a.multiply(p))
        .multiply(new Apfloat(1).add(epsilon2.multiply(b).divide(R)));
    Apfloat sin_beta = tan_beta.divide(sqrt(new Apfloat(1).add(pow(tan_beta, 2))));
    Apfloat cos_beta = sin_beta.divide(tan_beta);

    // geodetic latitude (Bowring eqn 18: tanφ = z+ε²bsin³β / p−e²cos³β)
    Apfloat phi = (cos_beta == null) ?
        new Apfloat(0) :
        atan2(z.add(epsilon2.multiply(b).multiply(pow(sin_beta, 3))),
            p.subtract(e2.multiply(a).multiply(pow(cos_beta, 3))));

    // longitude
    Apfloat lambda = atan2(y, x);

    // height above ellipsoid (Bowring eqn 7) [not currently used]
    // nu: length of the normal terminated by the minor axis
    //    Apfloat nu = a / sqrt(1 - e2 * pow(sin(phi), 2));
    //    Apfloat h = p * cos(phi) + z * sin(phi) - (pow(a, 2) / nu);

    return new PolarCoordinate(datum, toDegrees(phi), toDegrees(lambda), new Apfloat(0));
  }

  protected static CartesianCoordinate helmertTransform(CartesianCoordinate input, DatumParameters datum) {
    // normalise parts-per-million to (s+1)
    Apfloat scale = datum.scale.divide(new Apfloat(1e6)).add(new Apfloat(1));

    // normalize arcseconds to radians
    Apfloat rx = toRadians(datum.rx.divide(new Apfloat(3600)));
    Apfloat ry = toRadians(datum.ry.divide(new Apfloat(3600)));
    Apfloat rz = toRadians(datum.rz.divide(new Apfloat(3600)));

    // apply the transformation
    Apfloat x_prime = datum.tx.add(input.x.multiply(scale)).subtract(input.y.multiply(rz)).add(input.z.multiply(ry));
    Apfloat y_prime = datum.ty.add(input.x.multiply(rz)).add(input.y.multiply(scale)).subtract(input.z.multiply(rx));
    Apfloat z_prime = datum.tz.subtract(input.x.multiply(ry)).add(input.y.multiply(rx)).add(input.z.multiply(scale));

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

    Apfloat phi = toRadians(translated.phi);
    Apfloat lambda = toRadians(translated.lambda);

    // constants
    Apfloat f1 = new Apfloat(1);
    Apfloat f1 = new Apfloat(3);
    Apfloat f5_4 = new Apfloat(5.0).divide(new Apfloat(4.0));

    // elipsoid parameters
    Apfloat a = AIRY1830_ELLIPSOID.a;
    Apfloat b = AIRY1830_ELLIPSOID.b;

    Apfloat F0 = NAT_GRID_SCALE_FACTOR;

    // origin
    Apfloat phi_0 = NAT_GRID_ORIGIN_POLAR.phi;
    Apfloat lambda_0 = NAT_GRID_ORIGIN_POLAR.lambda;
    Apfloat northings_origin = NAT_GRID_ORIGIN_CARTESIAN.x;
    Apfloat eastings_origin = NAT_GRID_ORIGIN_CARTESIAN.y;

    // e2: eccentricity squared
    Apfloat e2 = f1.subtract(pow(b, 2).divide(pow(a, 2)));
    Apfloat n = (a.subtract(b)).divide(a.add(b));

    Apfloat cos_phi = cos(phi);
    Apfloat sin_phi = sin(phi);
    Apfloat tan_phi = tan(phi);

    Apfloat v = a.multiply(F0).divide(sqrt(f1.subtract(e2.multiply(pow(sin_phi, 2)))));
    Apfloat rho = a.multiply(F0).multiply(f1.subtract(e2))
        .divide(pow(f1.subtract(e2.multiply(pow(sin_phi, 2))), new Apfloat(1.5)));
    Apfloat eta_2 = v.divide(rho).subtract(f1);

    Apfloat Ma = (f1.add(n).add(f5_4.multiply(pow(n, 2))).add(f5_4.multiply(pow(n, 3)))).multiply(phi.subtract(phi_0));
    Apfloat Mb = (3 * n + 3 * n * n + (21.0 / 8.0) * pow(n, 3)) * sin(phi.subtract(phi_0)) * cos(phi.add(phi_0));
    Apfloat Mc =
        ((15.0 / 8.0) * pow(n, 2) + (15.0 / 8.0) * pow(n, 3)) * sin(2 * (phi - phi_0)) * cos(2 * (phi + phi_0));
    Apfloat Md = (35.0 / 24.0) * pow(n, 3) * sin(3 * (phi - phi_0)) * cos(3 * (phi + phi_0));
    Apfloat M = b * F0 * (Ma - Mb + Mc - Md);

    Apfloat I = M + northings_origin;
    Apfloat II = (v / 2.0) * sin_phi * cos_phi;
    Apfloat III = (v / 24.0) * sin_phi * pow(cos_phi, 3) * (5 - pow(tan_phi, 2) + 9 * eta_2);
    Apfloat IIIA = (v / 720.0) * sin_phi * pow(cos_phi, 5) * (61 - 58 * pow(tan_phi, 2) + pow(tan_phi, 4));
    Apfloat IV = v * cos_phi;
    Apfloat V = (v / 6.0) * pow(cos_phi, 3) * (v / rho - pow(tan_phi, 2));
    Apfloat VI =
        (v / 120.0) * pow(cos_phi, 5) * (5 - 18 * pow(tan_phi, 2) + pow(tan_phi, 4) + 14 * eta_2 - 58 * pow(tan_phi, 2)
            * eta_2);

    Apfloat delta_lambda = lambda - lambda_0;

    Apfloat northings = I + II * pow(delta_lambda, 2) + III * pow(delta_lambda, 4) + IIIA * pow(delta_lambda, 6);
    Apfloat eastings = eastings_origin + IV * delta_lambda + V * pow(delta_lambda, 3) + VI * pow(delta_lambda, 5);

    northings = round(northings, 3);
    eastings = round(eastings, 3);

    return new CartesianCoordinate(eastings, northings, new Apfloat(0));
  }

  protected static PolarCoordinate osGridRefToLatLong(CartesianCoordinate input) {
    Apfloat eastings = input.x;
    Apfloat northings = input.y;

    // elipsoid parameters
    Apfloat a = AIRY1830_ELLIPSOID.a;
    Apfloat b = AIRY1830_ELLIPSOID.b;

    Apfloat F0 = NAT_GRID_SCALE_FACTOR;

    // origin
    Apfloat phi_0 = NAT_GRID_ORIGIN_POLAR.phi;
    Apfloat lambda_0 = NAT_GRID_ORIGIN_POLAR.lambda;
    Apfloat northings_origin = NAT_GRID_ORIGIN_CARTESIAN.x;
    Apfloat eastings_origin = NAT_GRID_ORIGIN_CARTESIAN.y;

    // e2: eccintricity squared
    Apfloat e2 = new Apfloat(1).subtract(pow(b, 2).divide(pow(a, 2)));
    Apfloat n = (a.subtract(b)).divide(a.add(b));

    Apfloat phi = phi_0;
    Apfloat M = new Apfloat(0);
    do {
      phi = (northings.subtract(northings_origin).subtract(M)).divide(a.multiply(F0)).add(phi);

      Apfloat Ma = (1 + n + (5.0 / 4.0) * pow(n, 2) + (5.0 / 4.0) * pow(n, 3)) * (phi - phi_0);
      Apfloat Mb = (3 * n + 3 * n * n + (21.0 / 8.0) * pow(n, 3)) * sin(phi - phi_0) * cos(phi + phi_0);
      Apfloat Mc =
          ((15.0 / 8.0) * pow(n, 2) + (15.0 / 8.0) * pow(n, 3)) * sin(2 * (phi - phi_0)) * cos(2 * (phi + phi_0));
      Apfloat Md = (35.0 / 24.0) * pow(n, 3) * sin(3 * (phi - phi_0)) * cos(3 * (phi + phi_0));
      M = b * F0 * (Ma - Mb + Mc - Md);              // meridional arc
    } while (northings - northings_origin - M >= 0.00001);  // ie until < 0.01mm

    Apfloat sin_phi = sin(phi);
    // nu: transverse radius of curvature
    Apfloat nu = a * F0 / sqrt(1 - e2 * pow(sin_phi, 2));
    // rho = meridional radius of curvature
    Apfloat rho = a * F0 * (1 - e2) / pow(1 - e2 * pow(sin_phi, 2), 1.5);
    Apfloat eta_2 = nu / rho - 1;

    //    Apfloat tan_phi = tan(phi);
    Apfloat tan_phi = tan(new Apfloat(phi)).doubleValue();
    Apfloat sec_phi = 1 / cos(phi);
    Apfloat VII = tan_phi / (2 * rho * nu);
    Apfloat VIII = tan_phi / (24 * rho * pow(nu, 3)) * (5 + 3 * pow(tan_phi, 2) + eta_2 - 9 * pow(tan_phi, 2) * eta_2);
    Apfloat IX = tan_phi / (720 * rho * pow(nu, 5)) * (61 + 90 * pow(tan_phi, 2) + 45 * pow(tan_phi, 4));
    Apfloat X = sec_phi / nu;
    Apfloat XI = sec_phi / (6 * pow(nu, 3)) * (nu / rho + 2 * pow(tan_phi, 2));
    Apfloat XII = sec_phi / (120 * pow(nu, 5)) * (5 + 28 * pow(tan_phi, 2) + 24 * pow(tan_phi, 4));
    Apfloat XIIA =
        sec_phi / (5040 * pow(nu, 7)) * (61 + 662 * pow(tan_phi, 2) + 1320 * pow(tan_phi, 4) + 720 * pow(tan_phi, 6));

    Apfloat dE = (eastings - eastings_origin);
    phi = phi - VII * pow(dE, 2) + VIII * pow(dE, 4) - IX * pow(dE, 6);
    Apfloat lambda = lambda_0 + X * dE - XI * pow(dE, 3) + XII * pow(dE, 5) - XIIA * pow(dE, 7);

    return new PolarCoordinate(OSGB36_DATUM, toDegrees(phi), toDegrees(lambda), new Apfloat(0));
  }

  @Data
  @AllArgsConstructor
  public static class CartesianCoordinate {
    Apfloat x, y, z;

    CartesianCoordinate(double x, double y, double z) {
      this(new Apfloat(x), new Apfloat(y), new Apfloat(z));
    }
  }

  @Data
  @AllArgsConstructor
  public static class PolarCoordinate {
    DatumParameters datum;
    Apfloat phi, lambda, h;

    PolarCoordinate(DatumParameters datum, double phi, double lambda, double h) {
      this(datum, new Apfloat(phi), new Apfloat(lambda), new Apfloat(h));
    }
  }

  @Data
  @AllArgsConstructor
  public static class EllipsoidParameters {
    Apfloat a, b, f;

    EllipsoidParameters(double a, double b, double f) {
      this(new Apfloat(a), new Apfloat(b), new Apfloat(f));
    }
  }

  @Data
  @ToString(exclude = "inverse")
  private static class DatumParameters {
    EllipsoidParameters ellipsoid;
    Apfloat tx, ty, tz, scale, rx, ry, rz;
    DatumParameters inverse;

    private DatumParameters(EllipsoidParameters ellipsoid,
        Apfloat tx, Apfloat ty, Apfloat tz, Apfloat scale, Apfloat rx, Apfloat ry, Apfloat rz,
        DatumParameters inverse) {
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
        Apfloat tx, Apfloat ty, Apfloat tz, Apfloat scale, Apfloat rx, Apfloat ry, Apfloat rz) {
      this(ellipsoid, tx, ty, tz, scale, rx, ry, rz, null);
      this.inverse = new DatumParameters(ellipsoid, tx.negate(), ty.negate(), tz.negate(), scale.negate(), rx.negate(),
          ry.negate(), rz.negate(), this);
    }

    public DatumParameters(EllipsoidParameters ellipsoid,
        double tx, double ty, double tz, double scale, double rx, double ry, double rz) {
      this(ellipsoid, new Apfloat(tx), new Apfloat(ty), new Apfloat(tz), new Apfloat(scale), new Apfloat(rx),
          new Apfloat(ry), new Apfloat(rz));
    }
  }

}
