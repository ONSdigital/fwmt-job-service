package uk.gov.ons.fwmt.job_service.service;

import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacyStaffIngest;

import java.util.List;

public interface LegacyStaffPublishService {
  void publishStaff(List<LegacyStaffIngest> staff);
}
