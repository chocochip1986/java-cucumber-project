package cdit_automation.models;

import cdit_automation.enums.CeasedCitizenNationalityEnum;
import cdit_automation.enums.CeasedCitizenNricCancelledStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
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

    @Enumerated(EnumType.STRING)
    @Column(name = "nationality")
    private CeasedCitizenNationalityEnum nationality;

    @Column(name = "citizen_renunciation_date")
    private LocalDate citizenRenunciationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "nric_cancelled_status")
    private CeasedCitizenNricCancelledStatusEnum nricCancelledStatus;
}
