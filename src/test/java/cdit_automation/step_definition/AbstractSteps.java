package cdit_automation.step_definition;

import cdit_automation.configuration.StepDefLevelTestContext;
import cdit_automation.configuration.TestManager;
import cdit_automation.data_helpers.PersonIdService;
import cdit_automation.page_navigation.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractSteps {

    @Autowired
    protected TestManager testManager;

    @Autowired
    protected PageUtils pageUtils;

    @Autowired
    protected StepDefLevelTestContext testContext;

    @Autowired
    protected PersonIdService personIdService;
}
