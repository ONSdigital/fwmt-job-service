package uk.gov.ons.fwmt.job_service.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateJobHeaderRequestMessage;

import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType;
import uk.gov.ons.fwmt.job_service.rest.client.FieldPeriodResourceServiceClient;
import uk.gov.ons.fwmt.job_service.rest.client.JobResourceServiceClient;
import uk.gov.ons.fwmt.job_service.rest.client.UserResourceServiceClient;
import uk.gov.ons.fwmt.job_service.rest.client.dto.JobDto;
import uk.gov.ons.fwmt.job_service.rest.client.dto.UserDto;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMJobConverterService;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class JobProcessorTests {
  @InjectMocks private JobProcessor jobProcessor;
  @Mock private UserResourceServiceClient userResourceServiceClient;
  @Mock private JobResourceServiceClient jobResourceServiceClient;
  @Mock private TMJobConverterService tmJobConverterService;
  @Mock private TMService tmService;
  @Mock private FieldPeriodResourceServiceClient fieldPeriodResourceServiceClient;

  //  @Mock private Logger log;
  
  @Test
  public void givenALegacySampleIngestWithAnExistingUser_findUserShouldReturnAUserBasedOnAuthNo() {
    UserDto authUserDto = UserDto.builder().authNo("auth").tmUsername("I is Auth").build();
    when(userResourceServiceClient.findByAuthNo("auth")).thenReturn(Optional.of(authUserDto));

    LegacySampleIngest lsi = LegacySampleIngest.builder().auth("auth").build();
    Optional<UserDto> user = jobProcessor.findUser(lsi);
    assertEquals(authUserDto, user.get());
  }  
  
  @Test
  public void givenALegacySampleIngestWithAnAlternateExistingUser_findUserShouldFindByAltAuth_AndReturnAUser() {
    UserDto altAuthUserDto = UserDto.builder().authNo("alt-auth").tmUsername("I is Alt Auth").build();
    when(userResourceServiceClient.findByAuthNo("alt-auth")).thenReturn(Optional.empty());
    when(userResourceServiceClient.findByAlternateAuthNo("alt-auth")).thenReturn(Optional.of(altAuthUserDto));

    LegacySampleIngest lsi = LegacySampleIngest.builder().auth("alt-auth").build();
    Optional<UserDto> user = jobProcessor.findUser(lsi);
    assertEquals(altAuthUserDto, user.get());
  }
  
  @Test
  public void givenALegacySampleIngestWithAnNonExistingUser_findUserShouldNotReturnAUser() {
    when(userResourceServiceClient.findByAuthNo("non-existing")).thenReturn(Optional.empty());
    when(userResourceServiceClient.findByAlternateAuthNo("non-existing")).thenReturn(Optional.empty());

    LegacySampleIngest lsi = LegacySampleIngest.builder().auth("non-existing").build();
    Optional<UserDto> user = jobProcessor.findUser(lsi);
    assertTrue(!user.isPresent());
  }

  @Test 
  public void givenJobHasAlreadyBeenSent_whenSendingJobToUser_confirmErrortIsLoggedAndTheJobIsNotProcessed(){
    LegacySampleIngest lsi = LegacySampleIngest.builder().build();
    UserDto userDto = UserDto.builder().build();
    when(jobResourceServiceClient.existsByTmJobIdAndLastAuthNo(lsi.getTmJobId(), userDto.getAuthNo())).thenReturn(true);

    jobProcessor.sendJobToUser(0, lsi, userDto, true);

    // How to verify logger is called? Static methods
    // verify(log).error(anyString(), an());

    verify(tmJobConverterService, never()).updateJob(any(LegacySampleIngest.class), anyString());
  }

  @Test
  public void givenJobHasntAlreadyBeenSentAndIsRelloactionIsTrue_whenSendingJobToUser_confirmProcessRellocationIsCalled(){
    LegacySampleIngest lsi = LegacySampleIngest.builder().build();
    UserDto userDto = UserDto.builder().tmUsername("bob").build();
    SendUpdateJobHeaderRequestMessage msg = new SendUpdateJobHeaderRequestMessage();

    when(jobResourceServiceClient.existsByTmJobIdAndLastAuthNo(lsi.getTmJobId(), userDto.getAuthNo())).thenReturn(false);
    when(jobResourceServiceClient.findByTmJobId(anyString())).thenReturn(Optional.empty());
    when(tmJobConverterService.updateJob(lsi, "bob")).thenReturn(msg);

    jobProcessor.sendJobToUser(0, lsi, userDto, true);

    verify(tmJobConverterService, times(1)).updateJob(eq(lsi), eq("bob"));
    verify(tmService, times(1)).send(eq(msg));
  }

  @Test
  public void givenJobHasntAlreadyBeenSentAndIsRelloactionIsTrue_whenSendingJobToUser_confirmProcessBySurveyTypeIsCalled(){
    UserDto userDto = UserDto.builder().build();
    String validDate ="2014-11-03T11:15:30";
    LocalDateTime lastUpdateParsed = LocalDateTime.parse(validDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    LegacySampleIngest lsi = LegacySampleIngest.builder().lastUpdated(validDate).legacySampleSurveyType(LegacySampleSurveyType.GFF).stage("333").build();

    when(jobResourceServiceClient.existsByTmJobIdAndLastAuthNo(lsi.getTmJobId(), userDto.getAuthNo())).thenReturn(false);
    when(jobResourceServiceClient.findByTmJobId(anyString())).thenReturn(Optional.empty());

    JobDto expectedDto = new JobDto(lsi.getTmJobId(), lsi.getAuth(), lastUpdateParsed);
    SendCreateJobRequestMessage msg = new SendCreateJobRequestMessage();
    when(tmJobConverterService.createJob(any(LegacySampleIngest.class), anyString())).thenReturn(msg);

    jobProcessor.sendJobToUser(0, lsi, userDto, false);

    verify(jobResourceServiceClient, times(1)).createJob(eq(expectedDto));
  }

  
  @Test
  public void givenJobJobExisitsInJobResource_processReallocation_confirmJobIsUpdated(){
    LegacySampleIngest lsi = LegacySampleIngest.builder().tmJobId("123").build();
    UserDto userDto = UserDto.builder().tmUsername("dummy").build();
    Optional<JobDto> oJ = Optional.of(JobDto.builder().build());
    when(jobResourceServiceClient.findByTmJobId(anyString())).thenReturn(oJ);
   
    jobProcessor.processReallocation(lsi, userDto);

    verify(jobResourceServiceClient, times(1)).updateJob(oJ.get());
  }
  
  @Test
  public void givenJobJobDosNotExisitsInJobResource_processReallocation_confirmJobIsNotUpdated(){
    LegacySampleIngest lsi = LegacySampleIngest.builder().tmJobId("123").build();
    UserDto userDto = UserDto.builder().build();
    when(jobResourceServiceClient.findByTmJobId(anyString())).thenReturn(Optional.empty());
    
    jobProcessor.processReallocation(lsi, userDto);

    verify(jobResourceServiceClient, times(0)).updateJob(any(JobDto.class));
  }
  
  @Test
  public void givenLastUpdateValueisInValid_whenProcessByServiceTypeIsCalled_confirmThatJobIsNotProcessed(){
    String invalidDate ="2014-NOV-03T11:15:30";
    LegacySampleIngest lsi = LegacySampleIngest.builder().lastUpdated(invalidDate).build();
    UserDto userDto = UserDto.builder().build();
    
    try {
      jobProcessor.processBySurveyType(lsi, userDto, 0);
    } catch (Exception e) {
    }
   
    verify(tmJobConverterService, never()).createJob(any(LegacySampleIngest.class), anyString());
    verify(tmService, never()).send(any(SendUpdateJobHeaderRequestMessage.class));
  }
  
  @Test
  public void givenLastUpdateValueisNull_whenProcessByServiceTypeIsCalled_confirmThatJobIsNotProcessed(){
    String invalidDate = null;
    LegacySampleIngest lsi = LegacySampleIngest.builder().lastUpdated(invalidDate).build();
    UserDto userDto = UserDto.builder().build();
    
    try {
      jobProcessor.processBySurveyType(lsi, userDto, 0);
    } catch (Exception e) {
    }
   
    verify(tmJobConverterService, never()).createJob(any(LegacySampleIngest.class), anyString());
    verify(tmService, never()).send(any(SendUpdateJobHeaderRequestMessage.class));
  }
  
  @Test
  public void givenJobIsOfTypeGFF_whenProcessByServiceTypeIsCalled_confirmThatTheJobIsProcessedAsGFF(){
    String validDate ="2014-11-03T11:15:30";
    LegacySampleIngest lsi = LegacySampleIngest.builder().lastUpdated(validDate).legacySampleSurveyType(LegacySampleSurveyType.GFF).stage("333").build();
    UserDto userDto = UserDto.builder().tmUsername("bob").build();
    SendCreateJobRequestMessage msg = new SendCreateJobRequestMessage();

    when(tmJobConverterService.createReissue(lsi, "bob")).thenReturn(msg);
    
    jobProcessor.processBySurveyType(lsi, userDto, 0);

    verify(tmJobConverterService, times(1)).createReissue(eq(lsi), eq("bob"));
    verify(tmService, times(1)).send(eq(msg));
  }
  
  @Test
  public void givenJobIsOfTypeLFS_whenProcessByServiceTypeIsCalled_confirmThatTheJobIsProcessedAsLFS(){
    String validDate ="2014-11-03T11:15:30";
    LocalDateTime lastUpdateParsed = LocalDateTime.parse(validDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    LegacySampleIngest lsi = LegacySampleIngest.builder().lastUpdated(validDate).legacySampleSurveyType(LegacySampleSurveyType.LFS).build();
    UserDto userDto = UserDto.builder().build();
    
    JobDto expectedDto = new JobDto(lsi.getTmJobId(), lsi.getAuth(), lastUpdateParsed);
    SendCreateJobRequestMessage msg = new SendCreateJobRequestMessage();
    when(tmJobConverterService.createJob(any(LegacySampleIngest.class), anyString())).thenReturn(msg);

    jobProcessor.processBySurveyType(lsi, userDto, 0);

    verify(jobResourceServiceClient, times(1)).createJob(eq(expectedDto));
  }

  @Test
  public void givenGFFJobIsAReissue_whenprocessGFFSampleIsCalled_confirmThatCreateReissueIsCalled(){
    String validDate ="2014-11-03T11:15:30";
    LocalDateTime localDateTime = LocalDateTime.parse(validDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    LegacySampleIngest lsi = LegacySampleIngest.builder().lastUpdated(validDate).legacySampleSurveyType(LegacySampleSurveyType.GFF).stage("333").build();
    UserDto userDto = UserDto.builder().tmUsername("dummy").build();
        
    jobProcessor.processGFFSample(lsi, userDto, localDateTime);
    verify(tmJobConverterService, times(1)).createReissue(any(LegacySampleIngest.class), anyString());
    verify(tmJobConverterService, times(0)).createJob(any(LegacySampleIngest.class), anyString());
  }
  
  @Test
  public void givenGFFJobIsNotAReissue_whenprocessGFFSampleIsCalled_confirmThatCreateReissueIsCalled(){
    String validDate ="2014-11-03T11:15:30";
    LocalDateTime localDateTime = LocalDateTime.parse(validDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    LegacySampleIngest lsi = LegacySampleIngest.builder().lastUpdated(validDate).legacySampleSurveyType(LegacySampleSurveyType.GFF).stage("000").build();
    UserDto userDto = UserDto.builder().tmUsername("dummy").build();
        
    jobProcessor.processGFFSample(lsi, userDto, localDateTime);
    verify(tmJobConverterService, times(0)).createReissue(any(LegacySampleIngest.class), anyString());
    verify(tmJobConverterService, times(1)).createJob(any(LegacySampleIngest.class), anyString());
  }
  
  @Test
  public void givenIngestHasValidDate_whenUpdateLegacyLastUpdatedIsCalled_confimThatJobDtoHasTheSameDate(){
    String validDate ="2014-11-03T11:15:30";
    LocalDateTime expectedDate = LocalDateTime.parse(validDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    LegacySampleIngest ingest = LegacySampleIngest.builder().lastUpdated(validDate).build();
    JobDto jobDto = JobDto.builder().build();
    
    jobProcessor.updateLegacyLastUpdated(ingest, jobDto);
    
    assertEquals(expectedDate, jobDto.getLastUpdated());
  }

  @Test
  public void givenJobDtoLastUpdateDateIsNull_confirmIngestIsLatestTransactionReturnsTrue(){
    String ingestDate ="2014-11-03T11:15:30";
    LocalDateTime jobDtoDate =null;
    
    LegacySampleIngest ingest = LegacySampleIngest.builder().lastUpdated(ingestDate).build();
    JobDto jobDto = JobDto.builder().lastUpdated(jobDtoDate).build();

    boolean isLatest = jobProcessor.ingestIsLatestTransaction(ingest, jobDto);
    
    assertEquals(true, isLatest);
  }
  
  public void givenIngestLastUpdatedIsAfterJobDtoLastUpdateDate_confirmIngestIsLatestTransactionReturnsTrue(){
    String ingestDate ="2014-11-03T11:15:30";
    String jobDtoDate ="2014-10-03T11:15:30";
    
    LegacySampleIngest ingest = LegacySampleIngest.builder().lastUpdated(ingestDate).build();
    JobDto jobDto = JobDto.builder().lastUpdated(LocalDateTime.parse(jobDtoDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)).build();

    boolean isLatest = jobProcessor.ingestIsLatestTransaction(ingest, jobDto);
    
    assertEquals(true, isLatest);
  }
  
  @Test
  public void givenIngestLastUpdatedIsBeforeJobDtoLastUpdateDate_confirmIngestIsLatestTransactionReturnsFalse(){
    String ingestDate ="2014-11-03T11:15:30";
    String jobDtoDate ="2014-12-03T11:15:30";
    
    LegacySampleIngest ingest = LegacySampleIngest.builder().lastUpdated(ingestDate).build();
    JobDto jobDto = JobDto.builder().lastUpdated(LocalDateTime.parse(jobDtoDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)).build();

    boolean isLatest = jobProcessor.ingestIsLatestTransaction(ingest, jobDto);
    
    assertEquals(false, isLatest);
  }
  
  @Test 
  public void givenIngestAndJobDtosHaveTheSameAuthNo_confirmIsUsersTheSameReturnsTrue(){
    String ingestAuthNo = "Bob123";
    String jobDtoAuthNo = "Bob123";

    LegacySampleIngest ingest = LegacySampleIngest.builder().auth(ingestAuthNo).build();
    JobDto jobDto = JobDto.builder().lastAuthNo(jobDtoAuthNo).build();
    
    boolean isSame = jobProcessor.isUsersTheSame(ingest, jobDto);

    assertEquals(true, isSame);
  }

  @Test 
  public void givenIngestAndJobDtosHaveDifferentAuthNo_confirmIsUsersTheSameReturnsFalse(){
    String ingestAuthNo = "Angel123";
    String jobDtoAuthNo = "Bob123";

    LegacySampleIngest ingest = LegacySampleIngest.builder().auth(ingestAuthNo).build();
    JobDto jobDto = JobDto.builder().lastAuthNo(jobDtoAuthNo).build();
    
    boolean isSame = jobProcessor.isUsersTheSame(ingest, jobDto);

    assertEquals(false, isSame);
  }

  @Test
  public void givenJobExists_confirmJobTypeIsReallocation(){
    boolean isExistingJob = true;
    String jobType = jobProcessor.findJobType(isExistingJob);
    
    assertEquals("Reallocation", jobType);
  }
  
  @Test
  public void givenJobDoesntExists_confirmJobTypeIsAllocation(){
    boolean isExistingJob = false;
    String jobType = jobProcessor.findJobType(isExistingJob);
    
    assertEquals("Allocation", jobType);
  }
  
}
