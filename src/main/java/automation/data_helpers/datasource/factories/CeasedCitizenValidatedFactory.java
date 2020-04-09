package automation.data_helpers.datasource.factories;

import automation.models.datasource.Batch;
import automation.models.datasource.CeasedCitizenValidated;
import org.springframework.stereotype.Component;

@Component
public class CeasedCitizenValidatedFactory extends AbstractFactory {
    public CeasedCitizenValidated create(Batch batch) {
        return CeasedCitizenValidated.create(batch);
    }
}
