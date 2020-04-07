package cds_automation.data_helpers.datasource.factories;

import cds_automation.models.datasource.OldNcaAddress;
import org.springframework.stereotype.Component;

@Component
public class OldNcaAddressFactory extends AbstractFactory {
    public OldNcaAddress create() {
        return OldNcaAddress.create();
    }
}
