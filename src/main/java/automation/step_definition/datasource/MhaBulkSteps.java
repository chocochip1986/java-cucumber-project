package automation.step_definition.datasource;

import automation.data_helpers.datasource.batch_entities.MhaBulkCitizenFileEntry;
import automation.enums.datasource.FileTypeEnum;
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
        } else {
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

    @Given("^the mha bulk file is created with DateOfRun (.*) and CutOffDate (.*) with one record$")
    public void theMhaBulkFileIsCreatedWithDateOfRunDateOfRunAndCutOffDateCutOffDate(
            String dateOfRun, String cutOffDate) {
        String resolvedDateOfRun = mhaBulkFileDataPrep.resolveDateString(dateOfRun);
        String resolvedCutOffDate = mhaBulkFileDataPrep.resolveDateString(cutOffDate);
        MhaBulkCitizenFileEntry entry = new MhaBulkCitizenFileEntry("T4945521B", "-", "<AUTO>",
                "19881003", "        ", "M", "C", "C",
                "<AUTO>", "D", "19881003");
        batchFileDataWriter.begin(
                resolvedDateOfRun.concat(resolvedCutOffDate), FileTypeEnum.MHA_BULK_CITIZEN, 1);
        batchFileDataWriter.chunkOrWrite(entry.toRawString());
        batchFileDataWriter.end();
    }

    @Given("MHA send MHA_BULK_CITIZEN file with the following data:")
    public void mhaSendMHA_BULK_CITIZENFileWithTheFollowingData(DataTable dataTable) {
        List<String> formattedEntries = mhaBulkFileDataPrep.formatEntries(dataTable);
        mhaBulkFileDataPrep.writeToFileUsingCurrentDateAsHeader(formattedEntries);
    }
}
