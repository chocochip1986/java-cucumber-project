package cdit_automation.data_helpers.datasource_file.mha.DataFields;

import cdit_automation.data_helpers.datasource_file.DataField;
import cdit_automation.enums.datasource.AddressIndicatorEnum;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class AddressIndicatorField extends DataField {
    public AddressIndicatorField() {
        this.value = AddressIndicatorEnum.pick().toString();
    }

    @Override
    public String name() {
        return "addressIndicator";
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
