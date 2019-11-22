package cdit_automation.step_definition;

import cdit_automation.api_helpers.ApiHelper;
import cdit_automation.configuration.AddressJacksonObjectMapper;
import cdit_automation.configuration.StepDefLevelTestContext;
import cdit_automation.configuration.TestManager;
import cdit_automation.data_helpers.BatchFileCreator;
import cdit_automation.data_helpers.MhaBulkFileDataPrep;
import cdit_automation.data_helpers.MhaDeathDateFileDataPrep;
import cdit_automation.data_helpers.MhaDualCitizenFileDataPrep;
import cdit_automation.data_helpers.PersonIdService;
import cdit_automation.page_navigation.PageUtils;
import cdit_automation.repositories.BatchRepo;
import cdit_automation.repositories.DeathDateValidatedRepo;
import cdit_automation.repositories.ErrorMessageRepo;
import cdit_automation.repositories.FileDetailRepo;
import cdit_automation.repositories.FileReceivedRepo;
import cdit_automation.repositories.IncomingRecordRepo;
import cdit_automation.repositories.NationalityRepo;
import cdit_automation.repositories.PersonDetailRepo;
import cdit_automation.repositories.PersonIdRepo;
import cdit_automation.repositories.PersonRepo;
import cdit_automation.utilities.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractSteps {

    //Test suite related helpers
    @Autowired protected TestManager testManager;

    @Autowired protected PageUtils pageUtils;
    @Autowired protected DateUtils dateUtils;

    @Autowired protected StepDefLevelTestContext testContext;

    @Autowired protected BatchFileCreator batchFileCreator;

    @Autowired protected ApiHelper apiHelper;

    //Data creators
    @Autowired
    protected PersonIdService personIdService;

    //Batch File Data Helpers
    @Autowired protected MhaDualCitizenFileDataPrep mhaDualCitizenFileDataPrep;
    @Autowired protected MhaDeathDateFileDataPrep mhaDeathDateFileDataPrep;
    @Autowired protected MhaBulkFileDataPrep mhaBulkFileDataPrep;

    //Model Repositories
    @Autowired protected BatchRepo batchRepo;
    @Autowired protected FileReceivedRepo fileReceivedRepo;
    @Autowired protected FileDetailRepo fileDetailRepo;
    @Autowired protected IncomingRecordRepo incomingRecordRepo;
    @Autowired protected NationalityRepo nationalityRepo;
    @Autowired protected PersonRepo personRepo;
    @Autowired protected ErrorMessageRepo errorMessageRepo;
    @Autowired protected PersonIdRepo personIdRepo;
    @Autowired protected PersonDetailRepo personDetailRepo;
    @Autowired protected DeathDateValidatedRepo deathDateValidatedRepo;
    @Autowired AddressJacksonObjectMapper addressJacksonObjectMapper;


    protected int parseStringSize(String size) {
        try {
            return Integer.valueOf(size);
        } catch (NumberFormatException e) {
            //Do nothing
        }
        return 0;
    }
}
