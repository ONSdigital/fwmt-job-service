package uk.gov.ons.fwmt.job_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.job_service.entity.JobEntity;
import uk.gov.ons.fwmt.job_service.rest.dto.JobDto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

@Repository
public interface JobRepo extends JpaRepository<JobEntity, Long> {
  boolean existsByJobId(String jobId);
  boolean existsByJobIdAndLastAuthNo(final String jobId, final String lastAuthNo);
  Optional<JobEntity> findByJobId(final String jobId);

}
