package cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.NcaAddress;

import cdit_automation.data_helpers.datasource.datasource_file.DataField;
import cdit_automation.data_setup.Phaker;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class NcaUnitNoField extends DataField {
    public NcaUnitNoField() {
        this.value = new StringBuilder()
                .append(faker.random().nextInt(724))
                .append(faker.random().nextBoolean() ? Phaker.randomLetter() : "")
                .toString();
    }

    @Override
    public String name() {
        return "ncaUnitNo";
    }

    @Override
    public int length() {
        return 5;
    }

    @Override
    public String toRawString() {
        return Strings.padEnd(this.value, this.length(), ' ');
    }
}
