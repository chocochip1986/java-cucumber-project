package cdit_automation.models;

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
@Table(name = "single_date_header_validated")
public class SingleDateHeaderValidated extends AbstractValidated {
    @Column(name = "extraction_date")
    private LocalDate extractionDate;

    public static SingleDateHeaderValidated create(LocalDate extractionDate, Batch batch) {
        return build(extractionDate, batch);
    }

    private static SingleDateHeaderValidated build(LocalDate extractionDate, Batch batch) {
        return SingleDateHeaderValidated.builder()
                .extractionDate(extractionDate)
                .batch(batch)
                .build();
    }
}
