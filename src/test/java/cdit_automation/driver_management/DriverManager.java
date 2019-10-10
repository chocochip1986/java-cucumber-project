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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverManager {
    private static DriverManager SINGLE_INSTANCE = null;

    private Map<BrowserTypeEnums, WebDriver> drivers;
    private WebDriver driver;

    private DriverManager() {
        initialize();
    }

    public static DriverManager instance() {
        if ( SINGLE_INSTANCE == null ) {
            SINGLE_INSTANCE = new DriverManager();
        }
        return SINGLE_INSTANCE;
    }

    private void initialize() {
        drivers = new HashMap<>();
        registerDrivers();
    }

    public void setCurrentDriver(BrowserTypeEnums browser) {
        driver = getDriverFromRegisteredDrivers(browser);
    }

    public WebDriver getDriverFromRegisteredDrivers(BrowserTypeEnums browser) {
        return drivers.get(browser);
    }

    private void registerDrivers() {
        for ( BrowserTypeEnums browser : BrowserTypeEnums.values() ) {
            drivers.put(browser, createDriverFor(browser));
        }
    }

    private WebDriver createDriverFor(BrowserTypeEnums browserName) {
        switch(browserName) {
            case CHROME:
                return new ChromeDriver();
            case FIREFOX:
                return new FirefoxDriver();
            case SAFARI:
                return new SafariDriver();
            case IE:
                return new InternetExplorerDriver();
            case EDGE:
                return new EdgeDriver();
            case OPERA:
                return new OperaDriver();
            default:
                throw new UnsupportedWebDriverException("Unsupported webdriver: "+browserName.toString());
        }
    }
}
