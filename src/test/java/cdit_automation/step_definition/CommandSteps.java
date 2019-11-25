package cdit_automation.step_definition;

import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.FileReceived;
import io.cucumber.java.en.When;
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

    private void triggerBatchJob(FileReceived fileReceived) {
        apiHelper.sendCallToTriggerBatchJob(fileReceived);
    }

    @When("the mha bulk file is ran")
    public void theMhaBulkFileIsRan() {
        log.info("Triggering MHA bulk batch job is run");
        trigger();
    }

    private void trigger() {
        FileReceived fileReceived = testContext.get("fileReceived");
        if ( fileReceived == null ) {
            throw new TestFailException("No file received record created!!!");
        }
        triggerBatchJob(fileReceived);
    }

    @When("^the mha ceased sc job is ran$")
    public void theMhaCeasedScJobIsRan() {
        log.info("Triggering MHA ceased citizen batch job to run");
        FileReceived fileReceived = testContext.get("fileReceived");
        if ( fileReceived == null ) {
            throw new TestFailException("No file received record created!!!");
        }
        triggerBatchJob(fileReceived);
    }
}
