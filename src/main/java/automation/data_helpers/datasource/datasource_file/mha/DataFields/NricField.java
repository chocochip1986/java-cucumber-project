package automation.data_helpers.datasource.datasource_file.mha.DataFields;

import automation.data_setup.Phaker;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class NricField extends NricOrFinField {
    public NricField() {
        this.value = Phaker.uniqueOrderNric();
    }

    @Override
    public String name() {
        return "nric";
    }
}
