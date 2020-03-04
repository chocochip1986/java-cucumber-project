package cdit_automation.models;

import cdit_automation.constants.ErrorMessageConstants;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.CeasedCitizenNationalityEnum;
import cdit_automation.enums.CeasedCitizenNricCancelledStatusEnum;
import cdit_automation.utilities.DateUtils;
import cdit_automation.utilities.StringUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.tomcat.jni.Local;

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
  private String name;

  @Column(name = "nationality")
  private String nationality;

  @Column(name = "citizen_renunciation_date")
  private LocalDate citizenRenunciationDate;

  @Override
  public String toString() {
    return StringUtils.rightPad(this.nric, 9)
            + StringUtils.rightPad(this.name, 66)
            + StringUtils.rightPad(this.nationality, 2)
            + StringUtils.rightPad(this.citizenRenunciationDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")), 8);
  }

  public static CeasedCitizenValidated create(@NotNull Batch batch) {
    return build(batch, Phaker.validNric(), Phaker.validName(), Phaker.randomNonSGCountryCode(), Phaker.validPastDate());
  }

  public static CeasedCitizenValidated create(@NotNull Batch batch, @NotNull LocalDate citizenRenunciationDate) {
    return build(batch, Phaker.validNric(), Phaker.validName(), Phaker.randomNonSGCountryCode(), citizenRenunciationDate);
  }

  public static CeasedCitizenValidated create(@NotNull Batch batch,
                                              @NotNull String nric,
                                              @NotNull String name,
                                              @NotNull String nationality,
                                              @NotNull LocalDate citizenRenunciationDate) {
    return build(batch, nric, name, nationality, citizenRenunciationDate);
  }

  public static CeasedCitizenValidated build(Batch batch, String nric, String name, String nationality, LocalDate citizenRenunciationDate ) {
    return CeasedCitizenValidated.builder()
            .batch(batch)
            .nric(nric)
            .name(name)
            .nationality(nationality)
            .citizenRenunciationDate(citizenRenunciationDate)
            .build();
  }
}
