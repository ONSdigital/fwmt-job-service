package uk.gov.ons.fwmt.job_service.service;

import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.job_service.exceptions.types.InvalidFileNameException;
import uk.gov.ons.fwmt.job_service.exceptions.types.MediaTypeNotSupportedException;

import java.io.IOException;

public interface JobProcessService {
    void processSampleFile (MultipartFile file)
            throws IOException, InvalidFileNameException, MediaTypeNotSupportedException;
}
