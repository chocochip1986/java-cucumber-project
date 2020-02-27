package cdit_automation.step_definition;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.BatchStatusEnum;
import cdit_automation.enums.FileStatusEnum;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.SpringJobStatusEnum;
import cdit_automation.enums.views.FileStatusSubTextEnum;
import cdit_automation.models.Batch;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import cdit_automation.models.JobExecution;
import cdit_automation.models.JobExecutionParams;
import io.cucumber.java.en.Given;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class DatasourceFileDataSteps extends AbstractSteps {
    @Given("^There are ([0-9]+) files that were previously processed by Datasource$")
    public void thereAreMHAFilesThatWerePreviouslyProcessedByDatasource(int count) {
        log.info("Creating {} files randomly...", count);
        for ( int i = 0 ; i < count ; i++ ) {
            generateFileReceived(BatchStatusEnum.randomValidBatchStatusEnum(), count);
        }
    }
    @Given("^There is a file of each current status type processed by Datasource$")
    public void thereAreOneFileForEachTypeOfCurrentStatus() {
        // Processing, Failed File, Exceeded Error Rate, Rejected File, System Error, 100% Pass
        Map<FileStatusSubTextEnum, BatchStatusEnum> currentStatusToBatchStatusMap
                = new HashMap<FileStatusSubTextEnum, BatchStatusEnum>(){{
                    put(FileStatusSubTextEnum.PROCESSING, BatchStatusEnum.FILE_CHECKED);
                    put(FileStatusSubTextEnum.FAILED_FILE, BatchStatusEnum.FILE_ERROR);
                    put(FileStatusSubTextEnum.EXCEEDED_ERROR_RATE, BatchStatusEnum.ERROR_RATE_ERROR);
                    put(FileStatusSubTextEnum.REJECTED_FILE, BatchStatusEnum.MAPPING_ERROR);
                    put(FileStatusSubTextEnum.SYSTEM_ERROR, BatchStatusEnum.INIT_ERROR);
                    put(FileStatusSubTextEnum.HUNDRED_PERCENT_PASS, BatchStatusEnum.CLEANUP);
        }};
        testContext.set("currentStatusToBatchStatusMap", currentStatusToBatchStatusMap);

        log.info("Creating file for each current status type...");
        for ( BatchStatusEnum batchStatusEnum : currentStatusToBatchStatusMap.values() ) {
            long id = 1;
            generateFileReceived(batchStatusEnum, id);
            id++;
        }
    }

    private void generateFileReceived(BatchStatusEnum batchStatusEnum, long id) {
        Batch batch = Batch.create(batchStatusEnum);

        // Build/Get FileDetail
        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.randomValidFileTypeEnum());

        // Build FileReceived
        FileReceived fileReceived = FileReceived.builder()
                .fileDetail(fileDetail)
                .filePath("/subdir1/subdir2/subdir3/"+fileDetail.getFileName()+".txt")
                .receivedTimestamp(dateUtils.beginningOfDayToTimestamp(Phaker.validPastDate()))
                .batches(new ArrayList<Batch>(){{ add(batch); }})
                .fileSize(Double.valueOf(Phaker.validNumber(6)))
                .fileStatusEnum(FileStatusEnum.randomValidFileStatusEnum())
                .build();

        batch.setFileReceived(fileReceived);

        fileReceived = fileReceivedRepo.save(fileReceived);
        batchRepo.save(batch);

        // Build and Save JobExecutionParam
        JobExecutionParams jobExecutionParams =
                JobExecutionParams.builder()
                        .id(Long.valueOf(id))
                        .keyName("fileReceivedId")
                        .longVal(fileReceived.getId())
                        .build();
        jobExecutionParams = batchJobExecutionParamsRepo.save(jobExecutionParams);

        // Build and Save JobExecution
        JobExecution jobExecution =
                JobExecution.builder()
                        .id(jobExecutionParams.getId())
                        .status(SpringJobStatusEnum.COMPLETED)
                        .build();
        batchJobExecutionRepo.save(jobExecution);
    }
}
