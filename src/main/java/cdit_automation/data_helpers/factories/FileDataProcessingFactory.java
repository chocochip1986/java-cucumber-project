package cdit_automation.data_helpers.factories;

import cdit_automation.enums.BatchStatusEnum;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.ReasonablenessCheckDataItemEnum;
import cdit_automation.enums.SpringJobStatusEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.Batch;
import cdit_automation.models.BulkCitizenValidated;
import cdit_automation.models.CeasedCitizenValidated;
import cdit_automation.models.ChangeAddressValidated;
import cdit_automation.models.DeathDateValidated;
import cdit_automation.models.DoubleDateHeaderValidated;
import cdit_automation.models.DualCitizenValidated;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import cdit_automation.models.IncomingRecord;
import cdit_automation.models.JobExecution;
import cdit_automation.models.JobExecutionParams;
import cdit_automation.models.NewCitizenValidated;
import cdit_automation.models.PersonDetailChangeValidated;
import cdit_automation.models.ReasonablenessCheckStatistic;
import cdit_automation.models.SingleDateHeaderValidated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Component
public class FileDataProcessingFactory extends AbstractFactory {
    public Batch generateRecordsBasedOn(BatchStatusEnum batchStatusEnum, FileTypeEnum fileTypeEnum, int totalRecordCount) {
        return generateRecordsBasedOn(batchStatusEnum, fileTypeEnum, totalRecordCount, dateUtils.now());
    }

    public Batch generateRecordsBasedOn(BatchStatusEnum batchStatusEnum, FileTypeEnum fileTypeEnum, int totalRecordCount, LocalDate batchJobProcessedDate) {
        Batch batch = null;
        int validatedCount = 0;
        switch (batchStatusEnum) {
            case INIT:
            case INIT_ERROR:
            case CHECKING_FILE:
                //Only batch record is created
                return createBatchJobRecords(batchStatusEnum, fileTypeEnum, batchJobProcessedDate);
            case FILE_CHECKED:
                //Create incoming records only
            case FILE_ERROR:
                //Create incoming records only
                //Some errors occured i.e. no footer, no header, incorrect header to footer, incorrect bound count
                batch = createBatchJobRecords(batchStatusEnum, fileTypeEnum, batchJobProcessedDate);
                createIncomingRecords(batch, totalRecordCount);
                break;
            case BULK_CHECK_VALIDATED_DATA:
                //validated records are created, statistics are also created
//                break;
            case BULK_CHECK_VALIDATION_ERROR:
                //validated records are created, but got some errors. could be header dates invalid, etc
                batch = createBatchJobRecords(batchStatusEnum, fileTypeEnum, batchJobProcessedDate);
                createIncomingRecords(batch, totalRecordCount);
                createHeaderValidatedRecord(batch, fileTypeEnum);
                validatedCount = createValidatedRecords(batch, totalRecordCount, fileTypeEnum);
                createStatisticalRecords(batch, validatedCount);
                break;
            case ERROR_RATE:
                //validated records are created, passed error rate check
            case ERROR_RATE_ERROR:
                batch = createBatchJobRecords(batchStatusEnum, fileTypeEnum, batchJobProcessedDate);
                createIncomingRecords(batch, totalRecordCount);
                createHeaderValidatedRecord(batch, fileTypeEnum);
                validatedCount = createValidatedRecords(batch, totalRecordCount, fileTypeEnum);
                createStatisticalRecords(batch, validatedCount);
                //validated records are created, failed error rate check
                break;
            case MAPPED_DATA:
                //prepared data persisted, no issues
            case MAPPING_ERROR:
                batch = createBatchJobRecords(batchStatusEnum, fileTypeEnum, batchJobProcessedDate);
                createIncomingRecords(batch, totalRecordCount);
                createHeaderValidatedRecord(batch, fileTypeEnum);
                validatedCount = createValidatedRecords(batch, totalRecordCount, fileTypeEnum);
                createStatisticalRecords(batch, validatedCount);
                //prepared data persisted got issues
                break;
            case BULK_MAPPED_DATA:
                //prepared data persisted
            case BULK_MAPPED_DATA_ERROR:
                batch = createBatchJobRecords(batchStatusEnum, fileTypeEnum, batchJobProcessedDate);
                createIncomingRecords(batch, totalRecordCount);
                createHeaderValidatedRecord(batch, fileTypeEnum);
                validatedCount = createValidatedRecords(batch, totalRecordCount, fileTypeEnum);
                createStatisticalRecords(batch, validatedCount);
                //prepared data persisted
                break;
            case CLEANUP:
                //prepared data persisted
            case CLEANUP_ERROR:
                batch = createBatchJobRecords(batchStatusEnum, fileTypeEnum, batchJobProcessedDate);
                createIncomingRecords(batch, totalRecordCount);
                createHeaderValidatedRecord(batch, fileTypeEnum);
                validatedCount = createValidatedRecords(batch, totalRecordCount, fileTypeEnum);
                createStatisticalRecords(batch, validatedCount);
                //prepared data persisted
                break;
            default:
                throw new TestFailException("Unsupported batch status '"+batchStatusEnum+" for data creation");
        }
        return batch;
    }

