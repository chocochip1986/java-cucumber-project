package cdit_automation.data_helpers.datasource_file.mha.DataFields;

import cdit_automation.data_helpers.datasource_file.DataField;
import cdit_automation.enums.PersonDetailDataItemChangedEnum;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class PersonDetailItemChangedValueField extends DataField {
    public PersonDetailItemChangedValueField() {
        this.value = "M";
    }

    @Override
    public String name() {
        return "itemChangeValue";
    }

    @Override
    public int length() {
        return 66;
    }

    @Override
    public String toRawString() {
        return Strings.padEnd(this.value, this.length(), ' ');
    }
}
