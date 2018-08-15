package uk.gov.ons.fwmt.job_service.integration_tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.job_service.mock_logging.MockMessage;

import javax.annotation.PostConstruct;
import java.util.Base64;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("integration")
public class GFFIntegrationTest {
  @Value("${server.port}") int port;
  @Value("${mock.port}") int mockPort;
  @Autowired TaskExecutor taskExecutor;

  private String url;
  private String mockUrl;

  @PostConstruct
  public void postConstruct() {
    url = "http://localhost:" + Integer.toString(port);
    mockUrl = "http://localhost:" + Integer.toString(mockPort);
  }

  @Test
  public void gffIntegrationTest() {
    // // // Create request
    RestTemplate restTemplate = new RestTemplate();

    LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
    map.add("file", new ClassPathResource("sampledata/integration_tests/sample_GFF_2018-06-06T06-06-06Z.csv"));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    byte[] encodedBytes = Base64.getEncoder().encode("user:password".getBytes());
    headers.add("Authorization", "Basic " + new String(encodedBytes));

    HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

    // // // Reset mock

    restTemplate.getForObject("http://localhost:9099/logger/reset", Void.class);

    // // // Send request

    // // // Send request to the DEFINED_PORT
    restTemplate.postForEntity(url + "/jobs/samples", requestEntity, Void.class);

    // // // Wait for results
    long timeout = 10000;

    long startTime = System.currentTimeMillis();
    while (((ThreadPoolTaskExecutor) taskExecutor).getActiveCount() > 0) {
      if (startTime > System.currentTimeMillis() + timeout) {
        fail("Timed out waiting for all tasks to finish");
      }
      // wait
    }

    // // // Verify results
    MockMessage[] messages = restTemplate.getForObject(mockUrl + "/logger/allMessages", MockMessage[].class);

    assertEquals(1, messages.length);
    assertFalse(messages[0].isFault);
    assertEquals(messages[0].endpoint, "MessageQueueWs");
    assertEquals(messages[0].method, "sendUpdateJobHeaderRequestMessage");
    assertTrue(messages[0].requestRawHtml.contains("tla_1-REISS1-001-826"));
  }
}
