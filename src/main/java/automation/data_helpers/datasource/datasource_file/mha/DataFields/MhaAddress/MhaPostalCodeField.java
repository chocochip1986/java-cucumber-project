package automation.data_helpers.datasource.datasource_file.mha.DataFields.MhaAddress;

import automation.data_helpers.datasource.datasource_file.mha.DataFields.NcaAddress.NcaPostalCodeField;
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
