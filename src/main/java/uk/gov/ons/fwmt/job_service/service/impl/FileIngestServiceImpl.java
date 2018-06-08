package uk.gov.ons.fwmt.job_service.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.ons.fwmt.job_service.data.file_ingest.FileIngest;
import uk.gov.ons.fwmt.job_service.data.file_ingest.Filename;
import uk.gov.ons.fwmt.job_service.data.legacy_ingest.LegacySampleSurveyType;
import uk.gov.ons.fwmt.job_service.exceptions.types.InvalidFileNameException;
import uk.gov.ons.fwmt.job_service.service.FileIngestService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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

  protected Filename verifyCSVFilename(String rawFilename, String expectedEndpoint) throws InvalidFileNameException {
    log.info("Began a filename parse for " + rawFilename);

    // Check file extension
    String[] filenameSplitByDot = checkFileExtension(rawFilename);

    // Split the filename by underscore
    String[] filenameSplitByUnderscore = filenameSplitByDot[0].split("_");

    String endpoint = extractEndpoint(rawFilename, expectedEndpoint, filenameSplitByUnderscore);

    // Detect survey type
    LegacySampleSurveyType tla = getLegacySampleSurveyType(filenameSplitByUnderscore, endpoint);

    // Timestamp validation
    String rawTimestamp = filenameSplitByUnderscore[filenameSplitByUnderscore.length - 1];
    log.debug("File timestamp detected as " + rawTimestamp);
    LocalDateTime timestamp = getLocalDateTime(rawFilename, rawTimestamp);

    log.info("Passed a filename check");

    return new Filename(endpoint, tla, timestamp);
  }

  protected String extractEndpoint(String rawFilename, String expectedEndpoint, String[] filenameSplitByUnderscore)
      throws InvalidFileNameException {
    String endpoint = filenameSplitByUnderscore[0];
    log.debug("File endpoint detected as " + endpoint);

    if (!expectedEndpoint.equals(endpoint)) {
      throw new InvalidFileNameException(rawFilename, "File had an incorrect endpoint of " + endpoint);
    }

    switch (endpoint) {
    case "staff":
      if (filenameSplitByUnderscore.length != 2)
        throw new InvalidFileNameException(rawFilename, "File names for staff should contain one underscore");
      break;
    case "sample":
      if (filenameSplitByUnderscore.length != 3)
        throw new InvalidFileNameException(rawFilename, "File names for samples should contain two underscores");
      break;
    default:
      throw new IllegalArgumentException("File had an unrecognized endpoint of " + endpoint);
    }
    return endpoint;
  }

  protected String[] checkFileExtension(String rawFilename) throws InvalidFileNameException {
    String[] filenameSplitByDot = rawFilename.split("\\.");
    if (filenameSplitByDot.length != 2 || !("csv".equals(filenameSplitByDot[1]))) {
      throw new InvalidFileNameException(rawFilename, "No 'csv' extension");
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

  protected LocalDateTime getLocalDateTime(String rawFilename, String rawTimestamp) throws InvalidFileNameException {
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
        throw new InvalidFileNameException(rawFilename, "Invalid timestamp of " + rawTimestamp, e);
      }
    }
    return timestamp;
  }

  public FileIngest ingestSampleFile(MultipartFile file) throws IOException, InvalidFileNameException {
    Filename filename = verifyCSVFilename(file.getOriginalFilename(), "sample");

    Reader reader = new InputStreamReader(file.getInputStream());

    return new FileIngest(filename, reader);
  }
}
