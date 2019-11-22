package cdit_automation.step_definition;

import cdit_automation.enums.FileTypeEnum;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.util.List;
import java.util.Map;

@Slf4j
@Ignore
public class MhaBulkSteps extends AbstractSteps {
    @Given("^the mha bulk file has the following details:$")
    public void theMhaBulkFileHasTheFollowingDetails(DataTable table) {
        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_DEATH_DATE);
        FileReceived fileReceived = batchFileCreator.fileCreator(fileDetail, "mha_bulk_citizen");
        testContext.set("fileReceived", fileReceived);

        List<Map<String, String>> list = table.asMaps(String.class, String.class);
        List<String> body = mhaBulkFileDataPrep.createBodyOfTestScenarios(list, testContext);
    }
}
