package uk.gov.ons.fwmt.job_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.ons.fwmt.job_service.entity.UserEntity;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {
  UserEntity findByAuthNo(String authNo);

  UserEntity findByAlternateAuthNo(String authNo);
}
