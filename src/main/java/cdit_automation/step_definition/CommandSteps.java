package cdit_automation.step_definition;

import cdit_automation.aws.modules.Slack;
import cdit_automation.configuration.TestEnv;
import cdit_automation.enums.FileStatusEnum;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.function.Supplier;

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
        apiHelper.sendCallToTriggerOutgoingIrasAiJob( filePath.toFile(), dateUtils.now());
        testContext.set("filePath", filePath);
    }

    @When("^Datasource is triggered to generate the (first|subsequent) IRAS AI Tri Monthly file on ([0-9]{8})$")
    public void datasourceIsTriggeredToGenerateTheIRASAITriMonthlyFile(String val, String date) {
        boolean isFirstTriMonthly = val.equalsIgnoreCase("first");
        LocalDate extractionDate = LocalDate.parse(date, dateUtils.DATETIME_FORMATTER_YYYYMMDD);
        Path filePath = Paths.get(testManager.getProjectRoot().toString(), "src", "main", "resources", "artifacts");
        apiHelper.sendCallToTriggerOutgoingIrasTriMonthlyAiJob(
                extractionDate,
                filePath.toFile(),
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

    @When("^MHA sends the (MHA_BULK_CITIZEN|MHA_NO_INTERACTION) file to Datasource sftp for processing$")
    public void mhaSendsTheDeath_dateFileToDatasourceSftpForProcessing(FileTypeEnum fileTypeEnum) {
        if ( testManager.getTestEnvironment().equals(TestEnv.Env.LOCAL) ) {
            FileDetail fileDetail = fileDetailRepo.findByFileEnum(fileTypeEnum);
            FileReceived fileReceived = batchFileCreator.createFileReceived(fileDetail, fileTypeEnum.getValue().toLowerCase());
            testContext.set("fileReceived", fileReceived);
        } else {
            //Fire call to sftp, for testing purposes only
            slack.sendToSlack(testManager.testEnv.getTopicArn(), slack.MHA_BULK_FILE_VERIFICATION_MSG, Slack.Level.NEUTRAL);
            log.info("Uploading file "+fileTypeEnum.getValue().toLowerCase()+" at "+batchFileDataWriter.getDestionationFile().getAbsolutePath()+" to S3...");
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

    private void trigger() {
        if ( testManager.getTestEnvironment().equals(TestEnv.Env.LOCAL) ) {
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
