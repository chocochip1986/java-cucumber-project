package cdit_automation.data_helpers.factories;

import cdit_automation.models.datasource.Batch;
import cdit_automation.models.datasource.NewCitizenValidated;
import org.springframework.stereotype.Component;

@Component
public class NewCitizenValidatedFactory extends AbstractFactory {
    public NewCitizenValidated createValidNewCitizenValidatedRecord(Batch batch) {
        return NewCitizenValidated.builder().build();
    }
}
