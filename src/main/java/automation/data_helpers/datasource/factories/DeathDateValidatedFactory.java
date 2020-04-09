package automation.data_helpers.datasource.factories;

import automation.models.datasource.Batch;
import automation.models.datasource.DeathDateValidated;
import org.springframework.stereotype.Component;

@Component
public class DeathDateValidatedFactory extends AbstractFactory {
    public DeathDateValidated create(Batch batch) {
        return DeathDateValidated.create(batch);
    }
}
