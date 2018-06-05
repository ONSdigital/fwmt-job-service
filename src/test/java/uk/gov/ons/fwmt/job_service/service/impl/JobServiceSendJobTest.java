package uk.gov.ons.fwmt.job_service.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.ons.fwmt.job_service.data.csv_parser.UnprocessedCSVRow;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleGFFDataIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType;
import uk.gov.ons.fwmt.job_service.rest.JobResourceService;
import uk.gov.ons.fwmt.job_service.rest.dto.JobDto;
import uk.gov.ons.fwmt.job_service.rest.dto.UserDto;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMJobConverterService;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMService;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobServiceSendJobTest {
  @Mock JobResourceService mockJobResourceService;
  @Mock TMJobConverterService mockTmJobConverterService;
  @Mock TMService mockTmService;
  @InjectMocks private JobServiceImpl jobService;

  @Before
  public void setup() {
    when(mockTmService.send(any())).thenReturn(null);
  }

  @Test
  public void sendJob_alreadySent() {
    // setup
    String testJobId = "test_job_already_sent";
    String testUserID = "1111";

    LegacySampleIngest ingest = new LegacySampleIngest();
    ingest.setTmJobId(testJobId);

    UserDto userDto = new UserDto();
    userDto.setAuthNo(testUserID);

    // a single job exists, with testJobId and testUserId
    JobDto jobDto = new JobDto(testJobId, testUserID);
    when(mockJobResourceService.existsByTmJobId(testJobId)).thenReturn(true);
    when(mockJobResourceService.existsByTmJobIdAndLastAuthNo(testJobId, testUserID)).thenReturn(true);
    when(mockJobResourceService.findByTmJobId(testJobId)).thenReturn(Optional.of(jobDto));

    // run
    Optional<UnprocessedCSVRow> optional = jobService.sendJobToUser(1, ingest, userDto);

    // verify
    assertTrue(optional.isPresent());
    assertEquals("Job has been sent previously", optional.get().getMessage());
  }

  @Test
  public void sendJob_reallocation() {
    // setup
    String testJobId = "test_reallocation";
    String originalTestUserID = "1111";
    String newTestUserID = "2222";

    LegacySampleIngest ingest = new LegacySampleIngest();
    ingest.setTmJobId(testJobId);

    UserDto userDto = new UserDto();
    userDto.setAuthNo(newTestUserID);

    // a single job exists, with testJobId and originalTestUserId
    JobDto jobDto = new JobDto(testJobId, originalTestUserID);
    when(mockJobResourceService.existsByTmJobId(testJobId)).thenReturn(true);
    when(mockJobResourceService.existsByTmJobIdAndLastAuthNo(testJobId, originalTestUserID)).thenReturn(true);
    when(mockJobResourceService.findByTmJobId(testJobId)).thenReturn(Optional.of(jobDto));

    // run
    Optional<UnprocessedCSVRow> optional = jobService.sendJobToUser(1, ingest, userDto);

    // verify
    assertFalse(optional.isPresent());
    verify(mockTmJobConverterService).updateJob((LegacySampleIngest) any(), any());
    verify(mockJobResourceService).updateJob(any());
  }

  @Test
  public void sendJob_GFFReissue() {
    // setup
    String testJobId = "test_gff_reissue";
    String testUserID = "1111";

    LegacySampleIngest ingest = new LegacySampleIngest();
    ingest.setTmJobId(testJobId);
    ingest.setLegacySampleSurveyType(LegacySampleSurveyType.GFF);
    ingest.setStage("X13");

    UserDto userDto = new UserDto();
    userDto.setAuthNo(testUserID);

    // run
    Optional<UnprocessedCSVRow> optional = jobService.sendJobToUser(1, ingest, userDto);

    // verify
    assertFalse(optional.isPresent());
    verify(mockTmJobConverterService).createReissue(any(), any());
    verify(mockJobResourceService).createJob(any());
  }

  @Test
  public void sendJob_GFFNew() {
    // setup
    String testJobId = "test_gff_new";
    String testUserID = "1111";

    LegacySampleIngest ingest = new LegacySampleIngest();
    ingest.setTmJobId(testJobId);
    ingest.setLegacySampleSurveyType(LegacySampleSurveyType.GFF);
    ingest.setStage("X12");

    UserDto userDto = new UserDto();
    userDto.setAuthNo(testUserID);

    // run
    Optional<UnprocessedCSVRow> optional = jobService.sendJobToUser(1, ingest, userDto);

    // verify
    assertFalse(optional.isPresent());
    verify(mockTmJobConverterService).createJob(any(), any());
    verify(mockJobResourceService).createJob(any());
  }

  @Test
  public void sendJob_LFSNew() {
    // setup
    String testJobId = "test_lfs_new";
    String testUserID = "1111";

    LegacySampleIngest ingest = new LegacySampleIngest();
    ingest.setTmJobId(testJobId);
    ingest.setLegacySampleSurveyType(LegacySampleSurveyType.LFS);

    UserDto userDto = new UserDto();
    userDto.setAuthNo(testUserID);

    // run
    Optional<UnprocessedCSVRow> optional = jobService.sendJobToUser(1, ingest, userDto);

    // verify
    assertFalse(optional.isPresent());
    verify(mockTmJobConverterService).createJob(any(), any());
    verify(mockJobResourceService).createJob(any());
  }
}
