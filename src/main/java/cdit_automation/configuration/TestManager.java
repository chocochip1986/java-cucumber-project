package cdit_automation.configuration;

import cdit_automation.aws.modules.Slack;
import cdit_automation.driver_management.DriverManager;
import cdit_automation.enums.BrowserTypeEnums;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.exceptions.UnsupportedBrowserException;
import cdit_automation.page_navigation.PageUtils;
import io.cucumber.java.Scenario;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestManager {

    private static final Long DEFAULT_WAIT=10L;
    private final String TEST_SUMMARY_TITLE="==============Test Run Summary==============";
    private final String FAILURE_TITLE = "==============Failing Scenarios==============";

    @Autowired
    private DriverManager driverManager;

    @Autowired
    private PageUtils pageUtils;

    @Autowired
    public TestEnv testEnv;

    @Autowired
    public Slack slack;

    private List<Scenario> listOfFailingScenarios;
    private List<Scenario> listOfScenariosRan;
    private BrowserTypeEnums currentBrowserType;
    private Path projectRoot;
    private Path outputArtifactsDir;
    private Path testResultsDir;
    private boolean hasStarted;
    private ZonedDateTime testRunStartTime;
    private ZonedDateTime testRunEndTime;

    @Autowired
    public TestManager() {
        initialize();
    }

    private void initialize() {
        log.info("Initializing test suite...");

        listOfFailingScenarios = new ArrayList<Scenario>();
        listOfScenariosRan = new ArrayList<Scenario>();
        currentBrowserType = getEnvVarBrowserType();
        projectRoot = setProjectRoot();
        outputArtifactsDir = setOutputArtifactsDir();
        testResultsDir = setTestResultsDir();
        testRunStartTime = null;
        testRunEndTime = null;
        hasStarted = false;
    }

    public void begin() {
        if ( !hasStarted ) {
            String message = "CDS Datasource Automation begins running at "+ LocalDateTime.now(ZoneId.systemDefault()).toString();
            constructTestRunSummary(message);
            sendNotificationToSlack(message);
            establishTestSuiteShutDownHook();
            testRunStartTime = getLocalDateTimeNow();
            hasStarted = true;
        }
    }

    public File takeScreenshot() {
        return ((TakesScreenshot)getDriverManager().getDriver()).getScreenshotAs(OutputType.FILE);
    }

    public boolean isBrowserOpened() {
        return getDriverManager().getDriver() != null;
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
        driverManager.setPathToWebDriver();
    }

    @Autowired
    public void setExplicitWait() {
        pageUtils.setExplicitWait(testEnv.getExplicitWait());
    }

    public boolean failFastEnabled() {
        return testEnv.isFailFast();
    }

    public WebDriver openBrowser() {
        WebDriver webDriver = driverManager.open();
        driverManager.setImplicitWait(testEnv.getImplicitWait());
        pageUtils.setupExplicitWait();
        return webDriver;
    }

    public void closeBrowser() {
        driverManager.close();
    }

    public DriverManager getDriverManager () {
        return driverManager;
    }

    public void quit() {
        sendNotificationToSlack(constructTestRunSummary(constructSummaryNotification()));
        tearDown();
    }

    public void sendNotificationToSlack(String message) {
        if ( testEnv.getEnv().equals(TestEnv.Env.QA) ) {
//            slack.sendToSlack(testEnv.getTopicArn(), message, Slack.Level.NEUTRAL);
        }
    }

    public boolean isDevEnvironment() {
        return testEnv.getEnv().equals(TestEnv.Env.LOCAL) || testEnv.getEnv().equals(TestEnv.Env.AUTOMATION);
    }

    private String constructTestRunSummary(String message) {
        message = System.lineSeparator()+TEST_SUMMARY_TITLE+message;
        message += System.lineSeparator()+TEST_SUMMARY_TITLE;
        return message;
    }

    private String constructSummaryNotification() {
        String summary = System.lineSeparator()+"Number of tests ran: "+listOfScenariosRan.size();
        summary += System.lineSeparator()+"Number of tests passed: "+(listOfScenariosRan.size()-listOfFailingScenarios.size());
        summary += System.lineSeparator()+"Number of tests failed: "+listOfFailingScenarios.size();
        summary += System.lineSeparator()+"Test Run Duration: "+getTestRunDuration()+" mins";
        summary += System.lineSeparator()+FAILURE_TITLE;
        for ( Scenario scenario : listOfFailingScenarios ) {
            summary += System.lineSeparator()+scenario.getName();
        }
        return summary;
    }

    private void tearDown() {
        log.info("Exiting test suite...");
        throw new RuntimeException("Exiting test suite...");
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

    public TestEnv getTestEnvironment() {
        return this.testEnv;
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

    public Path getOutputArtifactsDir(){
        return outputArtifactsDir;
    }

    public Path getTestResultsDir() {
        return testResultsDir;
    }

    public boolean clearOutputArtifactsDirectory() {
        File outputDir = new File(this.outputArtifactsDir.toString());
        return clearDirectory(outputDir);
    }

    private boolean clearDirectory(File targetedDirectory) {
        try {
            FileUtils.cleanDirectory(targetedDirectory);
            return true;
        } catch ( IOException e ) {
            throw new TestFailException("Unable to clear directory at: "+targetedDirectory.getAbsolutePath());
        }
    }

    private Path setProjectRoot() {
        return Paths.get(System.getProperty("user.dir"));
    }

    private Path setOutputArtifactsDir() {
        File outputDir = new File(projectRoot+File.separator+"output_artifacts");
        if ( !outputDir.exists() || !outputDir.isDirectory() ) {
            outputDir.mkdir();
            ensureDirectoryIsAssessable(outputDir);
        }
        return outputDir.toPath();
    }

    private Path setTestResultsDir() {
        File outputDir = new File(projectRoot+File.separator+"test_results");
        if ( !outputDir.exists() || !outputDir.isDirectory() ) {
            outputDir.mkdir();
            ensureDirectoryIsAssessable(outputDir);
        }
        return outputDir.toPath();
    }

    private void ensureDirectoryIsAssessable(File directory) {
        if (!directory.setExecutable(true, false)) {
            throw new TestFailException("Directory is non-accessible!");
        }
        if (!directory.setWritable(true, false)) {
            throw new TestFailException("Directory is non-writable!");
        }
        if (!directory.setReadable(true, false)) {
            throw new TestFailException("Directory is non-readable!");
        }
    }

    private void establishTestSuiteShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                testRunEndTime = getLocalDateTimeNow();
                log.info(constructTestRunSummary(constructSummaryNotification()));
                sendNotificationToSlack(constructTestRunSummary(constructSummaryNotification()));
            }
        });
    }

    private long getTestRunDuration() {
        if ( testRunStartTime != null && testRunEndTime != null ) {
            return ChronoUnit.MINUTES.between(testRunStartTime, testRunEndTime);
        } else {
            return 0;
        }
    }

    private ZonedDateTime getLocalDateTimeNow() {
        return ZonedDateTime.now(ZoneId.of("Asia/Singapore"));
    }
}
