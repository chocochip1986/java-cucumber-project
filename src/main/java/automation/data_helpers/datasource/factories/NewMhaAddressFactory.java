package automation.data_helpers.datasource.factories;

import automation.models.datasource.NewMhaAddress;
import org.springframework.stereotype.Component;

@Component
public class NewMhaAddressFactory {
    public NewMhaAddress create() {
        return NewMhaAddress.create();
    }
}
