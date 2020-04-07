package cds_automation.data_helpers.datasource.datasource_file.mha.DataFields;

import cds_automation.data_helpers.datasource.datasource_file.DataField;
import cds_automation.data_setup.Phaker;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class GenderField extends DataField {
    public GenderField() {
        this.value = Phaker.validGender().toString();
    }

    @Override
    public String name() {
        return "gender";
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
