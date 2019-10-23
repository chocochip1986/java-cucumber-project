package cdit_automation.page_navigation;

import cdit_automation.driver_management.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Duration;
import java.util.List;

@Component
public class PageUtils {

    @Autowired
    DriverManager driverManager;

    private Wait wait;

    public PageUtils() {
        //DO NOTHING
        wait = new FluentWait(driverManager.getDriver())
                .withTimeout(Duration.ofSeconds(driverManager.getExplicitWait()))
                .pollingEvery(Duration.ofSeconds(1L))
                .ignoring(Exception.class);
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
        try {
            WebElement webElement = driverManager.getDriver().findElement(By.xpath(xpath));
            return webElement;
        }
        catch (NoSuchElementException e) {
            errorMessage(null);
            throw e;
        }
    }

    public List<WebElement> findWebElementsByCss(String css) {
        List<WebElement> webElements = driverManager.getDriver().findElements(By.cssSelector(css));
        return webElements;
    }

    public WebElement findWebElementByCss(String css) {
        WebElement webElement = driverManager.getDriver().findElement(By.cssSelector(css));

        return webElement;
    }

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
        //TODO log error message
        takeScreenshot();
    }

    private void takeScreenshot() {
        File srcFile = ((TakesScreenshot)driverManager.getDriver()).getScreenshotAs(OutputType.FILE);


    }
}
