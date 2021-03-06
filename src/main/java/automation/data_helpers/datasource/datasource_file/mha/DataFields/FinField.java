package automation.data_helpers.datasource.datasource_file.mha.DataFields;

import automation.data_setup.Phaker;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class FinField extends NricOrFinField {
    public FinField() {
        this.value = Phaker.uniqueOrderFin();
    }

    @Override
    public String name() {
        return "fin";
    }
}
