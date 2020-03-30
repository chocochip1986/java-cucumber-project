package cdit_automation.data_helpers.datasource_file.mha.DataFields;

import cdit_automation.data_helpers.datasource_file.DataField;
import cdit_automation.data_setup.Phaker;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class NameField extends DataField {
    public NameField() {
        this.value = faker.name().fullName();
    }

    @Override
    public String name() {
        return "name";
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
