package uk.gov.ons.fwmt.job_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.ons.fwmt.job_service.entity.FieldPeriodEntity;

import java.util.Optional;

@Repository
public interface FieldPeriodRepo extends JpaRepository<FieldPeriodEntity, Long> {
  Optional<FieldPeriodEntity> findByFieldPeriod(String fieldPeriod);
}

