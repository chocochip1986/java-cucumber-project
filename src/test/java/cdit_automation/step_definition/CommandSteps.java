package cdit_automation.step_definition;

import cdit_automation.enums.FileTypeEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.FileReceived;
import io.cucumber.java.en.When;

public class CommandSteps extends AbstractSteps {
    @When("^the mha dual citizen job is ran$")
    public void triggerMhaDualCitizenBatchJob() {
        FileReceived fileReceived = testContext.get("fileReceived");
        if ( fileReceived == null ) {
            throw new TestFailException("No file received record created!!!");
        }
        triggerBatchJob(FileTypeEnum.MHA_DUAL_CITIZEN, fileReceived);
    }

    private void triggerBatchJob(FileTypeEnum fileTypeEnum, FileReceived fileReceived) {
        apiHelper.sendCallToTriggerBatchJob(fileTypeEnum, fileReceived);
    }
}
