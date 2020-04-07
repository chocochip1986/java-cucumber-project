package cds_automation.data_helpers.datasource.factories;

import cds_automation.models.datasource.Batch;
import cds_automation.models.datasource.DeathDateValidated;
import org.springframework.stereotype.Component;

@Component
public class DeathDateValidatedFactory extends AbstractFactory {
    public DeathDateValidated create(Batch batch) {
        return DeathDateValidated.create(batch);
    }
}
