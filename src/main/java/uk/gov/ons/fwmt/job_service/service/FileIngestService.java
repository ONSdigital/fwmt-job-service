package uk.gov.ons.fwmt.job_service.service;

import java.io.File;
import java.io.IOException;

import uk.gov.ons.fwmt.job_service.data.dto.SampleSummaryDTO;
import uk.gov.ons.fwmt.job_service.exceptions.types.InvalidFileNameException;
import uk.gov.ons.fwmt.job_service.exceptions.types.MediaTypeNotSupportedException;

public interface FileIngestService {
  SampleSummaryDTO validateSampleFile(File file) throws InvalidFileNameException, MediaTypeNotSupportedException, IOException;
}
