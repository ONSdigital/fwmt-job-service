package uk.gov.ons.fwmt.job_service.service.impl;

import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.job_service.exceptions.types.InvalidFileNameException;
import uk.gov.ons.fwmt.job_service.exceptions.types.MediaTypeNotSupportedException;

import java.io.IOException;
import java.io.InputStream;

public class FileIngestServiceImplTest {

  private FileIngestServiceImpl fileIngestService = new FileIngestServiceImpl();

  @Test
  public void verifyGFFSampleCSVFilename() throws InvalidFileNameException {
    //Given
    String expectedRawFilename = "sample_GFF_2018-04-24T19-09-54Z.csv";
    String expectedEndpoint = "sample";

    //When
    fileIngestService.verifyCSVFilename(expectedRawFilename,expectedEndpoint);
  }

  @Test
  public void verifyLFSSampleCSVFilename() throws InvalidFileNameException {
    //Given
    String expectedRawFilename = "sample_LFS_2018-04-24T19-09-54Z.csv";
    String expectedEndpoint = "sample";

    //When
    fileIngestService.verifyCSVFilename(expectedRawFilename,expectedEndpoint);
  }

  @Test(expected=InvalidFileNameException.class)
  public void invalidFileExtension() throws InvalidFileNameException {
    //Given
    String expectedRawFilename = "sample_GFF_2018-04-24T19-09-54Z.txt";
    String expectedEndpoint = "wrong";

    //When
    fileIngestService.verifyCSVFilename(expectedRawFilename,expectedEndpoint);
  }

  @Test(expected=InvalidFileNameException.class)
  public void invalidEndpoint() throws InvalidFileNameException {
    //Given
    String expectedRawFilename = "sample_GFF_2018-04-24T19-09-54Z.csv";
    String expectedEndpoint = "wrongEndpoint";

    //When
    fileIngestService.verifyCSVFilename(expectedRawFilename,expectedEndpoint);
  }

  @Test(expected=InvalidFileNameException.class)
  public void sampleFileOnlyHasOneUnderscore() throws InvalidFileNameException {
    //Given
    String expectedRawFilename = "sample_GFF-2018-04-24T19-09-54Z.csv";
    String expectedEndpoint = "sample";

    //When
    fileIngestService.verifyCSVFilename(expectedRawFilename,expectedEndpoint);
  }

  @Test(expected=InvalidFileNameException.class)
  public void staffFileHasTwoUnderscores() throws InvalidFileNameException {
    //Given
    String expectedRawFilename = "staff_GFF_2018-04-24T19-09-54Z.csv";
    String expectedEndpoint = "staff";

    //When
    fileIngestService.verifyCSVFilename(expectedRawFilename,expectedEndpoint);
  }

  @Test(expected=IllegalArgumentException.class)
  public void unrecognisedTLA() throws InvalidFileNameException {
    //Given
    String expectedRawFilename = "sample_TLA_2018-04-24T19-09-54Z.csv";
    String expectedEndpoint = "sample";

    //When
    fileIngestService.verifyCSVFilename(expectedRawFilename,expectedEndpoint);
  }
  // TODO timestamp tests

  @Test
  public void ingestSampleFile() throws IOException, InvalidFileNameException, MediaTypeNotSupportedException {
    //Given
    MultipartFile testFile = new MockMultipartFile("sampleFile","sample_GFF_2018-04-24T19-09-54Z.csv","text/csv", (InputStream) null);

    //When
    fileIngestService.ingestSampleFile(testFile);
  }
}