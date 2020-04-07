package cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.MhaAddress;

import cdit_automation.data_helpers.datasource.datasource_file.DataField;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class MhaFloorNoField extends DataField {
    public MhaFloorNoField() {
        this.value = new StringBuilder()
                .append(faker.random().nextBoolean() ? "B" : "")
                .append(faker.random().nextInt(100)).toString();
        this.value = this.value.substring(0, Math.min(this.length(), this.value.length()));
    }

    @Override
    public String name() {
        return "mhaFloorNo";
    }

    @Override
    public int length() {
        return 2;
    }

    @Override
    public String toRawString() {
        return Strings.padEnd(this.value, this.length(), ' ');
    }
}
