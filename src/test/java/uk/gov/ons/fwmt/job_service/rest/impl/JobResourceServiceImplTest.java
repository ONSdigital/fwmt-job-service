package uk.gov.ons.fwmt.job_service.rest.impl;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import uk.gov.ons.fwmt.job_service.rest.JobResourceService;
import uk.gov.ons.fwmt.job_service.rest.dto.JobDto;

import static org.junit.Assert.*;

@Ignore
public class JobResourceServiceImplTest {

    @InjectMocks private JobResourceService jobResourceService;

    @Test
    public void findByTmJobId() {
        assertEquals("1122", jobResourceService.findByTmJobId("quota_1-addressno_1-801").get().getTmJobId());
    }

    @Test
    public void createJob() {
      assertTrue(jobResourceService.createJob(new JobDto("quota_1-addressno_1-801", "testUser")));
    }

    @Test
    public void updateJob() {
        assertEquals(true, jobResourceService.updateJob(new JobDto("testA2", "2235")));
    }
}