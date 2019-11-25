package cdit_automation.models;

import cdit_automation.constants.ErrorMessageConstants;
import cdit_automation.enums.CeasedCitizenNationalityEnum;
import cdit_automation.enums.CeasedCitizenNricCancelledStatusEnum;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ceased_citizen_validated")
public class CeasedCitizenValidated extends AbstractEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne // (cascade = CascadeType.ALL)
  @JoinColumn(name = "batch_id")
  @NotNull
  private Batch batch;

  @Column(name = "nric", length = 9)
  @Size(min = 9, max = 9)
  private String nric;

  @Column(name = "name", length = 66)
  @Size(max = 66)
  @NotBlank(message = ErrorMessageConstants.INVALID_NAME)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "nationality")
  private CeasedCitizenNationalityEnum nationality;

  @Column(name = "citizen_renunciation_date")
  @NotNull(message = ErrorMessageConstants.INVALID_RENUNCIATION_DATE)
  private LocalDate citizenRenunciationDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "nric_cancelled_status")
  private CeasedCitizenNricCancelledStatusEnum nricCancelledStatus;
}
