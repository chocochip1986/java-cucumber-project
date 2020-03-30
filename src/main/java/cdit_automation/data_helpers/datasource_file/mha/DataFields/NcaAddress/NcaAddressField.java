package cdit_automation.data_helpers.datasource_file.mha.DataFields.NcaAddress;

import cdit_automation.data_helpers.datasource_file.DataField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.FillerField;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.List;

@SuperBuilder
public class NcaAddressField extends DataField {
    List<DataField> value;

    public NcaAddressField() {
        this.value = Arrays.asList(
                new NcaAddressTypeField(),
                new NcaBlockNoField(),
                new NcaStreetCodeField(),
                new NcaLevelNoField(),
                new NcaUnitNoField(),
                new NcaPostalCodeField(),
                new NcaNewPostalCodeField(),
                new FillerField(55)
        );
    }

    @Override
    public String name() {
        return "ncaAddress";
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
