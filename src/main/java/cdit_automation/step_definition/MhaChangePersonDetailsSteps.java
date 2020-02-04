package cdit_automation.step_definition;

import cdit_automation.enums.FileTypeEnum;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Ignore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Ignore
public class MhaChangePersonDetailsSteps extends AbstractSteps{
    @Given("^the mha change in person details file is empty$")
    public void theMhaChangeInPersonDetailsFileIsEmpty() throws IOException {
        log.info("Creating an empty person details file file...");
        batchFileDataWriter.begin(mhaChangePersonDetailsDataPrep.generateDoubleHeader(), FileTypeEnum.MHA_PERSON_DETAIL_CHANGE, null);
        batchFileDataWriter.end();
    }

    @Given("the mha person details file has the following details:")
    public void theMhaPersonDetailsFileHasTheFollowingDetails(DataTable dataTable) throws IOException {
        batchFileDataWriter.begin(mhaChangePersonDetailsDataPrep.generateDoubleHeader(), FileTypeEnum.MHA_PERSON_DETAIL_CHANGE, null);

        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        mhaChangePersonDetailsDataPrep.createBodyOfTestScenarios(list);

        batchFileDataWriter.end();
    }

    @Given("^the mha person details file is being created$")
    public void theMhaPersonDetailsFileIsBeingCreated() {
        batchFileDataWriter.begin(mhaChangePersonDetailsDataPrep.generateDoubleHeader(), FileTypeEnum.MHA_PERSON_DETAIL_CHANGE, null);
    }
}
