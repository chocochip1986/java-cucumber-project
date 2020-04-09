package automation.configuration.datasource;

import automation.api_helpers.datasource.ApiHelper;
import automation.asserts.Assert;
import automation.aws.modules.S3;
import automation.aws.modules.Slack;
import automation.configuration.TestManager;
import automation.data_helpers.datasource.BatchFileCreator;
import automation.data_helpers.datasource.BatchFileDataWriter;
import automation.data_helpers.datasource.IrasAssessableIncomeFileDataPrep;
import automation.data_helpers.datasource.IrasTriMonthlyEgressDataPrep;
import automation.data_helpers.datasource.MhaBulkFileDataPrep;
import automation.data_helpers.datasource.MhaCeasedCitizenFileDataPrep;
import automation.data_helpers.datasource.MhaChangeAddressDataPrep;
import automation.data_helpers.datasource.MhaChangeAddressFileDataPrep;
import automation.data_helpers.datasource.MhaChangePersonDetailsDataPrep;
import automation.data_helpers.datasource.MhaDeathDateFileDataPrep;
import automation.data_helpers.datasource.MhaDualCitizenFileDataPrep;
import automation.data_helpers.datasource.MhaNewCitizenFileDataPrep;
import automation.data_helpers.datasource.MhaNoInteractionFileDataPrep;
import automation.data_helpers.datasource.factories.AddressFactory;
import automation.data_helpers.datasource.factories.BulkCitizenValidatedFactory;
import automation.data_helpers.datasource.factories.CeasedCitizenValidatedFactory;
import automation.data_helpers.datasource.factories.ChangeAddressValidatedFactory;
import automation.data_helpers.datasource.factories.DeathDateValidatedFactory;
import automation.data_helpers.datasource.factories.DualCitizenValidatedFactory;
import automation.data_helpers.datasource.factories.FileDataProcessingFactory;
import automation.data_helpers.datasource.factories.JpaRepoFactory;
import automation.data_helpers.datasource.factories.NewCitizenValidatedFactory;
import automation.data_helpers.datasource.factories.NewMhaAddressFactory;
import automation.data_helpers.datasource.factories.NewNcaAddressFactory;
import automation.data_helpers.datasource.factories.OldMhaAddressFactory;
import automation.data_helpers.datasource.factories.OldNcaAddressFactory;
import automation.data_helpers.datasource.factories.PersonDetailChangeValidatedFactory;
import automation.data_helpers.datasource.factories.PersonFactory;
import automation.models.datasource.Batch;
import automation.page_navigation.PageUtils;
import automation.repositories.datasource.AnnualValueRepo;
import automation.repositories.datasource.AssessableIncomeValidatedRepo;
import automation.repositories.datasource.BatchJobExecutionParamsRepo;
import automation.repositories.datasource.BatchJobExecutionRepo;
import automation.repositories.datasource.BatchRepo;
import automation.repositories.datasource.BulkCitizenValidatedRepo;
import automation.repositories.datasource.BulkMhaAddressValidatedRepo;
import automation.repositories.datasource.BulkNcaAddressValidatedRepo;
import automation.repositories.datasource.CeasedCitizenValidatedRepo;
import automation.repositories.datasource.ChangeAddressValidatedRepo;
import automation.repositories.datasource.DeathDateValidatedRepo;
import automation.repositories.datasource.DoubleDateHeaderValidatedRepo;
import automation.repositories.datasource.DualCitizenValidatedRepo;
import automation.repositories.datasource.ErrorMessageRepo;
import automation.repositories.datasource.FileDetailRepo;
import automation.repositories.datasource.FileReceivedRepo;
import automation.repositories.datasource.GenderRepo;
import automation.repositories.datasource.IncomeRepo;
import automation.repositories.datasource.IncomingRecordRepo;
import automation.repositories.datasource.NationalityRepo;
import automation.repositories.datasource.NewCitizenValidatedRepo;
import automation.repositories.datasource.NewMhaAddressRepo;
import automation.repositories.datasource.NewNcaAddressRepo;
import automation.repositories.datasource.NoInteractionValidatedRepo;
import automation.repositories.datasource.OldMhaAddressRepo;
import automation.repositories.datasource.OldNcaAddressRepo;
import automation.repositories.datasource.PersonDetailChangeValidatedRepo;
import automation.repositories.datasource.PersonDetailRepo;
import automation.repositories.datasource.PersonIdRepo;
import automation.repositories.datasource.PersonNameRepo;
import automation.repositories.datasource.PersonPropertyRepo;
import automation.repositories.datasource.PersonRepo;
import automation.repositories.datasource.PersonStatusRepo;
import automation.repositories.datasource.PropertyDetailRepo;
import automation.repositories.datasource.PropertyRepo;
import automation.repositories.datasource.PropertyValidatedRepo;
import automation.repositories.datasource.ReasonablenessCheckStatisticRepo;
import automation.repositories.datasource.SingleDateHeaderValidatedRepo;
import automation.repositories.datasource.SingleDateNoOfRecordsHeaderValidatedRepo;
import automation.repositories.datasource.SingleDateRecordTypeHeaderRepo;
import automation.repositories.datasource.SpecialMappingRepo;
import automation.repositories.datasource.SpecialPropertyRepo;
import automation.repositories.datasource.view.ReasonablenessCheckStatisticViewJpaRepo;
import automation.utilities.DateUtils;
import automation.utilities.PersonPropertyTimelineReconstruction;
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
