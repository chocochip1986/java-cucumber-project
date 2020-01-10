package cdit_automation.step_definition;

import cdit_automation.enums.FileTypeEnum;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import org.assertj.core.util.Lists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MhaChangePersonDetailsSteps extends AbstractSteps{
    @Given("^the mha change in person details file is empty$")
    public void theMhaChangeInPersonDetailsFileIsEmpty() throws IOException {
        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_PERSON_DETAIL_CHANGE);
        testContext.set("fileReceived", batchFileCreator.replaceFile(fileDetail, FileTypeEnum.MHA_PERSON_DETAIL_CHANGE.getValue().toLowerCase()));

        List<String> listOfIdentifiersToWriteToFile = new ArrayList<>();;
        List<String> body = Lists.emptyList();

        listOfIdentifiersToWriteToFile.add(mhaDualCitizenFileDataPrep.generateDoubleHeader());
        listOfIdentifiersToWriteToFile.addAll(body);
        listOfIdentifiersToWriteToFile.add(String.valueOf(body.size()));

        batchFileCreator.writeToFile(FileTypeEnum.MHA_PERSON_DETAIL_CHANGE.getValue().toLowerCase(), listOfIdentifiersToWriteToFile);
    }

    @Given("the mha person details file has the following details:")
    public void theMhaPersonDetailsFileHasTheFollowingDetails(DataTable dataTable) throws IOException {
        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_PERSON_DETAIL_CHANGE);
        FileReceived fileReceived = batchFileCreator.replaceFile(fileDetail, FileTypeEnum.MHA_PERSON_DETAIL_CHANGE.getValue().toLowerCase());
        testContext.set("fileReceived", fileReceived);

        List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
        List<String> body = mhaChangePersonDetailsDataPrep.createBodyOfTestScenarios(list);

        List<String> listOfIdentifiersToWriteToFile = new ArrayList<>();
        listOfIdentifiersToWriteToFile.add(mhaChangePersonDetailsDataPrep.generateDoubleHeader());
        listOfIdentifiersToWriteToFile.addAll(body);
        listOfIdentifiersToWriteToFile.add(String.valueOf(body.size()));
        batchFileCreator.writeToFile(FileTypeEnum.MHA_PERSON_DETAIL_CHANGE.getValue().toLowerCase(), listOfIdentifiersToWriteToFile);
    }
}
