package uk.gov.ons.fwmt.job_service.service.impl;

import org.junit.Test;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType.GFF;
import static uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType.LFS;

public class FileIngestServiceImplTest {

  private final String[] validSampleFileNames = {
      "sample_GFF_2018-04-24T19:09:54Z.csv",
      "sample_GFF_2018-04-24T19-09-54Z.csv",
      "sample_LFS_2018-04-24T19:31:25Z.csv",
      "sample_LFS_2018-04-24T19-31-25Z.csv",
  };
  private final String[] validStaffFileNames = {
      "staff_2016-04-24T19:09:54Z.csv",
      "staff_2018-04-24T19:31:25Z.csv",
      "staff_2018-04-24T19-31-25Z.csv",
  };
  private final String[] invalidSampleFileNames = {
      "BLAH_BLAH-THIS_IS_WRONG",
      "sample_2018-04-24T19-31-25Z.csv",
      "sample_LFS_2018-04-24T19-31-25Z.csp",
  };
  private final String[] invalidStaffFileNames = {
      "BLAH_BLAH-THIS_IS_WRONG",
      "staff_2018-04-24R19-31-25Z.csv",
      "staff_EG_2018-04-24L19-31-25Z.csv",
      "foo_LFS_2018-04-24L19-31-25Z.csv",
      "staff_LFS_2018-04-24T19-31-25Z.csv",
      "staff_2018-04-24T19:31:25Z.csp",
  };
  private FileIngestServiceImpl fileIngestService = new FileIngestServiceImpl();

  @Test
  public void checkValidSampleFileNames() {
    for (String filename : validSampleFileNames) {
      assertNotNull(fileIngestService.verifyCSVFilename(filename, "sample"));
    }
  }

  @Test
  public void checkValidStaffFileNames() {
    for (String filename : validStaffFileNames) {
      assertNotNull(fileIngestService.verifyCSVFilename(filename, "staff"));
    }
  }

  // this normally throws InvalidFileNameException
  // TODO add checks to make sure that this throws the right exception
  @Test(expected = FWMTCommonException.class)
  public void checkInvalidSampleFileNames() {
    for (String filename : invalidSampleFileNames) {
      assertNotNull(fileIngestService.verifyCSVFilename(filename, "sample"));
      // we should throw an InvalidFileNameException before this point
      fail("False negative - filename '" + filename + "' should be invalid");
    }
  }

  // this normally throws InvalidFileNameException
  // TODO add checks to make sure that this throws the right exception
  @Test(expected = FWMTCommonException.class)
  public void checkInvalidStaffFileNames() {
    for (String filename : invalidStaffFileNames) {
      assertNotNull(fileIngestService.verifyCSVFilename(filename, "staff"));
      // we should throw an InvalidFileNameException before this point
      fail("False negative - filename '" + filename + "' should be invalid");
    }
  }

  @Test
  public void extractSampleEndpoint() {
    //Given
    String rawFilename = "sample_GFF_2018-04-24T19:09:54Z.csv";
    String expectedEndpoint = "sample";
    String[] filenameSplitByUnderscore = {"sample", "GFF", "2018-04-24T19:09:54Z.csv"};

    //When
    String result = fileIngestService.extractEndpoint(rawFilename, expectedEndpoint, filenameSplitByUnderscore);

    //Then
    assertEquals(expectedEndpoint, result);
  }

  @Test
  public void extractStaffEndpoint() {
    //Given
    String rawFilename = "staff_2016-04-24T19:09:54Z.csv";
    String expectedEndpoint = "staff";
    String[] filenameSplitByUnderscore = {"staff", "2018-04-24T19:09:54Z.csv"};

    //When
    String result = fileIngestService.extractEndpoint(rawFilename, expectedEndpoint, filenameSplitByUnderscore);

    //Then
    assertEquals(expectedEndpoint, result);
  }

