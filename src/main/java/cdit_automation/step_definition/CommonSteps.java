package cdit_automation.step_definition;

import cdit_automation.api_helpers.ApiHelper;
import cdit_automation.asserts.Assert;
import cdit_automation.enums.BatchStatusEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.Batch;
import cdit_automation.models.ErrorMessage;
import cdit_automation.models.FileReceived;
import io.cucumber.java.en.And;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
            testContext.set("batch", batch);

            if ( testManager.getTestEnvironment().equals("qa") ) {
                if (batch == null) {
                    slack.sendToSlack(testManager.testEnv.getTopicArn(), "No batch record created for fileReceived record: "+fileReceived.getId().toString(), AwsSteps.Level.NEUTRAL);
                } else {
                    slack.sendToSlack(testManager.testEnv.getTopicArn(), String.format("Status:%s", batch.getStatus()), AwsSteps.Level.NEUTRAL);
                }
            }

            Assert.assertNotNull(batch, "No batch record created for fileReceived record: "+fileReceived.getId().toString());
            Assert.assertEquals(expectedBatchStatus, batch.getStatus(), "The "+batchJobName+" job from "+agencyName+" did not complete!!!");
        } else {
            throw new TestFailException("No batch job previously created!");
        }
    }

    @And("^the error message is (.*)$")
    public void theErrorMessageIs(String errorMsg) {
        FileReceived fileReceived = testContext.get("fileReceived");
        Batch batch = testContext.get("batch");
        if ( batch == null ) {
            throw new TestFailException("No batch record is found in the testContext!!!");
        }
        log.info("Verifying that the batch job ("+batch.getId()+") contains a corresponding error message record with error: "+errorMsg);

        List<ErrorMessage> errorMessage = errorMessageRepo.findByBatch(batch);

        Assert.assertEquals(true, errorMessage.stream().filter(errMsg -> errMsg.getMessage().equals(errorMsg)).findFirst().isPresent(), "No such error message is found for batch: "+batch.getId());
    }
}
