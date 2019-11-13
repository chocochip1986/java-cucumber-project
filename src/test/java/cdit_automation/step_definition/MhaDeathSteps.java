package cdit_automation.step_definition;

import cdit_automation.enums.FileTypeEnum;
import cdit_automation.models.FileDetail;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.assertj.core.util.Lists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MhaDeathSteps extends AbstractSteps {
    @Given("^the mha death file is empty$")
    public void theMhaDeathFileIsEmpty() throws IOException {
        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_DUAL_CITIZEN);
        testContext.set("fileReceived", batchFileCreator.fileCreator(fileDetail, "mha_death_date"));

        List<String> listOfIdentifiersToWriteToFile = new ArrayList<>();;
        List<String> body = Lists.emptyList();

        listOfIdentifiersToWriteToFile.add(mhaDualCitizenFileDataPrep.generateDoubleHeader());
        listOfIdentifiersToWriteToFile.addAll(body);
        listOfIdentifiersToWriteToFile.add(String.valueOf(body.size()));

        batchFileCreator.writeToFile("mha_death_date.txt", listOfIdentifiersToWriteToFile);
    }

    @Given("^the mha death file has the following details:$")
    public void theMhaDeathFileHasTheFollowingDetails(DataTable table) {

    }
}
