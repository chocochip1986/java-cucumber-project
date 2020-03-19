package cdit_automation.step_definition;

import cdit_automation.data_setup.Phaker;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.PersonId;
import io.cucumber.java.en.Given;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class UserDataHelperSteps extends  AbstractSteps {

    @Given("^There is (?:an|a) (SC|PR|FR) person$")
    public void thereIsAnPerson(String residentialStatus) {
        System.out.println("Residential Status: "+residentialStatus);
        PersonId personId = personFactory.createNewPersonId(residentialStatus);
        log.info("Created personId: "+personId.getNaturalId());
    }

    @Given("^there are (\\d+) Singaporeans(?: who passed away within the year (\\d{4}))?$")
    public void thereAreSingaporeans(int numOfSCs, Integer deathYear) {
        log.info("Creating "+numOfSCs+" Singaporeans in Datasource...");
        if ( deathYear != null && deathYear > dateUtils.now().getYear() ) {
            throw new TestFailException("No future death year please!");
        }
        List<String> listOfPpl = new ArrayList<>();
        for ( int i = 0 ; i < numOfSCs ; i++ ) {
            PersonId personId = personFactory.createNewSCPersonId();
            if ( deathYear != null ) {
                if ( deathYear == LocalDate.now().getYear() ) {
                    personDetailRepo.updateDeathDateForPerson(Phaker.validDate(LocalDate.of(deathYear, 1, 1), LocalDate.now()), personId.getPerson());
                } else {
                    personDetailRepo.updateDeathDateForPerson(Phaker.validDate(LocalDate.of(deathYear, 1, 1), LocalDate.of(deathYear, 12, 31)), personId.getPerson());
                }
                onlyRecordPplWhoPassedAwayFromTheStartOfLastYear(listOfPpl, deathYear, personId);
            } else {
                listOfPpl.add(personId.getNaturalId());
            }
        }

        if (testContext.contains("listOfPpl")) {
            List<String> newBody = testContext.remove("listOfPpl");
            newBody.addAll(listOfPpl);
            testContext.set("listOfPpl", newBody);
        }
        else {
            testContext.set("listOfPpl", listOfPpl);
        }
    }

    private void onlyRecordPplWhoPassedAwayFromTheStartOfLastYear(List<String> listOfPpl, Integer deathYear, PersonId personId){
        if ( deathYear > dateUtils.now().getYear() - 2 ) {
            listOfPpl.add(personId.getNaturalId());
        }
    }
}
