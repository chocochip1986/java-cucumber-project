package cdit_automation.step_definition;

import cdit_automation.models.PersonId;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class UserDataHelperSteps extends  AbstractSteps {

    @Given("^There is (?:an|a) (SC|PR|FR) person$")
    public void thereIsAnPerson(String residentialStatus) {
        System.out.println("Residential Status: "+residentialStatus);
        PersonId personId = personIdService.createNewPersonId(residentialStatus);
        log.info("Created personId: "+personId.getNaturalId());
    }
}