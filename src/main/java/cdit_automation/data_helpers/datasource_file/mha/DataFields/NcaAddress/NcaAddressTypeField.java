package cdit_automation.data_helpers.datasource_file.mha.DataFields.NcaAddress;

import cdit_automation.data_helpers.datasource_file.DataField;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class NcaAddressTypeField extends DataField {
    public NcaAddressTypeField() {
        this.value = "S";
    }

    @Override
    public String name() {
        return "ncaAddressType";
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
