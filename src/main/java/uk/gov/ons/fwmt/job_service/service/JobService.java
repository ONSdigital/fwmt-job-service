package uk.gov.ons.fwmt.job_service.service;

import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.job_service.data.dto.SampleSummaryDTO;

import java.io.IOException;

public interface JobService {
  SampleSummaryDTO processSampleFile(MultipartFile file) throws IOException;
}
