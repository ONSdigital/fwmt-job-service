package uk.gov.ons.fwmt.job_service.utilities;

import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;

import java.time.LocalDate;

public class TestIngestBuilder extends LegacySampleIngest {

  public LegacySampleIngest ingestBuild() {
    LegacySampleIngest ingest = new LegacySampleIngest();
    ingest.setTimestamp("testTimestamp");
    ingest.setSerNo("testSerNo");
    ingest.setTla("testTla");
    ingest.setStage("807");
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
    ingest.setLastUpdated("2018-08-01T01:06:01");
    ingest.setTmJobId("testJobID");
    ingest.setDueDate(LocalDate.of(2018, 10, 10));
    ingest.setDivAddInd("0");
    ingest.setLat(1111.0f);
    ingest.setLng(1111.0f);

    //additional properties
    ingest.setGeoX(3.56f);
    ingest.setGeoY(3.56f);
    ingest.setIncentive("testIncentive");
    ingest.setContactName("TestName");
    return ingest;
  }

  public LegacySampleIngest ingestBuildWithEmptyAddresses() {
    LegacySampleIngest ingest = new LegacySampleIngest();
    ingest.setTimestamp("testTimestamp");
    ingest.setSerNo("testSerNo");
    ingest.setTla("testTla");
    ingest.setStage("807");
    ingest.setWave("testWave");
    ingest.setAddressLine1("testAddressLine1");
    ingest.setAddressLine2("testAddressLine2");
    ingest.setAddressLine3("");
    ingest.setAddressLine4("");
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
    ingest.setLastUpdated("2018-08-01T01:06:01");
    ingest.setTmJobId("testJobID");
    ingest.setDueDate(LocalDate.of(2018, 10, 10));
    ingest.setDivAddInd("0");
    ingest.setLat(1111.0f);
    ingest.setLng(1111.0f);

    //additional properties
    ingest.setGeoX(3.56f);
    ingest.setGeoY(3.56f);
    ingest.setIncentive("testLcfIncentive");
    ingest.setContactName("TestName");
    return ingest;
  }

  public LegacySampleIngest ingestBuildDivAddIndOne() {
    LegacySampleIngest ingest = new LegacySampleIngest();
    ingest.setTimestamp("testTimestamp");
    ingest.setSerNo("testSerNo");
    ingest.setTla("testTla");
    ingest.setStage("807");
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
    ingest.setLastUpdated("2018-08-01T01:06:01");
    ingest.setTmJobId("testJobID");
    ingest.setDueDate(LocalDate.of(2018, 10, 10));
    ingest.setDivAddInd("1");
    ingest.setLat(1111.0f);
    ingest.setLng(1111.0f);

    //additional properties
    ingest.setGeoX(3.56f);
    ingest.setGeoY(3.56f);
    ingest.setIncentive("testLcfIncentive");
    ingest.setContactName("TestName");
    return ingest;
  }

  public LegacySampleIngest ingestBuildDivAddIndTwo() {
    LegacySampleIngest ingest = new LegacySampleIngest();
    ingest.setTimestamp("testTimestamp");
    ingest.setSerNo("testSerNo");
    ingest.setTla("testTla");
    ingest.setStage("807");
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
    ingest.setLastUpdated("2018-08-01T01:06:01");
    ingest.setTmJobId("testJobID");
    ingest.setDueDate(LocalDate.of(2018, 10, 10));
    ingest.setDivAddInd("2");
    ingest.setLat(1111.0f);
    ingest.setLng(1111.0f);

    //additional properties
    ingest.setGeoX(3.56f);
    ingest.setGeoY(3.56f);
    ingest.setIncentive("testLcfIncentive");
    ingest.setContactName("TestName");
    return ingest;
  }

  public LegacySampleIngest ingestBuildDivAddIndIsEmpty() {
    LegacySampleIngest ingest = new LegacySampleIngest();
    ingest.setTimestamp("testTimestamp");
    ingest.setSerNo("testSerNo");
    ingest.setTla("testTla");
    ingest.setStage("807");
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
    ingest.setLastUpdated("2018-08-01T01:06:01");
    ingest.setTmJobId("testJobID");
    ingest.setDueDate(LocalDate.of(2018, 10, 10));
    ingest.setDivAddInd("");
    ingest.setLat(1111.0f);
    ingest.setLng(1111.0f);

    //additional properties
    ingest.setGeoX(3.56f);
    ingest.setGeoY(3.56f);
    ingest.setIncentive("testLcfIncentive");
    ingest.setContactName("Test Name");
    return ingest;
  }

  public LegacySampleIngest ingestBuildDivAddIndIsNotExpectedNumber() {
    LegacySampleIngest ingest = new LegacySampleIngest();
    ingest.setTimestamp("testTimestamp");
    ingest.setSerNo("testSerNo");
    ingest.setTla("testTla");
    ingest.setStage("807");
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
    ingest.setLastUpdated("2018-08-01T01:06:01");
    ingest.setTmJobId("testJobID");
    ingest.setDueDate(LocalDate.of(2018, 10, 10));
    ingest.setDivAddInd("unexpected");
    ingest.setLat(1111.0f);
    ingest.setLng(1111.0f);

    //additional properties
    ingest.setGeoX(3.56f);
    ingest.setGeoY(3.56f);
    ingest.setIncentive("testLcfIncentive");
    ingest.setContactName("TestName");
    return ingest;
  }

  public LegacySampleIngest ingestBuildDivAddIndIsNull() {
    LegacySampleIngest ingest = new LegacySampleIngest();
    ingest.setTimestamp("testTimestamp");
    ingest.setSerNo("testSerNo");
    ingest.setTla("testTla");
    ingest.setStage("807");
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
    ingest.setLastUpdated("2018-08-01T01:06:01");
    ingest.setTmJobId("testJobID");
    ingest.setDueDate(LocalDate.of(2018, 10, 10));
    ingest.setDivAddInd(null);
    ingest.setLat(1111.0f);
    ingest.setLng(1111.0f);

    //additional properties
    ingest.setGeoX(3.56f);
    ingest.setGeoY(3.56f);
    ingest.setIncentive("testLcfIncentive");
    ingest.setContactName("TestName");
    return ingest;
  }
}
