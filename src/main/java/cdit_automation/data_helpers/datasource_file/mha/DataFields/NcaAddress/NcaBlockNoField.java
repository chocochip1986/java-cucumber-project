package cdit_automation.data_helpers.datasource_file.mha.DataFields.NcaAddress;

import cdit_automation.data_helpers.datasource_file.DataField;
import cdit_automation.data_setup.Phaker;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class NcaBlockNoField extends DataField {
    public NcaBlockNoField() {
        this.value = new StringBuilder().append(faker.random().nextInt(1000))
        .append(faker.random().nextBoolean() ? " " : "")
        .append(Phaker.randomLetter()).toString();
    }

    @Override
    public String name() {
        return "ncaBlockNo";
    }

    @Override
    public int length() {
        return 10;
    }

    @Override
    public String toRawString() {
        return Strings.padEnd(this.value, this.length(), ' ');
    }
}
