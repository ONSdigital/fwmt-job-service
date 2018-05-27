package uk.gov.ons.fwmt.job_service.service;

public interface TMService {
  <I, O> O send(I message);
}
