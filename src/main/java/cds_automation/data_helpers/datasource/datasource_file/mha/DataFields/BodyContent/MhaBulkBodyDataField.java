package cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.BodyContent;

import cds_automation.data_helpers.datasource.datasource_file.DataField;
import cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.AddressDataField;
import cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.BulkNricFinBirthdayAttainmentDateFields;
import cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.CitizenshipAttainmentDateField;
import cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.DateOfBirthField;
import cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.DateOfDeathField;
import cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.FinField;
import cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.GenderField;
import cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.InvalidAddressTagField;
import cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.NameField;
import cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.NricField;
import cds_automation.data_setup.Phaker;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SuperBuilder
public class MhaBulkBodyDataField extends MhaBodyDataField {

    public List<DataField> bodyContent;
    public NricField nric;
    public FinField fin;
    public NameField name;
    public DateOfBirthField dateOfBirth;
    public DateOfDeathField dateOfDeath;
    public GenderField gender;
    public AddressDataField address;
    public InvalidAddressTagField invalidAddressTag;
    public CitizenshipAttainmentDateField attainmentDate;

    public MhaBulkBodyDataField(List<DataField> body) {
        this.bodyContent = body;
    }

    public MhaBulkBodyDataField() {
        this.setBodyContent();
    }

    public void setBodyContent() {
        final BulkNricFinBirthdayAttainmentDateFields logicalBulkFields = new BulkNricFinBirthdayAttainmentDateFields();
        final LocalDate attainment = logicalBulkFields.attainmentDate.value.isEmpty() ? LocalDate.now() : LocalDate.parse(logicalBulkFields.attainmentDate.value, Phaker.DATETIME_FORMATTER_YYYYMMDD);
        final String attainmentString = logicalBulkFields.attainmentDate.value.isEmpty() ? "" : attainment.format(Phaker.DATETIME_FORMATTER_YYYYMMDD);

        this.nric =logicalBulkFields.nric;
        this.fin =logicalBulkFields.fin;
        this.name =new NameField();
        this.dateOfBirth = logicalBulkFields.dateOfBirth;
        this.dateOfDeath = logicalBulkFields.dateOfDeath;
        this.gender =new GenderField();
        this.address =new AddressDataField();
        this.invalidAddressTag =new InvalidAddressTagField();
        this.attainmentDate = logicalBulkFields.attainmentDate;

        refreshBody();
    }

    private void refreshBody() {
        this.bodyContent = Arrays.asList(
                this.nric,
                this.fin,
                this.name,
                this.dateOfBirth,
                this.dateOfDeath,
                this.gender,
                this.address,
                this.invalidAddressTag,
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
