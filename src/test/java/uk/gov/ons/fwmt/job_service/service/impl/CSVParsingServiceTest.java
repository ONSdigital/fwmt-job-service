package uk.gov.ons.fwmt.job_service.service.impl;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType;
import uk.gov.ons.fwmt.job_service.rest.FieldPeriodResourceService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CSVParsingServiceTest {
  @Mock CSVRecord record;
  @Mock FieldPeriodResourceService fieldPeriodResourceService;

  @InjectMocks CSVParsingServiceImpl csvParsingService;

  @Test
  public void constructTmJobId_GFF_test() {
    // setup
    when(record.get("Quota")).thenReturn("1");
    when(record.get("AddressNo")).thenReturn("2");
    when(record.get("Stage")).thenReturn("18F");

    // run
    String tmJobId = csvParsingService.constructTmJobId(record, LegacySampleSurveyType.GFF);

    // verify
    assertEquals("1-2-18F", tmJobId);
  }

  @Test
  public void constructTmJobId_LFS_test() {
    // setup
    when(record.get("QUOTA")).thenReturn("");
    when(record.get("WEEK")).thenReturn("");
    when(record.get("W1YR")).thenReturn("");
    when(record.get("QRTR")).thenReturn("");
    when(record.get("ADDR")).thenReturn("");
    when(record.get("WAVFND")).thenReturn("");
    when(record.get("HHLD")).thenReturn("");
    when(record.get("CHKLET")).thenReturn("");
    when(record.get("FP")).thenReturn("");

    // run
    String tmJobId = csvParsingService.constructTmJobId(record, LegacySampleSurveyType.LFS);

    // verify
//    return record.get("QUOTA") + " " + record.get("WEEK") + " " + record.get("W1YR") + " " + record.get("QRTR") + " "
//        + record.get("ADDR") + " " + record.get("WAVFND") + " " + record.get("HHLD") + " " + record.get("CHKLET") +
//        " - " + record.get("FP");
    assertEquals("? ? ? ? ? ? ? ? - ?", tmJobId);
  }
}
