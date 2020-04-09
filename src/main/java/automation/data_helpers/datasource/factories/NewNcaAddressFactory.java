package automation.data_helpers.datasource.factories;

import automation.models.datasource.NewNcaAddress;
import org.springframework.stereotype.Component;

@Component
public class NewNcaAddressFactory extends AbstractFactory {
    public NewNcaAddress create() {
        return NewNcaAddress.create();
    }
}
