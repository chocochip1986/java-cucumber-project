package cdit_automation.step_definition;

import cdit_automation.TestApplication;
import cdit_automation.configuration.TestManager;
import cdit_automation.utilities.Timer;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration( classes = {TestApplication.class} )
@SpringBootTest
public class AbstractSteps {

}
