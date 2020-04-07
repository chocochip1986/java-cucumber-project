package cdit_automation.data_helpers.datasource.factories;

import cdit_automation.models.datasource.OldMhaAddress;
import org.springframework.stereotype.Component;

@Component
public class OldMhaAddressFactory {
    public OldMhaAddress create() {
        return OldMhaAddress.create();
    }
}
