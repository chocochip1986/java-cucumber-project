package cdit_automation.step_definition;

import cdit_automation.aws.modules.S3;
import cdit_automation.aws.modules.Slack;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

@Slf4j
public class AwsSteps extends AbstractSteps {

    public enum Level {
        NEUTRAL,
        SUCCESS,
        FAIL
    }

    @Then("^I notify in slack ([A-Za-z\\s]+)$")
    public void notifySlack (@NotNull String msg) {
        if ( testManager.getTestEnvironment().equals("qa")) {
            slack.sendToSlack(testManager.testEnv.getTopicArn(), msg, Level.NEUTRAL);
        }
    }

    public void notifySlack (@NotNull String msg, Level level) {
        slack.sendToSlack(testManager.testEnv.getTopicArn(), msg, level);
    }

    @And("^I upload the file (.*) to bucket (.*) at path (.*)$")
    public void uploadToS3(String fileName, String bucketName, String bucketPath) {
        if ( testManager.getTestEnvironment().equals("qa")) {
            log.info("Uploading file "+fileName+" at "+batchFileDataWriter.getDestionationFile().getAbsolutePath()+" to S3...");
            s3.uploadToS3(batchFileDataWriter.getDestionationFile(),bucketName,bucketPath);
        }
    }
}
