package uk.gov.ons.fwmt.job_service.data.csv_parser;

import java.util.Iterator;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;

@Slf4j
public abstract class CSVIterator<T> implements Iterator<CSVParseResult<T>> {
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
