package cdit_automation.models;

import cdit_automation.enums.PersonDetailDataItemChangedEnum;
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
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "person_detail_change_validated")
public class PersonDetailChangeValidated extends AbstractValidated {
    @Column(name = "nric")
    private String nric;

    @Column(name = "data_item_changed")
    @Enumerated(EnumType.STRING)
    private PersonDetailDataItemChangedEnum dataItemChanged;

    @Column(name = "data_item_new_value")
    private String dataItemNewValue;

    @Column(name = "duplicate_record_marker")
    private int duplicateRecordMarker;

    @Column(name = "incoming_record_details")
    private String incomingRecordDetails;

    @Column(name = "data_item_changed_date")
    private LocalDate dataItemChangedDate;
}
