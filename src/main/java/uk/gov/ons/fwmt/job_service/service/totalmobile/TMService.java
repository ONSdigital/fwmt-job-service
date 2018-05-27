package uk.gov.ons.fwmt.job_service.service.totalmobile;

public interface TMService {
  <I, O> O send(I message);
}
