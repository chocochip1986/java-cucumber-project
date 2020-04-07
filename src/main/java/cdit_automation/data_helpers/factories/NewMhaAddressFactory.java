package cdit_automation.data_helpers.factories;

import cdit_automation.models.datasource.NewMhaAddress;
import org.springframework.stereotype.Component;

@Component
public class NewMhaAddressFactory {
    public NewMhaAddress create() {
        return NewMhaAddress.create();
    }
}
