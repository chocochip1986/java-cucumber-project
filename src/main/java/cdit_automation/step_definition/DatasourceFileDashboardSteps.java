package cdit_automation.step_definition;

import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.FileReceived;
import cdit_automation.pages.FilesDashBoardPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
        FileReceived fileReceived = testContext.get("fileReceived");
        filesDashBoardPage.searchForFile(fileReceived);
    }

    @And("^the current status of the file in the Dashboard is (.*) : (.*)$")
    public void theCurrentStatusOfTheFileInTheDashboard(String mainStatus, String subStatus) {
        log.info("Verifying that the file is in the Dashboard with status: " + mainStatus + " : " + subStatus);

        FileReceived fileReceived = testContext.get("fileReceived");
        WebElement targetFileWebElement = filesDashBoardPage.findFileReceivedWebElement(fileReceived);
        
        if (targetFileWebElement == null) {
            throw new TestFailException("Unable to find file: " + fileReceived.getFilePath());
        }
        
        List<WebElement> cellWebElements = targetFileWebElement.findElements(By.cssSelector(".mat-cell"));
        WebElement currentStatusElement = cellWebElements.get(6);
        
        String mainStatusCellText = 
                currentStatusElement.findElement(By.cssSelector(FilesDashBoardPage.CURRENT_STATUS_MAIN_TEXT)).getText();
        String subStatusCellText = 
                currentStatusElement.findElement(By.cssSelector(FilesDashBoardPage.CURRENT_STATUS_SUB_TEXT)).getText();

        testAssert.assertEquals
                (mainStatus, mainStatusCellText, "Main status expected does not match with main status displayed.");
        testAssert.assertEquals
                (subStatus, subStatusCellText, "Sub status expected does not match with sub status displayed.");
    }
}
