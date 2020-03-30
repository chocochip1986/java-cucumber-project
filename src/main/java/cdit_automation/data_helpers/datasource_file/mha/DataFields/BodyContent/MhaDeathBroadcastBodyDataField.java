package cdit_automation.data_helpers.datasource_file.mha.DataFields.BodyContent;

import cdit_automation.data_helpers.datasource_file.DataField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.DateField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.NricOrFinField;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.List;

@SuperBuilder
public class MhaDeathBroadcastBodyDataField extends MhaBodyDataField {

    public List<DataField> bodyContent;
    public NricOrFinField uid;
    public DateField deathDate;

    public MhaDeathBroadcastBodyDataField(List<DataField> body) {
        this.bodyContent = body;
    }

    public MhaDeathBroadcastBodyDataField() {
        this.setBodyContent();
    }

    public void setBodyContent() {
        this.uid = new NricOrFinField();
        this.deathDate = new DateField();

        refreshBody();
    }

    private void refreshBody() {
        this.bodyContent = Arrays.asList(
            this.uid,
            this.deathDate
        );
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
}
