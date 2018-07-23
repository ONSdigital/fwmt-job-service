package uk.gov.ons.fwmt.job_service.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType.GFF;
import static uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType.LFS;

import java.time.LocalDateTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType;
import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service.utils.SampleFileUtils;

public class FileIngestServiceImplTest {
  @Rule public ExpectedException expectedException = ExpectedException.none();

  private final String[] validSampleFileNames = {
      "sample_GFF_2018-04-24T19:09:54Z.csv",
      "sample_GFF_2018-04-24T19-09-54Z.csv",
      "sample_gff_2018-04-24T19-09-54Z.csv",
      "sample_LFS_2018-04-24T19:31:25Z.csv",
      "sample_LFS_2018-04-24T19-31-25Z.csv",
      "sample_lfs_2018-04-24T19-31-25Z.csv",
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

  @Test
  public void checkValidSampleFileNames() {
    for (String filename : validSampleFileNames) {
      assertNotNull(SampleFileUtils.buildSampleFilenameComponents(filename, "sample"));
    }
  }

  @Test
  public void checkValidStaffFileNames() {
    for (String filename : validStaffFileNames) {
      assertNotNull(SampleFileUtils.buildSampleFilenameComponents(filename, "staff"));
    }
  }

  @Test
  public void checkInvalidSampleFileNames() {
    for (String filename : invalidSampleFileNames) {
      try {
        assertNotNull(SampleFileUtils.buildSampleFilenameComponents(filename, "sample"));
      } catch (FWMTCommonException e) {
        if (!e.getCode().equals(ExceptionCode.INVALID_FILE_NAME)) {
          fail("Odd positive - filename '" + filename + "' failed for an unexpected reason");
        } else {
          continue;
        }
      }
      // we should throw an INVALID_FILE_NAME exception before this point
      fail("False negative - filename '" + filename + "' should be invalid");
    }
  }

  @Test
  public void checkInvalidStaffFileNames() {
    for (String filename : invalidStaffFileNames) {
      try {
        assertNotNull(SampleFileUtils.buildSampleFilenameComponents(filename, "staff"));
      } catch (FWMTCommonException e) {
        if (!e.getCode().equals(ExceptionCode.INVALID_FILE_NAME)) {
          fail("Odd positive - filename '" + filename + "' failed for an unexpected reason");
        } else {
          continue;
        }
      }
      // we should throw an INVALID_FILE_NAME exception before this point
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
    String result = SampleFileUtils.extractEndpoint(rawFilename, expectedEndpoint, filenameSplitByUnderscore);

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
    String result = SampleFileUtils.extractEndpoint(rawFilename, expectedEndpoint, filenameSplitByUnderscore);

    //Then
    assertEquals(expectedEndpoint, result);
  }

  @Test
  public void wrongEndpointInSampleFilename() {
    //Given
    String rawFilename = "wrong_GFF_2018-04-24T19:09:54Z.csv";
    String expectedEndpoint = "sample";
    String[] filenameSplitByUnderscore = {"wrong", "GFF", "2018-04-24T19:09:54Z.csv"};

    expectedException.expect(FWMTCommonException.class);
    expectedException.expectMessage(ExceptionCode.INVALID_FILE_NAME.getCode());

    //When
    SampleFileUtils.extractEndpoint(rawFilename, expectedEndpoint, filenameSplitByUnderscore);
  }

  @Test
  public void correctFilenameSentToWrongEndpoint() {
    //Given
    String rawFilename = "sample_GFF_2018-04-24T19:09:54Z.csv";
    String expectedEndpoint = "wrong";
    String[] filenameSplitByUnderscore = {"wrong", "GFF", "2018-04-24T19:09:54Z.csv"};

    expectedException.expect(IllegalArgumentException.class);

    //When
    SampleFileUtils.extractEndpoint(rawFilename, expectedEndpoint, filenameSplitByUnderscore);
  }

  @Test
  public void sampleFilenameFormattedIncorrectly() {
    //Given
    String rawFilename = "sample_GFF-2018-04-24T19:09:54Z.csv";
    String expectedEndpoint = "sample";
    String[] filenameSplitByUnderscore = {"sample", "GFF-2018-04-24T19:09:54Z.csv"};

    expectedException.expect(FWMTCommonException.class);
    expectedException.expectMessage(ExceptionCode.INVALID_FILE_NAME.getCode());

    //When
    SampleFileUtils.extractEndpoint(rawFilename, expectedEndpoint, filenameSplitByUnderscore);
  }

  @Test
  public void staffFilenameFormattedIncorrectly() {
    //Given
    String rawFilename = "staff__2016-04-24T19:09:54Z.csv";
    String expectedEndpoint = "staff";
    String[] filenameSplitByUnderscore = {"staff", "", "2018-04-24T19:09:54Z.csv"};

    expectedException.expect(FWMTCommonException.class);
    expectedException.expectMessage(ExceptionCode.INVALID_FILE_NAME.getCode());

    //When
    SampleFileUtils.extractEndpoint(rawFilename, expectedEndpoint, filenameSplitByUnderscore);
  }

  @Test
  public void checkCorrectFileExtension() {
    //Given
    String rawFilename = "sample_GFF_2018-04-24T19:09:54Z.csv";
    String expectedFileName = "sample_GFF_2018-04-24T19:09:54Z";
    String expectedExtension = "csv";

    //When
    String[] result = SampleFileUtils.checkFileExtension(rawFilename);

    //Then
    assertEquals(rawFilename, result[0] + "." + result[1]);
    assertEquals(expectedFileName, result[0]);
    assertEquals(expectedExtension, result[1]);
  }

  @Test
  public void incorrectFileExtension() {
    //Given
    String rawFilename = "sample_GFF_2018-04-24T19:09:54Z.jpg";

    expectedException.expect(FWMTCommonException.class);
    expectedException.expectMessage(ExceptionCode.INVALID_FILE_NAME.getCode());

    //When
    SampleFileUtils.checkFileExtension(rawFilename);
  }

  @Test
  public void getLegacySampleSurveyTypeGFF() {
    //Given
    String[] filenameSplitByUnderscore = {"sample", "GFF", "2018-04-24T19:09:54Z.csv"};
    String endpoint = "sample";

    //When
    LegacySampleSurveyType result = SampleFileUtils.getLegacySampleSurveyType(filenameSplitByUnderscore, endpoint);

    //Then
    assertEquals(GFF, result);
  }

  @Test
  public void getLegacySampleSurveyTypeLFS() {
    //Given
    String[] filenameSplitByUnderscore = {"sample", "LFS", "2018-04-24T19:09:54Z.csv"};
    String endpoint = "sample";

    //When
    LegacySampleSurveyType result = SampleFileUtils.getLegacySampleSurveyType(filenameSplitByUnderscore, endpoint);

    //Then
    assertEquals(LFS, result);
  }

  @Test
  public void unrecognizedLegacySampleSurveyType() {
    //Given
    String[] filenameSplitByUnderscore = {"sample", "TLA", "2018-04-24T19:09:54Z.csv"};
    String endpoint = "sample";

    expectedException.expect(IllegalArgumentException.class);

    //When
    SampleFileUtils.getLegacySampleSurveyType(filenameSplitByUnderscore, endpoint);
  }

  @Test
  public void getLocalDateTime() {
    //Given
    String rawFilename = "sample_GFF_2018-04-24T19:09:54Z.csv";
    String rawTimestamp = "2018-04-24T19:09:54Z";

    //When
    LocalDateTime result = SampleFileUtils.getLocalDateTime(rawFilename, rawTimestamp);

    //Then
    assertNotNull(result);
  }

  @Test
  public void noEffortForACorrectDateTimeFormat() {
    //Given
    String rawFilename = "sample_GFF_2018-04-24T19:09:54Z.csv";
    String rawTimestamp = "I am clearly not anything related to date or time";

    expectedException.expect(FWMTCommonException.class);
    expectedException.expectMessage(ExceptionCode.INVALID_FILE_NAME.getCode());

    //When
    SampleFileUtils.getLocalDateTime(rawFilename, rawTimestamp);
  }
}