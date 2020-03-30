package cdit_automation.data_helpers.datasource_file.mha.DataFields.MhaAddress;

import cdit_automation.data_helpers.datasource_file.mha.DataFields.NcaAddress.NcaPostalCodeField;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class MhaPostalCodeField extends NcaPostalCodeField {
    public MhaPostalCodeField() {
        super();
    }

    @Override
    public String name() {
        return "mhaPostalCode";
    }
}
