package uk.gov.ons.fwmt.job_service.rest.client;

import java.io.File;
import java.util.Optional;

import uk.gov.ons.fwmt.job_service.rest.client.dto.JobDto;

public interface JobResourceServiceClient {
  boolean existsByTmJobId(String tmJobId);

  boolean existsByTmJobIdAndLastAuthNo(final String tmJobId, final String lastAuthNo);

  Optional<JobDto> findByTmJobId(final String tmJobId);

  void createJob(final JobDto jobDto);

  void updateJob(final JobDto jobDto);

  void storeCSVFile(final File file, final boolean valid);
}
