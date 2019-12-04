package cdit_automation.step_definition;

import cdit_automation.models.PersonId;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Ignore
public class UserDataHelperSteps extends  AbstractSteps {

    @Given("^There is (?:an|a) (SC|PR|FR) person$")
    public void thereIsAnPerson(String residentialStatus) {
        System.out.println("Residential Status: "+residentialStatus);
        PersonId personId = personFactory.createNewPersonId(residentialStatus);
        log.info("Created personId: "+personId.getNaturalId());
    }

    @Given("^there are (\\d+) Singaporeans(?: who passed away in the year (\\d+))?$")
    public void thereAreSingaporeans(int numOfSCs, int deathYear) {
        log.info("Creating "+numOfSCs+" Singaporeans in Datasource...");
        List<String> listOfPpl = new ArrayList<>();
        for ( int i = 0 ; i < numOfSCs ; i++ ) {
            listOfPpl.add(personFactory.createNewSCPersonId().getNaturalId());
        }
        testContext.set("listOfPpl", listOfPpl);
    }
}
