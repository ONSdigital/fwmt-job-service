package uk.gov.ons.fwmt.job_service.data.csv_parser.legacysample;

import java.time.LocalDate;
import java.util.Optional;

import org.apache.commons.csv.CSVRecord;

import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service.rest.client.FieldPeriodResourceServiceClient;
import uk.gov.ons.fwmt.job_service.rest.client.dto.FieldPeriodDto;

public final class LegacySampleUtils {
  private LegacySampleUtils() {}
  
  /**
   * Create a unique Job ID that can be used by TotalMobile from existing fields
   * within the CSV The method varies on the type of survey
   */
  public static String constructTmJobId(CSVRecord record, LegacySampleSurveyType surveyType) {
    switch (surveyType) {
    case GFF:
      // quota '-' addr '-' stage
      return record.get("TLA") + "-" + record.get("Quota") + "-" + record.get("AddressNo") + "-" + record.get("Stage");
    case LFS:
      // quota week w1yr qrtr addr wavfnd hhld chklet
      if (!record.get("Issue_No").equals("1")) {
        return record.get("QUOTA") + " " + record.get("WEEK") + " " + record.get("W1YR") + " " + record.get("QRTR")
            + " "
            + record.get("ADDR")
            + " " + record.get("WAVFND") + " " + record.get("HHLD") + " " + record.get("CHKLET") + " - "
            + record.get("FP") + " [R" + record.get("Issue_No") + "]";
      } else {
        return record.get("QUOTA") + " " + record.get("WEEK") + " " + record.get("W1YR") + " " + record.get("QRTR")
            + " "
            + record.get("ADDR")
            + " " + record.get("WAVFND") + " " + record.get("HHLD") + " " + record.get("CHKLET") + " - "
            + record.get("FP");
      }
    default:
      throw new IllegalArgumentException("Invalid survey type");
    }
  }

  
  public static LocalDate convertToFieldPeriodDate(String stage, FieldPeriodResourceServiceClient fieldPeriodResourceServiceClient) throws FWMTCommonException{
    final Optional<FieldPeriodDto> existsByFieldperiod = fieldPeriodResourceServiceClient.findByFieldPeriod(stage);
    if (existsByFieldperiod.isPresent()) {
      final FieldPeriodDto fieldPeriod = existsByFieldperiod.get();
      return fieldPeriod.getEndDate();
    } else {
      throw FWMTCommonException.makeUnknownFieldPeriodException(stage);
    }
  }
}
