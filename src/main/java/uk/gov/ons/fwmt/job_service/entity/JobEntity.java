package uk.gov.ons.fwmt.job_service.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "jobs")
public class JobEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String tmJobId;

  @Column(nullable = false)
  private String lastAuthNo;
}
