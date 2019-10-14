package cdit_automation.step_definition;

import cdit_automation.configuration.TestManager;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractSteps {

    @Autowired
    protected TestManager testManager;
}
