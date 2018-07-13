package uk.gov.ons.fwmt.job_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.ons.fwmt.job_service.entity.FieldPeriodEntity;

@Repository
public interface FieldPeriodRepo extends JpaRepository<FieldPeriodEntity, Long> {
  FieldPeriodEntity findByFieldPeriod(String fieldPeriod);
}

