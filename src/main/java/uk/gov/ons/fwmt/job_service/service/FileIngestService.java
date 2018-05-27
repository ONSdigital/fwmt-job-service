package uk.gov.ons.fwmt.job_service.service;

import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.job_service.data.file_ingest.FileIngest;
import uk.gov.ons.fwmt.job_service.error.InvalidFileNameException;
import uk.gov.ons.fwmt.job_service.error.MediaTypeNotSupportedException;

import java.io.IOException;

public interface FileIngestService {
  FileIngest ingestSampleFile(MultipartFile file)
      throws IOException, InvalidFileNameException, MediaTypeNotSupportedException;
}
