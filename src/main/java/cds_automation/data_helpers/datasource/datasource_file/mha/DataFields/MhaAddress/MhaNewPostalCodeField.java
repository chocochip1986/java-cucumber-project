package cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.MhaAddress;

import cds_automation.data_helpers.datasource.datasource_file.mha.DataFields.NcaAddress.NcaNewPostalCodeField;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class MhaNewPostalCodeField extends NcaNewPostalCodeField {
    public MhaNewPostalCodeField() {
        super();
    }

    @Override
    public String name() {
        return "mhaPostalCode";
    }
}
