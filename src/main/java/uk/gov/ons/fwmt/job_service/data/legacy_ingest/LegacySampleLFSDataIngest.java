package uk.gov.ons.fwmt.job_service.data.legacy_ingest;

import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.ons.fwmt.job_service.data.annotation.CSVColumn;
import uk.gov.ons.fwmt.job_service.data.annotation.JobAdditionalProperty;

@Data
@NoArgsConstructor
public class LegacySampleLFSDataIngest {
  @CSVColumn(value = "Quota_No", ignored = true)
  private String quotaNo;

  @CSVColumn("REFDATE")
  @JobAdditionalProperty("referenceDate")
  private String refDate;

  @CSVColumn("LSTHO")
  @JobAdditionalProperty("lstHo")
  private String lstho;

  @CSVColumn("MAIN")
  @JobAdditionalProperty("mainResp")
  private String main;

  @CSVColumn("NUMHHLD")
  @JobAdditionalProperty("numHhld")
  private String numberHouseholds;

  @CSVColumn("HHLDDESC")
  @JobAdditionalProperty("hhldDesc")
  private String householdsDesc;

  // // // Names
  @CSVColumn("QRES_LINE_NAME_1")
  @JobAdditionalProperty("respondentName1")
  private String respondentName1;

  @CSVColumn("QRES_LINE_NAME_2")
  @JobAdditionalProperty("respondentName2")
  private String respondentName2;

  @CSVColumn("QRES_LINE_NAME_3")
  @JobAdditionalProperty("respondentName3")
  private String respondentName3;

  @CSVColumn("QRES_LINE_NAME_4")
  @JobAdditionalProperty("respondentName4")
  private String respondentName4;

  @CSVColumn("QRES_LINE_NAME_5")
  @JobAdditionalProperty("respondentName5")
  private String respondentName5;

  @CSVColumn("QRES_LINE_NAME_6")
  @JobAdditionalProperty("respondentName6")
  private String respondentName6;

  @CSVColumn("QRES_LINE_NAME_7")
  @JobAdditionalProperty("respondentName7")
  private String respondentName7;

  @CSVColumn("QRES_LINE_NAME_8")
  @JobAdditionalProperty("respondentName8")
  private String respondentName8;

  @CSVColumn("QRES_LINE_NAME_9")
  @JobAdditionalProperty("respondentName9")
  private String respondentName9;

  @CSVColumn("QRES_LINE_NAME_10")
  @JobAdditionalProperty("respondentName10")
  private String respondentName10;

  @CSVColumn("QRES_LINE_NAME_11")
  @JobAdditionalProperty("respondentName11")
  private String respondentName11;

  @CSVColumn("QRES_LINE_NAME_12")
  @JobAdditionalProperty("respondentName12")
  private String respondentName12;

  @CSVColumn("QRES_LINE_NAME_13")
  @JobAdditionalProperty("respondentName13")
  private String respondentName13;

  @CSVColumn("QRES_LINE_NAME_14")
  @JobAdditionalProperty("respondentName14")
  private String respondentName14;

  @CSVColumn("QRES_LINE_NAME_15")
  @JobAdditionalProperty("respondentName15")
  private String respondentName15;

  @CSVColumn("QRES_LINE_NAME_16")
  @JobAdditionalProperty("respondentName16")
  private String respondentName16;

  // // // Ages

  @CSVColumn("QRES_LINE_AGE_1")
  @JobAdditionalProperty("respondentAge1")
  private String respondentAge1;

  @CSVColumn("QRES_LINE_AGE_2")
  @JobAdditionalProperty("respondentAge2")
  private String respondentAge2;

  @CSVColumn("QRES_LINE_AGE_3")
  @JobAdditionalProperty("respondentAge3")
  private String respondentAge3;

  @CSVColumn("QRES_LINE_AGE_4")
  @JobAdditionalProperty("respondentAge4")
  private String respondentAge4;

