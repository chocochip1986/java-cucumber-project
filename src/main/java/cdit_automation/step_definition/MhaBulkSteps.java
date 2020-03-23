package cdit_automation.step_definition;

import cdit_automation.enums.FileTypeEnum;
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

    @Given("the mha bulk file is being created with no records")
    public void theMhaBulkFileIsBeingCreatedWithNoRecords() {
        batchFileDataWriter.begin(mhaBulkFileDataPrep.generateDoubleHeader(), FileTypeEnum.MHA_BULK_CITIZEN, null);
        batchFileDataWriter.end();
    }
}
