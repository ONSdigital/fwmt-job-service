package uk.gov.ons.fwmt.job_service.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.fwmt.job_service.data.annotation.CSVColumn;
import uk.gov.ons.fwmt.job_service.data.csv_parser.CSVParseResult;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleGFFDataIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleLFSDataIngest;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType;
import uk.gov.ons.fwmt.job_service.exceptions.ExceptionCode;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service.rest.FieldPeriodResourceService;
import uk.gov.ons.fwmt.job_service.rest.dto.FieldPeriodDto;
import uk.gov.ons.fwmt.job_service.service.CSVParsingService;

import java.io.IOException;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

@Slf4j
@Service
public class CSVParsingServiceImpl implements CSVParsingService {

  private FieldPeriodResourceService fieldPeriodResourceService;

  @Autowired
  public CSVParsingServiceImpl(FieldPeriodResourceService fieldPeriodResourceService) {
    this.fieldPeriodResourceService = fieldPeriodResourceService;
  }

  /**
   * Read the CSVColumn annotations on the class T and set Java bean properties
   * from the columns of a CSV record
   *
   * @param instance An instance of the class that will be mutated
   * @param record   A row of a CSV file
   * @param pivot    A string used to determine which field name should be used, in
   *                 events where there are many options
   * @param <T>      A class with fields annotated with CSVColumn
   */

  protected <T> void setFromCSVColumnAnnotations(T instance, CSVRecord record, String pivot)
      throws FWMTCommonException {
    Class<?> tClass = instance.getClass();
    PropertyAccessor accessor = PropertyAccessorFactory.forBeanPropertyAccess(instance);
    for (Field field : tClass.getDeclaredFields()) {
      Optional<Annotation> annotation = Arrays.stream(field.getDeclaredAnnotations())
          .filter(an -> an.annotationType() == CSVColumn.class)
          .findFirst();
      if (annotation.isPresent()) {
        CSVColumn csvColumn = (CSVColumn) annotation.get();
        String columnName;
        if (!csvColumn.value().isEmpty()) {
          columnName = csvColumn.value();
        } else if (csvColumn.values().length > 0) {
          Optional<CSVColumn.Mapping> mapping = Arrays.stream(csvColumn.values())
              .filter(m -> m.when().equals(pivot))
              .findFirst();
          if (mapping.isPresent()) {
            columnName = mapping.get().value();
          } else {
            throw FWMTCommonException.makeCsvOtherException("Given pivot does not occur in the CSVColumn annotation");
          }
        } else {
          throw FWMTCommonException.makeCsvOtherException("CSVColumn lacked a 'value' or 'values' field");
        }
        // if it's mandatory or set, try
        // if it's ignored, don't try
        if (!csvColumn.ignored()) {
          if (record.isSet(columnName)) {
            accessor.setPropertyValue(field.getName(), record.get(columnName));
          } else {
            if (csvColumn.mandatory()) {
              throw FWMTCommonException.makeCsvMissingColumnException(columnName);
            }
          }
        }
      }
    }
  }

  /**
   * Create a unique Job ID that can be used by TotalMobile from existing fields
   * within the CSV The method varies on the type of survey
   */
  protected String constructTmJobId(CSVRecord record, LegacySampleSurveyType surveyType) {
    switch (surveyType) {
    case GFF:
      // quota '-' addr '-' stage
      return record.get("Quota") + "-" + record.get("AddressNo") + "-" + record.get("Stage");
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

  public LocalDate convertToLFSDate(String fp) throws FWMTCommonException {
    final Optional<FieldPeriodDto> existsByFieldPeriod = fieldPeriodResourceService.findByFieldPeriod(fp);
    if (existsByFieldPeriod.isPresent()) {
      final FieldPeriodDto fieldPeriod = existsByFieldPeriod.get();
      return fieldPeriod.getEndDate();
    } else {
      throw FWMTCommonException.makeUnknownFieldPeriodException(fp);
    }
  }

  // TODO replace with a function that calculates the GFF date deterministically
  // Currently, it falls back on checking the database
  public LocalDate convertToGFFDate(String stage) {
    return convertToLFSDate(stage);
  }

  private static CSVFormat getCSVFormat() {
    return CSVFormat.DEFAULT.withHeader();
  }

  public void parseLegacySampleGFFData(LegacySampleIngest instance, CSVRecord record) throws FWMTCommonException {
    // set normal fields
    setFromCSVColumnAnnotations(instance, record, "GFF");
    // set derived due date
    LocalDate date = convertToGFFDate(instance.getStage());
    instance.setDueDate(date);
    instance.setCalculatedDueDate(String.valueOf(date));
    // set survey type and extra data
    instance.setLegacySampleSurveyType(LegacySampleSurveyType.GFF);
    instance.setGffData(new LegacySampleGFFDataIngest());
    instance.setLfsData(null);
    setFromCSVColumnAnnotations(instance.getGffData(), record, null);
  }

  public void parseLegacySampleLFSData(LegacySampleIngest instance, CSVRecord record) throws FWMTCommonException {
    // set normal fields
    setFromCSVColumnAnnotations(instance, record, "LFS");
    // set derived due date
    LocalDate date = convertToLFSDate(instance.getStage());
    instance.setDueDate(date);
    instance.setCalculatedDueDate(String.valueOf(date));
    // set survey type and extra data
    instance.setLegacySampleSurveyType(LegacySampleSurveyType.LFS);
    instance.setGffData(null);
    instance.setLfsData(new LegacySampleLFSDataIngest());
    setFromCSVColumnAnnotations(instance.getLfsData(), record, null);
  }

  // TODO possibly simplify this horribleness?
  @Override
  public Iterator<CSVParseResult<LegacySampleIngest>> parseLegacySample(Reader reader,
      LegacySampleSurveyType legacySampleSurveyType) throws IOException {
    log.debug("Began parsing file: surveyType={}", legacySampleSurveyType);
    CSVParser parser = getCSVFormat().parse(reader);
    return new CSVIterator<LegacySampleIngest>(parser) {
      @Override
      public LegacySampleIngest ingest(CSVRecord record) throws FWMTCommonException {
        LegacySampleIngest instance = new LegacySampleIngest();
        // handle fields specific to a survey type
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
        // derive the TM job id
        instance.setTmJobId(constructTmJobId(record, legacySampleSurveyType));
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
        return instance;
      }
    };
  }

  private abstract class CSVIterator<T> implements Iterator<CSVParseResult<T>> {
    private final Iterator<CSVRecord> iter;
    private int rowNumber;

    protected CSVIterator(CSVParser parser) {
      this.rowNumber = 0;
      this.iter = parser.iterator();
    }

    abstract public T ingest(CSVRecord record) throws FWMTCommonException;

    @Override
    public boolean hasNext() {
      return iter.hasNext();
    }

    @Override
    public CSVParseResult<T> next() {
      rowNumber++;
      CSVRecord record = iter.next();
      if (record == null) {
        return null;
      }
      try {
        T result = ingest(record);
        return CSVParseResult.withResult(rowNumber, result);
      } catch (Exception e) {
        log.error("Error in CSV parser", e);
        return CSVParseResult.withError(rowNumber, e.toString());
      }
    }
  }
}
