package cdit_automation.data_helpers.factories;

import cdit_automation.enums.BatchStatusEnum;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.Batch;
import cdit_automation.models.BulkCitizenValidated;
import cdit_automation.models.BulkMhaAddressValidated;
import cdit_automation.models.CeasedCitizenValidated;
import cdit_automation.models.ChangeAddressValidated;
import cdit_automation.models.DeathDateValidated;
import cdit_automation.models.DoubleDateHeaderValidated;
import cdit_automation.models.DualCitizenValidated;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import cdit_automation.models.IncomingRecord;
import cdit_automation.models.NewCitizenValidated;
import cdit_automation.models.PersonDetailChangeValidated;
import cdit_automation.models.SingleDateHeaderValidated;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class FileDataProcessingFactory extends AbstractFactory {
    public void generateDataAtFileProcessingStage(FileTypeEnum fileTypeEnum, BatchStatusEnum batchStatusEnum, int totalRecordCount) {
    }

    public Batch generateRecordsBasedOn(BatchStatusEnum batchStatusEnum, FileTypeEnum fileTypeEnum, int totalRecordCount) {
        Batch batch = null;
        switch (batchStatusEnum) {
            case INIT:
            case INIT_ERROR:
            case CHECKING_FILE:
                //Only batch record is created
                return createBatchAndFileReceived(batchStatusEnum, fileTypeEnum);
            case FILE_CHECKED:
                //Create incoming records only
            case FILE_ERROR:
                //Create incoming records only
                //Some errors occured i.e. no footer, no header, incorrect header to footer, incorrect bound count
                batch = createBatchAndFileReceived(batchStatusEnum, fileTypeEnum);
                createIncomingRecords(batch, totalRecordCount);
                break;
            case BULK_CHECK_VALIDATED_DATA:
                //validated records are created, statistics are also created
//                break;
            case BULK_CHECK_VALIDATION_ERROR:
                //validated records are created, but got some errors. could be header dates invalid, etc
                batch = createBatchAndFileReceived(batchStatusEnum, fileTypeEnum);
                createIncomingRecords(batch, totalRecordCount);
                createHeaderValidatedRecord(batch, fileTypeEnum);
                createValidatedRecords(batch, totalRecordCount, fileTypeEnum);
                break;
            case ERROR_RATE:
                //validated records are created, passed error rate check
                break;
            case ERROR_RATE_ERROR:
                //validated records are created, failed error rate check
                break;
            case MAPPED_DATA:
                //prepared data persisted, no issues
                break;
            case MAPPING_ERROR:
                //prepared data persisted got issues
                break;
            case BULK_MAPPED_DATA:
                //prepared data persisted
                break;
            case BULK_MAPPED_DATA_ERROR:
                //prepared data persisted
                break;
            case CLEANUP:
                //prepared data persisted
                break;
            case CLEANUP_ERROR:
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
                singleDateHeaderValidatedRepo.save(SingleDateHeaderValidated.create(dateUtils.daysBeforeToday(5)));
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
                doubleDateHeaderValidatedRepo.save(DoubleDateHeaderValidated.create(dateUtils.daysBeforeToday(5), dateUtils.daysBeforeToday(5)));
        }
    }

    private void createValidatedRecords(Batch batch, int totalRecordCount, FileTypeEnum fileTypeEnum) {
        for(int i = 0 ; i < totalRecordCount ; i++ ) {
            switch (fileTypeEnum) {
                case MHA_BULK_CITIZEN:
                    List<BulkCitizenValidated> bulkCitizenValidateds = new ArrayList<>();
                    bulkCitizenValidateds.add(bulkCitizenValidatedFactory.createValidBulkCitizenValidatedRecord(batch));
                    bulkCitizenValidatedRepo.saveAll(bulkCitizenValidateds);
                    break;
                case MHA_DUAL_CITIZEN:
                    List<DualCitizenValidated> dualCitizenValidateds = new ArrayList<>();
                    dualCitizenValidateds.add(dualCitizenValidatedFactory.create(batch));
                    dualCitizenValidatedRepo.saveAll(dualCitizenValidateds);
                case MHA_PERSON_DETAIL_CHANGE:
                    List<PersonDetailChangeValidated> personDetailChangeValidateds = new ArrayList<>();
                    personDetailChangeValidateds.add(personDetailChangeValidatedFactory.create(batch));
                    personDetailChangeValidatedRepo.saveAll(personDetailChangeValidateds);
                case MHA_CEASED_CITIZEN:
                    List<CeasedCitizenValidated> ceasedCitizenValidateds = new ArrayList<>();
                    ceasedCitizenValidateds.add(ceasedCitizenValidatedFactory.create(batch));
                    ceasedCitizenValidatedRepo.saveAll(ceasedCitizenValidateds);
                case MHA_DEATH_DATE:
                    List<DeathDateValidated> deathDateValidateds = new ArrayList<>();
                    deathDateValidateds.add(deathDateValidatedFactory.create(batch));
                    deathDateValidatedRepo.saveAll(deathDateValidateds);
                case MHA_CHANGE_ADDRESS:
                    List<ChangeAddressValidated> changeAddressValidateds = new ArrayList<>();
                    changeAddressValidateds.add(changeAddressValidatedFactory.create(batch));
                    changeAddressValidatedRepo.saveAll(changeAddressValidateds);
                case MHA_NEW_CITIZEN:
                    List<NewCitizenValidated> newCitizenValidateds = new ArrayList<>();
                default:
                    throw new TestFailException("Unsupported batch status '"+fileTypeEnum.getValue()+" for data creation");
            }
        }
    }

    private void createStatisticalRecords(Batch batch) {

    }

    private void createIncomingRecords(Batch batch, int totalRecordCount) {
        List<IncomingRecord> incomingRecordList = new ArrayList<>();
        for ( int i = 0 ; i < totalRecordCount ; i++ ) {
            IncomingRecord.createBodyRAW(batch, "This is a fake line of data for body");
        }
        incomingRecordList.add(0, IncomingRecord.createHeaderRAW(batch, "This is a fake line of data for header"));
        incomingRecordList.add(IncomingRecord.createFooterRAW(batch, "This is a fake line of data for footer"));
        incomingRecordRepo.saveAll(incomingRecordList);

    }

    private Batch createBatchAndFileReceived(BatchStatusEnum batchStatusEnum, FileTypeEnum fileTypeEnum) {
        Batch batch = Batch.create(batchStatusEnum, dateUtils.beginningOfDayToTimestamp(dateUtils.now()));

        FileDetail fileDetail = fileDetailRepo.findByFileEnum(fileTypeEnum);
        FileReceived fileReceived = FileReceived.createOk(fileDetail,
                generateFakeFilePath(fileDetail),
                batch.getCreatedAt(),
                Arrays.asList(batch));

        batch.setFileReceived(fileReceived);
        batchRepo.save(batch);
        fileReceivedRepo.save(fileReceived);

        return batch;
    }

    private String generateFakeFilePath(FileDetail fileDetail) {
        return "/subdir1/subdir2/subdir3/"+fileDetail.getFileName()+".txt";
    }
}
