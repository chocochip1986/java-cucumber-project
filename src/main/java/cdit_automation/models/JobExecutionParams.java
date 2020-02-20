package cdit_automation.models;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@IdClass(JobExecutionParamCompositeKey.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "batch_job_execution_params")
public class JobExecutionParams extends AbstractEntity {
  @Id
  @Column(name = "job_execution_id")
  private Long id;

  @Column(name = "type_cd")
  private String type;

  @Column(name = "key_name")
  private String keyName;

  @Column(name = "string_val")
  private String stringVal;

  @Column(name = "date_val")
  private Timestamp dateVal;

  @Id
  @Column(name = "long_val")
  private Long longVal;

  @Column(name = "double_val")
  private Double doubleVal;

  private String identifying;
}
