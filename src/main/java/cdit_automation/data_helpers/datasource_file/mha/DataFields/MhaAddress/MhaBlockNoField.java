package cdit_automation.data_helpers.datasource_file.mha.DataFields.MhaAddress;

import cdit_automation.data_helpers.datasource_file.mha.DataFields.NcaAddress.NcaBlockNoField;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class MhaBlockNoField extends NcaBlockNoField {
    public MhaBlockNoField() {
        super();
    }

    @Override
    public String name() {
        return "mhaBlockNo";
    }
}