  @CSVColumn("QRES_LINE_AGE_5")
  @JobAdditionalProperty("respondentAge5")
  private String respondentAge5;

  @CSVColumn("QRES_LINE_AGE_6")
  @JobAdditionalProperty("respondentAge6")
  private String respondentAge6;

  @CSVColumn("QRES_LINE_AGE_7")
  @JobAdditionalProperty("respondentAge7")
  private String respondentAge7;

  @CSVColumn("QRES_LINE_AGE_8")
  @JobAdditionalProperty("respondentAge8")
  private String respondentAge8;

  @CSVColumn("QRES_LINE_AGE_9")
  @JobAdditionalProperty("respondentAge9")
  private String respondentAge9;

  @CSVColumn("QRES_LINE_AGE_10")
  @JobAdditionalProperty("respondentAge10")
  private String respondentAge10;

  @CSVColumn("QRES_LINE_AGE_11")
  @JobAdditionalProperty("respondentAge11")
  private String respondentAge11;

  @CSVColumn("QRES_LINE_AGE_12")
  @JobAdditionalProperty("respondentAge12")
  private String respondentAge12;

  @CSVColumn("QRES_LINE_AGE_13")
  @JobAdditionalProperty("respondentAge13")
  private String respondentAge13;

  @CSVColumn("QRES_LINE_AGE_14")
  @JobAdditionalProperty("respondentAge14")
  private String respondentAge14;

  @CSVColumn("QRES_LINE_AGE_15")
  @JobAdditionalProperty("respondentAge15")
  private String respondentAge15;

  @CSVColumn("QRES_LINE_AGE_16")
  @JobAdditionalProperty("respondentAge16")
  private String respondentAge16;

  // // // Indicators

  // // Respondent 1

  @CSVColumn("QINDIV_1_WRKING")
  @JobAdditionalProperty("respondentWorkIndicator1")
  private String respondentWorkIndicator1;

  @CSVColumn(value = "QINDIV_1_JBAWAY", ignored = true)
  private String qindiv1Jbaway;

  @CSVColumn(value = "QINDIV_1_OWNBUS", ignored = true)
  private String qindiv1Ownbus;

  @CSVColumn(value = "QINDIV_1_RELBUS", ignored = true)
  private String qindiv1Relbus;

  @CSVColumn("QINDIV_1_LOOK4")
  @JobAdditionalProperty("respondentLookingForWork1")
  private String respondentLookingForWork1;

  @CSVColumn(value = "QINDIV_1_DIFJOB", ignored = true)
  private String qindiv1Difjob;

  @CSVColumn("QINDIV_1_INDOUT")
  @JobAdditionalProperty("respondentInterviewType1")
  private String respondentInterviewType1;

  // // Respondent 2

  @CSVColumn("QINDIV_2_WRKING")
  @JobAdditionalProperty("respondentWorkIndicator2")
  private String respondentWorkIndicator2;

  @CSVColumn(value = "QINDIV_2_JBAWAY", ignored = true)
  private String qindiv2Jbaway;

  @CSVColumn(value = "QINDIV_2_OWNBUS", ignored = true)
  private String qindiv2Ownbus;

  @CSVColumn(value = "QINDIV_2_RELBUS", ignored = true)
  private String qindiv2Relbus;

  @CSVColumn("QINDIV_2_LOOK4")
  @JobAdditionalProperty("respondentLookingForWork2")
  private String respondentLookingForWork2;

  @CSVColumn(value = "QINDIV_2_DIFJOB", ignored = true)
  private String qindiv2Difjob;

  @CSVColumn("QINDIV_2_INDOUT")
  @JobAdditionalProperty("respondentInterviewType2")
  private String respondentInterviewType2;

  // // Respondent 3

  @CSVColumn("QINDIV_3_WRKING")
  @JobAdditionalProperty("respondentWorkIndicator3")
  private String respondentWorkIndicator3;

