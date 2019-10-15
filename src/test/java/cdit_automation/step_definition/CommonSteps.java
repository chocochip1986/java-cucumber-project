package cdit_automation.step_definition;

import io.cucumber.java.en.And;

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
