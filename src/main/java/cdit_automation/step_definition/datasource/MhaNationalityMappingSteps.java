package cdit_automation.step_definition.datasource;

import static org.assertj.core.api.Assertions.assertThat;

import cdit_automation.aws.modules.Slack;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.BodyContent.MhaCeasedCitizenBodyDataField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.BodyContent.MhaDualCitizenBodyDataField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.BodyContent.MhaNewCitizenBodyDataField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.CitizenshipAttainmentDateField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.DateField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.DateOfBirthField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.FinField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.FooterOfBodyCountField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.NameField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.NationalityField;
import cdit_automation.data_helpers.datasource_file.mha.DataFields.NricField;
import cdit_automation.data_helpers.datasource_file.mha.MhaCeasedCitizenFile;
import cdit_automation.data_helpers.datasource_file.mha.MhaDualCitizenFile;
import cdit_automation.data_helpers.datasource_file.mha.MhaNewCitizenFile;
import cdit_automation.data_helpers.datasource_file.mha.TestDataStruct.G1224TestData;
import cdit_automation.enums.datasource.BatchStatusEnum;
import cdit_automation.enums.datasource.FileStatusEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.datasource.FileDetail;
import cdit_automation.models.datasource.FileReceived;
import cdit_automation.utilities.FileUtils;
import com.github.javafaker.Faker;
import com.google.common.base.Joiner;
import com.google.common.base.Supplier;
import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.Uninterruptibles;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.Ignore;

import java.io.File;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Ignore
public class MhaNationalityMappingSteps extends AbstractSteps {

    @Given("Mha New Citizen G-1224 file (.*) with Date of Run (\\d+) and following new citizen details:$")
    public void mhaNewCitizenHasDateOfRunAndFollowingNewCitizenDetails(String fileName, String dateOfRun, DataTable table) {
        final List<G1224TestData> list = table.asList(G1224TestData.class);
        final int numberOfRecords = list.size();

        log.info(String.format("Test Automation: Creating Mha New Citizen file %s with Date of Run %s and %d record/s", fileName, dateOfRun, numberOfRecords));

        MhaNewCitizenFile newCitizenFile = new MhaNewCitizenFile(numberOfRecords);
        newCitizenFile.header.value = dateOfRun;

        IntStream.range(0, numberOfRecords).forEach(i -> {
            ((MhaNewCitizenBodyDataField) newCitizenFile.body.get(i)).nric = NricField.builder().value(list.get(i).nric).build();
            ((MhaNewCitizenBodyDataField) newCitizenFile.body.get(i)).fin = FinField.builder().value(list.get(i).fin).build();
            ((MhaNewCitizenBodyDataField) newCitizenFile.body.get(i)).dateOfBirth = DateOfBirthField.builder().value(list.get(i).dateOfBirth).build();
            ((MhaNewCitizenBodyDataField) newCitizenFile.body.get(i)).name = NameField.builder().value(list.get(i).name).build();
            ((MhaNewCitizenBodyDataField) newCitizenFile.body.get(i)).attainmentDate = CitizenshipAttainmentDateField.builder().value(list.get(i).attainmentDate).build();
        });
        newCitizenFile.header.value = dateOfRun;
        newCitizenFile.footer.value = FooterOfBodyCountField.builder().value(numberOfRecords + "").build().toRawString();
        newCitizenFile.fileName = fileName;
        newCitizenFile.createFile();

        log.info(String.format("Test Automation: Creation of Mha New Citizen file %s completed", fileName));
    }


    @And("Mha Dual Citizen G-1224 file (.*) with Date of Run (\\d+) and the following dual citizen details:$")
    public void mhaDualCitizenHasDateOfRunAndFollowingDualCitizenDetails(String fileName, String dateOfRun, DataTable table) {
        final List<G1224TestData> list = table.asList(G1224TestData.class);
        final int numberOfRecords = list.size();

        log.info(String.format("Test Automation: Creating Mha Dual Citizen file %s with Date of Run %s and %d record/s", fileName, dateOfRun, numberOfRecords));

        MhaDualCitizenFile file = new MhaDualCitizenFile(numberOfRecords);

        IntStream.range(0, numberOfRecords).forEach(i -> {
            ((MhaDualCitizenBodyDataField) file.body.get(i)).nric = NricField.builder().value(list.get(i).nric).build();
        });

        file.header.value = dateOfRun;
        file.footer.value = FooterOfBodyCountField.builder().value(numberOfRecords + "").build().toRawString();
        file.fileName = fileName;
        file.createFile();

        log.info(String.format("Test Automation: Creation of Mha Dual Citizen file %s completed", fileName));
    }


