package uk.gov.ons.fwmt.job_service.service;

import java.io.File;
import java.io.IOException;

import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;

import uk.gov.ons.fwmt.job_service.data.dto.SampleSummaryDTO;


public interface FileIngestService {
  SampleSummaryDTO validateSampleFile(File file) throws InvalidFileNameException, IOException;
}
