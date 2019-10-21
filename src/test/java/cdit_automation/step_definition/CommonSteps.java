package cdit_automation.step_definition;

import io.cucumber.java.en.And;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class CommonSteps extends AbstractSteps {

    @And("I open a new tab")
    public void iOpenANewTab() {
        pageUtils.openNewTab();
    }

    @And("I close current tab")
    public void iCloseCurrentTab() {
        pageUtils.closeTab();
    }
}
