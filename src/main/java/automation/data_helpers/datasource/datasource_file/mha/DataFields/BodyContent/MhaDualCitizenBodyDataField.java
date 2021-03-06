package automation.data_helpers.datasource.datasource_file.mha.DataFields.BodyContent;

import automation.data_helpers.datasource.datasource_file.DataField;
import automation.data_helpers.datasource.datasource_file.mha.DataFields.NricField;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.List;

@SuperBuilder
public class MhaDualCitizenBodyDataField extends MhaBodyDataField {

    public List<DataField> bodyContent;
    public NricField nric;

    public MhaDualCitizenBodyDataField(List<DataField> body) {
        this.bodyContent = body;
    }

    public MhaDualCitizenBodyDataField() {
        this.setBodyContent();
    }

    public void setBodyContent() {
        this.nric = new NricField();

        refreshBody();
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
        refreshBody();
        return this.bodyContent.stream().map(i -> i.toRawString()).reduce("", String::concat);
    }

    private void refreshBody() {
        this.bodyContent = Arrays.asList(
                this.nric
        );
    }
}
