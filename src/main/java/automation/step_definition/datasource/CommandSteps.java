package automation.step_definition.datasource;

import automation.enums.datasource.FileStatusEnum;
import automation.enums.datasource.FileTypeEnum;
import automation.exceptions.TestFailException;
import automation.models.datasource.FileDetail;
import automation.models.datasource.FileReceived;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class CommandSteps extends AbstractSteps {
    @When("^the mha dual citizen job is ran$")
    public void triggerMhaDualCitizenBatchJob() {
        log.info("Triggering MHA dual citizen batch job to run");
        trigger();
    }

    @When("^the mha death job is ran$")
    public void triggerMhaDeathBatchJob() {
        log.info("Triggering MHA death batch job to run");
        trigger();
    }

    @When("the mha bulk file is ran")
    public void theMhaBulkFileIsRan() {
        log.info("Triggering MHA bulk batch job is run");
        trigger();
    }

    @When("^the mha ceased sc job is ran$")
    public void theMhaCeasedScJobIsRan() {
        log.info("Triggering MHA ceased citizen batch job to run");
        trigger();
    }

    @When("^the mha person details job is ran")
    public void theMhaChangeInPersonalDetailsJobIsRan() {
        log.info("Triggering MHA change of personal details job to run");
        trigger();
    }

    @When("the MHA no interaction file is ran")
    public void theMhaNoInteractionFileIsRan() {
        log.info("Triggering MHA no interaction batch job to run");
        trigger();
    }

    @When("^Datasource is triggered to generate the IRAS AI Bulk file$")
    public void datasourceIsTriggeredToGenerateTheIRASAIBulkFile() {
        Path filePath = Paths.get(testManager.getProjectRoot().toString(), "src", "main", "resources", "artifacts");
        apiHelper.sendCallToTriggerOutgoingIrasAiJob(dateUtils.now());
        testContext.set("filePath", filePath);
    }

    @When("^Datasource is triggered to generate the (first|subsequent) IRAS AI Tri Monthly file on ([0-9]{8})$")
    public void datasourceIsTriggeredToGenerateTheIRASAITriMonthlyFile(String val, String date) {
        boolean isFirstTriMonthly = val.equalsIgnoreCase("first");
        LocalDate extractionDate = LocalDate.parse(date, dateUtils.DATETIME_FORMATTER_YYYYMMDD);
        Path filePath = Paths.get(testManager.getProjectRoot().toString(), "src", "main", "resources", "artifacts");
        apiHelper.sendCallToTriggerOutgoingIrasTriMonthlyAiJob(
                extractionDate,
                isFirstTriMonthly);
        testContext.set("filePath", filePath);
        testContext.set("isFirstTriMonthly", isFirstTriMonthly);
        testContext.set("extractionDate", extractionDate);
    }

    @When("^Datasource is triggered to create a file received record for (MHA_DEATH_DATE)$")
    public void datasourceIsTriggeredToCreateAFileReceivedRecord(FileTypeEnum fileTypeEnum) {
        FileDetail fileDetail = fileDetailRepo.findByFileEnum(fileTypeEnum);
        testContext.set("fileReceived", batchFileCreator.replaceFile(fileDetail, fileTypeEnum.getValue().toLowerCase()));
    }

    @When("^(?:MHA|IRAS) sends the (MHA_BULK_CITIZEN|MHA_NEW_CITIZEN|MHA_NO_INTERACTION|MHA_CHANGE_ADDRESS|MHA_DUAL_CITIZEN|MHA_PERSON_DETAIL_CHANGE|MHA_DEATH_DATE|MHA_CEASED_CITIZEN|IRAS_BULK_AI|IRAS_THRICE_MONTHLY_AI) file to Datasource sftp for processing$")
    public void mhaSendsTheFileToDatasourceSftpForProcessing(FileTypeEnum fileTypeEnum) {
        if ( testManager.isDevEnvironment() ) {
            FileDetail fileDetail = fileDetailRepo.findByFileEnum(fileTypeEnum);
            FileReceived fileReceived = batchFileCreator.createFileReceived(fileDetail, fileDetail.getFileName(), testContext.get("receivedTimestamp"));
            if(!testContext.contains("fileReceived")){
                testContext.set("fileReceived", fileReceived);
            }else{
                testContext.replace("fileReceived", fileReceived);
            }

            log.info("Triggering "+fileTypeEnum.getHumanized_value()+" job is run");
            trigger();
        } else {
            //Fire call to sftp, for testing purposes only
//            slack.sendToSlack(testManager.testEnv.getTopicArn(), slack.slackMessage(fileTypeEnum), Slack.Level.NEUTRAL);
            log.info("Uploading file "+fileTypeEnum.getHumanized_value()+" to S3...");
            s3.uploadToS3(batchFileDataWriter.getDestionationFile(), s3.MHA_READY_BUCKET_PATH);

            log.info("Verifying that file received record is created...");
            FileDetail fileDetail = fileDetailRepo.findByFileEnum(fileTypeEnum);
            boolean isFound = waitUntilCondition(new Supplier<Boolean>(){
                public Boolean get() {
                    FileReceived fileReceived = fileReceivedRepo.findTopByFileDetailIdAndFileStatusEnumOrderByReceivedTimestampDesc(fileDetail.getId(), FileStatusEnum.OK);
                    if (fileReceived != null) {
                        return Boolean.TRUE;
                    } else {
                        return Boolean.FALSE;
                    }
                }
            });

            if ( !isFound ) {
                throw new TestFailException("Unable to find a File Received record...");
            }

            FileReceived fileReceived = fileReceivedRepo.findTopByFileDetailIdAndFileStatusEnumOrderByReceivedTimestampDesc(fileDetail.getId(), FileStatusEnum.OK);
            if ( !testContext.contains("fileReceived") ) {
                testContext.set("fileReceived", fileReceived);
            }
        }
    }

    @Then("^I verify that file received record for (MHA_BULK_CITIZEN) file exists with status (OK)$")
    public void iVerifyThatFileReceivedRecordExistsWithStatusOk(FileTypeEnum fileTypeEnum, FileStatusEnum fileStatusEnum) {
        log.info("Verifying that file received record is created...");
        FileDetail fileDetail = fileDetailRepo.findByFileEnum(fileTypeEnum);
        boolean isFound = waitUntilCondition(new Supplier<Boolean>(){
            public Boolean get() {
                FileReceived fileReceived = fileReceivedRepo.findTopByFileDetailIdAndFileStatusEnumOrderByReceivedTimestampDesc(fileDetail.getId(), fileStatusEnum);
                if (fileReceived != null) {
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            }
        });

        if ( !isFound ) {
            throw new TestFailException("Unable to find a File Received record...");
        }

        FileReceived fileReceived = fileReceivedRepo.findTopByFileDetailIdAndFileStatusEnumOrderByReceivedTimestampDesc(fileDetail.getId(), fileStatusEnum);
        if ( !testContext.contains("fileReceived") ) {
            testContext.set("fileReceived", fileReceived);
        }
    }

    //As of cucumber 5.1.0, this is not fully supported by Intellij's cucumber plugin, hence steps which are using this will be marked by Intellij as undefined.
    //However, the test suite runs fine without syntax error.
    //This may make it difficult and confusing for anyone working on the test suite, as it will come across as a false negative. So for the mean time, please refrain from using this.
    //As an example, your step definition has to be as such:
    //@When("the {fileType} is wicked")
    //You can't declare this in a parent step and use this in a child step. It does not work.
    //Secondly, when you use such a regex, you cannot have other forms of regular expressions in the same step like so:
    //@When("^the (hihi|hoho) {fileType} is wicked$")
    //The first capture group is unrecognizable by cucumber and the '^' and '$' is also unrecognizable by cucumber. Strange but not sure why.
    @ParameterType(name = "fileType", value = "MHA_BULK_CITIZEN|MHA_NEW_CITIZEN|MHA_NO_INTERACTION|MHA_CHANGE_ADDRESS|MHA_DUAL_CITIZEN|MHA_PERSON_DETAIL_CHANGE|MHA_DEATH_DATE|MHA_CEASED_CITIZEN")
    public FileTypeEnum fileType(String fileType) {
        return FileTypeEnum.fromString(fileType);
    }

    private void trigger() {
        if ( testManager.isDevEnvironment() ) {
            FileReceived fileReceived = testContext.get("fileReceived");
            if ( fileReceived == null ) {
                throw new TestFailException("No file received record created!!!");
            }
            triggerBatchJob(fileReceived);
        } else {
            //DO NOTHING
        }
    }

    private void triggerBatchJob(FileReceived fileReceived) {
        apiHelper.sendCallToTriggerBatchJob(fileReceived);
    }
}
