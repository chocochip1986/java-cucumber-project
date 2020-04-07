package cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.MhaAddress;

import cdit_automation.data_helpers.datasource.datasource_file.DataField;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class MhaStreetNameField extends DataField {
    public MhaStreetNameField() {
        final String streetName = faker.address().streetName();
        this.value = streetName.substring(0, Math.min(this.length(), streetName.length()));
    }

    @Override
    public String name() {
        return "mhaStreetName";
    }

    @Override
    public int length() {
        return 32;
    }

    @Override
    public String toRawString() {
        return Strings.padEnd(this.value, this.length(), ' ');
    }
}
