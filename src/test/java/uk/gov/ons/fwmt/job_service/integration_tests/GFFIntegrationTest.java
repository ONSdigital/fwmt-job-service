package uk.gov.ons.fwmt.job_service.integration_tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static org.junit.Assert.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"spring.profiles.active=integration"})
@RunWith(SpringRunner.class)
public class GFFIntegrationTest {
  @Autowired
  @Qualifier("processExecutor")
  TaskExecutor executor;

  @Test
  public void gffIntegrationTest() {
    RestTemplate restTemplate = new RestTemplate();

    LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
    map.add("file", new ClassPathResource("sampledata/integration_tests/sample_GFF_2018-06-06T06-06-06Z.csv"));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    byte[] encodedBytes = Base64.getEncoder().encode("user:password".getBytes());
    headers.add("Authorization", "Basic " + new String(encodedBytes));

    HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
    restTemplate.postForEntity("http://localhost:9091/jobs/samples", requestEntity, Void.class);

    long startTime = System.currentTimeMillis();
    long timeout = 10000;
    while (((ThreadPoolTaskExecutor) executor).getActiveCount() > 0 ||
        (System.currentTimeMillis() - startTime < timeout)) {
      // wait
    }

    if (((ThreadPoolTaskExecutor) executor).getActiveCount() > 0) {
      fail("The application failed to process the request within " + Long.toString(timeout) + " milliseconds");
    }

  }
}
