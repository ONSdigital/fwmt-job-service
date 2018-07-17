package uk.gov.ons.fwmt.job_service.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.ons.fwmt.job_service.data.file_ingest.FileIngest;
import uk.gov.ons.fwmt.job_service.data.file_ingest.Filename;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType;
import uk.gov.ons.fwmt.job_service.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service.service.FileIngestService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * This service handles the checking of file attributes, media types and filenames
 * Then, the service hands off control to the CSVParsingService
 */
@Slf4j
@Service
public class FileIngestServiceImpl implements FileIngestService {
  public static final DateTimeFormatter TIMESTAMP_FORMAT_LINUX = DateTimeFormatter
      .ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
  public static final DateTimeFormatter TIMESTAMP_FORMAT_WINDOWS = DateTimeFormatter
      .ofPattern("yyyy-MM-dd'T'HH-mm-ss'Z'");

  protected Filename verifyCSVFilename(String rawFilename, String expectedEndpoint) {
    log.debug("verifyCSVFilename: Began a filename parse for " + rawFilename);

    // Check file extension
    String[] filenameSplitByDot = checkFileExtension(rawFilename);

    // Split the filename by underscore
    String[] filenameSplitByUnderscore = filenameSplitByDot[0].split("_");

    String endpoint = extractEndpoint(rawFilename, expectedEndpoint, filenameSplitByUnderscore);
    log.debug("endpoint detected as {}", endpoint);

    // Detect survey type
    LegacySampleSurveyType tla = getLegacySampleSurveyType(filenameSplitByUnderscore, endpoint);
    log.debug("tla detected as {}", tla);

    // Timestamp validation
    String rawTimestamp = filenameSplitByUnderscore[filenameSplitByUnderscore.length - 1];
    LocalDateTime timestamp = getLocalDateTime(rawFilename, rawTimestamp);
    log.debug("timestamp detected as {}", timestamp);

    Filename filename = new Filename(endpoint, tla, timestamp);
    log.debug("Passed a filename check with {}", filename);
    return filename;
  }

  protected String extractEndpoint(String rawFilename, String expectedEndpoint, String[] filenameSplitByUnderscore) {
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
    case "sample":
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

  protected String[] checkFileExtension(String rawFilename) {
    String[] filenameSplitByDot = rawFilename.split("\\.");
    if (filenameSplitByDot.length != 2 || !("csv".equals(filenameSplitByDot[1]))) {
      throw FWMTCommonException.makeInvalidFileNameException(rawFilename, "No 'csv' extension");
    }
    return filenameSplitByDot;
  }

  protected LegacySampleSurveyType getLegacySampleSurveyType(String[] filenameSplitByUnderscore, String endpoint) {
    LegacySampleSurveyType tla = null;
    if (endpoint.equals("sample")) {
      String tlaString = filenameSplitByUnderscore[1];
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

  protected LocalDateTime getLocalDateTime(String rawFilename, String rawTimestamp) {
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

  public FileIngest ingestSampleFile(File file) throws IOException {
    log.debug("ingestSampleFile began with filename {}", file.getName());
    final Filename filename = verifyCSVFilename(file.getName(), "sample");
    final Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
    FileIngest fileIngest = new FileIngest(filename, reader);
    log.debug("ingestSampleFile passed with {}", fileIngest);
    return fileIngest;
  }
}
