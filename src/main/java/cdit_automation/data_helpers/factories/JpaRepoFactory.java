package cdit_automation.data_helpers.factories;

import cdit_automation.enums.FileTypeEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.Batch;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class JpaRepoFactory extends AbstractFactory {
    public long countValidatedRecordsBasedOnFileTypeAndBatch(@NonNull FileTypeEnum fileTypeEnum, Batch batch)
        throws TestFailException{

        switch(fileTypeEnum){
            case MHA_BULK_CITIZEN:
                return bulkCitizenValidatedRepo.countByBatch(batch);
            default:
                throw new TestFailException("Jpa Repo for file type not supported.");
        }
    }

}

