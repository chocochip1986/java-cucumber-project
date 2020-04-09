package automation.models.datasource;

import automation.enums.datasource.PersonDetailDataItemChangedEnum;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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

    @Column(name = "record_details_validated")
    private String recordDetailsValidated;

    @Column(name = "data_item_changed_date")
    private LocalDate dataItemChangedDate;

    @Column(name = "is_mappable")
    private Boolean isMappable;

    public static PersonDetailChangeValidated createGender(Batch batch, String nric, String dataItemNewValue, LocalDate dataItemChangedDate) {
        return build(batch, nric, PersonDetailDataItemChangedEnum.GENDER, dataItemNewValue, dataItemChangedDate);
    }

    public static PersonDetailChangeValidated createBoD(Batch batch, String nric, String dataItemNewValue, LocalDate dataItemChangedDate) {
        return build(batch, nric, PersonDetailDataItemChangedEnum.DATE_OF_BIRTH, dataItemNewValue, dataItemChangedDate);
    }

    public static PersonDetailChangeValidated createName(Batch batch, String nric, String dataItemNewValue, LocalDate dataItemChangedDate) {
        return build(batch, nric, PersonDetailDataItemChangedEnum.NAME, dataItemNewValue, dataItemChangedDate);
    }

    public static PersonDetailChangeValidated create(Batch batch,
                                                     String nric,
                                                     PersonDetailDataItemChangedEnum personDetailDataItemChangedEnum,
                                                     String dataItemNewValue,
                                                     LocalDate dataItemChangedDate) {
        return build(batch, nric, personDetailDataItemChangedEnum, dataItemNewValue, dataItemChangedDate);
    }

    private static PersonDetailChangeValidated build(Batch batch,
                                                     String nric,
                                                     PersonDetailDataItemChangedEnum personDetailDataItemChangedEnum,
                                                     String dataItemNewValue,
                                                     LocalDate dataItemChangedDate) {
        return PersonDetailChangeValidated.builder()
                .batch(batch)
                .nric(nric)
                .dataItemChanged(personDetailDataItemChangedEnum)
                .dataItemNewValue(dataItemNewValue)
                .dataItemChangedDate(dataItemChangedDate)
                .build();
    }
}
