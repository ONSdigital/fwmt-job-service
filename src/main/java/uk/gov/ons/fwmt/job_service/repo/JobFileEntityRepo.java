package uk.gov.ons.fwmt.job_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.ons.fwmt.job_service.entity.JobFileEntity;

public interface JobFileEntityRepo extends JpaRepository<JobFileEntity, Long> {
  JobFileEntity findByfilename(String filename);
}
