package cds_automation.data_helpers.datasource.factories;

import cds_automation.models.datasource.Batch;
import cds_automation.models.datasource.CeasedCitizenValidated;
import org.springframework.stereotype.Component;

@Component
public class CeasedCitizenValidatedFactory extends AbstractFactory {
    public CeasedCitizenValidated create(Batch batch) {
        return CeasedCitizenValidated.create(batch);
    }
}
