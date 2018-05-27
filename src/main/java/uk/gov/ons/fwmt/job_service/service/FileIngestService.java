package uk.gov.ons.fwmt.job_service.service;

import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.job_service.data.file_ingest.FileIngest;
import uk.gov.ons.fwmt.job_service.error.InvalidFileNameException;
import uk.gov.ons.fwmt.job_service.error.MediaTypeNotSupportedException;

import java.io.IOException;

public interface FileIngestService {
//  SampleSummaryDTO ingestSampleFile(MultipartFile file)
//      throws IOException, InvalidFileNameException, MediaTypeNotSupportedException;
//  StaffSummaryDTO ingestStaffFile(MultipartFile file)
//      throws IOException, InvalidFileNameException, MediaTypeNotSupportedException;
  FileIngest ingestSampleFile(MultipartFile file)
      throws IOException, InvalidFileNameException, MediaTypeNotSupportedException;
  FileIngest ingestStaffFile(MultipartFile file)
      throws IOException, InvalidFileNameException, MediaTypeNotSupportedException;
}