  @CSVColumn(value = "QINDIV_3_JBAWAY", ignored = true)
  private String qindiv3Jbaway;

  @CSVColumn(value = "QINDIV_3_OWNBUS", ignored = true)
  private String qindiv3Ownbus;

  @CSVColumn(value = "QINDIV_3_RELBUS", ignored = true)
  private String qindiv3Relbus;

  @CSVColumn("QINDIV_3_LOOK4")
  @JobAdditionalProperty("respondentLookingForWork3")
  private String respondentLookingForWork3;

  @CSVColumn(value = "QINDIV_3_DIFJOB", ignored = true)
  private String qindiv3Difjob;

  @CSVColumn("QINDIV_3_INDOUT")
  @JobAdditionalProperty("respondentLookingForWork3")
  private String respondentInterviewType3;

  // // Respondent 4

  @CSVColumn("QINDIV_4_WRKING")
  @JobAdditionalProperty("respondentWorkIndicator4")
  private String respondentWorkIndicator4;

  @CSVColumn(value = "QINDIV_4_JBAWAY", ignored = true)
  private String qindiv4Jbaway;

  @CSVColumn(value = "QINDIV_4_OWNBUS", ignored = true)
  private String qindiv4Ownbus;

  @CSVColumn(value = "QINDIV_4_RELBUS", ignored = true)
  private String qindiv4Relbus;

  @CSVColumn("QINDIV_4_LOOK4")
  @JobAdditionalProperty("respondentLookingForWork4")
  private String respondentLookingForWork4;

  @CSVColumn(value = "QINDIV_4_DIFJOB", ignored = true)
  private String qindiv4Difjob;

  @CSVColumn("QINDIV_4_INDOUT")
  @JobAdditionalProperty("respondentInterviewType4")
  private String respondentInterviewType4;

  // // Respondent 5

  @CSVColumn("QINDIV_5_WRKING")
  @JobAdditionalProperty("respondentWorkIndicator5")
  private String respondentWorkIndicator5;

  @CSVColumn(value = "QINDIV_5_JBAWAY", ignored = true)
  private String qindiv5Jbaway;

  @CSVColumn(value = "QINDIV_5_OWNBUS", ignored = true)
  private String qindiv5Ownbus;

  @CSVColumn(value = "QINDIV_5_RELBUS", ignored = true)
  private String qindiv5Relbus;

  @CSVColumn("QINDIV_5_LOOK4")
  @JobAdditionalProperty("respondentLookingForWork5")
  private String respondentLookingForWork5;

  @CSVColumn(value = "QINDIV_5_DIFJOB", ignored = true)
  private String qindiv5Difjob;

  @CSVColumn("QINDIV_5_INDOUT")
  @JobAdditionalProperty("respondentInterviewType5")
  private String respondentInterviewType5;

  // // Respondent 6

  @CSVColumn("QINDIV_6_WRKING")
  @JobAdditionalProperty("respondentWorkIndicator6")
  private String respondentWorkIndicator6;

  @CSVColumn(value = "QINDIV_6_JBAWAY", ignored = true)
  private String qindiv6Jbaway;

  @CSVColumn(value = "QINDIV_6_OWNBUS", ignored = true)
  private String qindiv6Ownbus;

  @CSVColumn(value = "QINDIV_6_RELBUS", ignored = true)
  private String qindiv6Relbus;

  @CSVColumn("QINDIV_6_LOOK4")
  @JobAdditionalProperty("respondentLookingForWork6")
  private String respondentLookingForWork6;

  @CSVColumn(value = "QINDIV_6_DIFJOB", ignored = true)
  private String qindiv6Difjob;

  @CSVColumn("QINDIV_6_INDOUT")
  @JobAdditionalProperty("respondentInterviewType6")
  private String respondentInterviewType6;

  // // Respondent 7

  @CSVColumn("QINDIV_7_WRKING")
  @JobAdditionalProperty("respondentWorkIndicator7")
  private String respondentWorkIndicator7;

