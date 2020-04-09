package automation.data_helpers.datasource.factories;

import automation.enums.datasource.FileTypeEnum;
import automation.exceptions.TestFailException;
import automation.models.datasource.Batch;
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

