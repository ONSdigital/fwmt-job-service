package uk.gov.ons.fwmt.job_service.service.impl.totalmobile;

import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.AdditionalPropertyCollectionType;
import com.consiliumtechnologies.schemas.mobile._2009._03.visitstypes.AdditionalPropertyType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.CreateJobRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisemessages.UpdateJobHeaderRequest;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.AddressDetailType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.ContactInfoType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.JobHeaderType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.JobIdentityType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.JobType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.LocationType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.NameValueAttributeCollectionType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.ObjectFactory;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.ResourceIdentityType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.SkillCollectionType;
import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.WorldIdentityType;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendMessageRequestInfo;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendUpdateJobHeaderRequestMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.stereotype.Service;
import uk.gov.ons.fwmt.job_service.data.annotation.JobAdditionalProperty;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.service.totalmobile.TMJobConverterService;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TMJobConverterServiceImpl implements TMJobConverterService {
  protected static final String JOB_QUEUE = "\\OPTIMISE\\INPUT";
  protected static final String JOB_SKILL = "Survey";
  protected static final String JOB_WORK_TYPE = "SS";
  protected static final String JOB_WORLD = "Default";

  private final DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
  private final ObjectFactory factory = new ObjectFactory();

  public TMJobConverterServiceImpl() throws DatatypeConfigurationException {
  }

  protected static void addAdditionalProperty(CreateJobRequest request, String key, String value) {
    AdditionalPropertyType propertyType = new AdditionalPropertyType();
    propertyType.setName(key);
    propertyType.setValue(value);
    request.getJob().getAdditionalProperties().getAdditionalProperty().add(propertyType);
  }

  /**
   * Read the JobAdditionalProperty annotations on the class T and set additional properties on the TM request
   */
  private static <T> void setFromAdditionalPropertyAnnotations(T instance, CreateJobRequest request) {
    Class<?> tClass = instance.getClass();
    PropertyAccessor accessor = PropertyAccessorFactory.forBeanPropertyAccess(instance);
    for (Field field : tClass.getDeclaredFields()) {
      Optional<Annotation> annotation = Arrays.stream(field.getDeclaredAnnotations())
          .filter(an -> an.annotationType() == JobAdditionalProperty.class)
          .findFirst();
      if (annotation.isPresent()) {
        JobAdditionalProperty jobAdditionalProperty = (JobAdditionalProperty) annotation.get();
        Object value = accessor.getPropertyValue(field.getName());
        if (value != null) {
          addAdditionalProperty(request, jobAdditionalProperty.value(), value.toString());
        } else {
          log.warn("Unprocessed job property: " + field.getName());
          addAdditionalProperty(request, jobAdditionalProperty.value(), "");
        }
      }
    }
  }

  protected CreateJobRequest createJobRequestFromIngest(LegacySampleIngest ingest, String username) {
    CreateJobRequest request = new CreateJobRequest();
    JobType job = new JobType();
    request.setJob(job);
    job.setLocation(new LocationType());
    job.setIdentity(new JobIdentityType());
    job.getLocation().setAddressDetail(new AddressDetailType());
    job.getLocation().getAddressDetail().setLines(new AddressDetailType.Lines());
    job.setContact(new ContactInfoType());
    job.setAttributes(new NameValueAttributeCollectionType());
    job.setAllocatedTo(new ResourceIdentityType());
    job.setSkills(new SkillCollectionType());
    job.setAdditionalProperties(new AdditionalPropertyCollectionType());
    job.setWorld(new WorldIdentityType());

    request.getJob().getIdentity().setReference(ingest.getTmJobId());

    LocationType location = request.getJob().getLocation();
    List<String> addressLines = location.getAddressDetail().getLines().getAddressLine();

    addAddressLines(addressLines, ingest.getAddressLine1());
    addAddressLines(addressLines, ingest.getAddressLine2());
    addAddressLines(addressLines, ingest.getAddressLine3());
    addAddressLines(addressLines, ingest.getAddressLine4());
    addAddressLines(addressLines, ingest.getDistrict());
    addAddressLines(addressLines, ingest.getPostTown());
    checkNumberOfAddressLines(addressLines);

    location.getAddressDetail().setPostCode(ingest.getPostcode());
    location.setReference(ingest.getSerNo());

    request.getJob().getContact().setName(ingest.getPostcode());
    request.getJob().getSkills().getSkill().add(JOB_SKILL);
    request.getJob().setWorkType(JOB_WORK_TYPE);
    request.getJob().getWorld().setReference(JOB_WORLD);

    GregorianCalendar dueDateCalendar = GregorianCalendar
        .from(ingest.getDueDate().atTime(23, 59, 59).atZone(ZoneId.of("UTC")));
    request.getJob().setDueDate(datatypeFactory.newXMLGregorianCalendar(dueDateCalendar));

    if (ingest.getDivAddInd().equals("0"))
      request.getJob().setDescription(ingest.getTla() + " Wave " + ingest.getWave());

    request.getJob().getAllocatedTo().setUsername(username);

    // additional properties
    setFromAdditionalPropertyAnnotations(ingest, request);
    switch (ingest.getLegacySampleSurveyType()) {
    case GFF:
      // TODO does splitSampleType need extra mapping?
      setGffDividedAddressIndicator(ingest,request);
      setFromAdditionalPropertyAnnotations(ingest.getGffData(), request);
      break;
    case LFS:
      setLfsDividedAddressIndicator(ingest, request);
      setFromAdditionalPropertyAnnotations(ingest.getLfsData(), request);
      break;
    }

    request.getJob().setDuration(1);
    request.getJob().setVisitComplete(false);
    request.getJob().setDispatched(false);
    request.getJob().setAppointmentPending(false);
    request.getJob().setEmergency(false);

    return request;
  }

  private void setLfsDividedAddressIndicator(LegacySampleIngest ingest, CreateJobRequest request) {
    if (ingest.getDivAddInd().equals("1")){
      request.getJob().setDescription(ingest.getTla() + " Wave " + ingest.getWave() + "\n"
          + "** Divided address – This part only **");
    } else if (ingest.getDivAddInd().equals("2") && ingest.getTla().equals("LFS")) {
      request.getJob().setDescription(ingest.getTla() + " Wave " + ingest.getWave() + "\n"
          + "** Divided Address – This part or one not listed **");
    }
  }

  private void setGffDividedAddressIndicator(LegacySampleIngest ingest, CreateJobRequest request) {
    if (ingest.getDivAddInd().equals("1") || ingest.getDivAddInd().equals("2"))
      request.getJob().setDescription(ingest.getTla() + " Wave " + ingest.getWave() + "\n"
          + ("** Warning Divided Address **"));
  }

  protected void addAddressLines(List<String> addressLines, String addressLine) {
    if (StringUtils.isNotBlank((addressLine))) {
      addressLines.add(addressLine);
    }
  }

  protected void checkNumberOfAddressLines(List<String> addressLines) {
    if (addressLines.size() == 6 ) {
      String addressConcat = addressLines.get(2) + " " + addressLines.get(3);
      addressLines.set(2, addressConcat);
      addressLines.remove(3);
    }
  }

  protected UpdateJobHeaderRequest makeUpdateJobHeaderRequest(String tmJobId, String username) {
    UpdateJobHeaderRequest request = new UpdateJobHeaderRequest();
    request.setJobHeader(new JobHeaderType());
    request.getJobHeader().setAllocatedTo(new ResourceIdentityType());
    request.getJobHeader().setJobIdentity(new JobIdentityType());

    request.getJobHeader().getAllocatedTo().setUsername(username);
    request.getJobHeader().getJobIdentity().setReference(tmJobId);

    return request;
  }

  protected SendMessageRequestInfo makeSendMessageRequestInfo(String key) {
    SendMessageRequestInfo info = new SendMessageRequestInfo();
    info.setQueueName(JOB_QUEUE);
    info.setKey(key);
    return info;
  }

  public SendCreateJobRequestMessage createJob(LegacySampleIngest ingest, String username) {
    CreateJobRequest request = createJobRequestFromIngest(ingest, username);

    SendCreateJobRequestMessage message = new SendCreateJobRequestMessage();
    message.setSendMessageRequestInfo(makeSendMessageRequestInfo(ingest.getTmJobId()));
    message.setCreateJobRequest(request);

    return message;
  }

  public SendUpdateJobHeaderRequestMessage updateJob(String tmJobId, String username) {
    UpdateJobHeaderRequest request = makeUpdateJobHeaderRequest(tmJobId, username);

    SendUpdateJobHeaderRequestMessage message = new SendUpdateJobHeaderRequestMessage();
    message.setSendMessageRequestInfo(makeSendMessageRequestInfo(tmJobId));
    message.setUpdateJobHeaderRequest(request);

    return message;
  }

  public SendUpdateJobHeaderRequestMessage updateJob(LegacySampleIngest ingest, String username) {
    return updateJob(ingest.getTmJobId(), username);
  }

  public SendCreateJobRequestMessage createReissue(LegacySampleIngest ingest, String username) {
    return createJob(ingest, username);
  }
}

