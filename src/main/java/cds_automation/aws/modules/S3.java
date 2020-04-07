package cds_automation.aws.modules;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class S3 {

    public final String TEST_BUCKET = "gds-cpfb-ftp-trial";
    public final String MHA_READY_BUCKET_PATH = "ready/mha";

    public void uploadToS3(File file, String bucketPath) {
        long millis = System.currentTimeMillis();
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_1).build();
        s3.putObject(
                TEST_BUCKET,
                bucketPath + File.separator + file.getName() + '_' + millis + ".txt",
                file
        );
    }
}
