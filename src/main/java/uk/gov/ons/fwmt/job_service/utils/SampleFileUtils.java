package uk.gov.ons.fwmt.job_service.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.fwmt.job_service.data.file_ingest.SampleFilenameComponents;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;

@Slf4j
public final class SampleFileUtils {
  private static final String SAMPLE_ENDPOINT_VALUE = "sample";
  public static final DateTimeFormatter TIMESTAMP_FORMAT_LINUX = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
  public static final DateTimeFormatter TIMESTAMP_FORMAT_WINDOWS = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss'Z'");
  
  public static final SampleFilenameComponents buildSampleFilenameComponents(File file) throws IOException, InvalidFileNameException {
    final SampleFilenameComponents filename = buildSampleFilenameComponents(file.getName(), SAMPLE_ENDPOINT_VALUE);
    return filename;
  }
    public final static SampleFilenameComponents buildSampleFilenameComponents(String rawFilename, String expectedEndpoint) {
    log.debug("Began a filename parse for " + rawFilename);

    String[] filenameSplitByDot = checkFileExtension(rawFilename);
    String[] filenameSplitByUnderscore = filenameSplitByDot[0].split("_");
    String endpoint = extractEndpoint(rawFilename, expectedEndpoint, filenameSplitByUnderscore);
    LegacySampleSurveyType tla = getLegacySampleSurveyType(filenameSplitByUnderscore, endpoint);

    String rawTimestamp = filenameSplitByUnderscore[filenameSplitByUnderscore.length - 1];
    log.debug("File timestamp detected as " + rawTimestamp);
    LocalDateTime timestamp = getLocalDateTime(rawFilename, rawTimestamp);

    log.debug("Passed a filename check for " + rawFilename);

    return new SampleFilenameComponents(endpoint, tla, timestamp);
  }
  
  public final static String extractEndpoint(String rawFilename, String expectedEndpoint, String[] filenameSplitByUnderscore){
    String endpoint = filenameSplitByUnderscore[0];

    if (!expectedEndpoint.equals(endpoint)) {
      throw FWMTCommonException
          .makeInvalidFileNameException(rawFilename, "File had an incorrect endpoint of " + endpoint);
    }

    switch (endpoint) {
    case "staff":
      if (filenameSplitByUnderscore.length != 2) {
        throw FWMTCommonException
            .makeInvalidFileNameException(rawFilename, "File names for staff should contain one underscore");
      }
      break;
    case SAMPLE_ENDPOINT_VALUE:
      if (filenameSplitByUnderscore.length != 3) {
        throw FWMTCommonException
            .makeInvalidFileNameException(rawFilename, "File names for samples should contain two underscores");
      }
      break;
    default:
      throw new IllegalArgumentException("File had an unrecognized endpoint of " + endpoint);
    }
    return endpoint;
  }
  
  public final static String[] checkFileExtension(String rawFilename){
    String[] filenameSplitByDot = rawFilename.split("\\.");
    if (filenameSplitByDot.length != 2 || !("csv".equals(filenameSplitByDot[1]))) {
      throw FWMTCommonException.makeInvalidFileNameException(rawFilename, "No 'csv' extension");
    }
    return filenameSplitByDot;
  }

  public final static LocalDateTime getLocalDateTime(String rawFilename, String rawTimestamp){
    LocalDateTime timestamp;
    try {
      timestamp = LocalDateTime.parse(rawTimestamp, TIMESTAMP_FORMAT_WINDOWS);
    } catch (DateTimeParseException e) {
      log.warn("Failed to parse a windows timestamp, trying a linux timestamp");
      try {
        timestamp = LocalDateTime.parse(rawTimestamp, TIMESTAMP_FORMAT_LINUX);
      } catch (DateTimeParseException f) {
        log.error("Failed to parse a windows timestamp", e);
        log.error("Failed to parse a linux timestamp", f);
        // we throw the exception with cause 'e', only because it is the more likely of the two to have been intended
        throw FWMTCommonException.makeInvalidFileNameException(rawFilename, "Invalid timestamp of " + rawTimestamp, e);
      }
    }
    return timestamp;
  }

  public final static LegacySampleSurveyType getLegacySampleSurveyType(String[] filenameSplitByUnderscore, String endpoint) {
    LegacySampleSurveyType tla = null;
    if (endpoint.equals(SAMPLE_ENDPOINT_VALUE)) {
      String tlaString = filenameSplitByUnderscore[1].toUpperCase();
      log.debug("File TLA detected as " + tlaString);
      switch (tlaString) {
      case "LFS":
        tla = LegacySampleSurveyType.LFS;
        break;
      case "GFF":
        tla = LegacySampleSurveyType.GFF;
        break;
      default:
        throw new IllegalArgumentException("File had an unrecognized TLA of " + tlaString);
      }
    }
    return tla;
  }
  
  public static File convertToRegularFile(MultipartFile file) throws IOException{
    File convFile = new File (file.getOriginalFilename());
    FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(convFile));
    return convFile;
  }
}
