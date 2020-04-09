package automation.data_helpers.datasource.datasource_file.mha.DataFields.MhaAddress;

import automation.data_helpers.datasource.datasource_file.mha.DataFields.NcaAddress.NcaBlockNoField;
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
