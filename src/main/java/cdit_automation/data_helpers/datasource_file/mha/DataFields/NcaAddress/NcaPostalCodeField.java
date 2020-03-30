package cdit_automation.data_helpers.datasource_file.mha.DataFields.NcaAddress;

import cdit_automation.data_helpers.datasource_file.DataField;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class NcaPostalCodeField extends DataField {
    public NcaPostalCodeField() {
        this.value = new StringBuilder()
                .append(faker.random().nextBoolean() ? faker.random().nextInt(10000) : "")
                .toString();
    }

    @Override
    public String name() {
        return "ncaPostalCode";
    }

    @Override
    public int length() {
        return 4;
    }

    @Override
    public String toRawString() {
        return Strings.padEnd(this.value, this.length(), ' ');
    }
}
