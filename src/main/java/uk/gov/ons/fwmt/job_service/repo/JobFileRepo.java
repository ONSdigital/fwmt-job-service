package uk.gov.ons.fwmt.job_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.job_service.entity.JobFileEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public interface JobFileRepo extends JpaRepository<JobFileEntity, Long> {
  Optional<JobFileEntity> findByfilename(String filename);
}
