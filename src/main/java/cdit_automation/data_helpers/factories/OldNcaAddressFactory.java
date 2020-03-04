package cdit_automation.data_helpers.factories;

import cdit_automation.models.OldNcaAddress;
import org.springframework.stereotype.Component;

@Component
public class OldNcaAddressFactory extends AbstractFactory {
    public OldNcaAddress create() {
        return OldNcaAddress.create();
    }
}
