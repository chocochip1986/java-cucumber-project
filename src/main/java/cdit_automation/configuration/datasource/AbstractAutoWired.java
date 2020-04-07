package cdit_automation.configuration.datasource;

import cdit_automation.api_helpers.datasource.ApiHelper;
import cdit_automation.asserts.Assert;
import cdit_automation.aws.modules.S3;
import cdit_automation.aws.modules.Slack;
import cdit_automation.configuration.TestManager;
import cdit_automation.data_helpers.datasource.BatchFileCreator;
import cdit_automation.data_helpers.datasource.BatchFileDataWriter;
import cdit_automation.data_helpers.datasource.IrasAssessableIncomeFileDataPrep;
import cdit_automation.data_helpers.datasource.IrasTriMonthlyEgressDataPrep;
import cdit_automation.data_helpers.datasource.MhaBulkFileDataPrep;
import cdit_automation.data_helpers.datasource.MhaCeasedCitizenFileDataPrep;
import cdit_automation.data_helpers.datasource.MhaChangeAddressDataPrep;
import cdit_automation.data_helpers.datasource.MhaChangeAddressFileDataPrep;
import cdit_automation.data_helpers.datasource.MhaChangePersonDetailsDataPrep;
import cdit_automation.data_helpers.datasource.MhaDeathDateFileDataPrep;
import cdit_automation.data_helpers.datasource.MhaDualCitizenFileDataPrep;
import cdit_automation.data_helpers.datasource.MhaNewCitizenFileDataPrep;
import cdit_automation.data_helpers.datasource.MhaNoInteractionFileDataPrep;
import cdit_automation.data_helpers.datasource.factories.AddressFactory;
import cdit_automation.data_helpers.datasource.factories.BulkCitizenValidatedFactory;
import cdit_automation.data_helpers.datasource.factories.CeasedCitizenValidatedFactory;
import cdit_automation.data_helpers.datasource.factories.ChangeAddressValidatedFactory;
import cdit_automation.data_helpers.datasource.factories.DeathDateValidatedFactory;
import cdit_automation.data_helpers.datasource.factories.DualCitizenValidatedFactory;
import cdit_automation.data_helpers.datasource.factories.FileDataProcessingFactory;
import cdit_automation.data_helpers.datasource.factories.JpaRepoFactory;
import cdit_automation.data_helpers.datasource.factories.NewCitizenValidatedFactory;
import cdit_automation.data_helpers.datasource.factories.NewMhaAddressFactory;
import cdit_automation.data_helpers.datasource.factories.NewNcaAddressFactory;
import cdit_automation.data_helpers.datasource.factories.OldMhaAddressFactory;
import cdit_automation.data_helpers.datasource.factories.OldNcaAddressFactory;
import cdit_automation.data_helpers.datasource.factories.PersonDetailChangeValidatedFactory;
import cdit_automation.data_helpers.datasource.factories.PersonFactory;
import cdit_automation.models.datasource.Batch;
import cdit_automation.page_navigation.PageUtils;
import cdit_automation.repositories.datasource.AnnualValueRepo;
import cdit_automation.repositories.datasource.AssessableIncomeValidatedRepo;
import cdit_automation.repositories.datasource.BatchJobExecutionParamsRepo;
import cdit_automation.repositories.datasource.BatchJobExecutionRepo;
import cdit_automation.repositories.datasource.BatchRepo;
import cdit_automation.repositories.datasource.BulkCitizenValidatedRepo;
import cdit_automation.repositories.datasource.BulkMhaAddressValidatedRepo;
import cdit_automation.repositories.datasource.BulkNcaAddressValidatedRepo;
import cdit_automation.repositories.datasource.CeasedCitizenValidatedRepo;
import cdit_automation.repositories.datasource.ChangeAddressValidatedRepo;
import cdit_automation.repositories.datasource.DeathDateValidatedRepo;
import cdit_automation.repositories.datasource.DoubleDateHeaderValidatedRepo;
import cdit_automation.repositories.datasource.DualCitizenValidatedRepo;
import cdit_automation.repositories.datasource.ErrorMessageRepo;
import cdit_automation.repositories.datasource.FileDetailRepo;
import cdit_automation.repositories.datasource.FileReceivedRepo;
import cdit_automation.repositories.datasource.GenderRepo;
import cdit_automation.repositories.datasource.IncomeRepo;
import cdit_automation.repositories.datasource.IncomingRecordRepo;
import cdit_automation.repositories.datasource.NationalityRepo;
import cdit_automation.repositories.datasource.NewCitizenValidatedRepo;
import cdit_automation.repositories.datasource.NewMhaAddressRepo;
import cdit_automation.repositories.datasource.NewNcaAddressRepo;
import cdit_automation.repositories.datasource.NoInteractionValidatedRepo;
import cdit_automation.repositories.datasource.OldMhaAddressRepo;
import cdit_automation.repositories.datasource.OldNcaAddressRepo;
import cdit_automation.repositories.datasource.PersonDetailChangeValidatedRepo;
import cdit_automation.repositories.datasource.PersonDetailRepo;
import cdit_automation.repositories.datasource.PersonIdRepo;
import cdit_automation.repositories.datasource.PersonNameRepo;
import cdit_automation.repositories.datasource.PersonPropertyRepo;
import cdit_automation.repositories.datasource.PersonRepo;
import cdit_automation.repositories.datasource.PersonStatusRepo;
import cdit_automation.repositories.datasource.PropertyDetailRepo;
import cdit_automation.repositories.datasource.PropertyRepo;
import cdit_automation.repositories.datasource.PropertyValidatedRepo;
import cdit_automation.repositories.datasource.ReasonablenessCheckStatisticRepo;
import cdit_automation.repositories.datasource.SingleDateHeaderValidatedRepo;
import cdit_automation.repositories.datasource.SingleDateNoOfRecordsHeaderValidatedRepo;
import cdit_automation.repositories.datasource.SingleDateRecordTypeHeaderRepo;
import cdit_automation.repositories.datasource.SpecialMappingRepo;
import cdit_automation.repositories.datasource.SpecialPropertyRepo;
import cdit_automation.repositories.datasource.view.ReasonablenessCheckStatisticViewJpaRepo;
import cdit_automation.utilities.DateUtils;
import cdit_automation.utilities.PersonPropertyTimelineReconstruction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractAutoWired {
    //Test suite related helpers
    @Autowired
    protected TestManager testManager;

    @Autowired protected PageUtils pageUtils;
    @Autowired protected DateUtils dateUtils;
    @Autowired protected Assert testAssert;

    @Autowired protected StepDefLevelTestContext testContext;

    @Autowired protected BatchFileCreator batchFileCreator;
    @Autowired protected BatchFileDataWriter batchFileDataWriter;

    @Autowired protected ApiHelper apiHelper;

    @Autowired protected Slack slack;
    @Autowired protected S3 s3;

    //Data creators
    @Autowired protected PersonFactory personFactory;
    @Autowired protected AddressFactory addressFactory;
    @Autowired protected JpaRepoFactory jpaRepoFactory;
    @Autowired protected BulkCitizenValidatedFactory bulkCitizenValidatedFactory;
    @Autowired protected NewCitizenValidatedFactory newCitizenValidatedFactory;
    @Autowired protected DualCitizenValidatedFactory dualCitizenValidatedFactory;
    @Autowired protected PersonDetailChangeValidatedFactory personDetailChangeValidatedFactory;
    @Autowired protected CeasedCitizenValidatedFactory ceasedCitizenValidatedFactory;
    @Autowired protected DeathDateValidatedFactory deathDateValidatedFactory;
    @Autowired protected ChangeAddressValidatedFactory changeAddressValidatedFactory;
    @Autowired protected OldNcaAddressFactory oldNcaAddressFactory;
    @Autowired protected NewNcaAddressFactory newNcaAddressFactory;
    @Autowired protected OldMhaAddressFactory oldMhaAddressFactory;
    @Autowired protected NewMhaAddressFactory newMhaAddressFactory;
    @Autowired protected FileDataProcessingFactory fileDataProcessingFactory;

    //Batch File Data Helpers
    @Autowired protected MhaDualCitizenFileDataPrep mhaDualCitizenFileDataPrep;
    @Autowired protected MhaDeathDateFileDataPrep mhaDeathDateFileDataPrep;
    @Autowired protected MhaChangePersonDetailsDataPrep mhaChangePersonDetailsDataPrep;
    @Autowired protected MhaCeasedCitizenFileDataPrep mhaCeasedCitizenFileDataPrep;
    @Autowired protected MhaBulkFileDataPrep mhaBulkFileDataPrep;
    @Autowired protected MhaNoInteractionFileDataPrep mhaNoInteractionFileDataPrep;
    @Autowired protected MhaChangeAddressFileDataPrep mhaChangeAddressFileDataPrep;
    @Autowired protected MhaChangeAddressDataPrep mhaChangeAddressDataPrep;
    @Autowired protected IrasTriMonthlyEgressDataPrep irasTriMonthlyEgressDataPrep;
    @Autowired protected MhaNewCitizenFileDataPrep mhaNewCitizenFileDataPrep;
    @Autowired protected IrasAssessableIncomeFileDataPrep irasAssessableIncomeFileDataPrep;

    //Model Repositories
    @Autowired protected BatchRepo batchRepo;
    @Autowired protected BatchJobExecutionRepo batchJobExecutionRepo;
    @Autowired protected BatchJobExecutionParamsRepo batchJobExecutionParamsRepo;
    @Autowired protected FileReceivedRepo fileReceivedRepo;
    @Autowired protected FileDetailRepo fileDetailRepo;
    @Autowired protected IncomingRecordRepo incomingRecordRepo;
    @Autowired protected NationalityRepo nationalityRepo;
    @Autowired protected ErrorMessageRepo errorMessageRepo;
    @Autowired protected PersonRepo personRepo;
    @Autowired protected GenderRepo genderRepo;
    @Autowired protected PersonIdRepo personIdRepo;
    @Autowired protected PersonDetailRepo personDetailRepo;
    @Autowired protected PersonPropertyRepo personPropertyRepo;
    @Autowired protected PropertyRepo propertyRepo;
    @Autowired protected PersonNameRepo personNameRepo;
    @Autowired protected PropertyDetailRepo propertyDetailRepo;
    @Autowired protected PersonStatusRepo personStatusRepo;
    @Autowired protected IncomeRepo incomeRepo;
    @Autowired protected AnnualValueRepo annualValueRepo;
    @Autowired protected SpecialPropertyRepo specialPropertyRepo;
    @Autowired protected SpecialMappingRepo specialMappingRepo;

    @Autowired protected PropertyValidatedRepo propertyValidatedRepo;
    @Autowired protected DeathDateValidatedRepo deathDateValidatedRepo;
    @Autowired protected NoInteractionValidatedRepo noInteractionValidatedRepo;
    @Autowired protected BulkCitizenValidatedRepo bulkCitizenValidatedRepo;
    @Autowired protected BulkNcaAddressValidatedRepo bulkNcaAddressValidatedRepo;
    @Autowired protected BulkMhaAddressValidatedRepo bulkMhaAddressValidatedRepo;
    @Autowired protected ChangeAddressValidatedRepo changeAddressValidatedRepo;
    @Autowired protected DualCitizenValidatedRepo dualCitizenValidatedRepo;
    @Autowired protected DoubleDateHeaderValidatedRepo doubleDateHeaderValidatedRepo;
    @Autowired protected SingleDateHeaderValidatedRepo singleDateHeaderValidatedRepo;
    @Autowired protected SingleDateNoOfRecordsHeaderValidatedRepo singleDateNoOfRecordsHeaderValidatedRepo;
    @Autowired protected CeasedCitizenValidatedRepo ceasedCitizenValidatedRepo;
    @Autowired protected NewCitizenValidatedRepo newCitizenValidatedRepo;
    @Autowired protected PersonDetailChangeValidatedRepo personDetailChangeValidatedRepo;
    @Autowired protected ReasonablenessCheckStatisticViewJpaRepo reasonablenessCheckStatisticViewJpaRepo;
    @Autowired protected ReasonablenessCheckStatisticRepo reasonablenessCheckStatisticRepo;
    @Autowired protected AssessableIncomeValidatedRepo assessableIncomeValidatedRepo;
    @Autowired protected SingleDateRecordTypeHeaderRepo singleDateRecordTypeHeaderRepo;

    @Autowired protected OldMhaAddressRepo oldMhaAddressRepo;
    @Autowired protected OldNcaAddressRepo oldNcaAddressRepo;
    @Autowired protected NewMhaAddressRepo newMhaAddressRepo;
    @Autowired protected NewNcaAddressRepo newNcaAddressRepo;

    @Autowired protected AddressJacksonObjectMapper addressJacksonObjectMapper;

    @Autowired protected PersonPropertyTimelineReconstruction personPropertyTimelineReconstruction;

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
        Double maxWaitDuration = testManager.getTestEnvironment().getWait();
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

    protected boolean waitUntilConditionForBatch(Predicate<Batch> function, Batch batch) {
        Double timer = 0.0;
        Double maxWaitDuration = testManager.getTestEnvironment().getWait();
        boolean isFound;
        do {
            isFound = function.test(batch);
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
