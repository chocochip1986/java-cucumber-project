package cdit_automation.step_definition;

import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.FileReceived;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

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

    @When("^Datasource is triggered to generate the IRAS AI Bulk file$")
    public void datasourceIsTriggeredToGenerateTheIRASAIBulkFile() {
        Path filePath = Paths.get(testManager.getProjectRoot().toString(), "src", "test", "resources", "artifacts");
        apiHelper.sendCallToTriggerOutgoingIrasAiJob( filePath.toFile(), dateUtils.now());
        testContext.set("filePath", filePath);
    }

    @When("^Datasource is triggered to generate the (first|subsequent) IRAS AI Tri Monthly file on ([0-9]{8})$")
    public void datasourceIsTriggeredToGenerateTheIRASAITriMonthlyFile(String val, String date) {
        boolean isFirstTriMonthly = val.equalsIgnoreCase("first");
        LocalDate extractionDate = LocalDate.parse(date, dateUtils.DATETIME_FORMATTER_YYYYMMDD);
        Path filePath = Paths.get(testManager.getProjectRoot().toString(), "src", "test", "resources", "artifacts");
        apiHelper.sendCallToTriggerOutgoingIrasTriMonthlyAiJob(
                extractionDate,
                filePath.toFile(),
                isFirstTriMonthly);
        testContext.set("filePath", filePath);
        testContext.set("isFirstTriMonthly", isFirstTriMonthly);
        testContext.set("extractionDate", extractionDate);
    }

    private void trigger() {
        FileReceived fileReceived = testContext.get("fileReceived");
        if ( fileReceived == null ) {
            throw new TestFailException("No file received record created!!!");
        }
        triggerBatchJob(fileReceived);
    }

    private void triggerBatchJob(FileReceived fileReceived) {
        apiHelper.sendCallToTriggerBatchJob(fileReceived);
    }
}
