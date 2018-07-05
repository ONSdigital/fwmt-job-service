package uk.gov.ons.fwmt.job_service.service.impl.totalmobile;

import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.AdditionalPropertyCollectionType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.CreateJobRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.UpdateJobHeaderRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.JobType;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendMessageRequestInfo;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateJobHeaderRequestMessage;
import org.junit.Test;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleGFFDataIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleLFSDataIngest;
import uk.gov.ons.fwmt.job_service.utilities.TestIngestBuilder;

import javax.xml.datatype.DatatypeConfigurationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType.GFF;
import static uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType.LFS;
import static uk.gov.ons.fwmt.job_service.service.impl.totalmobile.TMJobConverterServiceImpl.JOB_QUEUE;
import static uk.gov.ons.fwmt.job_service.service.impl.totalmobile.TMJobConverterServiceImpl.JOB_WORK_TYPE;

public class TMJobConverterServiceImplTest {

  private TMJobConverterServiceImpl tmJobConverterService = new TMJobConverterServiceImpl();

  public TMJobConverterServiceImplTest() throws DatatypeConfigurationException {
  }

  @Test
  public void addAdditionalProperty() {
    //Given
    String expectedKey = "testKey";
    String expectedValue = "testValue";
    CreateJobRequest createJobRequest = new CreateJobRequest();
    createJobRequest.setJob(new JobType());
    createJobRequest.getJob().setAdditionalProperties(new AdditionalPropertyCollectionType());
    createJobRequest.getJob().getAdditionalProperties();

    //When
    TMJobConverterServiceImpl.addAdditionalProperty(createJobRequest, expectedKey, expectedValue);

    //Then
    assertEquals(expectedKey,
        createJobRequest.getJob().getAdditionalProperties().getAdditionalProperty().get(0).getName());
    assertEquals(expectedValue,
        createJobRequest.getJob().getAdditionalProperties().getAdditionalProperty().get(0).getValue());
  }

  @Test
  public void createGFFJobRequestFromIngest() {
    //Given
    String username = "testUser";
    LegacySampleIngest testIngestData = new TestIngestBuilder().ingestBuild();
    testIngestData.setGffData(new LegacySampleGFFDataIngest());
    testIngestData.setLegacySampleSurveyType(GFF);

    //When
    CreateJobRequest result = tmJobConverterService.createJobRequestFromIngest(testIngestData, username);

    //Then
    assertNotNull(result.getJob());
    assertNotNull(result.getJob().getLocation());
    assertNotNull(result.getJob().getIdentity());
    assertNotNull(result.getJob().getSkills());
    assertNotNull(result.getJob().getWorld());
    assertEquals(JOB_WORK_TYPE, result.getJob().getWorkType());
    assertEquals(JOB_WORK_TYPE, result.getJob().getWorkType());
    assertEquals(testIngestData.getAddressLine1(),
        result.getJob().getLocation().getAddressDetail().getLines().getAddressLine().get(0));
    assertEquals(testIngestData.getAddressLine2(),
        result.getJob().getLocation().getAddressDetail().getLines().getAddressLine().get(1));
    assertEquals(testIngestData.getAddressLine3(),
        result.getJob().getLocation().getAddressDetail().getLines().getAddressLine().get(2));
    assertEquals(testIngestData.getAddressLine4(),
        result.getJob().getLocation().getAddressDetail().getLines().getAddressLine().get(3));
    assertEquals(testIngestData.getDistrict(),
        result.getJob().getLocation().getAddressDetail().getLines().getAddressLine().get(4));
    assertEquals(testIngestData.getPostTown(),
        result.getJob().getLocation().getAddressDetail().getLines().getAddressLine().get(5));
    assertEquals(testIngestData.getPostcode(), result.getJob().getLocation().getAddressDetail().getPostCode());
    assertEquals(testIngestData.getSerNo(), result.getJob().getLocation().getReference());
  }

