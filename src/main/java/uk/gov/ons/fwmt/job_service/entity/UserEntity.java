package uk.gov.ons.fwmt.job_service.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @Column(nullable = false)
  public String authNo;

  @Column
  public String tmUsername;

  @Column(nullable = false)
  public boolean active;

  // if this is set, it means that jobs designated for altAuthNo should be redirected to this user
  @Column
  public String alternateAuthNo;
}
