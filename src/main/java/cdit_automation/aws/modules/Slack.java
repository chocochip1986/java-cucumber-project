package cdit_automation.aws.modules;

import cdit_automation.step_definition.AwsSteps;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Slf4j
@Ignore
@Component
public class Slack {

    public final String MHA_BULK_FILE_VERIFICATION_MSG = "Running MHA Bulk Citizen File";

    public enum Level {
        NEUTRAL,
        SUCCESS,
        FAIL
    }

    public void sendToSlack (@NotNull String topicArn, @NotNull String msg, Level level) {
        // TODO: Implement the level
        AmazonSNS sns = AmazonSNSClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_1).build();
        final PublishRequest publishRequest = new PublishRequest(topicArn, msg);
        sns.publish(publishRequest);
    }
}
