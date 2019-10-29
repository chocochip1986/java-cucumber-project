package cdit_automation.step_definition;

import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class MhaDualCitizenSteps extends AbstractSteps {

    @Given("^there (?:is|are) (\\d+) existing dual citizen(?:s)?$")
    public void createExistingDualCitizen(int numOfDualCitizens) {
        log.info("Creating "+numOfDualCitizens+" existing dual citizens");

    }
}
