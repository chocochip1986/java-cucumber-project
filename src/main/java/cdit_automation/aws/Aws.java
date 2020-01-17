package cdit_automation.aws;

import cdit_automation.aws.modules.S3;
import cdit_automation.aws.modules.Slack;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Slf4j
public class Aws {

    @Value(value = "${aws.sns.topic-arn}")
    private String topicArn;

    public enum Level {
        NEUTRAL,
        SUCCESS,
        FAIL
    }

    @Then("I notify in slack (.*)")
    public void notifySlack (@NotNull String msg) {
        Slack slack = new Slack();
        slack.sendToSlack(topicArn, msg, Level.NEUTRAL);
    }

    public void notifySlack (@NotNull String msg, Level level) {
        Slack slack = new Slack();
        slack.sendToSlack(topicArn, msg, level);
    }

    @And("I upload the file (.*) to bucket (.*) at path (.*)$")
    public void uploadToS3(String fileName, String bucketName, String bucketPath) {
        S3 s3 = new S3();
        s3.uploadToS3(fileName,bucketName,bucketPath);
    }
}
