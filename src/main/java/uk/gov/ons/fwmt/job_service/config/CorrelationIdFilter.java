package uk.gov.ons.fwmt.job_service.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

import static uk.gov.ons.fwmt.job_service.config.MDCHelper.CID_HEADER;
import static uk.gov.ons.fwmt.job_service.config.MDCHelper.CID_KEY;
import static uk.gov.ons.fwmt.job_service.config.MDCHelper.CID_PREFIX;

@Slf4j
@Component
public class CorrelationIdFilter implements Filter {
  /**
   * The paths listed below are ignored for the purposes of creating correlation IDs
   */
  private static final String[] IGNORED_PATHS = {
      "/jobs/swagger-resources/**",
      "/jobs/swagger-ui.html",
      "/jobs/v2/api-docs",
      "/jobs/webjars/**",
      "/jobs/info"
  };

  private AntPathMatcher matcher = new AntPathMatcher();

  @Override
  public void destroy() {
    // nothing
  }

  @Override
  public void init(FilterConfig fc) {
    // nothing
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

    final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    String currentCid = httpServletRequest.getHeader(CID_HEADER);

    if (!isIgnoredPath(httpServletRequest.getRequestURI()) && !isRequestAnAsyncDispatcher(httpServletRequest)) {
      if (currentCid == null) {
        currentCid = makeNewCid();
        log.info("No correlationId found in Header. Generated: " + currentCid);
      } else {
        log.info("Found correlationId in Header: " + currentCid);
      }

      MDC.put(CID_KEY, currentCid);
    }

    filterChain.doFilter(httpServletRequest, servletResponse);
  }

  protected static String makeNewCid() {
    return CID_PREFIX + UUID.randomUUID().toString();
  }

  protected boolean isIgnoredPath(String path) {
    for (String pattern : IGNORED_PATHS) {
      if (matcher.match(pattern, path)) {
        return true;
      }
    }
    return false;
  }

  private static boolean isRequestAnAsyncDispatcher(HttpServletRequest httpServletRequest) {
    return httpServletRequest.getDispatcherType().equals(DispatcherType.ASYNC);
  }
}
