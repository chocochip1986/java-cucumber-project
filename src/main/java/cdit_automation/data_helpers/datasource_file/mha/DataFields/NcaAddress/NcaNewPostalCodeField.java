package cdit_automation.data_helpers.datasource_file.mha.DataFields.NcaAddress;

import cdit_automation.data_helpers.datasource_file.DataField;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class NcaNewPostalCodeField extends DataField {
    public NcaNewPostalCodeField() {
        this.value = new StringBuilder()
                .append(faker.random().nextInt(1000000))
                .toString();
    }

    @Override
    public String name() {
        return "ncaPostalCode";
    }

    @Override
    public int length() {
        return 6;
    }

    @Override
    public String toRawString() {
        return Strings.padEnd(this.value, this.length(), '0');
    }
}
