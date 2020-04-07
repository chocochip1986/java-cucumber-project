package cds_automation.data_helpers.datasource.datasource_file.mha.DataFields;

import cds_automation.data_helpers.datasource.datasource_file.DataField;
import com.google.common.base.Strings;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class FooterOfBodyCountField extends DataField {

    public FooterOfBodyCountField(int bodySize) {
        this.value = bodySize + "";
    }

    @Override
    public String name() {
        return "footer";
    }

    @Override
    public int length() {
        return 9;
    }

    @Override
    public String toRawString() {
        return Strings.padStart(this.value, this.length(), '0');
    }
}
