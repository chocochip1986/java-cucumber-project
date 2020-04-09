package automation.data_helpers.datasource.datasource_file.mha.DataFields;

import automation.data_helpers.datasource.datasource_file.DataField;
import automation.enums.datasource.InvalidAddressTagEnum;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class InvalidAddressTagField extends DataField {
    public InvalidAddressTagField() {
        this.value = InvalidAddressTagEnum.pick().toString();
    }

    @Override
    public String name() {
        return "invalidAddressTag";
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
