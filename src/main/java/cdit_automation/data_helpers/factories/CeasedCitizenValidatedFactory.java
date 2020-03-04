package cdit_automation.data_helpers.factories;

import cdit_automation.models.Batch;
import cdit_automation.models.CeasedCitizenValidated;
import org.springframework.stereotype.Component;

@Component
public class CeasedCitizenValidatedFactory extends AbstractFactory {
    public CeasedCitizenValidated create(Batch batch) {
        return CeasedCitizenValidated.create(batch);
    }
}
