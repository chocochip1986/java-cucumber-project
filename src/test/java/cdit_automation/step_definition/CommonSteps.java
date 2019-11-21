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

    @And("^the (Mha|Iras) (.*) batch job completes running with status (.*)$")
    public void theBatchJobCompletesRunning(String agencyName, String batchJobName, BatchStatusEnum expectedBatchStatus) {
        log.info("Veryfing that batch job ended with status: "+expectedBatchStatus);
        if (testContext.contains("fileReceived")) {
            FileReceived fileReceived = testContext.get("fileReceived");
            Batch batch = batchRepo.findByFileReceivedOrderByCreatedAtDesc(fileReceived);

            Assert.assertNotNull(batch, "No batch record created for fileReceived record: "+fileReceived.getId().toString());
            Assert.assertEquals(expectedBatchStatus, batch.getStatus(), "The "+batchJobName+" job from "+agencyName+" did not complete!!!");
        } else {
            throw new TestFailException("No batch job previously created!");
        }
    }
}
