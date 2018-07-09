package uk.gov.ons.fwmt.job_service.rest;

import uk.gov.ons.fwmt.job_service.rest.dto.JobDto;

import java.util.Optional;

public interface JobResourceService {
    boolean existsByTmJobId(String tmJobId);
    boolean existsByTmJobIdAndLastAuthNo(final String tmJobId, final String lastAuthNo);
    Optional<JobDto> findByTmJobId(final String tmJobId);
    void createJob(final JobDto jobDto);
    void updateJob(final JobDto jobDto);

}
