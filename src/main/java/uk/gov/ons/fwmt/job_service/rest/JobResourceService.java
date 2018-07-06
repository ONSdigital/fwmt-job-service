package uk.gov.ons.fwmt.job_service.rest;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.job_service.rest.dto.JobDto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public interface JobResourceService {
    boolean existsByTmJobId(String tmJobId);
    boolean existsByTmJobIdAndLastAuthNo(final String tmJobId, final String lastAuthNo);
    Optional<JobDto> findByTmJobId(final String tmJobId);
    boolean createJob(final JobDto jobDto);
    boolean updateJob(final JobDto jobDto);
    boolean sendCSV(final MultipartFile file) throws HttpClientErrorException, FileNotFoundException, IOException;


}
