package cds_automation.models.datasource;

import cds_automation.data_setup.Phaker;
import cds_automation.utilities.StringUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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

  @Column(name = "record_details_validated")
  private String recordDetailsValidated;

  @Column(name = "duplicate_record_marker")
  private Long duplicateRecordMarker;

  @Column(name = "is_mappable")
  private Boolean isMappable;

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
            .isMappable(true)
            .build();
  }
}
