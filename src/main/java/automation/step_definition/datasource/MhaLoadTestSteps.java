package automation.step_definition.datasource;

import automation.data_helpers.datasource.datasource_file.mha.DataFields.FooterOfBodyCountField;
import automation.data_helpers.datasource.datasource_file.mha.MhaBulkFile;
import automation.data_helpers.datasource.datasource_file.mha.TestDataStruct.LoadTestData;
import automation.enums.datasource.FileTypeEnum;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.util.List;

@Slf4j
@Ignore
public class MhaLoadTestSteps extends AbstractSteps {
    @Given("Mha load testing files:")
    public void mhaLoadTestingFiles(DataTable table) {
        final List<LoadTestData> list = table.asList(LoadTestData.class);
        final int numberOfRecords = list.size();

        list.stream().forEach(i -> {
            if (i.getFileType() == FileTypeEnum.MHA_BULK_CITIZEN) {
                MhaBulkFile bulkFile = new MhaBulkFile();
                bulkFile.fileName = i.fileName;
                bulkFile.footer = new FooterOfBodyCountField(i.count);
                bulkFile.createLoadTestFile(i.count);
            }
        });
    }

    @When("uploading these files in sequence to S3 with logging of timestamp:")
    public void uploadTheseFilesInSequenceToS3WithLoggingOfTimestamp(DataTable dataTable) {
        int x = 0;
    }

}