package cdit_automation.data_helpers.datasource_file.mha.DataFields;

import cdit_automation.data_setup.Phaker;
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
