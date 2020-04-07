package cdit_automation.data_helpers.datasource.factories;

import cdit_automation.models.datasource.Batch;
import cdit_automation.models.datasource.DeathDateValidated;
import org.springframework.stereotype.Component;

@Component
public class DeathDateValidatedFactory extends AbstractFactory {
    public DeathDateValidated create(Batch batch) {
        return DeathDateValidated.create(batch);
    }
}
