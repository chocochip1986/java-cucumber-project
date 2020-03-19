package cdit_automation.models;

import cdit_automation.data_setup.Phaker;
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
@Table(name = "dual_citizen_validated")
public class DualCitizenValidated extends AbstractValidated {
    @Column(name = "nric")
    private String nric;

    public static DualCitizenValidated create(Batch batch) {
        return build(batch, Phaker.validNric());
    }

    public static DualCitizenValidated create(Batch batch, String nric) {
        return build(batch, nric);
    }

    private static DualCitizenValidated build(Batch batch, String nric) {
        return DualCitizenValidated.builder().batch(batch).nric(nric).build();
    }
}
