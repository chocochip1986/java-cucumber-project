package cdit_automation.models;

import cdit_automation.enums.SpringJobStatusEnum;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "batch_job_execution")
public class JobExecution extends AbstractEntity {
  @Id
  @Column(name = "job_execution_id")
  private Long id;

  private Long version;

  @Column(name = "job_instance_id")
  private Long instanceId;

  @Column(name = "create_time")
  private Timestamp createTime;

  @Column(name = "start_time")
  private Timestamp startTime;

  @Column(name = "end_time")
  private Timestamp endTime;

  @Enumerated(EnumType.STRING)
  private SpringJobStatusEnum status;

  @Column(name = "exit_code")
  private String exitCode;

  @Column(name = "exit_message")
  private String exitMessage;

  @Column(name = "last_updated")
  private Timestamp lastUpdated;

  @Column(name = "job_configuration_location")
  private String configurationLocation;
}
