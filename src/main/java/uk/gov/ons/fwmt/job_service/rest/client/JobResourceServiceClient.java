package uk.gov.ons.fwmt.job_service.rest.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import org.springframework.web.client.HttpClientErrorException;

import uk.gov.ons.fwmt.job_service.rest.client.dto.JobDto;

public interface JobResourceServiceClient {
  
    boolean existsByTmJobId(String tmJobId);
    
    boolean existsByTmJobIdAndLastAuthNo(final String tmJobId, final String lastAuthNo);
    
    Optional<JobDto> findByTmJobId(final String tmJobId);
    
    boolean createJob(final JobDto jobDto);
    
    boolean updateJob(final JobDto jobDto);
    
    void storeCSVFile(final File file, boolean valid) throws HttpClientErrorException, FileNotFoundException, IOException;


}
