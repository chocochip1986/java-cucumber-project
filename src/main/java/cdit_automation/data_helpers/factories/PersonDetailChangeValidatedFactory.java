package cdit_automation.data_helpers.factories;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.datasource.PersonDetailDataItemChangedEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.datasource.Batch;
import cdit_automation.models.datasource.PersonDetailChangeValidated;
import org.springframework.stereotype.Component;

@Component
public class PersonDetailChangeValidatedFactory extends AbstractFactory {
    public PersonDetailChangeValidated create(Batch batch) {
        switch(PersonDetailDataItemChangedEnum.pick()) {
            case NAME:
                return PersonDetailChangeValidated.createName(batch, Phaker.validNric(), Phaker.validName(), Phaker.validPastDate());
            case GENDER:
                return PersonDetailChangeValidated.createGender(batch, Phaker.validNric(), Phaker.validGender().getValue(), Phaker.validPastDate());
            case DATE_OF_BIRTH:
                return PersonDetailChangeValidated.createBoD(batch, Phaker.validNric(), Phaker.validPastDate().format(Phaker.DATETIME_FORMATTER_YYYYMMDD), Phaker.validPastDate());
            default:
                throw new TestFailException("Unsupported Person Detail Change Category for validated model creation!");
        }
    }
}
