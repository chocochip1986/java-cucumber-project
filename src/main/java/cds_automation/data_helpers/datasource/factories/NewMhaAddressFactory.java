package cds_automation.data_helpers.datasource.factories;

import cds_automation.models.datasource.NewMhaAddress;
import org.springframework.stereotype.Component;

@Component
public class NewMhaAddressFactory {
    public NewMhaAddress create() {
        return NewMhaAddress.create();
    }
}
