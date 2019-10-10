package cdit_automation.configuration;

import cdit_automation.driver_management.DriverManager;
import cdit_automation.enums.BrowserTypeEnums;
import cdit_automation.enums.TestEnvEnums;
import io.cucumber.core.api.Scenario;

import java.util.ArrayList;
import java.util.List;

public class TestManager {
    private static TestManager SINGLE_INSTANCE = null;

    private DriverManager driverManager;
    private List<Scenario> listOfFailingScenarios;
    private List<Scenario> listOfScenariosRan;
    private BrowserTypeEnums currentBrowserType;
    private TestEnvEnums testEnv;

    private TestManager() {
        initialize();
    }

    public static TestManager instance() {
        if ( SINGLE_INSTANCE == null ) {
            SINGLE_INSTANCE = new TestManager();
        }
        return SINGLE_INSTANCE;
    }

    private void initialize() {
        System.out.println("Initializing test suite...");

        driverManager = DriverManager.instance();
        listOfFailingScenarios = new ArrayList<Scenario>();
        listOfScenariosRan = new ArrayList<Scenario>();
        currentBrowserType = selectBrowserType();
        testEnv = selectTestEnvironment();
    }

    public void addToFailingListOfScenarios(Scenario scenario) {
        this.listOfFailingScenarios.add(scenario);
    }

    public void addToListOfScenariosRan(Scenario scenario) {
        this.listOfScenariosRan.add(scenario);
    }

    public List<Scenario> getListOfFailingScenarios() {
        return listOfFailingScenarios;
    }

    public List<Scenario> getListOfScenariosRan() {
        return listOfScenariosRan;
    }

    public BrowserTypeEnums getCurrentBrowserType() {
        return currentBrowserType;
    }

    public TestEnvEnums getTestEnv() {
        return testEnv;
    }

    public void updateTestStatistics(Scenario scenario) {
        switch( scenario.getStatus() ) {
            case PASSED:
                addToListOfScenariosRan(scenario);
                break;
            case FAILED:
                addToFailingListOfScenarios(scenario);
                addToListOfScenariosRan(scenario);
            default:
                //DO NOTHING
        }
    }

    private TestEnvEnums selectTestEnvironment() {
        if ( System.getProperty("env") == null ) {
            return TestEnvEnums.LOCAL;
        }
        else {
            return TestEnvEnums.valueOf(System.getProperty("env"));
        }
    }

    private BrowserTypeEnums selectBrowserType() {
        if ( System.getProperty("browser") == null ) {
            return BrowserTypeEnums.CHROME;
        }
        else {
            return BrowserTypeEnums.valueOf(System.getProperty("browser"));
        }
    }
}
