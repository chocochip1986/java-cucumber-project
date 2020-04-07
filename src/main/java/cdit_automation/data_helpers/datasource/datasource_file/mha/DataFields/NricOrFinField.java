package cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields;

import cdit_automation.data_helpers.datasource.datasource_file.DataField;
import cdit_automation.data_setup.Phaker;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class NricOrFinField extends DataField {
    public NricOrFinField() {
        this.value = faker.random().nextBoolean() ? Phaker.uniqueOrderNric() : Phaker.uniqueOrderFin();
    }

    @Override
    public String name() {
        return "uid";
    }

    @Override
    public int length() {
        return 9;
    }

    @Override
    public String toRawString() {
        return Strings.padEnd(this.value, this.length(), ' ');
    }
}
