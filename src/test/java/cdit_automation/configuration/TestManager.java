package cdit_automation.configuration;

import cdit_automation.driver_management.DriverManager;
import cdit_automation.enums.BrowserTypeEnums;
import cdit_automation.enums.TestEnvEnums;
import cdit_automation.exceptions.UnsupportedBrowserException;
import cdit_automation.exceptions.UnsupportedTestEnvException;
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
        currentBrowserType = getEnvVarBrowserType();
        testEnv = getEnvVarTestEnv();

        setCurrentDriver(currentBrowserType);
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

    public void setCurrentDriver(BrowserTypeEnums browser) {
        driverManager.setCurrentWebDriver(browser);
    }

    private BrowserTypeEnums getEnvVarBrowserType() {
        if ( System.getProperty("browser") == null ) {
            return BrowserTypeEnums.CHROME;
        }
        else {
            try {
                return BrowserTypeEnums.valueOf(System.getProperty("browser").toUpperCase());
            }
            catch (IllegalArgumentException e) {
                throw new UnsupportedBrowserException("Unsupported Browser Exception! "+System.getProperty("browser"));
            }
        }
    }

    public void openBrowser() {
        System.out.println("Starting browser...");
        driverManager.open();
    }

    public void closeBrowser() {
        System.out.println("Closing browser...");
        driverManager.close();
    }

    private TestEnvEnums getEnvVarTestEnv() {
        if ( System.getProperty("env") == null ) {
            return TestEnvEnums.LOCAL;
        }
        else {
            try {
                return TestEnvEnums.valueOf(System.getProperty("env"));
            }
            catch ( IllegalArgumentException e ) {
                throw new UnsupportedTestEnvException("Unsupported Test Environment Exception! "+System.getProperty("env"));
            }
        }
    }
}
