package cdit_automation.step_definition;

import cdit_automation.enums.FileTypeEnum;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Ignore
public class MhaBulkSteps extends AbstractSteps {
    @Given("^the mha bulk file has the following details:$")
    public void theMhaBulkFileHasTheFollowingDetails(DataTable table) {
        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_BULK_CITIZEN);
        FileReceived fileReceived = batchFileCreator.fileCreator(fileDetail, FileTypeEnum.MHA_BULK_CITIZEN.getValue().toLowerCase());
        testContext.set("fileReceived", fileReceived);

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
}
