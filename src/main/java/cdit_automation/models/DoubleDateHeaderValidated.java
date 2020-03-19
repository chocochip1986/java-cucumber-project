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
@Table(name = "double_date_header_validated")
public class DoubleDateHeaderValidated extends AbstractValidated {
    @Column(name = "extraction_date")
    private LocalDate extractionDate;

    @Column(name = "cut_off_date")
    private LocalDate cutOffDate;

    public static DoubleDateHeaderValidated create(LocalDate extractionDate, LocalDate cutOffDate, Batch batch) {
        return build(extractionDate, cutOffDate, batch);
    }

    private static DoubleDateHeaderValidated build(LocalDate extractionDate, LocalDate cutOffDate, Batch batch) {
        return DoubleDateHeaderValidated.builder()
                .batch(batch)
                .extractionDate(extractionDate)
                .cutOffDate(cutOffDate)
                .build();
    }
}
