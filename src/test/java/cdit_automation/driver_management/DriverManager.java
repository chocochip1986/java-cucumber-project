package cdit_automation.driver_management;


import cdit_automation.enums.BrowserTypeEnums;
import cdit_automation.exceptions.UnsupportedWebDriverException;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DriverManager {

    private BrowserTypeEnums currentWebDriver;
    private WebDriver driver;
    private List<String> listOfCurrentWindowHandles;
    private String currentWindowHandle;

    public DriverManager() {
        initialize();
    }

    private void initialize() {
        driver = null;
        currentWebDriver = null;
        currentWindowHandle = null;
        listOfCurrentWindowHandles = new ArrayList<>();
    }

    public void setCurrentWebDriver(BrowserTypeEnums browser) {
        this.currentWebDriver = browser;
    }

    public void visit(String url) {
        this.driver.get(url);
    }

    public void setImplicitWait(Long seconds) {
        this.driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void newTab() {
    }

    public void closeTab() {
        this.driver.close();
        removeWindowHandleFromList(currentWindowHandle);
        currentWindowHandle = this.driver.getWindowHandle();

    }

    public WebDriver open() {
        log.info("Opening browser...");
        driver = createDriverFor();
        currentWindowHandle = this.driver.getWindowHandle();
        listOfCurrentWindowHandles.add(currentWindowHandle);
        return driver;
    }

    public void close() {
        if ( driver != null ) {
            log.info("Closing browser...");
            driver.quit();
            currentWindowHandle = null;
            driver = null;
        }
    }

    private void removeWindowHandleFromList(String windowHandle) {
        listOfCurrentWindowHandles.remove(windowHandle);
    }

    private WebDriver createDriverFor() {
        switch(currentWebDriver) {
            case CHROME:
                return new ChromeDriver();
//            case FIREFOX:
//                return new FirefoxDriver();
//            case SAFARI:
//                return new SafariDriver();
//            case IE:
//                return new InternetExplorerDriver();
//            case EDGE:
//                return new EdgeDriver();
//            case OPERA:
//                return new OperaDriver();
            default:
                return new ChromeDriver();
        }
    }
}
