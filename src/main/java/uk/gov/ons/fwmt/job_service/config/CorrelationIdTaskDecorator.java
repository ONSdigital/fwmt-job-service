package uk.gov.ons.fwmt.job_service.config;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

public class CorrelationIdTaskDecorator implements TaskDecorator {
  @Override
  public Runnable decorate(Runnable runnable) {
    // Get the logging MDC context
    Map<String, String> contextMap = MDC.getCopyOfContextMap();

    return () -> {
      try {
        // Restore the MDC context
        if (contextMap != null) {
          MDC.setContextMap(contextMap);
        } else {
          MDC.clear();
        }

        // Run the new thread
        runnable.run();
      } finally {
        MDC.clear();
      }
    };
  }
}