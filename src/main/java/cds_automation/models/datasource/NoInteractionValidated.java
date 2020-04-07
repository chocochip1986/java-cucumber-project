package cds_automation.models.datasource;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "no_interaction_validated")
@SuperBuilder
public class NoInteractionValidated extends AbstractValidated {

    @Column(name = "nric")
    private String nric;

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @Column(name = "valid_till")
    private LocalDate validTill;
}
