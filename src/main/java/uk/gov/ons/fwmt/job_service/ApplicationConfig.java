/*
 * Copyright.. etc
 */

package uk.gov.ons.fwmt.job_service;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * Main entry point into the Legacy Gateway
 *
 * @author Thomas Poot
 * @author James Berry
 * @author Jacob Harrison
 */

@Slf4j
@SpringBootApplication
@EntityScan("uk.gov.ons.fwmt.job_service.entity")
@EnableJpaRepositories("uk.gov.ons.fwmt.job_service.repo")
@EnableScheduling
public class ApplicationConfig {
  /**
   * @param args
   */
  public static void main(String[] args) {
    SpringApplication.run(ApplicationConfig.class, args);
    log.debug("Started UI Application");
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

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.build();
  }

  @Bean
  public BasicAuthorizationInterceptor basicInterceptor(){
    return new BasicAuthorizationInterceptor("user", "password");
  }

}
