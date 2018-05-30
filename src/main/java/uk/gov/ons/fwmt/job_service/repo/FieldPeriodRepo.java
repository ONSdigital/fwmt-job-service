package uk.gov.ons.fwmt.job_service.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.gov.ons.fwmt.job_service.entity.FieldPeriod;

@Repository
public interface FieldPeriodRepo extends CrudRepository<FieldPeriod, Long> {
  boolean existsByFieldPeriod(String fieldPeriod);
  FieldPeriod findByFieldPeriod(String fieldPeriod);
}