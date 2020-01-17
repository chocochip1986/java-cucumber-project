package cdit_automation.aws.modules;

import cdit_automation.aws.Aws;
import cdit_automation.configuration.AwsEnv;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import io.cucumber.java.en.And;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Slf4j
@Ignore
public class Slack {

    public void sendToSlack (@NotNull String topicArn, @NotNull String msg, Aws.Level level) {
        // TODO: Implement the level
        AmazonSNS sns = AmazonSNSClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_1).build();
        final PublishRequest publishRequest = new PublishRequest(topicArn, msg);
        sns.publish(publishRequest);
    }
}
