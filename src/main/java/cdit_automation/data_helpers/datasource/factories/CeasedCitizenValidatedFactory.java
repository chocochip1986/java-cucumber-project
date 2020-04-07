package cdit_automation.data_helpers.datasource.factories;

import cdit_automation.models.datasource.Batch;
import cdit_automation.models.datasource.CeasedCitizenValidated;
import org.springframework.stereotype.Component;

@Component
public class CeasedCitizenValidatedFactory extends AbstractFactory {
    public CeasedCitizenValidated create(Batch batch) {
        return CeasedCitizenValidated.create(batch);
    }
}
