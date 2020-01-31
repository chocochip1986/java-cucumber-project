package cdit_automation.step_definition;

import cdit_automation.data_setup.Phaker;
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
    @Given("^There are ([0-9]+) files that were previously processed by Datasource$")
    public void thereAreMHAFilesThatWerePreviouslyProcessedByDatasource(int count) {
        log.info("Creating "+count+" files randomly...");
        for ( int i = 0 ; i < count ; i++ ) {
            Batch batch = Batch.create(BatchStatusEnum.randomValidBatchStatusEnum());

            FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.randomValidFileTypeEnum());
            FileReceived fileReceived = FileReceived.builder()
                    .fileDetail(fileDetail)
                    .filePath("/subdir1/subdir2/subdir3/"+fileDetail.getFileName()+".txt")
                    .receivedTimestamp(dateUtils.beginningOfDayToTimestamp(Phaker.validPastDate()))
                    .batches(Collections.singletonList(batch))
                    .fileSize(Double.valueOf(Phaker.validNumber(10000)))
                    .fileStatusEnum(FileStatusEnum.randomValidFileStatusEnum())
                    .build();

            fileReceivedRepo.save(fileReceived);
            batchRepo.save(batch);
        }
    }
}
