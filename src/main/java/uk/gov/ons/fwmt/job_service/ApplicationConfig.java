/*
 * Copyright.. etc
 */

package uk.gov.ons.fwmt.job_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.job_service.config.CorrelationIdInterceptor;
import uk.gov.ons.fwmt.job_service.config.MDCTaskDecorator;

import java.util.Collections;

/**
 * Main entry point into the Legacy Gateway
 *
 * @author Thomas Poot
 * @author James Berry
 * @author Jacob Harrison
 */

@Slf4j
@SpringBootApplication
@EnableAsync
public class ApplicationConfig {

  @Value("${service.resource.username}")
  private String userName;
  @Value("${service.resource.password}")
  private String password;

  /**
   * @param args
   */
  public static void main(String[] args) {
    SpringApplication.run(ApplicationConfig.class, args);
    log.debug("Started application");
  }

  /**
   * @param
   * @return
   */
  @Bean
  CommandLineRunner init() {
    return (args) -> {
      //storageService.deleteAll();
      //storageService.init();
    };
  }

  @Bean(name="processExecutor")
  public TaskExecutor workExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setThreadNamePrefix("Async-");
    threadPoolTaskExecutor.setCorePoolSize(3);
    threadPoolTaskExecutor.setMaxPoolSize(3);
    threadPoolTaskExecutor.setQueueCapacity(600);
    threadPoolTaskExecutor.setTaskDecorator(new MDCTaskDecorator());
    threadPoolTaskExecutor.afterPropertiesSet();
    return threadPoolTaskExecutor;
  }

  @Bean
  public RestTemplate resourcesRestTemplate(RestTemplateBuilder builder) {
    return builder
        .basicAuthorization(userName, password)
        .interceptors(Collections.singletonList(new CorrelationIdInterceptor()))
        .build();
  }

}
