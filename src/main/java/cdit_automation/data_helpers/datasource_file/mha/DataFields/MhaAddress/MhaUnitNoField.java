package cdit_automation.data_helpers.datasource_file.mha.DataFields.MhaAddress;

import cdit_automation.data_helpers.datasource_file.mha.DataFields.NcaAddress.NcaUnitNoField;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class MhaUnitNoField extends NcaUnitNoField {
    public MhaUnitNoField() {
        super();
    }

    @Override
    public String name() {
        return "mhaUnitNo";
    }
}
