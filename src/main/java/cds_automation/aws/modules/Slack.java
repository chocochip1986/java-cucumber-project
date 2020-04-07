package cds_automation.aws.modules;

import cds_automation.enums.datasource.FileTypeEnum;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.springframework.stereotype.Component;

@Slf4j
@Ignore
@Component
public class Slack {

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

    public String slackMessage(FileTypeEnum fileTypeEnum) {
        return "Running "+fileTypeEnum.getValue().replace("_", " ")+" File";
    }
}
