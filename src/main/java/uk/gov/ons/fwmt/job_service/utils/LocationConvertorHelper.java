package uk.gov.ons.fwmt.job_service.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.Math.pow;

public class LocationConvertorHelper {
  @Data
  @AllArgsConstructor
  private static class CartesianCoord {
    double x, y, z;
  }

  @Data
  @AllArgsConstructor
  private static class PolarCoord {
    double psi, lambda, h;
  }

  @Data
  @AllArgsConstructor
  private static class ElipsoidParameters {
    double a, b, f;
  }

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

  //  private CartesianCoord polarToCartesian(PolarCoord input) {
  //
  //  }

  //  private PolarCoord cartesianToPolar(CartesianCoord input) {
  //
  //  }

  protected static CartesianCoord helmertTransform(CartesianCoord input, CartesianCoord t, CartesianCoord r, double s) {
    double x_prime = t.x + ((1 + s) * input.x) - (r.z * input.y) + (r.y * input.z);
    double y_prime = t.y + (r.z * input.x) + ((1 + s) * input.y) - (r.x * input.z);
    double z_prime = t.z - (r.y * input.x) + (r.x * input.y) + ((1 + s) * input.z);
    return new CartesianCoord(x_prime, y_prime, z_prime);
  }

  protected static void transverseMercator() {

  }

  protected static PolarCoord latLongToOsGridRef(double lat, double lon) {
    double psi = toRadians(lat);
    double lambda = toRadians(lon);

    double a = 6377563.396;
    double b = 6356256.909;
    double F0 = 0.9996012717;
    double psi0 = toRadians(49);
    double lambda_0 = toRadians(-2);
    double N0 = -100000, E0 = 400000;
    double e2 = 1 - (b * b) / (a * a);
    double n = (a - b) / (a + b);

    double cos_psi = Math.cos(psi), sin_psi = Math.sin(psi);
    double v = a * F0 / Math.sqrt(1 - e2 * sin_psi * sin_psi);
    double rho = a * F0 * (1 - e2) / pow(1 - e2 * sin_psi * sin_psi, 1.5);
    double eta_2 = v / rho - 1;

    double Ma = (1 + n + (5 / 4) * pow(n, 2) + (5 / 4) * pow(n, 3)) * (psi - psi0);
    double Mb = (3 * n + 3 * n * n + (21 / 8) * pow(n, 3)) * Math.sin(psi - psi0) * Math.cos(psi + psi0);
    double Mc = ((15 / 8) * pow(n, 2) + (15 / 8) * pow(n, 3)) * Math.sin(2 * (psi - psi0)) * Math.cos(2 * (psi + psi0));
    double Md = (35 / 24) * pow(n, 3) * Math.sin(3 * (psi - psi0)) * Math.cos(3 * (psi + psi0));
    double M = b * F0 * (Ma - Mb + Mc - Md);

    double tan_psi = Math.tan(psi);

    double I = M + N0;
    double II = (v / 2) * sin_psi * cos_psi;
    double III = (v / 24) * sin_psi * pow(cos_psi, 3) * (5 - pow(tan_psi, 2) + 9 * eta_2);
    double IIIA = (v / 720) * sin_psi * pow(cos_psi, 5) * (61 - 58 * pow(tan_psi, 2) + pow(tan_psi, 4));
    double IV = v * cos_psi;
    double V = (v / 6) * pow(cos_psi, 3) * (v / rho - pow(tan_psi, 2));
    double VI =
        (v / 120) * pow(cos_psi, 5) * (5 - 18 * pow(tan_psi, 2) + pow(tan_psi, 4) + 14 * eta_2 - 58 * pow(tan_psi, 2)
            * eta_2);

    double delta_lambda = lambda - lambda_0;

    double N = I + II * pow(delta_lambda, 2) + III * pow(delta_lambda, 4) + IIIA * pow(delta_lambda, 6);
    double E = E0 + IV * delta_lambda + V * pow(delta_lambda, 3) + VI * pow(delta_lambda, 5);

    N = round(N, 3);
    E = round(E, 3);

    return new PolarCoord(E, N, 0);
  }

}
