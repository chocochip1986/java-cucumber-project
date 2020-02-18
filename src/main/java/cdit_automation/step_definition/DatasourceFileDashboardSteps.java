package cdit_automation.step_definition;

import cdit_automation.pages.FilesDashBoardPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Ignore
public class DatasourceFileDashboardSteps extends AbstractSteps {
    @Autowired
    FilesDashBoardPage filesDashBoardPage;

    @And("^I click on a file to access it's file trail$")
    public void iClickOnAFileToAccessFileTrail() {
        log.info("Accessing a file's trail at random...");
        filesDashBoardPage.accessRandomFileTrail();
    }

    @Then("^I should see the Files Dashboard$")
    public void iShouldSeeFilesDashboard() {
        log.info("Verifying that the Files Dashboard is displayed...");
        filesDashBoardPage.verifyFilesDashboardPage();
    }

    @Then("^I should see that there are files displayed$")
    public void iShouldSeeThatTHereAreFilesDisplayed() {
        log.info("Verifying that there are files displayed on the Files Dashboard...");
        filesDashBoardPage.verifyFilesExistsInTable();
    }
}