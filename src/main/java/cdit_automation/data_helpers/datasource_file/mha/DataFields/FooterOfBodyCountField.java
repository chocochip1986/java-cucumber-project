package cdit_automation.data_helpers.datasource_file.mha.DataFields;

import cdit_automation.data_helpers.datasource_file.DataField;
import cdit_automation.data_setup.Phaker;
import com.google.common.base.Strings;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

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
