package cdit_automation;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

import java.util.stream.Stream;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:test_results/cucumber-reports"},
        features = "classpath:cdit_automation",
        glue={"cdit_automation.step_definition.datasource"},
        tags = {"not @wip", "not @defect", "not @example_only"})
public class RunCucumberTest {
    private static String[] defaultOptions = {
            "--glue", "cdit_automation.step_definition",
            "--plugin", "pretty", "classpath:cdit_automation",
            "--tags", "not @wip",
            "--tags", "not @defect",
            "--tags", "not @example_only"
    };

    public static void main(String[] args) {
        System.out.println("Initializing test suite...");
        String[] cucumberOptions = Stream.concat(Stream.of(defaultOptions), Stream.of(args)).toArray(String[]::new);

        displayCucumberOptions(cucumberOptions);

        io.cucumber.core.cli.Main.main(cucumberOptions);
    }

    private static void displayCucumberOptions(String[] cucumberOptions) {
        System.out.println("cucumber options => "+ String.join(", ", cucumberOptions) );
    }
}
