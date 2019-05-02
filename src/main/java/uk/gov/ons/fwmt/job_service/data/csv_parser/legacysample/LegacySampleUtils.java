package uk.gov.ons.fwmt.job_service.data.csv_parser.legacysample;

import org.apache.commons.csv.CSVRecord;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleLFSDataIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType;
import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service.rest.client.FieldPeriodResourceServiceClient;
import uk.gov.ons.fwmt.job_service.rest.client.dto.FieldPeriodDto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

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

  public static FieldPeriodDto convertToFieldPeriodDate(String stage,
      FieldPeriodResourceServiceClient fieldPeriodResourceServiceClient) throws FWMTCommonException {
    final Optional<FieldPeriodDto> existsByFieldPeriod = fieldPeriodResourceServiceClient.findByFieldPeriod(stage);
    if (existsByFieldPeriod.isPresent()) {
      final FieldPeriodDto fieldPeriod = existsByFieldPeriod.get();
      return fieldPeriod;
    } else {
      throw FWMTCommonException.makeUnknownFieldPeriodException(stage);
    }
  }

  public static LegacySampleLFSDataIngest checkSetLookingForWorkIndicator (LegacySampleIngest instance, CSVRecord record) throws FWMTCommonException{
    String workIndicator;
    String jbaway;
    String ownbus;
    String relbus;
    String look4;
    String difJob;

    LegacySampleLFSDataIngest lfs = instance.getLfsData();
    Class lfsClass =lfs.getClass();

    Method workIndicatorMethod;
    Method look4WorkMethod;

    for (int i = 1; i <= 16; i++) {

      workIndicator = record.get("QINDIV_" + i + "_WRKING");
      jbaway = record.get("QINDIV_" + i + "_JBAWAY");
      ownbus = record.get("QINDIV_" + i + "_OWNBUS");
      relbus = record.get("QINDIV_" + i + "_RELBUS");
      look4 = record.get("QINDIV_" + i + "_LOOK4");
      difJob = record.get("QINDIV_" + i + "_DIFJOB");

      try {
        workIndicatorMethod = lfsClass.getDeclaredMethod("setRespondentWorkIndicator" + i, String.class);
        look4WorkMethod = lfsClass.getDeclaredMethod("setRespondentLookingForWork" + i, String.class);

        if ("1".equals(workIndicator) || "1".equals(jbaway) || "1".equals(ownbus) || "1".equals(relbus)) {
          workIndicatorMethod.invoke(lfs, "W");
        } else {
          workIndicatorMethod.invoke(lfs,"N");
        }

        if ("1".equals(look4) || "1".equals(difJob)) {
          look4WorkMethod.invoke(lfs,"L");
        } else {
          look4WorkMethod.invoke(lfs,"N");
        }
      }
       catch(IllegalAccessException|NoSuchMethodException|InvocationTargetException e){
        throw new FWMTCommonException(ExceptionCode.CSV_OTHER,"Error within job indicator", e);
      }
    }
    return lfs;
  }
}
