package cdit_automation.step_definition;

import cdit_automation.enums.FileTypeEnum;
import cdit_automation.models.Nationality;
import cdit_automation.models.PersonDetail;
import cdit_automation.models.PersonId;
import cdit_automation.models.PersonName;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.util.List;

@Slf4j
@Ignore
public class MhaBulkSteps extends AbstractSteps {
    @Given("^the mha bulk file has the following details:$")
    public void theMhaBulkFileHasTheFollowingDetails(DataTable table) {
        List<String> entries = table.asList();
        List<String> body = mhaBulkFileDataPrep.createBodyOfTestScenarios(entries, testContext);

        if (testContext.contains("body")) {
            List<String> newBody = testContext.remove("body");
            newBody.addAll(body);
            testContext.set("body", newBody);
        }
        else {
            testContext.set("body", body);
        }
    }

    @And("the mha bulk file is created")
    public void theMhaBulkFileIsCreated() {
        batchFileDataWriter.end();
    }

    @Given("^the mha bulk file is being created$")
    public void theMhaBulkFileIsBeingCreated() {
        batchFileDataWriter.begin(mhaBulkFileDataPrep.generateDoubleHeader(), FileTypeEnum.MHA_BULK_CITIZEN, null);
    }


    @And("^I verify that person with ([S|T|F|G][0-9]{7}[A-Z]) is persisted in Datasource$")
    public void iVerifyThatPersonWithFXIsPersistedInDatasource(String identifier) {
        log.info("Veryifing records of person with "+identifier);
        PersonId personId = personIdRepo.findByNaturalId(identifier);

        testAssert.assertNotNull(personId, "No PersonId record for "+identifier);
        testAssert.assertNotNull(personId.getPerson(), "No Person record for "+identifier);

        Nationality nationality = nationalityRepo.findNationalityByPerson(personId.getPerson());
        testAssert.assertNotNull(nationality, "No Nationality record for "+personId.getNaturalId());

        PersonName personName = personNameRepo.findByPerson(personId.getPerson());
        testAssert.assertNotNull(personName, "No PersonName record for "+personId.getNaturalId());

        PersonDetail personDetail = personDetailRepo.findByPerson(personId.getPerson());
        testAssert.assertNotNull(personDetail, "No Person Detail record for "+personId.getNaturalId());
    }
}
