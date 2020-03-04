package cdit_automation.data_helpers.factories;

import cdit_automation.models.Batch;
import cdit_automation.models.DeathDateValidated;
import org.springframework.stereotype.Component;

@Component
public class DeathDateValidatedFactory extends AbstractFactory {
    public DeathDateValidated create(Batch batch) {
        return DeathDateValidated.create(batch);
    }
}
