package cdit_automation.step_definition;

import cdit_automation.aws.modules.Slack;
import cdit_automation.configuration.TestEnv;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

@Slf4j
public class AwsSteps extends AbstractSteps {
    @Then("^I notify in slack ([A-Za-z\\s]+)$")
    public void notifySlack (@NotNull String msg) {
        if ( testManager.getTestEnvironment().equals(TestEnv.Env.QA)) {
            slack.sendToSlack(testManager.testEnv.getTopicArn(), msg, Slack.Level.NEUTRAL);
        }
    }
}
