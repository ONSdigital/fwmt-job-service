package uk.gov.ons.fwmt.job_service.rest;

import uk.gov.ons.fwmt.job_service.rest.dto.JobDto;

import java.io.File;
import java.util.Optional;

public interface JobResourceService {
    boolean existsByTmJobId(String tmJobId);
    boolean existsByTmJobIdAndLastAuthNo(final String tmJobId, final String lastAuthNo);
    Optional<JobDto> findByTmJobId(final String tmJobId);
    boolean createJob(final JobDto jobDto);
    boolean updateJob(final JobDto jobDto);

  void sendCSV(final File file, final boolean valid);


}
