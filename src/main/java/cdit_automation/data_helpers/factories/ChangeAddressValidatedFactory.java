package cdit_automation.data_helpers.factories;

import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.datasource.Batch;
import cdit_automation.models.datasource.ChangeAddressValidated;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class ChangeAddressValidatedFactory extends AbstractFactory {
    public final int[] ARRAY_OF_CHOICES = new int[]{1,2,3,4};
    public ChangeAddressValidated create(Batch batch) {
        int choice = ARRAY_OF_CHOICES[new Random().nextInt(ARRAY_OF_CHOICES.length)];
        switch(choice) {
            case 1:
                return ChangeAddressValidated.createOldMhaNewMha(batch, oldMhaAddressFactory.create(), newMhaAddressFactory.create());
            case 2:
                return ChangeAddressValidated.createOldNcaNewMha(batch, oldNcaAddressFactory.create(), newMhaAddressFactory.create());
            case 3:
                return ChangeAddressValidated.createOldMhaNewNca(batch, oldMhaAddressFactory.create(), newNcaAddressFactory.create());
            case 4:
                return ChangeAddressValidated.createOldNcaNewNca(batch, oldNcaAddressFactory.create(), newNcaAddressFactory.create());
            default:
                throw new TestFailException("Unable to create Change Address validated record due to invalid choice: "+choice);
        }
    }
}