  @CSVColumn(value = "QINDIV_7_JBAWAY", ignored = true)
  private String qindiv7Jbaway;

  @CSVColumn(value = "QINDIV_7_OWNBUS", ignored = true)
  private String qindiv7Ownbus;

  @CSVColumn(value = "QINDIV_7_RELBUS", ignored = true)
  private String qindiv7Relbus;

  @CSVColumn("QINDIV_7_LOOK4")
  @JobAdditionalProperty("respondentLookingForWork7")
  private String respondentLookingForWork7;

  @CSVColumn(value = "QINDIV_7_DIFJOB", ignored = true)
  private String qindiv7Difjob;

  @CSVColumn("QINDIV_7_INDOUT")
  @JobAdditionalProperty("respondentInterviewType7")
  private String respondentInterviewType7;

  // // Respondent 8

  @CSVColumn("QINDIV_8_WRKING")
  @JobAdditionalProperty("respondentWorkIndicator8")
  private String respondentWorkIndicator8;

  @CSVColumn(value = "QINDIV_8_JBAWAY", ignored = true)
  private String qindiv8Jbaway;

  @CSVColumn(value = "QINDIV_8_OWNBUS", ignored = true)
  private String qindiv8Ownbus;

  @CSVColumn(value = "QINDIV_8_RELBUS", ignored = true)
  private String qindiv8Relbus;

  @CSVColumn("QINDIV_8_LOOK4")
  @JobAdditionalProperty("respondentLookingForWork8")
  private String respondentLookingForWork8;

  @CSVColumn(value = "QINDIV_8_DIFJOB", ignored = true)
  private String qindiv8Difjob;

  @CSVColumn("QINDIV_8_INDOUT")
  @JobAdditionalProperty("respondentInterviewType8")
  private String respondentInterviewType8;

  // // Respondent 9

  @CSVColumn("QINDIV_9_WRKING")
  @JobAdditionalProperty("respondentWorkIndicator9")
  private String respondentWorkIndicator9;

  @CSVColumn(value = "QINDIV_9_JBAWAY", ignored = true)
  private String qindiv9Jbaway;

  @CSVColumn(value = "QINDIV_9_OWNBUS", ignored = true)
  private String qindiv9Ownbus;

  @CSVColumn(value = "QINDIV_9_RELBUS", ignored = true)
  private String qindiv9Relbus;

  @CSVColumn("QINDIV_9_LOOK4")
  @JobAdditionalProperty("respondentLookingForWork9")
  private String respondentLookingForWork9;

  @CSVColumn(value = "QINDIV_9_DIFJOB", ignored = true)
  private String qindiv9Difjob;

  @CSVColumn("QINDIV_9_INDOUT")
  @JobAdditionalProperty("respondentInterviewType9")
  private String respondentInterviewType9;

  // // Respondent 10

  @CSVColumn("QINDIV_10_WRKING")
  @JobAdditionalProperty("respondentWorkIndicator10")
  private String respondentWorkIndicator10;

  @CSVColumn(value = "QINDIV_10_JBAWAY", ignored = true)
  private String qindiv10Jbaway;

  @CSVColumn(value = "QINDIV_10_OWNBUS", ignored = true)
  private String qindiv10Ownbus;

  @CSVColumn(value = "QINDIV_10_RELBUS", ignored = true)
  private String qindiv10Relbus;

  @CSVColumn("QINDIV_10_LOOK4")
  @JobAdditionalProperty("respondentLookingForWork10")
  private String respondentLookingForWork10;

  @CSVColumn(value = "QINDIV_10_DIFJOB", ignored = true)
  private String qindiv10Difjob;

  @CSVColumn("QINDIV_10_INDOUT")
  @JobAdditionalProperty("respondentInterviewType10")
  private String respondentInterviewType10;

  // // Respondent 11

