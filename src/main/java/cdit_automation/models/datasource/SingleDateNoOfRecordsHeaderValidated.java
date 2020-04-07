package cdit_automation.models.datasource;

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
@Table(name = "single_date_no_of_records_header_validated")
public class SingleDateNoOfRecordsHeaderValidated extends AbstractValidated {
    @Column(name = "date_of_run")
    private LocalDate dateOfRun;

    @Column(name = "no_of_records")
    private int noOfRecords;
}
