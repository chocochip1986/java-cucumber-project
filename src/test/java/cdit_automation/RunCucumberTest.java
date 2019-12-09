package cdit_automation;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/resources/",
        glue="cdit_automation.step_definition",
        tags = {"not @wip", "not @defect", "not @example_only"})
public class RunCucumberTest {
}
