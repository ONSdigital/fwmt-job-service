package uk.gov.ons.fwmt.job_service;

import org.junit.Assert;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("integration")
public class LFSIntegrationTest {
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
  public void lfsIntegrationTest() {
    // // // Create request
    RestTemplate restTemplate = new RestTemplate();

    LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
    map.add("file", new ClassPathResource("sampledata/integration_tests/sample_LFS_2018-06-06T06-06-06Z.csv"));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    byte[] encodedBytes = Base64.getEncoder().encode("user:password".getBytes());
    headers.add("Authorization", "Basic " + new String(encodedBytes));

    HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

    // // // Clear the mock
    restTemplate.getForObject(mockUrl + "/logger/reset", Void.class);

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

    assertEquals(3, messages.length);

    // first line, auth="1234", quota="A", id="quota_1 1 1 1 1 1 1 1 - A [Rissue_no_1]"
    // allocation
    assertFalse(messages[0].isFault);
    Assert.assertEquals("MessageQueueWs", messages[0].endpoint);
    Assert.assertEquals("sendCreateJobRequestMessage", messages[0].method);
    assertTrue(messages[0].requestRawHtml.contains("quota_1 1 1 1 1 1 1 1 - A [Rissue_no_1]"));

    // second line, auth="1234", quota="B", id="quota_1 1 1 1 1 1 1 1 - B [Rissue_no_1]"
    // reallocation
    assertFalse(messages[1].isFault);
    Assert.assertEquals("MessageQueueWs", messages[1].endpoint);
    Assert.assertEquals("sendUpdateJobHeaderRequestMessage", messages[1].method);
    assertTrue(messages[1].requestRawHtml.contains("quota_1 1 1 1 1 1 1 1 - B [Rissue_no_1]"));

    // third line, auth="1234", quota="C", id="quota_1 1 1 1 1 1 1 1 - C [Rissue_no_1]"
    // allocation
    assertFalse(messages[2].isFault);
    Assert.assertEquals("MessageQueueWs", messages[2].endpoint);
    Assert.assertEquals("sendCreateJobRequestMessage", messages[2].method);
    assertTrue(messages[2].requestRawHtml.contains("quota_1 1 1 1 1 1 1 1 - C [Rissue_no_1]"));

    // fourth line, auth="5555", quota="C", id=""
    // invalid auth

    // fifth line, auth="1111", quota="D", id=""
    // invalid quota
  }
}
