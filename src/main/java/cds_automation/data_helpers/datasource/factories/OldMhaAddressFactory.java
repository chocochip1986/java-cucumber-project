package cds_automation.data_helpers.datasource.factories;

import cds_automation.models.datasource.OldMhaAddress;
import org.springframework.stereotype.Component;

@Component
public class OldMhaAddressFactory {
    public OldMhaAddress create() {
        return OldMhaAddress.create();
    }
}
