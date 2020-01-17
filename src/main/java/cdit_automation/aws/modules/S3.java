package cdit_automation.aws.modules;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class S3 {
    public void uploadToS3(File file, String bucketName, String bucketPath) {
        long millis = System.currentTimeMillis();
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_1).build();
        s3.putObject(
                bucketName,
                bucketPath + File.separator + file.getName() + '_' + millis + ".txt",
                file
        );
    }
}
