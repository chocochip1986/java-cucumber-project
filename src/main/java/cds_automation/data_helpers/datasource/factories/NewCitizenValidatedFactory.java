package cds_automation.data_helpers.datasource.factories;

import cds_automation.models.datasource.Batch;
import cds_automation.models.datasource.NewCitizenValidated;
import org.springframework.stereotype.Component;

@Component
public class NewCitizenValidatedFactory extends AbstractFactory {
    public NewCitizenValidated createValidNewCitizenValidatedRecord(Batch batch) {
        return NewCitizenValidated.builder().build();
    }
}
