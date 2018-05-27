package uk.gov.ons.fwmt.job_service.service;

import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.CreateJobRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.DeleteJobRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.UpdateJobHeaderRequest;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDeleteJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateJobHeaderRequestMessage;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;

public interface TMJobConverterService {
  SendCreateJobRequestMessage createJob(LegacySampleIngest ingest, String username);
  SendUpdateJobHeaderRequestMessage updateJob(String tmJobId, String username);
  SendUpdateJobHeaderRequestMessage updateJob(LegacySampleIngest ingest, String username);
  SendCreateJobRequestMessage createReissue(LegacySampleIngest ingest, String username);
  SendDeleteJobRequestMessage deleteJob(String tmJobId);
}

