package automation.data_helpers.datasource.datasource_file.mha.DataFields.NcaAddress;

import automation.data_helpers.datasource.datasource_file.DataField;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class NcaLevelNoField extends DataField {
    public NcaLevelNoField() {
        this.value = new StringBuilder()
                .append(faker.random().nextBoolean() ? "B" : "")
                .append(faker.random().nextInt(100)).toString();
    }

    @Override
    public String name() {
        return "ncaLevelNo";
    }

    @Override
    public int length() {
        return 3;
    }

    @Override
    public String toRawString() {
        return Strings.padEnd(this.value, this.length(), ' ');
    }
}
