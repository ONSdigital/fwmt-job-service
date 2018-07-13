package uk.gov.ons.fwmt.job_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.ons.fwmt.job_service.entity.JobEntity;

@Repository
public interface JobRepo extends JpaRepository<JobEntity, Long> {
  JobEntity findByTmJobId(String tmJobId);
}
