package cdit_automation.data_helpers.datasource_file.mha.DataFields;

import cdit_automation.data_helpers.datasource_file.DataField;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.PersonDetailDataItemChangedEnum;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
public class ItemChangedAndValueField extends DataField {

    public PersonDetailItemChangedField itemChanged;
    public PersonDetailItemChangedValueField itemChangedValue;

    public ItemChangedAndValueField() {
        this.itemChanged = new PersonDetailItemChangedField();
        switch (PersonDetailDataItemChangedEnum.fromString(this.itemChanged.value)) {
            case NAME:
                this.itemChangedValue = PersonDetailItemChangedValueField.builder().value(new NameField().toRawString()).build();
                break;
            case GENDER:
                this.itemChangedValue = PersonDetailItemChangedValueField.builder().value(new GenderField().toRawString()).build();
                break;
            case DATE_OF_BIRTH:
                this.itemChangedValue = PersonDetailItemChangedValueField.builder().value(new DateField().toRawString()).build();
                break;
        }
        this.value = this.itemChanged.toRawString() + this.itemChangedValue.toRawString();
    }

    @Override
    public String name() {
        return "itemChangedValue";
    }

    @Override
    public int length() {
        return 67;
    }

    @Override
    public String toRawString() {
        return Strings.padEnd(this.value, this.length(), ' ');
    }
}
