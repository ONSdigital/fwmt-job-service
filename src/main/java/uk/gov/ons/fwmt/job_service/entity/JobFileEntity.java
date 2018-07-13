package uk.gov.ons.fwmt.job_service.entity;

import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;

@Data
@Entity
@Table(name = "input_file")
public class JobFileEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private byte[] file;

  @Column(nullable = false)
  private String filename;

  @Column(name = "file_timestamp", nullable = false)
  @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
  private LocalDateTime fileTime;

  @Column(name = "received_timestamp", nullable = false)
  @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
  private LocalDateTime fileReceivedTime;

  /**
   * Getter and Setter to fix findBug issue with Mutable object.
   */
  public byte[] getFile() {
    final byte[] fileContents = Arrays.copyOf(file, file.length);
    return fileContents;
  }

  public void setFile(byte[] file) {
    final byte[] fileContents = Arrays.copyOf(file, file.length);
    this.file = fileContents;
  }
}
