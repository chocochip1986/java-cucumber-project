package cdit_automation.step_definition;

import cdit_automation.api_helpers.ApiHelper;
import cdit_automation.aws.modules.S3;
import cdit_automation.aws.modules.Slack;
import cdit_automation.configuration.AddressJacksonObjectMapper;
import cdit_automation.configuration.StepDefLevelTestContext;
import cdit_automation.configuration.TestManager;
import cdit_automation.data_helpers.BatchFileCreator;
import cdit_automation.data_helpers.BatchFileDataWriter;
import cdit_automation.data_helpers.MhaBulkFileDataPrep;
import cdit_automation.data_helpers.MhaChangePersonDetailsDataPrep;
import cdit_automation.data_helpers.MhaDeathDateFileDataPrep;
import cdit_automation.data_helpers.MhaDualCitizenFileDataPrep;
import cdit_automation.data_helpers.MhaCeasedCitizenFileDataPrep;
import cdit_automation.data_helpers.IrasTriMonthlyEgressDataPrep;
import cdit_automation.data_helpers.factories.PersonFactory;
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
import cdit_automation.repositories.CeasedCitizenRepo;
import cdit_automation.repositories.PersonNameRepo;
import cdit_automation.repositories.IncomeRepo;
import cdit_automation.utilities.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Supplier;

public class AbstractSteps {

    //Test suite related helpers
    @Autowired protected TestManager testManager;

    @Autowired protected PageUtils pageUtils;
    @Autowired protected DateUtils dateUtils;

    @Autowired protected StepDefLevelTestContext testContext;

    @Autowired protected BatchFileCreator batchFileCreator;
    @Autowired protected BatchFileDataWriter batchFileDataWriter;

    @Autowired protected ApiHelper apiHelper;

    @Autowired protected Slack slack;
    @Autowired protected S3 s3;

    //Data creators
    @Autowired protected PersonFactory personFactory;

    //Batch File Data Helpers
    @Autowired protected MhaDualCitizenFileDataPrep mhaDualCitizenFileDataPrep;
    @Autowired protected MhaDeathDateFileDataPrep mhaDeathDateFileDataPrep;
    @Autowired protected MhaChangePersonDetailsDataPrep mhaChangePersonDetailsDataPrep;
    @Autowired protected MhaCeasedCitizenFileDataPrep mhaCeasedCitizenFileDataPrep;
    @Autowired protected MhaBulkFileDataPrep mhaBulkFileDataPrep;
    @Autowired protected IrasTriMonthlyEgressDataPrep irasTriMonthlyEgressDataPrep;

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
    @Autowired protected CeasedCitizenRepo ceasedCitizenRepo;
    @Autowired protected PersonNameRepo personNameRepo;
    @Autowired protected IncomeRepo incomeRepo;
    @Autowired AddressJacksonObjectMapper addressJacksonObjectMapper;


    protected int parseStringSize(String size) {
        try {
            return Integer.valueOf(size);
        } catch (NumberFormatException e) {
            //Do nothing
        }
        return 0;
    }

    protected boolean waitUntilCondition(Supplier<Boolean> function) {
        Double timer = 0.0;
        Double maxWaitDuration;
        if ( testManager.getTestEnvironment().equals("local")) {
            maxWaitDuration = 10.0;
        } else {
            maxWaitDuration = 180.0;
        }
        boolean isFound;
        do {
            isFound = function.get();
            if ( isFound ) {
                break;
            } else {
                testManager.sleep();
                progressBar(timer);
                timer++;
            }
        } while ( timer < maxWaitDuration+1.0 );
        System.out.println(System.lineSeparator());
        return isFound;
    }

    private void progressBar(double timer) {
        StringBuilder stringCount = new StringBuilder(30);
        stringCount
                .append("\r")
                .append(String.format("  Waited for %d seconds...", (int)timer));
        System.out.print(stringCount);
    }
}
