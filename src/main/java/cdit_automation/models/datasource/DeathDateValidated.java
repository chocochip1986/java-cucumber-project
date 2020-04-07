package cdit_automation.models.datasource;

import cdit_automation.data_setup.Phaker;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
@Table(name = "death_date_validated")
public class DeathDateValidated extends AbstractValidated {
    @Column(name = "nric", length = 9)
    private String nric;

    @Column(name = "death_date")
    private LocalDate deathDate;

    @Column(name = "is_mappable")
    private Boolean isMappable;

    public static DeathDateValidated create(Batch batch) {
        return build(batch, Phaker.validNric(), Phaker.validPastDate());
    }

    private static DeathDateValidated build(Batch batch, String nric, LocalDate deathDate) {
        return DeathDateValidated.builder().batch(batch).nric(nric).deathDate(deathDate).build();
    }
}