  @CSVColumn("QINDIV_11_WRKING")
  @JobAdditionalProperty("respondentWorkIndicator11")
  private String respondentWorkIndicator11;

  @CSVColumn(value = "QINDIV_11_JBAWAY", ignored = true)
  private String qindiv11Jbaway;

  @CSVColumn(value = "QINDIV_11_OWNBUS", ignored = true)
  private String qindiv11Ownbus;

  @CSVColumn(value = "QINDIV_11_RELBUS", ignored = true)
  private String qindiv11Relbus;

  @CSVColumn("QINDIV_11_LOOK4")
  @JobAdditionalProperty("respondentLookingForWork11")
  private String respondentLookingForWork11;

  @CSVColumn(value = "QINDIV_11_DIFJOB", ignored = true)
  private String qindiv11Difjob;

  @CSVColumn("QINDIV_11_INDOUT")
  @JobAdditionalProperty("respondentInterviewType11")
  private String respondentInterviewType11;

  // // Respondent 12

  @CSVColumn("QINDIV_12_WRKING")
  @JobAdditionalProperty("respondentWorkIndicator12")
  private String respondentWorkIndicator12;

  @CSVColumn(value = "QINDIV_12_JBAWAY", ignored = true)
  private String qindiv12Jbaway;

  @CSVColumn(value = "QINDIV_12_OWNBUS", ignored = true)
  private String qindiv12Ownbus;

  @CSVColumn(value = "QINDIV_12_RELBUS", ignored = true)
  private String qindiv12Relbus;

  @CSVColumn("QINDIV_12_LOOK4")
  @JobAdditionalProperty("respondentLookingForWork12")
  private String respondentLookingForWork12;

  @CSVColumn(value = "QINDIV_12_DIFJOB", ignored = true)
  private String qindiv12Difjob;

  @CSVColumn("QINDIV_12_INDOUT")
  @JobAdditionalProperty("respondentInterviewType12")
  private String respondentInterviewType12;

  // // Respondent 13

  @CSVColumn("QINDIV_13_WRKING")
  @JobAdditionalProperty("respondentWorkIndicator13")
  private String respondentWorkIndicator13;

  @CSVColumn(value = "QINDIV_13_JBAWAY", ignored = true)
  private String qindiv13Jbaway;

  @CSVColumn(value = "QINDIV_13_OWNBUS", ignored = true)
  private String qindiv13Ownbus;

  @CSVColumn(value = "QINDIV_13_RELBUS", ignored = true)
  private String qindiv13Relbus;

  @CSVColumn("QINDIV_13_LOOK4")
  @JobAdditionalProperty("respondentLookingForWork13")
  private String respondentLookingForWork13;

  @CSVColumn(value = "QINDIV_13_DIFJOB", ignored = true)
  private String qindiv13Difjob;

  @CSVColumn("QINDIV_13_INDOUT")
  @JobAdditionalProperty("respondentInterviewType13")
  private String respondentInterviewType13;

  // // Respondent 14

  @CSVColumn("QINDIV_14_WRKING")
  @JobAdditionalProperty("respondentWorkIndicator14")
  private String respondentWorkIndicator14;

  @CSVColumn(value = "QINDIV_14_JBAWAY", ignored = true)
  private String qindiv14Jbaway;

  @CSVColumn(value = "QINDIV_14_OWNBUS", ignored = true)
  private String qindiv14Ownbus;

  @CSVColumn(value = "QINDIV_14_RELBUS", ignored = true)
  private String qindiv14Relbus;

  @CSVColumn("QINDIV_14_LOOK4")
  @JobAdditionalProperty("respondentLookingForWork14")
  private String respondentLookingForWork14;

  @CSVColumn(value = "QINDIV_14_DIFJOB", ignored = true)
  private String qindiv14Difjob;

  @CSVColumn("QINDIV_14_INDOUT")
  @JobAdditionalProperty("respondentInterviewType14")
  private String respondentInterviewType14;

