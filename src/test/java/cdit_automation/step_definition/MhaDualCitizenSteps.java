package cdit_automation.step_definition;

import cdit_automation.asserts.Assert;
import cdit_automation.exceptions.TestDataSetupErrorException;
import cdit_automation.models.PersonId;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Ignore
public class MhaDualCitizenSteps extends AbstractSteps {

    @Given("^there (?:is|are) (\\d+) existing dual citizen(?:s)?$")
    public void createExistingDualCitizen(int numOfDualCitizens) {
        log.info("Creating "+numOfDualCitizens+" existing dual citizens");
        Map<String, PersonId> hashOfDCs = new HashMap<>();
        for ( int i = 0 ; i < numOfDualCitizens ; i++ ) {
            PersonId personId = personIdService.createDualCitizen();
            hashOfDCs.put("personId"+String.valueOf(i), personId);
        }

        testContext.set("hashOfDCs", hashOfDCs);
    }

    @And("^I verify that the dual citizens exists$")
    public void iVerifyThatTheDualCitizensExists() {
        log.info("Verifying that the dual citizens exists");
        if ( testContext.doNotContain("hashOfDCs") ) {
            throw new TestDataSetupErrorException("No such variable as hashOfDCs stored in TestContext!");
        }

        Map<String, PersonId> hashOfDCs = testContext.get("hashOfDCs");
        for ( int i = 0 ; i < hashOfDCs.keySet().size() ; i++ ) {
            PersonId expectedPersonId = hashOfDCs.get("personId"+String.valueOf(i));
            PersonId actualPersonId = personIdRepo.findByNaturalId(expectedPersonId.getNaturalId());

            Assert.assertNotNull(actualPersonId, "No such person id db:" +expectedPersonId.getNaturalId());
            Assert.assertTrue(expectedPersonId.getNaturalId(), actualPersonId.getNaturalId(), "No such person in db!");
        }
    }
}
