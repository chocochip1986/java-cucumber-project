package cdit_automation.step_definition;

import cdit_automation.utilities.Timer;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assume;
import org.junit.Ignore;

@Slf4j
@Ignore
public class Hooks extends AbstractSteps {

    private static boolean failFastFlag = false;

    @Before(order=1)
    public void before() {
        testManager.begin();
    }

    @Before(value = "@truncate", order=2)
    public void beforeScenario(Scenario scenario) {
        truncateAllTables();
        clearOutputArtifactsDirectory();
        displayScenarioStartMessage(scenario);
    }

    @Before(value = "@ResetFailFastFlag", order = 3)
    public void beforeScenarioResetSkipFlag() {
        failFastFlag = false;
    }

    @Before(value = "@FailFast", order = 4)
    public void beforeFailFast() {
        if (failFastFlag) {
            Assume.assumeTrue(false);
        }
    }

    @After(value = "@FailFast", order = 3)
    public void afterFailFast(Scenario scenario) {
        if (scenario.isFailed()) {
            failFastFlag = true;
        }
    }

    @After(order=2)
    public void afterScenario(Scenario scenario) {
        displayScenarioEndMessage(scenario);
    }

    @After(order=1)
    public void after(Scenario scenario) {
        testManager.updateTestStatistics(scenario);
        if ( scenario.isFailed() ) {
            String message = "======================================================";
            message += System.lineSeparator()+"Scenario: "+scenario.getName()+ " => Status: "+scenario.getStatus();
            message += System.lineSeparator()+"======================================================";
            testManager.takeScreenshot(scenario);
            testManager.sendNotificationToSlack(message);
            if ( testManager.failFastEnabled() ) {
                reset();
                testManager.quit();
            }
        }
        reset();
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
        newCitizenValidatedRepo.truncateTable();
        bulkCitizenValidatedRepo.truncateTable();
        bulkMhaAddressValidatedRepo.truncateTable();
        ceasedCitizenValidatedRepo.truncateTable();
        deathDateValidatedRepo.truncateTable();
        bulkNcaAddressValidatedRepo.truncateTable();
        changeAddressValidatedRepo.truncateTable();
        newMhaAddressRepo.truncateTable();
        newNcaAddressRepo.truncateTable();
        oldMhaAddressRepo.truncateTable();
        oldNcaAddressRepo.truncateTable();
        incomeRepo.truncateTable();
        assessableIncomeValidatedRepo.truncateTable();
        singleDateRecordTypeHeaderRepo.truncateTable();
        annualValueRepo.truncateTable();
        dualCitizenValidatedRepo.truncateTable();
        doubleDateHeaderValidatedRepo.truncateTable();
        singleDateHeaderValidatedRepo.truncateTable();
        singleDateNoOfRecordsHeaderValidatedRepo.truncateTable();
        noInteractionValidatedRepo.truncateTable();
        personDetailChangeValidatedRepo.truncateTable();
        nationalityRepo.truncateTable();
        personIdRepo.truncateTable();
        personStatusRepo.truncateTable();
        personPropertyRepo.truncateTable();
        personNameRepo.truncateTable();
        genderRepo.truncateTable();
        propertyDetailRepo.truncateTable();
        specialPropertyRepo.truncateTable();
//        specialMappingRepo.truncateTable();
        propertyRepo.truncateTable();
        personDetailRepo.truncateTable();
        genderRepo.truncateTable();
        personRepo.truncateTable();
        genderRepo.truncateTable();
        errorMessageRepo.truncateTable();
        incomingRecordRepo.truncateTable();
        reasonablenessCheckStatisticViewJpaRepo.deleteAll();
        reasonablenessCheckStatisticRepo.truncateTable();
        batchRepo.truncateTable();
        fileReceivedRepo.truncateTable();
        batchJobExecutionRepo.truncateTable();
        batchJobExecutionParamsRepo.truncateTable();
        System.out.println("Truncated all Datasource tables...");
    }

    private void reset() {
        testManager.closeBrowser();
        testContext.flush();
        batchFileDataWriter.reset();
    }

    private void clearOutputArtifactsDirectory() {
        testManager.clearOutputArtifactsDirectory();
    }
}
