package uk.gov.ons.fwmt.job_service.service;

import uk.gov.ons.fwmt.job_service.data.file_ingest.FileIngest;

import java.io.File;
import java.io.IOException;

public interface FileIngestService {
  FileIngest ingestSampleFile(File file) throws IOException;
}
