package cdit_automation.data_helpers.datasource_file.mha.DataFields.BodyContent;

import cdit_automation.data_helpers.datasource_file.DataField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.AddressDataField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.CitizenshipAttainmentDateField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.DateOfAddressChangeField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.DateOfBirthField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.FinField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.GenderField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.InvalidAddressTagField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.NameField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.NricField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.NricFinAttainmentDateFields;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.NricOrFinField;
import cdit_automation.data_setup.Phaker;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SuperBuilder
public class MhaChangeAddressBodyDataField extends MhaBodyDataField {

    public List<DataField> bodyContent;
    public NricOrFinField uid;
    public AddressDataField oldAddress;
    public DataField dateOfAddressChange;
    public AddressDataField newAddress;

    public MhaChangeAddressBodyDataField(List<DataField> body) {
        this.bodyContent = body;
    }

    public MhaChangeAddressBodyDataField() {
        this.setBodyContent();
    }

    public void setBodyContent() {
        this.uid = new NricOrFinField();
        this.oldAddress =new AddressDataField();
        this.newAddress =new AddressDataField();
        this.dateOfAddressChange =new DateOfAddressChangeField();
        refreshBody();
    }

    private void refreshBody() {
        this.bodyContent = Arrays.asList(
                this.uid,
                this.oldAddress,
                this.newAddress,
                this.dateOfAddressChange
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
