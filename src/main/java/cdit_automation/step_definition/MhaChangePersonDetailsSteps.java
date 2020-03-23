package cdit_automation.step_definition;

import cdit_automation.enums.FileTypeEnum;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class MhaChangePersonDetailsSteps extends AbstractSteps{
    @Given("^the mha change in person details file is empty$")
    public void theMhaChangeInPersonDetailsFileIsEmpty() {
        log.info("Creating an empty person details file file...");
        batchFileDataWriter.begin(mhaChangePersonDetailsDataPrep.generateSingleHeader(), FileTypeEnum.MHA_PERSON_DETAIL_CHANGE, null);
        batchFileDataWriter.end();
    }

    @Given("the mha person details file has the following details:")
    public void theMhaPersonDetailsFileHasTheFollowingDetails(DataTable dataTable) throws IOException {
        batchFileDataWriter.begin(mhaChangePersonDetailsDataPrep.generateSingleHeader(), FileTypeEnum.MHA_PERSON_DETAIL_CHANGE, null);

        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        mhaChangePersonDetailsDataPrep.createBodyOfTestScenarios(list);

        batchFileDataWriter.end();
    }

    @Given("^the mha person details file is being created$")
    public void theMhaPersonDetailsFileIsBeingCreated() {
        batchFileDataWriter.begin(mhaChangePersonDetailsDataPrep.generateSingleHeader(), FileTypeEnum.MHA_PERSON_DETAIL_CHANGE, null);
    }
}
