package cdit_automation.data_helpers.datasource.datasource_file.mha.DataFields.MhaAddress;

import cdit_automation.data_helpers.datasource.datasource_file.DataField;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.List;

@SuperBuilder
public class MhaAddressField extends DataField {
    List<DataField> value;

    public MhaAddressField() {
        this.setValue(faker.random().nextBoolean());
    }

    public MhaAddressField(boolean isOverseasAddress) {
        this.setValue(isOverseasAddress);
    }

    private void setValue(boolean isOverseasAddress) {
        this.value = Arrays.asList(
                isOverseasAddress ? new MhaAddressTypeField("C") : new MhaAddressTypeField(),
                new MhaBlockNoField(),
                new MhaStreetNameField(),
                new MhaFloorNoField(),
                new MhaUnitNoField(),
                new MhaBuildingNameField(),
                new MhaPostalCodeField(),
                new MhaNewPostalCodeField()
        );
    }

    @Override
    public String name() {
        return "mhaAddress";
    }

    @Override
    public int length() {
        return 90;
    }

    @Override
    public String toRawString() {
        return value.stream().map(i -> i.toRawString()).reduce("", String::concat);
    }
}
