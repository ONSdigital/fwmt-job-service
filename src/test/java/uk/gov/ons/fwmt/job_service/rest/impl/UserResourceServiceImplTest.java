package uk.gov.ons.fwmt.job_service.rest.impl;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.ons.fwmt.job_service.ApplicationConfig;
import uk.gov.ons.fwmt.job_service.rest.UserResourceService;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationConfig.class})
@Ignore
public class UserResourceServiceImplTest {

    @Autowired
    UserResourceService userResourceService;

    @Test
    public void findByAuthNo() {
        assertEquals("1111", userResourceService.findByAuthNo("1111").getAuthNo());
    }

    @Test
    public void findByAlternateAuthNo() {
        assertEquals("1111", userResourceService.findByAlternateAuthNo("2222").getAuthNo());
    }

    @Test
    @Ignore
    public void existsByAuthNoAndActive() {
    }

    @Test
    @Ignore
    public void existsByAlternateAuthNoAndActive() {
    }
}