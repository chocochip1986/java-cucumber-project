package cdit_automation.driver_management;


import cdit_automation.enums.BrowserTypeEnums;
import cdit_automation.exceptions.UnsupportedWebDriverException;
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

@Component
public class DriverManager {

    private BrowserTypeEnums currentWebDriver;
    private WebDriver driver;

    public DriverManager() {
        initialize();
    }

    private void initialize() {
        driver = null;
        currentWebDriver = null;
    }

    public void setCurrentWebDriver(BrowserTypeEnums browser) {
        this.currentWebDriver = browser;
    }

    public void visit(String url) {
        this.driver.get(url);
    }

    public WebDriver open() {
        driver = createDriverFor();
        return driver;
    }

    public void close() {
        driver.quit();
        driver = null;
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
