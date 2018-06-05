package uk.gov.ons.fwmt.job_service.service.impl;

import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.job_service.data.file_ingest.FileIngest;
import uk.gov.ons.fwmt.job_service.exceptions.types.InvalidFileNameException;

import java.io.IOException;
import java.nio.CharBuffer;

import static org.junit.Assert.assertEquals;

public class MultipartFileIngestTest {
  private FileIngestServiceImpl fileIngestServiceImpl;

  public MultipartFileIngestTest() {
    fileIngestServiceImpl = new FileIngestServiceImpl();
  }

  public void runIngest(MultipartFile multipartFile, String content) throws IOException, InvalidFileNameException {
    FileIngest ingest = fileIngestServiceImpl.ingestSampleFile(multipartFile);
    CharBuffer charBuffer = CharBuffer.allocate(10);
    int result = ingest.getReader().read(charBuffer);
    charBuffer.flip();
    assertEquals(content.length(), result);
    assertEquals(content, charBuffer.toString());
  }

  @Test
  public void ingestValidFile() throws IOException, InvalidFileNameException {
    String content = "foo";
    MockMultipartFile multipartFile = new MockMultipartFile("file", "sample_GFF_2018-05-17T15-34-00Z.csv", "text/csv",
        content.getBytes());
    runIngest(multipartFile, content);
  }

  @Test(expected = InvalidFileNameException.class)
  public void ingestInvalidFile() throws IOException, InvalidFileNameException {
    String content = "foo";
    MockMultipartFile multipartFile = new MockMultipartFile("file", "sampl_GFF_2018-05-17T15-34-00Z.csv", "text/csv",
        content.getBytes());
    runIngest(multipartFile, content);
  }
}
