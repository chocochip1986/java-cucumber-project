package cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.MhaAddress;

import cdit_automation.data_helpers.datasource.datasource_file.DataField;
import cdit_automation.enums.datasource.MhaAddressTypeEnum;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class MhaAddressTypeField extends DataField {
    public MhaAddressTypeField() {
        this.value = MhaAddressTypeEnum.pick().toString();
    }

    public MhaAddressTypeField(String type) {
        this.value = type;
    }

    @Override
    public String name() {
        return "mhaAddressType";
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
