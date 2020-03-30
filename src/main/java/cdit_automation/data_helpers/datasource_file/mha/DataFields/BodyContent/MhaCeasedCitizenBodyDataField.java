package cdit_automation.data_helpers.datasource_file.mha.DataFields.BodyContent;

import cdit_automation.data_helpers.datasource_file.DataField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.AddressDataField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.CitizenshipAttainmentDateField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.DateField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.DateOfAddressChangeField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.DateOfBirthField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.FinField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.GenderField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.InvalidAddressTagField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.NameField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.NationalityField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.NricField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.NricFinAttainmentDateFields;
import cdit_automation.data_setup.Phaker;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SuperBuilder
public class MhaCeasedCitizenBodyDataField extends MhaBodyDataField {

    public List<DataField> bodyContent;
    public NricField nric;
    public NameField name;
    public DataField nationality;
    public DateField ceasedDate;

    public MhaCeasedCitizenBodyDataField(List<DataField> body) {
        this.bodyContent = body;
    }

    public MhaCeasedCitizenBodyDataField() {
        this.setBodyContent();
    }

    public void setBodyContent() {
        this.nric = new NricField();
        this.name = new NameField();
        this.nationality = new NationalityField();
        this.ceasedDate = new DateField();
    }

    private void refreshContent() {
        this.bodyContent = Arrays.asList(
                this.nric,
                this.name,
                this.nationality,
                this.ceasedDate
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
