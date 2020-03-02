package cdit_automation.data_helpers.factories;

import cdit_automation.constants.TestConstants;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.Gender;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.enums.PersonIdTypeEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.Batch;
import cdit_automation.models.Nationality;
import cdit_automation.models.Person;
import cdit_automation.models.PersonDetail;
import cdit_automation.models.PersonId;
import cdit_automation.models.PersonName;
import cdit_automation.models.embeddables.BiTemporalData;
import cdit_automation.repositories.BulkCitizenValidatedRepo;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

