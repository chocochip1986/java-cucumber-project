package cdit_automation.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

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
}
