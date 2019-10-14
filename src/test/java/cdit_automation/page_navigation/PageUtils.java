package cdit_automation.page_navigation;

import cdit_automation.driver_management.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PageUtils {

    @Autowired
    DriverManager driverManager;

    public PageUtils() {
        //DO NOTHING
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
        WebElement webElement = driverManager.getDriver().findElement(By.xpath(xpath));
        return webElement;
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

    public void refresh() {
        driverManager.getDriver().navigate().refresh();
    }

    public void browserBack() {
        driverManager.getDriver().navigate().back();
    }

    public void browserForward() {
        driverManager.getDriver().navigate().forward();
    }
}
