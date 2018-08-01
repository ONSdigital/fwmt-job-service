package uk.gov.ons.fwmt.job_service.data.csv_parser.legacysample;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

import uk.gov.ons.fwmt.job_service.data.annotation.CSVColumn;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;

public final class LegacySampleAnnotationProcessor {

  public static <T> void process(T instance, CSVRecord record, String pivot)
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

}
