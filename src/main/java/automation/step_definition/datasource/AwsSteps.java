package automation.step_definition.datasource;

import automation.aws.modules.Slack;
import automation.configuration.datasource.TestEnv;
import io.cucumber.java.en.Then;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AwsSteps extends AbstractSteps {
    @Then("^I notify in slack ([A-Za-z\\s]+)$")
    public void notifySlack (@NotNull String msg) {
        if ( testManager.getTestEnvironment().getEnv().equals(TestEnv.Env.QA)) {
            slack.sendToSlack(testManager.testEnv.getTopicArn(), msg, Slack.Level.NEUTRAL);
        }
    }
}
