package cdit_automation.step_definition;

import cdit_automation.asserts.Assert;
import cdit_automation.aws.modules.Slack;
import cdit_automation.configuration.TestEnv;
import cdit_automation.enums.BatchStatusEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.Batch;
import cdit_automation.models.ErrorMessage;
import cdit_automation.models.FileReceived;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

            if ( testManager.getTestEnvironment().getEnv().equals(TestEnv.Env.QA) ) {
                Batch batch = batchRepo.findByFileReceivedOrderByCreatedAtDesc(fileReceived);
                boolean isVerified = waitUntilConditionForBatch(new Predicate<Batch>(){
                    public boolean test(Batch batch) {
                        batch = batchRepo.findByFileReceivedOrderByCreatedAtDesc(fileReceived);
                        if ( batch != null && batch.getStatus().equals(expectedBatchStatus) ) {
                            return Boolean.TRUE;
                        } else {
                            return Boolean.FALSE;
                        }
                    }
                }, batch);
                batch = batchRepo.findByFileReceivedOrderByCreatedAtDesc(fileReceived);
                if (batch == null) {
                    slack.sendToSlack(testManager.testEnv.getTopicArn(), "No batch record created for fileReceived record: "+fileReceived.getId().toString(), Slack.Level.NEUTRAL);
                    Assert.assertNotNull(batch, "The "+batchJobName+" job from "+agencyName+" is null!!!");
                } else {
                    slack.sendToSlack(testManager.testEnv.getTopicArn(), String.format("Status:%s", batch.getStatus()), Slack.Level.NEUTRAL);
                    Assert.assertEquals(expectedBatchStatus, batch.getStatus(), "The "+batchJobName+" job from "+agencyName+" did not complete!!!");
                }
            } else {
                Batch batch = batchRepo.findByFileReceivedOrderByCreatedAtDesc(fileReceived);
                Assert.assertEquals(expectedBatchStatus, batch.getStatus(), "The "+batchJobName+" job from "+agencyName+" did not complete!!!");
            }
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

    @And("^the error message contains (.*)$")
    public void theErrorMessageContains(String errorMsg) {
        FileReceived fileReceived = testContext.get("fileReceived");
        Batch batch = testContext.get("batch");
        if ( batch == null ) {
            throw new TestFailException("No batch record is found in the testContext!!!");
        }
        log.info("Verifying that the batch job ("+batch.getId()+") contains a corresponding error message record with error: "+errorMsg);

        List<ErrorMessage> errorMessage = errorMessageRepo.findByBatch(batch);

        Assert.assertEquals(true, errorMessage.stream().filter(errMsg -> errMsg.getMessage().matches(".*"+errorMsg+".*")).findFirst().isPresent(), "No such error message is found for batch: "+batch.getId());
    }

    @And("I verify that the following error message appeared:")
    public void iVerifyThatTheFollowingErrorMessageAppeared(DataTable table) {
        
        Batch batch = batchRepo.findByFileReceivedOrderByCreatedAtDesc(testContext.get("fileReceived"));
        List<String> errorMessages =
                errorMessageRepo.findByBatch(batch).stream()
                        .map(ErrorMessage::getMessage)
                        .collect(Collectors.toList());
        
        List<Map<String, String>> dataMap = table.asMaps(String.class, String.class);
        dataMap
                .forEach(
                        m -> {
                            int expectedMessageCount = parseStringSize((String) m.get("Count"));
                            String expectedErrorMessage = m.get("Message").toString();
                            Assert.assertEquals(
                                    (long) expectedMessageCount,
                                    errorMessages.stream()
                                            .filter(z -> z.equalsIgnoreCase(expectedErrorMessage))
                                            .count(),
                                    "Unexpected repetition of [ " + expectedErrorMessage + " ] error message");
                            errorMessages.removeIf(e -> e.equalsIgnoreCase(expectedErrorMessage));
                        });
        
        Assert.assertEquals(Collections.emptyList(), errorMessages, "Unexpected error message found!");
    }
}
