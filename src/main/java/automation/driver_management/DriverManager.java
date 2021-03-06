package automation.driver_management;


import automation.enums.datasource.BrowserTypeEnums;
import automation.exceptions.UnsupportedWebDriverException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

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

    public void setPathToWebDriver() {
        switch(this.currentWebDriver) {
            case CHROME:
                System.setProperty("webdriver.chrome.driver", "webdrivers/chrome/chromedriver");
                break;
            default:
                throw new UnsupportedWebDriverException("No such driver supported! "+this.currentWebDriver.name());
        }
    }

    private void removeWindowHandleFromList(String windowHandle) {
        listOfCurrentWindowHandles.remove(windowHandle);
    }

    private WebDriver createDriverFor() {
        switch(currentWebDriver) {
            case CHROME:
                ChromeOptions chromeOptions = new ChromeOptions();
                return new ChromeDriver(chromeOptions);
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
