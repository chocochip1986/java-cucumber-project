package automation.data_helpers.datasource.datasource_file.mha.DataFields.BodyContent;

import automation.data_helpers.datasource.datasource_file.DataField;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
public class MhaBodyDataField extends DataField {

    public List<DataField> bodyContent;

    public MhaBodyDataField(List<DataField> body) {
        this.bodyContent = body;
    }

    @Override
    public String name() {
        return "body";
    }

    @Override
    public int length() {
        return -1;
    }

    @Override
    public String toRawString() {
        return this.bodyContent.stream().map(i -> i.toRawString()).reduce("", String::concat);
    }
}