    private void createHeaderValidatedRecord(Batch batch, FileTypeEnum fileTypeEnum) {
        switch(fileTypeEnum) {
            case MHA_CEASED_CITIZEN:
            case CPFB_LORONG_BUANGKOK:
            case IRAS_DECLARED_NTI:
            case MSF_PWD:
            case CPFB_CLASSIFIED_ACCOUNT:
            case CPFB_NURSING_HOME:
            case MHA_NEW_CITIZEN:
            case MHA_DUAL_CITIZEN:
            case MHA_DEATH_DATE:
                singleDateHeaderValidatedRepo.save(SingleDateHeaderValidated.create(dateUtils.daysBeforeToday(5), batch));
                break;
            case IRAS_BULK_AI:
            case IRAS_THRICE_MONTHLY_AI:
                //Different header
                break;
            case SINGPOST_SELF_EMPLOYED:
                //Different header
                break;
            case IRAS_FORM_B:
                //Different header
                break;
            default:
                doubleDateHeaderValidatedRepo.save(DoubleDateHeaderValidated.create(dateUtils.daysBeforeToday(5), dateUtils.daysBeforeToday(5), batch));
        }
    }

    private int createValidatedRecords(Batch batch, int totalRecordCount, FileTypeEnum fileTypeEnum) {
        switch (fileTypeEnum) {
            case MHA_BULK_CITIZEN:
                List<BulkCitizenValidated> bulkCitizenValidateds = new ArrayList<>();
                return createRecordsFor(bulkCitizenValidateds, totalRecordCount, bulkCitizenValidatedRepo, batch,
                        () -> bulkCitizenValidatedFactory.createValidBulkCitizenValidatedRecord(batch));
            case MHA_DUAL_CITIZEN:
                List<DualCitizenValidated> dualCitizenValidateds = new ArrayList<>();
                return createRecordsFor(dualCitizenValidateds, totalRecordCount, dualCitizenValidatedRepo, batch,
                        () -> dualCitizenValidatedFactory.create(batch));
            case MHA_PERSON_DETAIL_CHANGE:
                List<PersonDetailChangeValidated> personDetailChangeValidateds = new ArrayList<>();
                return createRecordsFor(personDetailChangeValidateds, totalRecordCount, personDetailChangeValidatedRepo, batch,
                        () -> personDetailChangeValidatedFactory.create(batch));
            case MHA_CEASED_CITIZEN:
                List<CeasedCitizenValidated> ceasedCitizenValidateds = new ArrayList<>();
                return createRecordsFor(ceasedCitizenValidateds, totalRecordCount, ceasedCitizenValidatedRepo, batch,
                        () -> ceasedCitizenValidatedFactory.create(batch));
            case MHA_DEATH_DATE:
                List<DeathDateValidated> deathDateValidateds = new ArrayList<>();
                return createRecordsFor(deathDateValidateds, totalRecordCount, deathDateValidatedRepo, batch,
                        () -> deathDateValidatedFactory.create(batch));
            case MHA_CHANGE_ADDRESS:
                List<ChangeAddressValidated> changeAddressValidateds = new ArrayList<>();
                return createRecordsFor(changeAddressValidateds, totalRecordCount, changeAddressValidatedRepo, batch,
                        () -> changeAddressValidatedFactory.create(batch));
            case MHA_NEW_CITIZEN:
                List<NewCitizenValidated> newCitizenValidateds = new ArrayList<>();
                return createRecordsFor(newCitizenValidateds, totalRecordCount, newCitizenValidatedRepo, batch,
                        () -> newCitizenValidatedFactory.createValidNewCitizenValidatedRecord(batch));
            default:
                throw new TestFailException("Unsupported batch status '"+fileTypeEnum.getValue()+" for data creation");
        }
    }

