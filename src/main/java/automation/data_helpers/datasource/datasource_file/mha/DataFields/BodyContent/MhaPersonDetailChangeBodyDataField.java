package automation.data_helpers.datasource.datasource_file.mha.DataFields.BodyContent;

import automation.data_helpers.datasource.datasource_file.DataField;
import automation.data_helpers.datasource.datasource_file.mha.DataFields.DateField;
import automation.data_helpers.datasource.datasource_file.mha.DataFields.ItemChangedAndValueField;
import automation.data_helpers.datasource.datasource_file.mha.DataFields.NricField;
import automation.data_helpers.datasource.datasource_file.mha.DataFields.PersonDetailItemChangedField;
import automation.data_setup.Phaker;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.List;

@SuperBuilder
public class MhaPersonDetailChangeBodyDataField extends MhaBodyDataField {

    public List<DataField> bodyContent;
    public NricField nric;
    public PersonDetailItemChangedField itemChanged;
    public DataField valueOfItemChanged;
    public DateField dateOfChange;

    public MhaPersonDetailChangeBodyDataField(List<DataField> body) {
        this.bodyContent = body;
    }

    public MhaPersonDetailChangeBodyDataField() {
        this.setBodyContent();
    }

    public void setBodyContent() {
        this.nric = NricField.builder().value(Phaker.uniqueOrderNric()).build();
        final ItemChangedAndValueField itemChangedAndValue = new ItemChangedAndValueField();
        this.itemChanged = itemChangedAndValue.itemChanged;
        this.valueOfItemChanged = itemChangedAndValue.itemChangedValue;
        this.dateOfChange = new DateField();
    }

    private void refreshContent() {
        this.bodyContent = Arrays.asList(
                this.nric,
                this.itemChanged,
                this.valueOfItemChanged,
                this.dateOfChange
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
        refreshContent();
        return this.bodyContent.stream().map(i -> i.toRawString()).reduce("", String::concat);
    }
}
