package uk.gov.ons.fwmt.job_service.rest.impl;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.ons.fwmt.job_service.ApplicationConfig;
import uk.gov.ons.fwmt.job_service.rest.JobResourceService;
import uk.gov.ons.fwmt.job_service.rest.dto.JobDto;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationConfig.class})
@Ignore
public class JobResourceServiceImplTest {

    @Autowired
    JobResourceService jobResourceService;

    @Test
    public void findByTmJobId() {
        assertEquals("1122", jobResourceService.findByTmJobId("quota_1-addressno_1-801").get().getTmJobId());
    }

    @Test
    public void createJob() {
        assertEquals(true, jobResourceService.createJob(new JobDto("quota_1-addressno_1-801", "testUser")));
    }

    @Test
    public void updateJob() {
        assertEquals(true, jobResourceService.updateJob(new JobDto("testA2", "2235")));
    }
}