    @And("Mha Ceased Citizen G-1224 file (.*) with Date of Run (\\d+) and the following ceased citizen details:$")
    public void mhaCeasedCitizenHasDateOfRunAndFollowingCeasedCitizenDetails(String fileName, String dateOfRun, DataTable table) {
        final List<G1224TestData> list = table.asList(G1224TestData.class);
        final int numberOfRecords = list.size();
        log.info(String.format("Test Automation: Creating Mha Ceased Citizen file %s with Date of Run %s and %d record/s", fileName, dateOfRun, numberOfRecords));
        MhaCeasedCitizenFile file = new MhaCeasedCitizenFile(numberOfRecords);

        IntStream.range(0, numberOfRecords).forEach(i -> {
            ((MhaCeasedCitizenBodyDataField) file.body.get(i)).nric = NricField.builder().value(list.get(i).nric).build();
            ((MhaCeasedCitizenBodyDataField) file.body.get(i)).name = NameField.builder().value(list.get(i).name).build();
            ((MhaCeasedCitizenBodyDataField) file.body.get(i)).nationality = NationalityField.builder().value("MY").build();
            ((MhaCeasedCitizenBodyDataField) file.body.get(i)).ceasedDate = DateField.builder().value(list.get(i).ceasedDate).build();
        });

        file.header.value = dateOfRun;
        file.footer.value = FooterOfBodyCountField.builder().value(numberOfRecords + "").build().toRawString();
        file.fileName = fileName;
        file.createFile();
        log.info(String.format("Test Automation: Creation of Mha Ceased Citizen file %s completed", fileName));
    }

    @When("uploading these files in sequence to S3:")
    public void uploadingTheseFilesInSequenceToS3(DataTable table) {
        final List<G1224TestData> list = table.asList(G1224TestData.class);
        log.info(String.format("Test Automation: Uploading the following files to S3: %s", Joiner.on(", ").join(list.stream().map(i -> i.fileName).collect(Collectors.toList()))));

        apiHelper.sendCallToVerifyEnvironmentHealthIsUp();

        // for each, post to S3. Wait until job completes
        list.stream().forEach(i -> {
            if (testManager.isDevEnvironment()) {
                apiHelper.sendCallToTriggerBatchJob(batchFileCreator.createFileReceived(fileDetailRepo.findByFileEnum(i.fileType), i.fileName.substring(0, i.fileName.length() - 4), testContext.get("receivedTimestamp")));
            } else {
                slack.sendToSlack(testManager.testEnv.getTopicArn(), Faker.instance().harryPotter().spell(), Slack.Level.NEUTRAL);
                log.info("Uploading file " + i.fileType.getHumanized_value() + " to S3...");
                s3.uploadToS3(FileUtils.findOrCreate(testManager.getOutputArtifactsDir() + File.separator + i.fileName), s3.MHA_READY_BUCKET_PATH);
                log.info("Verifying that file received record is created...");
                final FileDetail fileDetail = fileDetailRepo.findByFileEnum(i.fileType);
                boolean isFound = waitUntilCondition((Supplier<Boolean>) () -> fileReceivedRepo.findTopByFileDetailIdAndFileStatusEnumOrderByReceivedTimestampDesc(fileDetail.getId(), FileStatusEnum.OK) != null);
                if (!isFound) {
                    throw new TestFailException("Unable to find a File Received record...");
                }
                FileReceived fileReceived = fileReceivedRepo.findTopByFileDetailIdAndFileStatusEnumOrderByReceivedTimestampDesc(fileDetail.getId(), FileStatusEnum.OK);
                if (!testContext.contains("fileReceived")) {
                    testContext.set("fileReceived", fileReceived);
                }
            }
        });

        final Monitor monitor = new Monitor();
        monitor.enter();
        boolean fileIngressionSucceed = true;
        while (batchRepo.findAll().stream().filter(i -> i.getStatus() == BatchStatusEnum.CLEANUP).count() != 5) {
            if (batchRepo.findAll().stream().filter(i -> i.getStatus().toString().contains("ERROR")).count() > 0) {
                fileIngressionSucceed = false;
                break;
            }
            Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(10l));
        }
        monitor.leave();

        assertThat(fileIngressionSucceed).isTrue();

        log.info("Test Automation: Files upload completed.");
    }

    @Then("these are the following nationality mapping for G-1224 files:")
    public void theseAreTheFollowingNationalityMappingForG1224Files(DataTable table) {
        log.info("Test Automation: Verifying expected results with Prepared Database Nationality");
        final List<G1224TestData> expectedResult = table.asList(G1224TestData.class);

        final Collection<?> res = nationalityRepo.getNationalityWithNric();

        final Set<G1224TestData> actualResult = res.stream().map(i -> {
            final Object[] re = (Object[]) i;
            return G1224TestData.builder()
                    .nric(re[0].toString())
                    .nationality(re[1].toString())
                    .attainmentDate(re[2].toString().substring(0, 19))
                    .ceasedDate(re[3] == null ? "" : re[3].toString().substring(0, 19))
                    .validFrom(re[4].toString().substring(0, 19))
                    .validTill(re[5].toString().substring(0, 19))
                    .build();
        })
                .collect(Collectors.toSet());

        final SoftAssertions softly = new SoftAssertions();
        softly.assertThat(actualResult).hasSize(20);

        expectedResult.stream().forEach(e -> {
            softly.assertThat(actualResult).contains(e);
        });

        softly.assertAll();

        log.info("Test Automation: Assertion for Prepared Database for Nationality passes");
    }
}

