package automation.data_helpers.datasource.datasource_file.mha.DataFields;

import automation.data_helpers.datasource.datasource_file.DataField;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
