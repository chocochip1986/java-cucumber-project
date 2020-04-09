package automation.step_definition.datasource;

import automation.enums.datasource.FileTypeEnum;
import automation.exceptions.TestFailException;
import automation.models.datasource.Batch;
import automation.pages.datasource.TrendingRecordsPage;
import io.cucumber.java.en.Then;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Ignore
public class TrendingRecordsSteps extends AbstractSteps {
    @Autowired
    TrendingRecordsPage trendingRecordsPage;

    @Then("^I can see the statistics for (MHA|IRAS|SINGPOST|HDB|CPFB) " +
            "(BULK CITIZEN|NEW CITIZEN|DUAL CITIZEN|NO INTERACTION|DUAL CITIZEN|PERSON DETAIL CHANGE|DEATH DATE|CEASED CITIZEN)$")
    public void iCanSeeTheStatisticsFor(String agency, String fileName) {
        log.info("Verifying statistics for "+fileName);
        List<Batch> listOfExpectedBatchesWithStats = testContext.get("listOfExpectedBatches");
        listOfExpectedBatchesWithStats.add(0, testContext.get("batch"));
        if ( listOfExpectedBatchesWithStats == null ) {
            throw new TestFailException("This test needs to have 'listOfExpectedBatches' set in the testContext in order to do data verification!");
        }
        FileTypeEnum fileTypeEnum = FileTypeEnum.fromString(String.join("_", agency, fileName.replace(" ", "_")));
        trendingRecordsPage.verifyStatisticsForFile(fileTypeEnum, listOfExpectedBatchesWithStats);
    }
}
