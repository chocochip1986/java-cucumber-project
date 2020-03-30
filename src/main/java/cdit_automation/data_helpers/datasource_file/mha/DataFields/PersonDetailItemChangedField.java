package cdit_automation.data_helpers.datasource_file.mha.DataFields;

import cdit_automation.data_helpers.datasource_file.DataField;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.PersonDetailDataItemChangedEnum;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class PersonDetailItemChangedField extends DataField {
    public PersonDetailItemChangedField() {
        this.value = PersonDetailDataItemChangedEnum.pick().toString();
    }

    @Override
    public String name() {
        return "itemChanged";
    }

    @Override
    public int length() {
        return 1;
    }

    @Override
    public String toRawString() {
        return Strings.padEnd(this.value, this.length(), ' ');
    }
}