  // this normally throws InvalidFileNameException
  // TODO add checks to make sure that this throws the right exception
  @Test(expected = FWMTCommonException.class)
  public void wrongEndpointInSampleFilename() {
    //Given
    String rawFilename = "wrong_GFF_2018-04-24T19:09:54Z.csv";
    String expectedEndpoint = "sample";
    String[] filenameSplitByUnderscore = {"wrong", "GFF", "2018-04-24T19:09:54Z.csv"};

    //When
    String result = fileIngestService.extractEndpoint(rawFilename, expectedEndpoint, filenameSplitByUnderscore);

    //Then
    assertEquals(expectedEndpoint, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void correctFilenameSentToWrongEndpoint() {
    //Given
    String rawFilename = "sample_GFF_2018-04-24T19:09:54Z.csv";
    String expectedEndpoint = "wrong";
    String[] filenameSplitByUnderscore = {"wrong", "GFF", "2018-04-24T19:09:54Z.csv"};

    //When
    String result = fileIngestService.extractEndpoint(rawFilename, expectedEndpoint, filenameSplitByUnderscore);

    //Then
    assertEquals(expectedEndpoint, result);
  }

  // this normally throws InvalidFileNameException
  // TODO add checks to make sure that this throws the right exception
  @Test(expected = FWMTCommonException.class)
  public void sampleFilenameFormattedIncorrectly() {
    //Given
    String rawFilename = "sample_GFF-2018-04-24T19:09:54Z.csv";
    String expectedEndpoint = "sample";
    String[] filenameSplitByUnderscore = {"sample", "GFF-2018-04-24T19:09:54Z.csv"};

    //When
    String result = fileIngestService.extractEndpoint(rawFilename, expectedEndpoint, filenameSplitByUnderscore);

    //Then
    assertEquals(expectedEndpoint, result);
  }

  // this normally throws InvalidFileNameException
  // TODO add checks to make sure that this throws the right exception
  @Test(expected = FWMTCommonException.class)
  public void staffFilenameFormattedIncorrectly() {
    //Given
    String rawFilename = "staff__2016-04-24T19:09:54Z.csv";
    String expectedEndpoint = "staff";
    String[] filenameSplitByUnderscore = {"staff", "", "2018-04-24T19:09:54Z.csv"};

    //When
    String result = fileIngestService.extractEndpoint(rawFilename, expectedEndpoint, filenameSplitByUnderscore);

    //Then
    assertEquals(expectedEndpoint, result);
  }

  @Test
  public void checkCorrectFileExtension() {
    //Given
    String rawFilename = "sample_GFF_2018-04-24T19:09:54Z.csv";
    String expectedFileName = "sample_GFF_2018-04-24T19:09:54Z";
    String expectedExtension = "csv";

    //When
    String[] result = fileIngestService.checkFileExtension(rawFilename);

    //Then
    assertEquals(rawFilename, result[0] + "." + result[1]);
    assertEquals(expectedFileName, result[0]);
    assertEquals(expectedExtension, result[1]);
  }

  // this normally throws InvalidFileNameException
  // TODO add checks to make sure that this throws the right exception
  @Test(expected = FWMTCommonException.class)
  public void incorrectFileExtension() {
    //Given
    String rawFilename = "sample_GFF_2018-04-24T19:09:54Z.jpg";
    String expectedExtension = "csv";

    String[] result = fileIngestService.checkFileExtension(rawFilename);

    //Then
    assertNotEquals(expectedExtension, result[1]);
  }

  @Test
  public void getLegacySampleSurveyTypeGFF() {
    //Given
    String[] filenameSplitByUnderscore = {"sample", "GFF", "2018-04-24T19:09:54Z.csv"};
    String endpoint = "sample";

    //When
    LegacySampleSurveyType result = fileIngestService.getLegacySampleSurveyType(filenameSplitByUnderscore, endpoint);

    //Then
    assertEquals(GFF, result);
  }

  @Test
  public void getLegacySampleSurveyTypeLFS() {
    //Given
    String[] filenameSplitByUnderscore = {"sample", "LFS", "2018-04-24T19:09:54Z.csv"};
    String endpoint = "sample";

    //When
    LegacySampleSurveyType result = fileIngestService.getLegacySampleSurveyType(filenameSplitByUnderscore, endpoint);

    //Then
    assertEquals(LFS, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void unrecognizedLegacySampleSurveyType() {
    //Given
    String[] filenameSplitByUnderscore = {"sample", "TLA", "2018-04-24T19:09:54Z.csv"};
    String endpoint = "sample";

    //When
    LegacySampleSurveyType result = fileIngestService.getLegacySampleSurveyType(filenameSplitByUnderscore, endpoint);

    //Then
    assertNotEquals(LFS, result);
    assertNotEquals(GFF, result);
  }

  @Test
  public void getLocalDateTime() {
    //Given
    String rawFilename = "sample_GFF_2018-04-24T19:09:54Z.csv";
    String rawTimestamp = "2018-04-24T19:09:54Z";

    //When
    LocalDateTime result = fileIngestService.getLocalDateTime(rawFilename, rawTimestamp);

    //Then
    assertNotNull(result);
  }

  // this normally throws InvalidFileNameException
  // TODO add checks to make sure that this throws the right exception
  @Test(expected = FWMTCommonException.class)
  public void noEffortForACorrectDateTimeFormat() {
    //Given
    String rawFilename = "sample_GFF_2018-04-24T19:09:54Z.csv";
    String rawTimestamp = "I am clearly not anything related to date or time";
    String validTime = "2018-04-24T19:09:54";

    //When
    LocalDateTime result = fileIngestService.getLocalDateTime(rawFilename, rawTimestamp);

    //Then
    assertNotEquals(validTime, result);
  }
}