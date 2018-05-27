package uk.gov.ons.fwmt.job_service.integration_tests.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.ons.fwmt.job_service.ApplicationConfig;
import uk.gov.ons.fwmt.job_service.entity.TMJobEntity;
import uk.gov.ons.fwmt.job_service.entity.TMUserEntity;
import uk.gov.ons.fwmt.job_service.repo.TMJobRepo;
import uk.gov.ons.fwmt.job_service.repo.TMUserRepo;

import javax.transaction.Transactional;

import static junit.framework.TestCase.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationConfig.class})
public class RepoIT {
  @Autowired private TMUserRepo tmUserRepo;
  @Autowired private TMJobRepo tmJobRepo;

  @Test
  @Transactional
  public void storeUserInDb() {
    TMUserEntity tmUserEntity = new TMUserEntity("9999", "testuser", false, null);
    try {
      tmUserEntity.getAuthNo();
      tmUserEntity.getTmUsername();
      tmUserEntity.setActive(true);
      tmUserRepo.save(tmUserEntity);
    } catch (Exception e){
      fail("error");
    } finally {
      tmUserRepo.delete(tmUserEntity);
    }
  }

  @Test
  @Transactional
  public void storeJobInDb() {
    TMJobEntity tmJobEntity = new TMJobEntity("testjob", "2222");
    try {
      tmJobEntity.getTmJobId();
      tmJobRepo.save(tmJobEntity);
    } catch (Exception e) {
      fail("Error");
    } finally {
      tmJobRepo.delete(tmJobEntity);
    }
  }
}
