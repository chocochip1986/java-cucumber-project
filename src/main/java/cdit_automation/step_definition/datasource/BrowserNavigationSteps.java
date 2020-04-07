package cdit_automation.step_definition.datasource;

import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class BrowserNavigationSteps extends AbstractSteps {
    @When("I click the browser back button")
    public void iClickTheBrowserBackButton() {
        log.info("Clicking on the browser back button...");
        pageUtils.browserBack();
    }
}