    private void createStatisticalRecords(Batch batch, int validatedRecordsCount) {
        switch (batch.getFileReceived().getFileDetail().getFileEnum()) {
            case MHA_BULK_CITIZEN:
                ReasonablenessCheckStatistic reasonablenessCheckStatistic = ReasonablenessCheckStatistic.create(ReasonablenessCheckDataItemEnum.NO_OF_NEW_THIRTEEN_YEAR_OLD.getValue(), String.valueOf(validatedRecordsCount), batch);
                reasonablenessCheckStatisticRepo.save(reasonablenessCheckStatistic);
                break;
            case MHA_DUAL_CITIZEN:
            case MHA_PERSON_DETAIL_CHANGE:
            case MHA_CEASED_CITIZEN:
            case MHA_DEATH_DATE:
            case MHA_CHANGE_ADDRESS:
            case MHA_NEW_CITIZEN:
                break;
            default:
                throw new TestFailException("Unsupported batch status '"+batch.getFileReceived().getFileDetail().getFileEnum().getValue()+" for data creation");
        }
    }

    public <T> int createRecordsFor(List<T> validatedList, int totalRecordCount, JpaRepository repo, Batch batch, Supplier<T> function) {
        for ( int i = 0 ; i < totalRecordCount ; i++ ) {
            validatedList.add(function.get());
        }
        return repo.saveAll(validatedList).size();
    }

    private void createIncomingRecords(Batch batch, int totalRecordCount) {
        List<IncomingRecord> incomingRecordList = new ArrayList<>();
        for ( int i = 0 ; i < totalRecordCount ; i++ ) {
            incomingRecordList.add(IncomingRecord.createBodyRAW(batch, "This is a fake line of data for body"));
        }
        incomingRecordList.add(0, IncomingRecord.createHeaderRAW(batch, "This is a fake line of data for header"));
        incomingRecordList.add(IncomingRecord.createFooterRAW(batch, "This is a fake line of data for footer"));
        incomingRecordRepo.saveAll(incomingRecordList);
    }

    private Batch createBatchJobRecords(BatchStatusEnum batchStatusEnum, FileTypeEnum fileTypeEnum, LocalDate batchJobProcessedDate) {
        Batch batch = Batch.create(batchStatusEnum,
                dateUtils.beginningOfDayToTimestamp(batchJobProcessedDate));

        FileDetail fileDetail = fileDetailRepo.findByFileEnum(fileTypeEnum);
        FileReceived fileReceived = FileReceived.createOk(fileDetail,
                generateFakeFilePath(fileDetail),
                batch.getCreatedAt(),
                Arrays.asList(batch));

        batch.setFileReceived(fileReceived);
        fileReceivedRepo.save(fileReceived);
        batchRepo.save(batch);

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

        return batch;
    }

    private String generateFakeFilePath(FileDetail fileDetail) {
        return "/subdir1/subdir2/subdir3/"+fileDetail.getFileName()+".txt";
    }
}
