package cdit_automation.configuration;

import cdit_automation.driver_management.DriverManager;
import cdit_automation.enums.BrowserTypeEnums;
import cdit_automation.enums.TestEnvEnums;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.exceptions.UnsupportedBrowserException;
import cdit_automation.exceptions.UnsupportedTestEnvException;
import cdit_automation.page_navigation.PageUtils;
import io.cucumber.core.api.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TestManager {

    private static final Long DEFAULT_WAIT=10L;

    @Autowired
    private DriverManager driverManager;

    @Autowired
    private PageUtils pageUtils;

    @Autowired
    public TestEnv testEnv;

    private List<Scenario> listOfFailingScenarios;
    private List<Scenario> listOfScenariosRan;
    private BrowserTypeEnums currentBrowserType;
    private Path projectRoot;

    @Autowired
    public TestManager() {
        initialize();
    }

    private void initialize() {
        System.out.println("Initializing test suite...");

        listOfFailingScenarios = new ArrayList<Scenario>();
        listOfScenariosRan = new ArrayList<Scenario>();
        currentBrowserType = getEnvVarBrowserType();
        projectRoot = setProjectRoot();
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

    public Path getProjectRoot() {
        return projectRoot;
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

    @Autowired
    public void setWebDriverConfiguration() {
        driverManager.setCurrentWebDriver(currentBrowserType);
    }

    @Autowired
    public void setExplicitWait() {
        pageUtils.setExplicitWait(testEnv.getExplicitWait());
    }

    public void openBrowser() {
        driverManager.open();
        driverManager.setImplicitWait(testEnv.getImplicitWait());
        pageUtils.setupExplicitWait();
    }

    public void closeBrowser() {
        driverManager.close();
    }

    public DriverManager getDriverManager () {
        return driverManager;
    }

    public void sleep() {
        try {
            Thread.sleep(1000);
        } catch ( InterruptedException e ) {
            String errorMsg = "Somehow, system sleep operation is interrupted!"+System.lineSeparator()+e.getMessage();
            log.error(errorMsg);
            throw new TestFailException(errorMsg);
        }
    }

    private TestEnvEnums getEnvVarTestEnv() {
        if ( System.getProperty("env") == null ) {
            return TestEnvEnums.LOCAL;
        }
        else {
            try {
                return TestEnvEnums.valueOf(System.getProperty("env").toUpperCase());
            }
            catch ( IllegalArgumentException e ) {
                throw new UnsupportedTestEnvException("Unsupported Test Environment Exception! "+System.getProperty("env"));
            }
        }
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

    private Path setProjectRoot() {
        return Paths.get(System.getProperty("user.dir"));
    }
}
