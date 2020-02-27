package cdit_automation.page_navigation;

import cdit_automation.driver_management.DriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
@Component
public class PageUtils {

    @Autowired
    DriverManager driverManager;

    private Wait wait;
    private long EXPLICIT_WAIT;
    private final String LOG_WEB_NAVIGATION_PREFIX = "[WebNavigation]";

    public PageUtils() {
        //DO NOTHING
        EXPLICIT_WAIT = 0L;
    }

    public void setExplicitWait(long wait) {
        EXPLICIT_WAIT = wait;
    }

    public void setupExplicitWait() {
        wait = new FluentWait(driverManager.getDriver())
                .withTimeout(Duration.ofSeconds(EXPLICIT_WAIT))
                .pollingEvery(Duration.ofSeconds(1L))
                .ignoring(Exception.class);
    }

    public boolean hasNoElement(String cssOrXpath) {
        WebElement webElement;
        try {
            webElement = findElement(cssOrXpath);
        } catch (WebDriverException e) {
            return true;
        }

        if ( webElement == null ) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasElement(String cssOrXpath) {
        WebElement webElement;
        try {
            webElement = findElement(cssOrXpath);
        } catch (WebDriverException e) {
            return false;
        }

        if ( webElement == null ) {
            return false;
        } else {
            return true;
        }
    }

    public void waitForElementTobeStale(String cssOrXpath) {
        WebElement webElement = findElement(cssOrXpath);
        waitForElementTobeStale(webElement);
    }

    public void waitForElementTobeStale(WebElement webElement) {
        wait.until(ExpectedConditions.stalenessOf(webElement));
    }

    public WebElement findElementWithWait(String cssOrXpath) {
        WebElement resultWebElement = (WebElement)wait.until(new Function<WebDriver, WebElement>(){
            public WebElement apply(WebDriver webDriver) {
                return findElement(cssOrXpath);
            }
        });

        return resultWebElement;
    }

    public WebElement findElement(String cssOrXpath) {
        if ( cssOrXpath.startsWith("//") ) {
            return findWebElementByXpath(cssOrXpath);
        }
        else {
            return findWebElementByCss(cssOrXpath);
        }
    }

    public WebElement findFirstElement(String cssOrXpath) {
        List<WebElement> listOfWebElements = null;
        if ( cssOrXpath.startsWith("//") ) {
            listOfWebElements = findWebElementsByXpath(cssOrXpath);
        } else {
            listOfWebElements = findWebElementsByCss(cssOrXpath);
        }
        return listOfWebElements.get(0);
    }

    public List<WebElement> findAllWebElements(String cssOrXpath) {
        List<WebElement> listOfWebElements = null;
        if ( cssOrXpath.startsWith("//") ) {
            listOfWebElements = findWebElementsByXpath(cssOrXpath);
        } else {
            listOfWebElements = findWebElementsByCss(cssOrXpath);
        }

        return listOfWebElements;
    }

    public List<WebElement> findWebElementsByXpath(String xpath) {
        return yieldToBlock(new YieldInterface<String, String, WebElement>(){
            public List<WebElement> apply(String css, String errMsg) {
                List<WebElement> webElements = driverManager.getDriver().findElements(By.xpath(xpath));
                return webElements;
            }
        }, xpath, LOG_WEB_NAVIGATION_PREFIX+" - Cannot find such element: "+xpath);
    }

    public WebElement findWebElementByXpath(String xpath) {
        return yieldToBlock(new BiFunction<String, String, WebElement>() {
            public WebElement apply(String css, String errMsg) {
                WebElement webElement = driverManager.getDriver().findElement(By.xpath(xpath));
                return webElement;
            }
        }, xpath, LOG_WEB_NAVIGATION_PREFIX+" - Cannot find such element: "+xpath);
    }

    public List<WebElement> findWebElementsByCss(String css) {
        return yieldToBlock(new YieldInterface<String, String, WebElement>(){
            public List<WebElement> apply(String css, String errMsg) {
                List<WebElement> webElements = driverManager.getDriver().findElements(By.cssSelector(css));
                return webElements;
            }
        }, css, LOG_WEB_NAVIGATION_PREFIX+" - Cannot find such element: "+css);
    }

    public WebElement findWebElementByCss(String css) {
        return yieldToBlock(new BiFunction<String, String, WebElement>() {
            public WebElement apply(String css, String errMsg) {
                WebElement webElement = driverManager.getDriver().findElement(By.cssSelector(css));
                return webElement;
            }
        }, css, LOG_WEB_NAVIGATION_PREFIX+" - Cannot find such element: "+css);
    }

    public void asyncClickOn(String cssOrXpathButton, WebElement expected) {
        click_on(cssOrXpathButton);
        waitForElementTobeStale(expected);
    }

    public void asyncClickOn(String cssOrXpathButton, String cssOrXpathExpected) {
        WebElement webElementOfExpected = findElement(cssOrXpathExpected);
        click_on(cssOrXpathButton);
        waitForElementTobeStale(cssOrXpathExpected);
    }

    public void click_on(String cssOrXpath) {
        WebElement webElement = findElement(cssOrXpath);

        yieldToBlock(new ConsumerInterface<WebElement, String>() {
            public void apply(WebElement webElement, String s) {
                webElement.click();
            }
        }, webElement, LOG_WEB_NAVIGATION_PREFIX+" - Unable to click on element: "+cssOrXpath);
    }

    public void setText(String cssOrXpath, String keywords) {
        WebElement webElement = findElement(cssOrXpath);

        yieldToBlock(new ConsumerInterface<WebElement, String>() {
            public void apply(WebElement webElement, String s) {
                webElement.sendKeys(keywords);
            }
        }, webElement, LOG_WEB_NAVIGATION_PREFIX+" - Unable to set text in element: "+cssOrXpath);
    }

    public void uncheck(String cssOrXpath) {
        WebElement webElement = findElement(cssOrXpath);

        yieldToBlock(new ConsumerInterface<WebElement, String>() {
            public void apply(WebElement webElement, String s) {
                webElement.click();
            }
        }, webElement, LOG_WEB_NAVIGATION_PREFIX+" - Unable to uncheck element: "+cssOrXpath);
    }

    public void check(String cssOrXpath) {
        WebElement webElement = findElement(cssOrXpath);

        yieldToBlock(new ConsumerInterface<WebElement, String>() {
            public void apply(WebElement webElement, String s) {
                webElement.click();
            }
        }, webElement, LOG_WEB_NAVIGATION_PREFIX+" - Unable to check element: "+cssOrXpath);
    }

    public boolean isChecked(String cssOrXpath) {
        WebElement webElement = findElement(cssOrXpath);

        if ( webElement != null ) {
            return webElement.isSelected();
        }
        else {
            return false;
        }
    }

    public void openNewTab() {
        driverManager.newTab();
    }

    public void closeTab() {
        driverManager.closeTab();
    }

    public void refresh() {
        driverManager.getDriver().navigate().refresh();
    }

    public void browserBack() {
        driverManager.getDriver().navigate().back();
    }

    public void browserForward() {
        driverManager.getDriver().navigate().forward();
    }

    private void errorMessage(String error_message) {
        log.error(error_message);
        takeScreenshot();
    }

    private void takeScreenshot() {
        File srcFile = ((TakesScreenshot)driverManager.getDriver()).getScreenshotAs(OutputType.FILE);
    }

    private WebElement yieldToBlock(BiFunction<String, String, WebElement> function, String cssOrXpath, String errMsg) {
        try {
            return function.apply(cssOrXpath, errMsg);
        }
        catch (WebDriverException errorMessage) {
            errorMessage(errMsg);
            throw errorMessage;
        }
    }

    private List<WebElement> yieldToBlock(YieldInterface<String, String, WebElement> function, String cssOrXpath, String errMsg) {
        try {
            return function.apply(cssOrXpath, errMsg);
        }
        catch (WebDriverException errorMessage) {
            errorMessage(errMsg);
            throw errorMessage;
        }
    }

    private void yieldToBlock(ConsumerInterface<WebElement, String> function, WebElement webElement, String errMsg) {
        try {
            function.apply(webElement, errMsg);
        }
        catch (WebDriverException errorMessage) {
            errorMessage(errMsg);
            throw errorMessage;
        }
    }
}
