package cdit_automation.page_navigation;

import cdit_automation.driver_management.DriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
@Component
public class PageUtils {

    @Autowired
    DriverManager driverManager;

    private Wait wait;
    private long EXPLICIT_WAIT;

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
        List<WebElement> webElements = driverManager.getDriver().findElements(By.xpath(xpath));
        return webElements;
    }

    public WebElement findWebElementByXpath(String xpath) {
        return yieldToBlock(new BiFunction<String, String, WebElement>() {
            public WebElement apply(String css, String errMsg) {
                WebElement webElement = driverManager.getDriver().findElement(By.xpath(xpath));
                return webElement;
            }
        }, xpath, "[WebNavigation] - Cannot find such element: "+xpath);
    }

    public List<WebElement> findWebElementsByCss(String css) {
        List<WebElement> webElements = driverManager.getDriver().findElements(By.cssSelector(css));
        return webElements;
    }

    public WebElement findWebElementByCss(String css) {
        return yieldToBlock(new BiFunction<String, String, WebElement>() {
            public WebElement apply(String css, String errMsg) {
                WebElement webElement = driverManager.getDriver().findElement(By.cssSelector(css));
                return webElement;
            }
        }, css, "[WebNavigation] - Cannot find such element: "+css);
    }

    public WebElement yieldToBlock(BiFunction<String, String, WebElement> function, String cssOrXpath, String errMsg) {
        try {
            return function.apply(cssOrXpath, errMsg);
        }
        catch (WebDriverException errorMessage) {
            errorMessage(errMsg);
            throw errorMessage;
        }
    }
//
//    public List<WebElement> yieldToBlock(BiFunction<String, String, List<WebElement>> function, String cssOrXpath, String errMsg) {
//        try {
//            return function.apply(cssOrXpath, errMsg);
//        }
//        catch (WebDriverException errorMessage) {
//            errorMessage(errMsg);
//            throw errorMessage;
//        }
//    }

    public void click_on(String cssOrXpath) {
        WebElement webElement = findElement(cssOrXpath);

        if ( webElement != null ) {
            webElement.click();
        }
    }

    public void setText(String cssOrXpath, String keywords) {
        WebElement webElement = findElement(cssOrXpath);

        if ( webElement != null ) {
            webElement.sendKeys(keywords);
        }
    }

    public void uncheck(String cssOrXpath) {
        WebElement webElement = findElement(cssOrXpath);

        if ( webElement != null ) {
            webElement.click();
        }
    }

    public void check(String cssOrXpath) {
        WebElement webElement = findElement(cssOrXpath);

        if ( webElement != null ) {
            webElement.click();
        }
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
}
