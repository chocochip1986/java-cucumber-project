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
@Table(name = "single_date_header_validated")
public class SingleDateHeaderValidated extends AbstractValidated {
    @Column(name = "extraction_date")
    private LocalDate extractionDate;

    public static SingleDateHeaderValidated create(LocalDate extractionDate) {
        return build(extractionDate);
    }

    private static SingleDateHeaderValidated build(LocalDate extractionDate) {
        return SingleDateHeaderValidated.builder().extractionDate(extractionDate).build();
    }
}
