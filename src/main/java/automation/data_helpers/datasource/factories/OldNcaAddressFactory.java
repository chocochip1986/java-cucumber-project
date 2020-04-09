package automation.data_helpers.datasource.factories;

import automation.models.datasource.OldNcaAddress;
import org.springframework.stereotype.Component;

@Component
public class OldNcaAddressFactory extends AbstractFactory {
    public OldNcaAddress create() {
        return OldNcaAddress.create();
    }
}
