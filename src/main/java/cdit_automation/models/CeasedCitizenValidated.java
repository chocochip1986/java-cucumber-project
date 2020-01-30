package cdit_automation.models;

import cdit_automation.constants.ErrorMessageConstants;
import cdit_automation.enums.CeasedCitizenNationalityEnum;
import cdit_automation.enums.CeasedCitizenNricCancelledStatusEnum;
import cdit_automation.utilities.StringUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "ceased_citizen_validated")
public class CeasedCitizenValidated extends AbstractValidated {

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

  @Override
  public String toString() {
    return StringUtils.rightPad(this.nric, 9)
            + StringUtils.rightPad(this.name, 66)
            + StringUtils.rightPad(this.nationality.getValue(), 2)
            + StringUtils.rightPad(this.citizenRenunciationDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")), 8)
            + StringUtils.rightPad(this.nricCancelledStatus.getValue(), 1);
  }
}
