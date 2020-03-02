package cdit_automation.step_definition;

import cdit_automation.models.Batch;
import cdit_automation.pages.FileTrailPage;
import cdit_automation.pages.datasource.TrendingRecordsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Ignore
public class FileTrailSteps extends AbstractSteps {
    @Autowired
    FileTrailPage fileTrailPage;

    @Autowired
    TrendingRecordsPage trendingRecordsPage;

    @And("^I click on the back button$")
    public void iClickOnBackButton() {
        log.info("Clicking on Back button...");
        fileTrailPage.clickBack();
    }

    @Then("I verify that I see the file trail page")
    public void iVerifyThatISeeTheFileTrailPage() {
        log.info("Verifying file trail page is displayed...");
        Batch batch = testContext.get("batch");
        fileTrailPage.verifyFileTrailPage(batch);
    }

    @And("^I click on the reasonableness trending link$")
    public void iClickOnReasonablenessTrendingLink() {
        log.info("Clicking on reasonableness trending link...");
        fileTrailPage.clickReasonablenessTrending();
        trendingRecordsPage.verifyLoaded();
    }
}
