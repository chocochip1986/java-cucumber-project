package cdit_automation.aws.modules;

import cdit_automation.configuration.TestManager;
import cdit_automation.utilities.FileUtils;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import io.cucumber.java.en.And;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.Date;

@Slf4j
public class S3 {

    public void uploadToS3(String fileName, String bucketName, String bucketPath) {
        TestManager testManager = new TestManager();
        File file = FileUtils.findOrCreate(testManager.getOutputArtifactsDir() + File.separator + fileName + ".txt");
        long millis = System.currentTimeMillis();
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_1).build();
        s3.putObject(
                bucketName,
                bucketPath + File.separator + fileName + '_' + millis + ".txt",
                file
        );
    }
}
