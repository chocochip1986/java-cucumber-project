package cdit_automation.step_definition;

import cdit_automation.data_helpers.factories.BulkCitizenValidatedFactory;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.BatchStatusEnum;
import cdit_automation.enums.FileStatusEnum;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.SpringJobStatusEnum;
import cdit_automation.enums.views.FileStatusSubTextEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.Batch;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import cdit_automation.models.JobExecution;
import cdit_automation.models.JobExecutionParams;
import cdit_automation.utilities.DateUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class DatasourceFileDataSteps extends AbstractSteps {
    //File Trail Current Step
    private final String LOAD="Load";
    private final String CONTENT="Content";
    private final String FORMAT="Format";

    //File Trail Current Status
    private final String PENDING="Pending";
    private final String URGENT_ACTION= "Urgent Action";
    private final String FOLLOWUP="Follow-up";

    private final BatchStatusEnum[] FORMAT_PENDING = new BatchStatusEnum[]{BatchStatusEnum.INIT, BatchStatusEnum.CHECKING_FILE, BatchStatusEnum.LINE_CHECKED,BatchStatusEnum.FILE_CHECKED};
    private final BatchStatusEnum[] FORMAT_FOLLOWUP = new BatchStatusEnum[]{BatchStatusEnum.LINE_ERROR, BatchStatusEnum.FILE_ERROR, BatchStatusEnum.RAW_DATA_ERROR};
    private final BatchStatusEnum[] FORMAT_URGENT_ACTION = new BatchStatusEnum[]{BatchStatusEnum.INIT_ERROR};

    private final BatchStatusEnum[] CONTENT_PENDING = new BatchStatusEnum[]{BatchStatusEnum.BULK_CHECK_VALIDATED_DATA, BatchStatusEnum.VALIDATED_TO_PREPARED_DATA};
    private final BatchStatusEnum[] CONTENT_FOLLOWUP = new BatchStatusEnum[]{BatchStatusEnum.BULK_CHECK_VALIDATION_ERROR, BatchStatusEnum.VALIDATED_TO_PREPARED_ERROR};

    private final BatchStatusEnum[] LOAD_PENDING = new BatchStatusEnum[]{BatchStatusEnum.ERROR_RATE, BatchStatusEnum.MAPPED_DATA, BatchStatusEnum.BULK_MAPPED_DATA};
    private final BatchStatusEnum[] LOAD_FOLLOWUP = new BatchStatusEnum[]{BatchStatusEnum.MAPPING_ERROR, BatchStatusEnum.BULK_MAPPED_DATA_ERROR};
    private final BatchStatusEnum[] LOAD_URGENT_ACTION = new BatchStatusEnum[]{BatchStatusEnum.ERROR_RATE_ERROR, BatchStatusEnum.CLEANUP_ERROR};

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

    @And("there is a {fileType}")
    public void thereIsAFileType() {

    }

    @And("^there is (?:a|an) (MHA|IRAS|SINGPOST|HDB|CPFB) " +
            "(BULK CITIZEN|NEW CITIZEN|DUAL CITIZEN|NO INTERACTION|DUAL CITIZEN|PERSON DETAIL CHANGE|DEATH DATE|CEASED CITIZEN)" +
            " file at (Format|Content|Load) step with (Pending|Urgent Action|Follow-up) status processed ([0-9]+) days ago$")
    public void thereIsAFileAtFormatStepAtPendingStatus(String agency, String file, String fileTrailCurrentStep, String fileTrailCurrentStatus, long daysAgo) {
        String fileType = String.join("_", agency, file.replace(" ", "_"));
        LocalDate processedDate = dateUtils.daysBeforeToday(daysAgo);
        BatchStatusEnum batchStatusEnum = generateBatchStatusBasedOn(fileTrailCurrentStep, fileTrailCurrentStatus);

        Batch batch = Batch.create(batchStatusEnum);

        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.fromString(fileType));

        FileReceived fileReceived = FileReceived.builder()
                .fileDetail(fileDetail)
                .filePath("/subdir1/subdir2/subdir3/"+fileDetail.getFileName()+".txt")
                .receivedTimestamp(dateUtils.beginningOfDayToTimestamp(processedDate))
                .batches(new ArrayList<Batch>(){{ add(batch); }})
                .fileSize(Double.valueOf(Phaker.validNumber(6)))
                .fileStatusEnum(FileStatusEnum.OK)
                .build();

        batch.setFileReceived(fileReceived);
        fileReceived = fileReceivedRepo.save(fileReceived);
        batchRepo.save(batch);

        // Build and Save JobExecutionParam
        JobExecutionParams jobExecutionParams =
                JobExecutionParams.builder()
                        .keyName("fileReceivedId")
                        .id(fileReceived.getId())
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

        for ( int i = 0 ; i < 10 ; i++ ) {
            bulkCitizenValidatedFactory.createValidBulkCitizenValidatedRecord(batch);
        }

        testContext.set("selectForFileTrail", fileReceived);
        testContext.set("batch", batch);
    }

    private BatchStatusEnum generateBatchStatusBasedOn(String fileTrailCurrentStep, String fileTrailCurrentStatus) {
        switch(fileTrailCurrentStep) {
            case FORMAT:
                switch(fileTrailCurrentStatus) {
                    case PENDING:
                        return FORMAT_PENDING[new Random().nextInt(FORMAT_PENDING.length)];
                    case FOLLOWUP:
                        return FORMAT_FOLLOWUP[new Random().nextInt(FORMAT_FOLLOWUP.length)];
                    case URGENT_ACTION:
                        return FORMAT_URGENT_ACTION[new Random().nextInt(FORMAT_URGENT_ACTION.length)];
                    default:
                        throw new TestFailException("Unsupported File Trail status: "+fileTrailCurrentStatus);
                }
            case CONTENT:
                switch(fileTrailCurrentStatus) {
                    case PENDING:
                        return CONTENT_PENDING[new Random().nextInt(CONTENT_PENDING.length)];
                    case FOLLOWUP:
                        return CONTENT_FOLLOWUP[new Random().nextInt(CONTENT_FOLLOWUP.length)];
                    default:
                        throw new TestFailException("Unsupported File Trail status: "+fileTrailCurrentStatus);
                }
            case LOAD:
                switch(fileTrailCurrentStatus) {
                    case PENDING:
                        return LOAD_PENDING[new Random().nextInt(LOAD_PENDING.length)];
                    case FOLLOWUP:
                        return LOAD_FOLLOWUP[new Random().nextInt(LOAD_FOLLOWUP.length)];
                    case URGENT_ACTION:
                        return LOAD_URGENT_ACTION[new Random().nextInt(LOAD_URGENT_ACTION.length)];
                    default:
                        throw new TestFailException("Unsupported File Trail status: "+fileTrailCurrentStatus);
                }
            default:
                throw new TestFailException("Unsupported File Trail step: "+fileTrailCurrentStep);
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
                .receivedTimestamp(dateUtils.beginningOfDayToTimestamp(Phaker.validDate(dateUtils.daysBeforeToday(30), dateUtils.now())))
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
                        .id(fileReceived.getId())
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
