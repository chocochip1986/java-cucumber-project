package cdit_automation.step_definition;

import cdit_automation.configuration.TestManager;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {
    @Before
    public void before(Scenario scenario) {
        TestManager.instance();

        int i = 0;
    }

    @After
    public void after(Scenario scenario) {

    }

    private void displayScenarioStartMessage(Scenario scenario) {
        System.out.println("======================================================");
        System.out.println("Feature: " +scenario.getId());
        System.out.println("Starting - " +scenario.getName()+ "at line: "+scenario.getLine());
        System.out.println("Scenario tags: " +scenario.getSourceTagNames());
        System.out.println("Scenario url: " +scenario.getUri());
        System.out.println("======================================================");
    }

    private void displayScenarioEndMessage(Scenario scenario) {
        System.out.println("======================================================");
        System.out.println("Done - " +scenario.getName()+ " => Status: "+scenario.getStatus());
        System.out.println("======================================================");
    }
}
