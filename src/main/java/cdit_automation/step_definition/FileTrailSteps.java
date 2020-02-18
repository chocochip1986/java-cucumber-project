package cdit_automation.step_definition;

import cdit_automation.pages.FileTrailPage;
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

    @Then("^I see the File Trail of the file$")
    public void shouldSeeFileTrailOfFile() {
        log.info("Verifying file trail page is displayed...");
        fileTrailPage.verifyFileTrailPage();
    }

    @And("^I click on the back button$")
    public void iClickOnBackButton() {
        log.info("Clicking on Back button...");
        fileTrailPage.clickBack();
    }
}
