package cdit_automation.data_helpers.datasource_file.mha.DataFields;

import cdit_automation.data_helpers.datasource_file.DataField;
import cdit_automation.data_setup.Phaker;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Getter
@Setter
public class FillerField extends DataField {

    public FillerField(int size) {
        this.value = Strings.padStart("", size, ' ');
    }

    public FillerField(int size, char padChar) {
        this.value = Strings.padStart("", size, padChar);
    }

    @Override
    public String name() {
        return "filler";
    }

    @Override
    public int length() {
        return -1;
    }

    @Override
    public String toRawString() {
        return this.value;
    }
}
