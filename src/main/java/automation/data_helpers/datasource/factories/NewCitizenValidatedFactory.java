package automation.data_helpers.datasource.factories;

import automation.models.datasource.Batch;
import automation.models.datasource.NewCitizenValidated;
import org.springframework.stereotype.Component;

@Component
public class NewCitizenValidatedFactory extends AbstractFactory {
    public NewCitizenValidated createValidNewCitizenValidatedRecord(Batch batch) {
        return NewCitizenValidated.builder().build();
    }
}
