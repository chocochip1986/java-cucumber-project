package cdit_automation.step_definition;

import cdit_automation.asserts.Assert;
import cdit_automation.enums.BatchStatusEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.Batch;
import cdit_automation.models.FileReceived;
import io.cucumber.java.en.And;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class CommonSteps extends AbstractSteps {

    @And("I open a new tab")
    public void iOpenANewTab() {
        pageUtils.openNewTab();
    }

    @And("I close current tab")
    public void iCloseCurrentTab() {
        pageUtils.closeTab();
    }

    @And("^the batch job completes running with status (.*)$")
    public void theMhaDualCitizenJobCompletesRunning(BatchStatusEnum expectedBatchStatus) {
        log.info("Veryfing that batch job ended with status: "+expectedBatchStatus);
        if (testContext.contains("fileReceived")) {
            FileReceived fileReceived = testContext.get("fileReceived");
            Batch batch = batchRepo.findByFileReceivedOrderByCreatedAtDesc(fileReceived).get(0);

            Assert.assertNotNull(batch, "No batch record created for fileReceived record: "+fileReceived.getId().toString());
            Assert.assertEquals(expectedBatchStatus, batch.getStatus(), "MHA Dual Citizen job did not complete!!!");
        } else {
            throw new TestFailException("No batch job previously created!");
        }
    }
}