  // // Respondent 15

  @CSVColumn("QINDIV_15_WRKING")
  @JobAdditionalProperty("respondentWorkIndicator15")
  private String respondentWorkIndicator15;

  @CSVColumn(value = "QINDIV_15_JBAWAY", ignored = true)
  private String qindiv15Jbaway;

  @CSVColumn(value = "QINDIV_15_OWNBUS", ignored = true)
  private String qindiv15Ownbus;

  @CSVColumn(value = "QINDIV_15_RELBUS", ignored = true)
  private String qindiv15Relbus;

  @CSVColumn("QINDIV_15_LOOK4")
  @JobAdditionalProperty("respondentLookingForWork15")
  private String respondentLookingForWork15;

  @CSVColumn(value = "QINDIV_15_DIFJOB", ignored = true)
  private String qindiv15Difjob;

  @CSVColumn("QINDIV_15_INDOUT")
  @JobAdditionalProperty("respondentInterviewType15")
  private String respondentInterviewType15;

  // // Respondent 16

  @CSVColumn("QINDIV_16_WRKING")
  @JobAdditionalProperty("respondentWorkIndicator16")
  private String respondentWorkIndicator16;

  @CSVColumn(value = "QINDIV_16_JBAWAY", ignored = true)
  private String qindiv16Jbaway;

  @CSVColumn(value = "QINDIV_16_OWNBUS", ignored = true)
  private String qindiv16Ownbus;

  @CSVColumn(value = "QINDIV_16_RELBUS", ignored = true)
  private String qindiv16Relbus;

  @CSVColumn("QINDIV_16_LOOK4")
  @JobAdditionalProperty("respondentLookingForWork16")
  private String respondentLookingForWork16;

  @CSVColumn(value = "QINDIV_16_DIFJOB", ignored = true)
  private String qindiv16Difjob;

  @CSVColumn("QINDIV_16_INDOUT")
  @JobAdditionalProperty("respondentInterviewType16")
  private String respondentInterviewType16;

  // // // Notes?

  @CSVColumn(value = "tba")
  // TODO is this just a mis-spelling?
  private String respondentBriefNotes;

  // // // Other

  @CSVColumn(value = "WEEK", ignored = true)
  private String week;

  @CSVColumn(value = "W1YR", ignored = true)
  private String w1yr;

  @CSVColumn(value = "QRTR", ignored = true)
  private String qrtr;

  @CSVColumn(value = "WAVFND", ignored = true)
  private String wavfnd;

  @CSVColumn(value = "HHLD", ignored = true)
  private String hhld;

  @CSVColumn(value = "CHKLET", ignored = true)
  private String chklet;

  @CSVColumn(value = "DIVADDIND", ignored = true)
  private String divAddInd;

  @CSVColumn(value = "MO", ignored = true)
  private String mo;

  @CSVColumn(value = "HOUT", ignored = true)
  private String hOut;

  // duplicate LSTHO

  @CSVColumn(value = "LFSSAMP", ignored = true)
  private String lfsSamp;

  @CSVColumn(value = "THANKS", ignored = true)
  private String thanks;

  @CSVColumn(value = "THANKE", ignored = true)
  private String thanke;

  @CSVColumn(value = "RECPHONE", ignored = true)
  private String recPhone;

  @CSVColumn(value = "COUNTRY", ignored = true)
  private String country;

  @CSVColumn(value = "NUMPER", ignored = true)
  private String numper;

  // // // Comments

  // // Respondent 1

  @CSVColumn(value = "COMMENT_1_REFDTE", ignored = true)
  private String comment1RefDate;

  @CSVColumn("COMMENT_1_INTVNO")
  @JobAdditionalProperty("waveInterviewer1")
  private String comment1InterviewerNo;

  @CSVColumn(value = "COMMENT_1_BriefSDC1", ignored = true)
  private String comment1BriefSDC1;

