package cdit_automation.data_helpers.datasource_file.mha.DataFields;

import cdit_automation.data_helpers.datasource_file.DataField;
import cdit_automation.enums.InvalidAddressTagEnum;
import cdit_automation.enums.MhaAddressTypeEnum;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class InvalidAddressTagField extends DataField {
    public InvalidAddressTagField() {
        this.value = InvalidAddressTagEnum.pick().toString();
    }

    @Override
    public String name() {
        return "invalidAddressTag";
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
