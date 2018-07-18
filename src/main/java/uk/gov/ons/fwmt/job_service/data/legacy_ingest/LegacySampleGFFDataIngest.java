package uk.gov.ons.fwmt.job_service.data.legacy_ingest;

import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.ons.fwmt.job_service.data.annotation.CSVColumn;
import uk.gov.ons.fwmt.job_service.data.annotation.JobAdditionalProperty;

@Data
@NoArgsConstructor
public class LegacySampleGFFDataIngest {
  // TODO should this be 'name'?
  @CSVColumn("Name")
  @JobAdditionalProperty("respondentName")
  private String name;

  @CSVColumn("LAUA")
  private String laua;

  @CSVColumn("LAUA_Name")
  private String lauaName;

  @CSVColumn("SubSample")
  @JobAdditionalProperty("splitSampleType")
  private String subSample;

  @CSVColumn("Rand")
  @JobAdditionalProperty("gridSelection")
  private String rand;

  @CSVColumn("ADULT2")
  @JobAdditionalProperty("kishGridAdult2")
  private String adult2;

  @CSVColumn("ADULT3")
  @JobAdditionalProperty("kishGridAdult3")
  private String adult3;

  @CSVColumn("ADULT4")
  @JobAdditionalProperty("kishGridAdult4")
  private String adult4;

  @CSVColumn("ADULT5")
  @JobAdditionalProperty("kishGridAdult5")
  private String adult5;

  @CSVColumn("ADULT6")
  @JobAdditionalProperty("kishGridAdult6")
  private String adult6;

  @CSVColumn("ADULT7")
  @JobAdditionalProperty("kishGridAdult7")
  private String adult7;

  @CSVColumn("ADULT8")
  @JobAdditionalProperty("kishGridAdult8")
  private String adult8;

  @CSVColumn("ADULT9")
  @JobAdditionalProperty("kishGridAdult9")
  private String adult9;

  @CSVColumn("ADULT10")
  @JobAdditionalProperty("kishGridAdult10")
  private String adult10;

  @CSVColumn("ADULT11")
  @JobAdditionalProperty("kishGridAdult11")
  private String adult11;

  @CSVColumn("ADULT12")
  @JobAdditionalProperty("kishGridAdult12")
  private String adult12;

  @CSVColumn("ADULT13")
  @JobAdditionalProperty("kishGridAdult13")
  private String adult13;

  @CSVColumn("ADULT14")
  @JobAdditionalProperty("kishGridAdult14")
  private String adult14;

  @CSVColumn(value = "Ward", ignored = true)
  private String ward;

  @CSVColumn(value = "Ward_Name", ignored = true)
  private String wardName;

  @CSVColumn(value = "MO", ignored = true)
  private String mo;

  @CSVColumn(value = "DivAddInd", ignored = true)
  private String divAddInd;

  @CSVColumn(value = "GFFMU", ignored = true)
  private String gffmu;

  @CSVColumn("OldSerial")
  @JobAdditionalProperty("oldSerno")
  private String oldSerial;

}
