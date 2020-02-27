package cdit_automation.step_definition;

import cdit_automation.models.FileReceived;
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

    @Then("^I should see that there are ([0-9]+) rows displayed$")
    public void iShouldSeeThatThereAreSomeRowsDisplayed(int expectedNumOfRows) {
        log.info("Verifying that there are correct number of rows generated on the Files Dashboard..");
        filesDashBoardPage.verifyCorrectNumberOfRowsInTable(expectedNumOfRows);
    }

    @Then("^I select items per page to be (3|5|10)$")
    public void iSelectItemsPerPage(int count){
        filesDashBoardPage.selectItemsPerPage(count);
    }

    @Then("^The records should be displayed with the correct current status$")
    public void iShouldSeeTheCorrectCurrentStatusDisplayed() {
        log.info("Verifying that the correct current status is displayed for each file on Files Dashboard..");
        filesDashBoardPage.verifyCorrectCurrentStatusGenerated();

    }

    @Then("I verify that there are no files displayed")
    public void iVerifyThatThereAreNoFilesDisplayed() {
        log.info("Verifying that there are no files displayed on dashboard...");
        filesDashBoardPage.verifyEmptyDashboard();
    }

    @And("I search for the file")
    public void iSearchForTheFile() {
        log.info("Searching for file...");
        FileReceived fileReceived = testContext.get("selectForFileTrail");
        filesDashBoardPage.searchForFile(fileReceived);
    }
}
