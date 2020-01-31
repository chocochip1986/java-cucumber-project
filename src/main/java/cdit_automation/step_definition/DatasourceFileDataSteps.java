package cdit_automation.step_definition;

import cdit_automation.enums.BatchStatusEnum;
import cdit_automation.enums.FileStatusEnum;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.models.Batch;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import io.cucumber.java.en.Given;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class DatasourceFileDataSteps extends AbstractSteps {
    @Given("^There are ([0-9]+) MHA files that were previously processed by Datasource$")
    public void thereAreMHAFilesThatWerePreviouslyProcessedByDatasource(int count) {
        log.info("Creating "+count+" MHA files randomly...");
        for ( int i = 0 ; i < count ; i++ ) {
            Batch batch = Batch.builder().status(BatchStatusEnum.CLEANUP).build();

            FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_DUAL_CITIZEN);
            FileReceived fileReceived = FileReceived.builder().fileDetail(fileDetail).filePath("hihi").receivedTimestamp(dateUtils.beginningOfDayToTimestamp(dateUtils.now())).batches(Collections.singletonList(batch)).fileSize(10.0).fileStatusEnum(FileStatusEnum.OK).build();

            fileReceivedRepo.save(fileReceived);
            batchRepo.save(batch);
        }
    }
}
