package uk.gov.ons.fwmt.job_service.data.csv_parser.legacysample;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import uk.gov.ons.fwmt.job_service.data.csv_parser.CSVIterator;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleGFFDataIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleLFSDataIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service.rest.client.FieldPeriodResourceServiceClient;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

public class LegacySampleIterator extends CSVIterator<LegacySampleIngest> {
  private LegacySampleSurveyType legacySampleSurveyType;

  private FieldPeriodResourceServiceClient fieldPeriodResourceServiceClient;

  public LegacySampleIterator(CSVParser parser, LegacySampleSurveyType legacySampleSurveyType,
      FieldPeriodResourceServiceClient fieldPeriodResourceServiceClient) {
    super(parser);
    this.legacySampleSurveyType = legacySampleSurveyType;
    this.fieldPeriodResourceServiceClient = fieldPeriodResourceServiceClient;
  }

  @Override
  public LegacySampleIngest ingest(CSVRecord record) throws FWMTCommonException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    // handle fields specific to a survey type
    LegacySampleIngest instance = new LegacySampleIngest();
    switch (legacySampleSurveyType) {
    case LFS:
      parseLegacySampleLFSData(instance, record);
      break;
    case GFF:
      parseLegacySampleGFFData(instance, record);
      break;
    default:
      throw new IllegalArgumentException("Unknown survey type");
    }
    parseLegacySampleCommonData(instance, record);
    return instance;
  }

  protected void parseLegacySampleCommonData(LegacySampleIngest instance, CSVRecord record) {
    // derive the TM job id
    instance.setTmJobId(LegacySampleUtils.constructTmJobId(record, legacySampleSurveyType).trim());
    // derive the coordinates, if we were given a non-null non-empty grid ref
    if (instance.getOsGridRef() != null && instance.getOsGridRef().length() > 0) {
      String[] osGridRefSplit = instance.getOsGridRef().split(",", 2);
      if (osGridRefSplit.length != 2) {
        throw FWMTCommonException
            .makeCsvInvalidFieldException("OSGridRef", "Did not match the expected format of 'X,Y'");
      }
      instance.setGeoX(Float.parseFloat(osGridRefSplit[0]));
      instance.setGeoY(Float.parseFloat(osGridRefSplit[1]));
    }
  }

  protected void parseLegacySampleGFFData(LegacySampleIngest instance, CSVRecord record) throws FWMTCommonException {
    // set normal fields
    LegacySampleAnnotationProcessor.process(instance, record, "GFF");
    // set derived due date
    LocalDate date = LegacySampleUtils.convertToFieldPeriodDate(instance.getStage(), fieldPeriodResourceServiceClient);
    instance.setDueDate(date);
    instance.setCalculatedDueDate(String.valueOf(date));
    // set survey type and extra data
    instance.setLegacySampleSurveyType(LegacySampleSurveyType.GFF);
    instance.setGffData(new LegacySampleGFFDataIngest());
    instance.setLfsData(null);
    LegacySampleAnnotationProcessor.process(instance.getGffData(), record, null);
  }

  protected void parseLegacySampleLFSData(LegacySampleIngest instance, CSVRecord record) throws FWMTCommonException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // set normal fields
    LegacySampleAnnotationProcessor.process(instance, record, "LFS");
    // set derived due date
    LocalDate date = LegacySampleUtils.convertToFieldPeriodDate(instance.getStage(), fieldPeriodResourceServiceClient);
    instance.setDueDate(date);
    instance.setCalculatedDueDate(String.valueOf(date));
    // set if the record is looking for work

    // set survey type and extra data
    instance.setLegacySampleSurveyType(LegacySampleSurveyType.LFS);
    instance.setGffData(null);
    instance.setLfsData(new LegacySampleLFSDataIngest());

    try {
      instance.setLfsData(LegacySampleUtils.checkSetLookingForWorkIndicator(instance, record));
    } catch (NoSuchMethodException|IllegalAccessException|InvocationTargetException e) {
      throw e;
    }

    LegacySampleAnnotationProcessor.process(instance.getLfsData(), record, null);
  }

}
