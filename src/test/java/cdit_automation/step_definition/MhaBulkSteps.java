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
        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_DEATH_DATE);
        FileReceived fileReceived = batchFileCreator.fileCreator(fileDetail, "mha_bulk_citizen");
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
    public void theMhaBulkFileIsCreated() throws IOException {
        List<String> body = testContext.get("body");
        List<String> listOfIdentifiersToWriteToFile = new ArrayList<>();

        listOfIdentifiersToWriteToFile.add(mhaDeathDateFileDataPrep.generateDoubleHeader());
        listOfIdentifiersToWriteToFile.addAll(body);
        listOfIdentifiersToWriteToFile.add(String.valueOf(body.size()));

        batchFileCreator.writeToFile("mha_bulk_citizen.txt", listOfIdentifiersToWriteToFile);

        testContext.set("listOfIdentifiersToWriteToFile", listOfIdentifiersToWriteToFile);
    }
}
