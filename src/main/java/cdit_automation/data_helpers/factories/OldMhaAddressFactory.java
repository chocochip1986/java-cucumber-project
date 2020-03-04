package cdit_automation.data_helpers.factories;

import cdit_automation.models.OldMhaAddress;
import org.springframework.stereotype.Component;

@Component
public class OldMhaAddressFactory {
    public OldMhaAddress create() {
        return OldMhaAddress.create();
    }
}
