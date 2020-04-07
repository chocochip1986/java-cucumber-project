package cds_automation.configuration.datasource;

import cds_automation.api_helpers.datasource.ApiHelper;
import cds_automation.asserts.Assert;
import cds_automation.aws.modules.S3;
import cds_automation.aws.modules.Slack;
import cds_automation.configuration.TestManager;
import cds_automation.data_helpers.datasource.BatchFileCreator;
import cds_automation.data_helpers.datasource.BatchFileDataWriter;
import cds_automation.data_helpers.datasource.IrasAssessableIncomeFileDataPrep;
import cds_automation.data_helpers.datasource.IrasTriMonthlyEgressDataPrep;
import cds_automation.data_helpers.datasource.MhaBulkFileDataPrep;
import cds_automation.data_helpers.datasource.MhaCeasedCitizenFileDataPrep;
import cds_automation.data_helpers.datasource.MhaChangeAddressDataPrep;
import cds_automation.data_helpers.datasource.MhaChangeAddressFileDataPrep;
import cds_automation.data_helpers.datasource.MhaChangePersonDetailsDataPrep;
import cds_automation.data_helpers.datasource.MhaDeathDateFileDataPrep;
import cds_automation.data_helpers.datasource.MhaDualCitizenFileDataPrep;
import cds_automation.data_helpers.datasource.MhaNewCitizenFileDataPrep;
import cds_automation.data_helpers.datasource.MhaNoInteractionFileDataPrep;
import cds_automation.data_helpers.datasource.factories.AddressFactory;
import cds_automation.data_helpers.datasource.factories.BulkCitizenValidatedFactory;
import cds_automation.data_helpers.datasource.factories.CeasedCitizenValidatedFactory;
import cds_automation.data_helpers.datasource.factories.ChangeAddressValidatedFactory;
import cds_automation.data_helpers.datasource.factories.DeathDateValidatedFactory;
import cds_automation.data_helpers.datasource.factories.DualCitizenValidatedFactory;
import cds_automation.data_helpers.datasource.factories.FileDataProcessingFactory;
import cds_automation.data_helpers.datasource.factories.JpaRepoFactory;
import cds_automation.data_helpers.datasource.factories.NewCitizenValidatedFactory;
import cds_automation.data_helpers.datasource.factories.NewMhaAddressFactory;
import cds_automation.data_helpers.datasource.factories.NewNcaAddressFactory;
import cds_automation.data_helpers.datasource.factories.OldMhaAddressFactory;
import cds_automation.data_helpers.datasource.factories.OldNcaAddressFactory;
import cds_automation.data_helpers.datasource.factories.PersonDetailChangeValidatedFactory;
import cds_automation.data_helpers.datasource.factories.PersonFactory;
import cds_automation.models.datasource.Batch;
import cds_automation.page_navigation.PageUtils;
import cds_automation.repositories.datasource.AnnualValueRepo;
import cds_automation.repositories.datasource.AssessableIncomeValidatedRepo;
import cds_automation.repositories.datasource.BatchJobExecutionParamsRepo;
import cds_automation.repositories.datasource.BatchJobExecutionRepo;
import cds_automation.repositories.datasource.BatchRepo;
import cds_automation.repositories.datasource.BulkCitizenValidatedRepo;
import cds_automation.repositories.datasource.BulkMhaAddressValidatedRepo;
import cds_automation.repositories.datasource.BulkNcaAddressValidatedRepo;
import cds_automation.repositories.datasource.CeasedCitizenValidatedRepo;
import cds_automation.repositories.datasource.ChangeAddressValidatedRepo;
import cds_automation.repositories.datasource.DeathDateValidatedRepo;
import cds_automation.repositories.datasource.DoubleDateHeaderValidatedRepo;
import cds_automation.repositories.datasource.DualCitizenValidatedRepo;
import cds_automation.repositories.datasource.ErrorMessageRepo;
import cds_automation.repositories.datasource.FileDetailRepo;
import cds_automation.repositories.datasource.FileReceivedRepo;
import cds_automation.repositories.datasource.GenderRepo;
import cds_automation.repositories.datasource.IncomeRepo;
import cds_automation.repositories.datasource.IncomingRecordRepo;
import cds_automation.repositories.datasource.NationalityRepo;
import cds_automation.repositories.datasource.NewCitizenValidatedRepo;
import cds_automation.repositories.datasource.NewMhaAddressRepo;
import cds_automation.repositories.datasource.NewNcaAddressRepo;
import cds_automation.repositories.datasource.NoInteractionValidatedRepo;
import cds_automation.repositories.datasource.OldMhaAddressRepo;
import cds_automation.repositories.datasource.OldNcaAddressRepo;
import cds_automation.repositories.datasource.PersonDetailChangeValidatedRepo;
import cds_automation.repositories.datasource.PersonDetailRepo;
import cds_automation.repositories.datasource.PersonIdRepo;
import cds_automation.repositories.datasource.PersonNameRepo;
import cds_automation.repositories.datasource.PersonPropertyRepo;
import cds_automation.repositories.datasource.PersonRepo;
import cds_automation.repositories.datasource.PersonStatusRepo;
import cds_automation.repositories.datasource.PropertyDetailRepo;
import cds_automation.repositories.datasource.PropertyRepo;
import cds_automation.repositories.datasource.PropertyValidatedRepo;
import cds_automation.repositories.datasource.ReasonablenessCheckStatisticRepo;
import cds_automation.repositories.datasource.SingleDateHeaderValidatedRepo;
import cds_automation.repositories.datasource.SingleDateNoOfRecordsHeaderValidatedRepo;
import cds_automation.repositories.datasource.SingleDateRecordTypeHeaderRepo;
import cds_automation.repositories.datasource.SpecialMappingRepo;
import cds_automation.repositories.datasource.SpecialPropertyRepo;
import cds_automation.repositories.datasource.view.ReasonablenessCheckStatisticViewJpaRepo;
import cds_automation.utilities.DateUtils;
import cds_automation.utilities.PersonPropertyTimelineReconstruction;
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
