package uk.gov.ons.fwmt.job_service.utilities;

import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleGFFDataIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;

import java.time.LocalDate;

import static uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType.GFF;

public class TestIngestBuilder extends LegacySampleIngest {

  public LegacySampleIngest ingestBuild(){
    LegacySampleIngest ingest = new LegacySampleIngest();
    ingest.setTimestamp("testTimestamp");
    ingest.setSerNo("testSerNo");
    ingest.setTla("testTla");
    ingest.setStage("testStage");
    ingest.setWave("testWave");
    ingest.setAddressLine1("testAddressLine1");
    ingest.setAddressLine2("testAddressLine2");
    ingest.setAddressLine3("testAddressLine3");
    ingest.setAddressLine4("testAddressLine4");
    ingest.setDistrict("TestDistrict");
    ingest.setPostTown("testPostTown");
    ingest.setPostcode("testPostcode");
    ingest.setQuota("testQuota");
    ingest.setAddressNo("testAddressNo");
    ingest.setOsGridRef("testOsGridRef");
    ingest.setYear("testYear");
    ingest.setMonth("testMonth");
    ingest.setTelNo("testTelNo");
    ingest.setIssueNo("testIssue_No");
    ingest.setPart("testPart");
    ingest.setEmployeeNo("testEmployeeNo");
    ingest.setAuth("testAuth");
    ingest.setLastUpdated("testLastUpdated");
    ingest.setTmJobId("testJobID");
    ingest.setLegacySampleSurveyType(GFF);
    ingest.setDueDate(LocalDate.of(2018, 10, 10));
    //additional properties
    ingest.setGeoX(3.56f);
    ingest.setGeoY(3.56f);
    ingest.setGffData(new LegacySampleGFFDataIngest());
    return ingest;
  }
}
