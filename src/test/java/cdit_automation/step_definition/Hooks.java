package cdit_automation.step_definition;

import cdit_automation.configuration.TestManager;
import cdit_automation.utilities.Timer;
import gherkin.ast.Step;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;

public class Hooks {
    @Before(order=0)
    public void before() {
        TestManager.instance();
    }

    @Before(order=1)
    public void beforeScenario(Scenario scenario) {
        displayScenarioStartMessage(scenario);
    }

    @After(order=1)
    public void afterScenario(Scenario scenario) {
        displayScenarioEndMessage(scenario);
    }

    @After(order=0)
    public void after(Scenario scenario) {
        TestManager.instance().updateTestStatistics(scenario);
    }

    @BeforeStep(order=0)
    public void startStepTimer() {
        Timer.startTimer();
    }

    @AfterStep(order=0)
    public void stopStepTimer() {
        System.out.println("Step duration: "+ Timer.stopTimer().toString());
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
