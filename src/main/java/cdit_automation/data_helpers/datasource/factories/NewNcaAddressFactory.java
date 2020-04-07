package cdit_automation.data_helpers.datasource.factories;

import cdit_automation.models.datasource.NewNcaAddress;
import org.springframework.stereotype.Component;

@Component
public class NewNcaAddressFactory extends AbstractFactory {
    public NewNcaAddress create() {
        return NewNcaAddress.create();
    }
}