  @CSVColumn(value = "COMMENT_1_BriefSDC2", ignored = true)
  private String comment1BriefSDC2;

  @CSVColumn(value = "COMMENT_1_BriefSDC3", ignored = true)
  private String comment1BriefSDC3;

  @CSVColumn("COMMENT_1_BRIEF1")
  @JobAdditionalProperty("waveBriefNotes1")
  private String comment1BriefNotes1;

  // // Respondent 2

  @CSVColumn(value = "COMMENT_2_REFDTE", ignored = true)
  private String comment2RefDate;

  @CSVColumn("COMMENT_2_INTVNO")
  @JobAdditionalProperty("waveInterviewer2")
  private String comment2InterviewerNo;

  @CSVColumn(value = "COMMENT_2_BriefSDC1", ignored = true)
  private String comment2BriefSDC1;

  @CSVColumn(value = "COMMENT_2_BriefSDC2", ignored = true)
  private String comment2BriefSDC2;

  @CSVColumn(value = "COMMENT_2_BriefSDC3", ignored = true)
  private String comment2BriefSDC3;

  @CSVColumn("COMMENT_2_BRIEF1")
  @JobAdditionalProperty("waveBriefNotes2")
  private String comment2BriefNotes1;

  // // Respondent 3

  @CSVColumn(value = "COMMENT_3_REFDTE", ignored = true)
  private String comment3RefDate;

  @CSVColumn("COMMENT_3_INTVNO")
  @JobAdditionalProperty("waveInterviewer3")
  private String comment3InterviewerNo;

  @CSVColumn(value = "COMMENT_3_BriefSDC1", ignored = true)
  private String comment3BriefSDC1;

  @CSVColumn(value = "COMMENT_3_BriefSDC2", ignored = true)
  private String comment3BriefSDC2;

  @CSVColumn(value = "COMMENT_3_BriefSDC3", ignored = true)
  private String comment3BriefSDC3;

  @CSVColumn("COMMENT_3_BRIEF1")
  @JobAdditionalProperty("waveBriefNotes3")
  private String comment3BriefNotes1;

  // // Respondent 4

  @CSVColumn(value = "COMMENT_4_REFDTE", ignored = true)
  private String comment4RefDate;

  @CSVColumn("COMMENT_4_INTVNO")
  @JobAdditionalProperty("waveInterviewer4")
  private String comment4InterviewerNo;

  @CSVColumn(value = "COMMENT_4_BriefSDC1", ignored = true)
  private String comment4BriefSDC1;

  @CSVColumn(value = "COMMENT_4_BriefSDC2", ignored = true)
  private String comment4BriefSDC2;

  @CSVColumn(value = "COMMENT_4_BriefSDC3", ignored = true)
  private String comment4BriefSDC3;

  @CSVColumn("COMMENT_4_BRIEF1")
  @JobAdditionalProperty("waveBriefNotes4")
  private String comment4BriefNotes1;

  // // Respondent 5

  @CSVColumn(value = "COMMENT_5_REFDTE", ignored = true)
  private String comment5RefDate;

  @CSVColumn("COMMENT_5_INTVNO")
  @JobAdditionalProperty("waveInterviewer5")
  private String comment5InterviewerNo;

  @CSVColumn(value = "COMMENT_5_BriefSDC1", ignored = true)
  private String comment5BriefSDC1;

  @CSVColumn(value = "COMMENT_5_BriefSDC2", ignored = true)
  private String comment5BriefSDC2;

  @CSVColumn(value = "COMMENT_5_BriefSDC3", ignored = true)
  private String comment5BriefSDC3;

  @CSVColumn("COMMENT_5_BRIEF1")
  @JobAdditionalProperty("waveBriefNotes5")
  private String comment5BriefNotes1;

  // // // Other

  @CSVColumn("DIRECTION")
  @JobAdditionalProperty("operationalParaData")
  private String direction;


}
