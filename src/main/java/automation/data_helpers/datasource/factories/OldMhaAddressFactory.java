package automation.data_helpers.datasource.factories;

import automation.models.datasource.OldMhaAddress;
import org.springframework.stereotype.Component;

@Component
public class OldMhaAddressFactory {
    public OldMhaAddress create() {
        return OldMhaAddress.create();
    }
}
