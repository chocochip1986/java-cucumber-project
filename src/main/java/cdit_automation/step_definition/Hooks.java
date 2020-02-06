package cdit_automation.step_definition;

import cdit_automation.utilities.Timer;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class Hooks extends AbstractSteps {

    @Before(order=1)
    public void before() {
        testManager.begin();
    }

    @Before(order=2)
    public void beforeScenario(Scenario scenario) {
        truncateAllTables();
        displayScenarioStartMessage(scenario);
    }

    @After(order=2)
    public void afterScenario(Scenario scenario) {
        displayScenarioEndMessage(scenario);
        testManager.closeBrowser();
        testContext.flush();
        batchFileDataWriter.reset();
    }

    @After(order=1)
    public void after(Scenario scenario) {
        testManager.updateTestStatistics(scenario);
        if ( scenario.isFailed() ) {
            String message = "======================================================";
            message += System.lineSeparator()+"Scenario: "+scenario.getName()+ " => Status: "+scenario.getStatus();
            message += System.lineSeparator()+"======================================================";
            testManager.sendNotificationToSlack(message);
            if ( testManager.failFastEnabled() ) {
                testManager.quit();
            }
        }
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
        String sceanrioStartMessage = "======================================================";
        sceanrioStartMessage += System.lineSeparator()+"Feature: " +scenario.getId();
        sceanrioStartMessage += System.lineSeparator()+"Scenario - " +scenario.getName()+ " at line: "+scenario.getLine();
        sceanrioStartMessage += System.lineSeparator()+"Scenario tags: " +scenario.getSourceTagNames();
        sceanrioStartMessage += System.lineSeparator()+"Scenario url: " +scenario.getUri();
        sceanrioStartMessage += System.lineSeparator()+"======================================================";
        log.info(sceanrioStartMessage);
    }

    private void displayScenarioEndMessage(Scenario scenario) {
        String scenarioEndMessage = "======================================================";
        scenarioEndMessage += System.lineSeparator()+"Scenario - " +scenario.getName()+ " => Status: "+scenario.getStatus();
        scenarioEndMessage += System.lineSeparator()+"======================================================";
        log.info(scenarioEndMessage);
    }

    private void truncateAllTables() {
        System.out.println("Truncating all Datasource tables...");
        bulkCitizenValidatedRepo.deleteAllInBatch();
        bulkMhaAddressValidatedRepo.deleteAllInBatch();
        bulkNcaAddressValidatedRepo.deleteAllInBatch();
        changeAddressValidatedRepo.deleteAllInBatch();
        newCitizenValidatedRepo.deleteAllInBatch();
        newMhaAddressRepo.deleteAllInBatch();
        newNcaAddressRepo.deleteAllInBatch();
        oldMhaAddressRepo.deleteAllInBatch();
        oldNcaAddressRepo.deleteAllInBatch();
        incomeRepo.deleteAllInBatch();
        annualValueRepo.deleteAllInBatch();
        dualCitizenValidatedRepo.deleteAllInBatch();
        doubleDateHeaderValidatedRepo.deleteAllInBatch();
        deathDateValidatedRepo.deleteAllInBatch();
        ceasedCitizenValidatedRepo.deleteAllInBatch();
        noInteractionValidatedRepo.deleteAllInBatch();
        personDetailChangeValidatedRepo.deleteAllInBatch();
        nationalityRepo.deleteAllInBatch();
        personIdRepo.deleteAllInBatch();
        personStatusRepo.deleteAllInBatch();
        personPropertyRepo.deleteAllInBatch();
        personNameRepo.deleteAllInBatch();
        propertyRepo.deleteAllInBatch();
        propertyDetailRepo.deleteAllInBatch();
        personDetailRepo.deleteAllInBatch();
        personRepo.deleteAllInBatch();
        errorMessageRepo.deleteAllInBatch();
        incomingRecordRepo.deleteAllInBatch();
        batchRepo.deleteAllInBatch();
        fileReceivedRepo.deleteAllInBatch();
        System.out.println("Truncated all Datasource tables...");
    }
}
