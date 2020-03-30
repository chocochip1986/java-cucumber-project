package cdit_automation.data_helpers.factories;

import cdit_automation.enums.BatchStatusEnum;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.ReasonablenessCheckDataItemEnum;
import cdit_automation.enums.SpringJobStatusEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.AbstractValidated;
import cdit_automation.models.Batch;
import cdit_automation.models.BulkCitizenValidated;
import cdit_automation.models.CeasedCitizenValidated;
import cdit_automation.models.ChangeAddressValidated;
import cdit_automation.models.DeathDateValidated;
import cdit_automation.models.DoubleDateHeaderValidated;
import cdit_automation.models.DualCitizenValidated;
import cdit_automation.models.ErrorMessage;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import cdit_automation.models.IncomingRecord;
import cdit_automation.models.JobExecution;
import cdit_automation.models.JobExecutionParams;
import cdit_automation.models.NewCitizenValidated;
import cdit_automation.models.PersonDetailChangeValidated;
import cdit_automation.models.ReasonablenessCheckStatistic;
import cdit_automation.models.SingleDateHeaderValidated;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class FileDataProcessingFactory extends AbstractFactory {
    public Batch generateRecordsBasedOn(BatchStatusEnum batchStatusEnum, FileTypeEnum fileTypeEnum, int totalRecordCount, LocalDate localDate) {
        return generateRecordsBasedOn(batchStatusEnum, fileTypeEnum, totalRecordCount, localDate, 0);
    }

    public Batch generateRecordsBasedOn(BatchStatusEnum batchStatusEnum, FileTypeEnum fileTypeEnum, int totalRecordCount, LocalDate batchJobProcessedDate, int errorPercent) {
        Batch batch = null;
        List<? extends AbstractValidated> validatedList;
        switch (batchStatusEnum) {
            case INIT:
            case INIT_ERROR:
            case CHECKING_FILE:
                //Only batch record is created
                return createBatchJobRecords(batchStatusEnum, fileTypeEnum, batchJobProcessedDate);
            case FILE_CHECKED:
                batch = createBatchJobRecords(batchStatusEnum, fileTypeEnum, batchJobProcessedDate);
                createIncomingRecords(batch, totalRecordCount);
                break;
            case FILE_ERROR:
                //Create incoming records only
                //Some errors occured i.e. no footer, no header, incorrect header to footer, incorrect bound count
                batch = createBatchJobRecords(batchStatusEnum, fileTypeEnum, batchJobProcessedDate);
                createIncomingRecords(batch, totalRecordCount);
                createErrorMessagesForIncomingRecords(batch, errorPercent);
                break;
            case BULK_CHECK_VALIDATION_ERROR:
            case ERROR_RATE_ERROR:
                //validated records are created, failed error rate check
                batch = createBatchJobRecords(batchStatusEnum, fileTypeEnum, batchJobProcessedDate);
                createIncomingRecords(batch, totalRecordCount);
                createHeaderValidatedRecord(batch, fileTypeEnum);
                validatedList = createValidatedRecords(batch, totalRecordCount, fileTypeEnum);
                createStatisticalRecords(batch, validatedList.size());
                createErrorMessagesForValidatedRecords(validatedList, batch, errorPercent);
                break;
            case ERROR_RATE:
            case BULK_CHECK_VALIDATED_DATA:
            case MAPPED_DATA:
                //prepared data persisted, no issues
            case MAPPING_ERROR:
                //prepared data persisted got issues
            case BULK_MAPPED_DATA:
                //prepared data persisted
            case BULK_MAPPED_DATA_ERROR:
                //prepared data persisted
            case CLEANUP:
                //prepared data persisted
            case CLEANUP_ERROR:
                batch = createBatchJobRecords(batchStatusEnum, fileTypeEnum, batchJobProcessedDate);
                createIncomingRecords(batch, totalRecordCount);
                createHeaderValidatedRecord(batch, fileTypeEnum);
                validatedList = createValidatedRecords(batch, totalRecordCount, fileTypeEnum);
                createStatisticalRecords(batch, validatedList.size());
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

    private void createErrorMessagesForIncomingRecords(Batch batch, int errorPercent) {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        List<IncomingRecord> incomingRecords = incomingRecordRepo.findAllBodyByBatch(batch);

        int numerOfErrorenousRecords = Math.floorDiv(incomingRecords.size() * errorPercent, 100);
        for ( int i = 0 ; i < numerOfErrorenousRecords ; i++ ) {
            errorMessages.add(ErrorMessage.createErrorMsgForIncomingRecord(incomingRecords.get(0), batch));
        }
        errorMessageRepo.saveAll(errorMessages);
    }

    private void createErrorMessagesForValidatedRecords(List<? extends AbstractValidated> abstractValidateds, Batch batch, int errorPercent) {
        List<ErrorMessage> errorMessages = new ArrayList<>();

        int numberOfErrorenousRecords = Math.floorDiv(abstractValidateds.size() * errorPercent,  100);
        for ( int i = 0 ; i < numberOfErrorenousRecords ; i++ ) {
            errorMessages.add(ErrorMessage.createErrorMsgForValidatedRecord(abstractValidateds.get(i), batch));
        }
        errorMessageRepo.saveAll(errorMessages);
    }

    private List<? extends AbstractValidated> createValidatedRecords(Batch batch, int totalRecordCount, FileTypeEnum fileTypeEnum) {
        switch (fileTypeEnum) {
            case MHA_BULK_CITIZEN:
                List<BulkCitizenValidated> bulkCitizenValidateds = new ArrayList<>();
                createRecordsFor(bulkCitizenValidateds, totalRecordCount, bulkCitizenValidatedRepo, batch,
                        () -> bulkCitizenValidatedFactory.createValidBulkCitizenValidatedRecord(batch));
                return bulkCitizenValidateds;
            case MHA_DUAL_CITIZEN:
                List<DualCitizenValidated> dualCitizenValidateds = new ArrayList<>();
                createRecordsFor(dualCitizenValidateds, totalRecordCount, dualCitizenValidatedRepo, batch,
                        () -> dualCitizenValidatedFactory.create(batch));
                return dualCitizenValidateds;
            case MHA_PERSON_DETAIL_CHANGE:
                List<PersonDetailChangeValidated> personDetailChangeValidateds = new ArrayList<>();
                createRecordsFor(personDetailChangeValidateds, totalRecordCount, personDetailChangeValidatedRepo, batch,
                        () -> personDetailChangeValidatedFactory.create(batch));
                return personDetailChangeValidateds;
            case MHA_CEASED_CITIZEN:
                List<CeasedCitizenValidated> ceasedCitizenValidateds = new ArrayList<>();
                createRecordsFor(ceasedCitizenValidateds, totalRecordCount, ceasedCitizenValidatedRepo, batch,
                        () -> ceasedCitizenValidatedFactory.create(batch));
                return ceasedCitizenValidateds;
            case MHA_DEATH_DATE:
                List<DeathDateValidated> deathDateValidateds = new ArrayList<>();
                createRecordsFor(deathDateValidateds, totalRecordCount, deathDateValidatedRepo, batch,
                        () -> deathDateValidatedFactory.create(batch));
                return deathDateValidateds;
            case MHA_CHANGE_ADDRESS:
                List<ChangeAddressValidated> changeAddressValidateds = new ArrayList<>();
                createRecordsFor(changeAddressValidateds, totalRecordCount, changeAddressValidatedRepo, batch,
                        () -> changeAddressValidatedFactory.create(batch));
                return changeAddressValidateds;
            case MHA_NEW_CITIZEN:
                List<NewCitizenValidated> newCitizenValidateds = new ArrayList<>();
                createRecordsFor(newCitizenValidateds, totalRecordCount, newCitizenValidatedRepo, batch,
                        () -> newCitizenValidatedFactory.createValidNewCitizenValidatedRecord(batch));
                return newCitizenValidateds;
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
