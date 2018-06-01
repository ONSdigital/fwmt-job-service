package uk.gov.ons.fwmt.job_service.rest.impl;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.ons.fwmt.job_service.ApplicationConfig;
import uk.gov.ons.fwmt.job_service.rest.FieldPeriodResourceService;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationConfig.class})
@Ignore
public class FieldPeriodResourceServiceImplTest {

    @Autowired
    FieldPeriodResourceService fieldPeriodResourceService;

    @Test
    public void findByFieldPeriod() {
        assertEquals("94A", fieldPeriodResourceService.findByFieldPeriod("94A").get().getFieldPeriod());
    }
}