  @Test
  public void createLFSJobRequestFromIngest() {
    //Given
    String username = "testUser";

    LegacySampleIngest testIngestData = new TestIngestBuilder().ingestBuild();
    testIngestData.setLfsData(new LegacySampleLFSDataIngest());
    testIngestData.setLegacySampleSurveyType(LFS);

    //When
    CreateJobRequest result = tmJobConverterService.createJobRequestFromIngest(testIngestData, username);

    //Then
    assertNotNull(result.getJob());
    assertNotNull(result.getJob().getLocation());
    assertNotNull(result.getJob().getIdentity());
    assertNotNull(result.getJob().getSkills());
    assertNotNull(result.getJob().getWorld());
    assertEquals(JOB_WORK_TYPE, result.getJob().getWorkType());
    assertEquals(JOB_WORK_TYPE, result.getJob().getWorkType());
    assertEquals(testIngestData.getAddressLine1(),
        result.getJob().getLocation().getAddressDetail().getLines().getAddressLine().get(0));
    assertEquals(testIngestData.getAddressLine2(),
        result.getJob().getLocation().getAddressDetail().getLines().getAddressLine().get(1));
    assertEquals(testIngestData.getAddressLine3(),
        result.getJob().getLocation().getAddressDetail().getLines().getAddressLine().get(2));
    assertEquals(testIngestData.getAddressLine4(),
        result.getJob().getLocation().getAddressDetail().getLines().getAddressLine().get(3));
    assertEquals(testIngestData.getDistrict(),
        result.getJob().getLocation().getAddressDetail().getLines().getAddressLine().get(4));
    assertEquals(testIngestData.getPostTown(),
        result.getJob().getLocation().getAddressDetail().getLines().getAddressLine().get(5));
    assertEquals(testIngestData.getPostcode(), result.getJob().getLocation().getAddressDetail().getPostCode());
    assertEquals(testIngestData.getSerNo(), result.getJob().getLocation().getReference());
  }

  @Test
  public void updateJobHeaderRequestFromIngest() {
    //Given
    String tmJobId = "TestTmJobId";
    String username = "testUsername";

    //When
    UpdateJobHeaderRequest result = tmJobConverterService.makeUpdateJobHeaderRequest(tmJobId, username);

    //Then
    assertNotNull(result.getJobHeader());
    assertEquals(username, result.getJobHeader().getAllocatedTo().getUsername());
    assertEquals(tmJobId, result.getJobHeader().getJobIdentity().getReference());
  }

  @Test
  public void makeSendMessageRequestInfo() {
    //Given
    String expectedKey = "testKey";

    //When
    SendMessageRequestInfo result = tmJobConverterService.makeSendMessageRequestInfo(expectedKey);

    //Then
    assertEquals(expectedKey, result.getKey());
    assertEquals(JOB_QUEUE, result.getQueueName());
  }

  @Test
  public void createJob() {
    //Given
    String username = "testUsername";
    LegacySampleIngest testIngestData = new TestIngestBuilder().ingestBuild();
    testIngestData.setGffData(new LegacySampleGFFDataIngest());
    testIngestData.setLegacySampleSurveyType(GFF);

    //When
    SendCreateJobRequestMessage result = tmJobConverterService.createJob(testIngestData, username);

    //Then
    assertNotNull(result);
  }

  @Test
  public void updateJobFromTMJobId() {
    //Given
    String tmJobId = "TestTmId";
    String username = "testUsername";

    //When
    SendUpdateJobHeaderRequestMessage result = tmJobConverterService.updateJob(tmJobId, username);

    //Then
    assertEquals(tmJobId, result.getSendMessageRequestInfo().getKey());
    assertEquals(JOB_QUEUE, result.getSendMessageRequestInfo().getQueueName());
    assertEquals(tmJobId, result.getUpdateJobHeaderRequest().getJobHeader().getJobIdentity().getReference());
  }

  @Test
  public void updateJobFromIngest() {
    //Given
    String username = "testUsername";
    LegacySampleIngest testIngestData = new TestIngestBuilder().ingestBuild();
    testIngestData.setGffData(new LegacySampleGFFDataIngest());
    testIngestData.setLegacySampleSurveyType(GFF);

    //When
    SendUpdateJobHeaderRequestMessage result = tmJobConverterService.updateJob(testIngestData, username);

    //Then
    assertNotNull(result);
  }

  @Test
  public void checkIfAddressLineIsNotBlank() {
    //Given
    List<String> testAddressLines = new ArrayList<>();
    String testLine = "testLine";

    List<String> expectedArray = new ArrayList<>();
    expectedArray.add("testLine");

    //When
    tmJobConverterService.addAddressLines(testAddressLines,testLine);

    //Then
    assertEquals(expectedArray,testAddressLines);
  }

  @Test
  public void checkIfAddressLineIsBlank() {
    //Given
    List<String> testAddressLines = new ArrayList<>();
    String testLine = "";

    List<String> expectedArray = new ArrayList<>();
    expectedArray.add("testLine");

    //When
    tmJobConverterService.addAddressLines(testAddressLines,testLine);

    //Then
    assertNotEquals(testAddressLines, expectedArray);
    int expectedArraySize = 0;
    assertEquals(expectedArraySize, testAddressLines.size());
  }

  @Test
  public void createReissue() {
    //Given
    String username = "testUsername";
    LegacySampleIngest testIngestData = new TestIngestBuilder().ingestBuild();
    testIngestData.setGffData(new LegacySampleGFFDataIngest());
    testIngestData.setLegacySampleSurveyType(GFF);

    //When
    SendCreateJobRequestMessage result = tmJobConverterService.createReissue(testIngestData, username);

    //Then
    assertNotNull(result);
  }
}