package cdit_automation.step_definition;

import cdit_automation.configuration.AbstractAutoWired;
import cdit_automation.enums.BatchStatusEnum;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.models.Batch;
import cdit_automation.models.FileDetail;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class RevalidateFileSteps extends AbstractAutoWired {

    @Given("^There is (?:a|an) (MHA|IRAS|SINGPOST|HDB|CPFB) " +
            "(BULK CITIZEN|NEW CITIZEN|DUAL CITIZEN|NO INTERACTION|DUAL CITIZEN|PERSON DETAIL CHANGE|DEATH DATE|CEASED CITIZEN) " +
            "file, processed (\\d+) days ago, exceeded the error threshold by (\\d+)%$")
    public void thereIsAFileProcessedDaysAgoExceededTheErrorRate(String agency, String file, long daysAgo, int errorPercent) {
        String fileType = String.join("_", agency, file.replace(" ", "_"));
        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.fromString(fileType));

        Batch batch = fileDataProcessingFactory.generateRecordsBasedOn(BatchStatusEnum.ERROR_RATE_ERROR, fileDetail.getFileEnum(), 100, dateUtils.daysBeforeToday(daysAgo), errorPercent);

        testContext.set("fileReceived", batch.getFileReceived());
        testContext.set("batch", batch);
    }
}
