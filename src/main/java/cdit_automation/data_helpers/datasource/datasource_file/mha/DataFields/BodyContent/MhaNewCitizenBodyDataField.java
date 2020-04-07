package cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.BodyContent;

import cdit_automation.data_helpers.datasource.datasource_file.DataField;
import cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.AddressDataField;
import cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.CitizenshipAttainmentDateField;
import cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.DateOfAddressChangeField;
import cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.DateOfBirthField;
import cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.FinField;
import cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.GenderField;
import cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.InvalidAddressTagField;
import cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.NameField;
import cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.NricField;
import cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.NricFinAttainmentDateFields;
import cdit_automation.data_setup.Phaker;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SuperBuilder
public class MhaNewCitizenBodyDataField extends MhaBodyDataField {

    public List<DataField> bodyContent;
    public NricField nric;
    public FinField fin;
    public NameField name;
    public DateOfBirthField dateOfBirth;
    public GenderField gender;
    public AddressDataField oldAddress;
    public AddressDataField newAddress;
    public InvalidAddressTagField invalidAddressTag;
    public DataField dateOfAddressChange;
    public CitizenshipAttainmentDateField attainmentDate;

    public MhaNewCitizenBodyDataField(List<DataField> body) {
        this.bodyContent = body;
    }

    public MhaNewCitizenBodyDataField() {
        this.setBodyContent();
    }

    public void setBodyContent() {
        final NricFinAttainmentDateFields nricFinAttainmentDateFields = new NricFinAttainmentDateFields();
        final LocalDate attainment = nricFinAttainmentDateFields.attainmentDate.value.isEmpty() ? LocalDate.now() : LocalDate.parse(nricFinAttainmentDateFields.attainmentDate.value, Phaker.DATETIME_FORMATTER_YYYYMMDD);
        final String attainmentString = nricFinAttainmentDateFields.attainmentDate.value.isEmpty() ? "" : attainment.format(Phaker.DATETIME_FORMATTER_YYYYMMDD);

        this.nric =nricFinAttainmentDateFields.nric;
        this.fin =nricFinAttainmentDateFields.fin;
        this.name =new NameField();
        this.dateOfBirth =new DateOfBirthField(false, false, attainment);
        this.gender =new GenderField();
        this.oldAddress =new AddressDataField();
        this.newAddress =new AddressDataField();
        this.invalidAddressTag =new InvalidAddressTagField();
        this.dateOfAddressChange =new DateOfAddressChangeField();
        this.attainmentDate =CitizenshipAttainmentDateField.builder().value(attainmentString).build();

        refreshBody();
    }

    private void refreshBody() {
        this.bodyContent = Arrays.asList(
                this.nric,
                this.fin,
                this.name,
                this.dateOfBirth,
                this.gender,
                this.oldAddress,
                this.newAddress,
                this.invalidAddressTag,
                this.dateOfAddressChange,
                this.attainmentDate
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
