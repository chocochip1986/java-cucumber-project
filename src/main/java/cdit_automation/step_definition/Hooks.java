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
        System.out.println("Scenario - " +scenario.getName()+ " at line: "+scenario.getLine());
        System.out.println("Scenario tags: " +scenario.getSourceTagNames());
        System.out.println("Scenario url: " +scenario.getUri());
        System.out.println("======================================================");
    }

    private void displayScenarioEndMessage(Scenario scenario) {
        System.out.println("======================================================");
        System.out.println("Done - " +scenario.getName()+ " => Status: "+scenario.getStatus());
        System.out.println("======================================================");
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
        personPropertyRepo.deleteAllInBatch();
        personNameRepo.deleteAllInBatch();
        personDetailRepo.deleteAllInBatch();
        propertyRepo.deleteAllInBatch();
        personRepo.deleteAllInBatch();
        errorMessageRepo.deleteAllInBatch();
        incomingRecordRepo.deleteAllInBatch();
        batchRepo.deleteAllInBatch();
        fileReceivedRepo.deleteAllInBatch();
        System.out.println("Truncated all Datasource tables...");
    }
}
