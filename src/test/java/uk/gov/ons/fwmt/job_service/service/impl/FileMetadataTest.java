package uk.gov.ons.fwmt.job_service.service.impl;

import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import uk.gov.ons.fwmt.job_service.error.InvalidFileNameException;
import uk.gov.ons.fwmt.job_service.error.MediaTypeNotSupportedException;

import java.io.IOException;

public class FileMetadataTest {
  private static String VALID_FILE_NAME = "sample_GFF_2018-04-24T19:09:54Z.csv";

  private FileIngestServiceImpl fileIngestServiceImpl;

  public FileMetadataTest() {
    this.fileIngestServiceImpl = new FileIngestServiceImpl();
  }

  @Test
  public void testValidMetadata() throws IOException, InvalidFileNameException, MediaTypeNotSupportedException {
    MockMultipartFile mockMultipartFile = new MockMultipartFile(VALID_FILE_NAME, VALID_FILE_NAME, "text/csv",
        new byte[] {});
    fileIngestServiceImpl.verifyCSVFileMetadata(mockMultipartFile);
  }

  @Test(expected = MediaTypeNotSupportedException.class)
  public void testInvalidMetadata() throws IOException, InvalidFileNameException, MediaTypeNotSupportedException {
    MockMultipartFile mockMultipartFile = new MockMultipartFile(VALID_FILE_NAME, VALID_FILE_NAME, "form-data/multipart",
        new byte[] {});
    fileIngestServiceImpl.verifyCSVFileMetadata(mockMultipartFile);
  }
}
