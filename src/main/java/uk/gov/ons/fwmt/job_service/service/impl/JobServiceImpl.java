package uk.gov.ons.fwmt.job_service.service.impl;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.fwmt.job_service.data.dto.SampleSummaryDTO;
import uk.gov.ons.fwmt.job_service.exceptions.types.InvalidFileNameException;
import uk.gov.ons.fwmt.job_service.exceptions.types.MediaTypeNotSupportedException;
import uk.gov.ons.fwmt.job_service.rest.client.JobResourceServiceClient;
import uk.gov.ons.fwmt.job_service.service.FileIngestService;
import uk.gov.ons.fwmt.job_service.service.JobService;
import uk.gov.ons.fwmt.job_service.utils.SampleFileUtils;

@Slf4j
@Service
public class JobServiceImpl implements JobService {
  private FileIngestService fileIngestService;
  private JobResourceServiceClient jobResourceServiceClient;
  private JobProcessor jobProcessor;

  @Autowired
  public JobServiceImpl(
      FileIngestService fileIngestService,
      JobProcessor jobProcessService,
      JobResourceServiceClient jobResourceServiceClient) {
    this.fileIngestService = fileIngestService;
    this.jobResourceServiceClient = jobResourceServiceClient;
    this.jobProcessor = jobProcessService;
  }

  @Override
  public SampleSummaryDTO processSampleFile(MultipartFile multipartFile)
          throws IOException, InvalidFileNameException, MediaTypeNotSupportedException{
    jobResourceServiceClient.storeCSVFile(multipartFile);
    File regularFile = SampleFileUtils.convertToRegularFile(multipartFile);
    SampleSummaryDTO sampleSummaryDTO = fileIngestService.validateSampleFile(regularFile);
    jobProcessor.processSampleFile(regularFile); // This is an async call
    return sampleSummaryDTO;
  }


}
