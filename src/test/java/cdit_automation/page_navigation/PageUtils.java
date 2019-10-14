package cdit_automation.page_navigation;

import cdit_automation.driver_management.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageUtils {

    @Autowired
    DriverManager driverManager;

    public PageUtils() {
        //DO NOTHING
    }

    public WebElement findElement(String cssOrXpath) {
        if ( cssOrXpath.startsWith("//") ) {
            return findByXpath(cssOrXpath);
        }
        else {
            return findByCss(cssOrXpath);
        }
    }

    public WebElement findByXpath(String xpath) {
        WebElement webElement = driverManager.getDriver().findElement(By.xpath(xpath));
        return webElement;
    }

    public WebElement findByCss(String css) {
        WebElement webElement = driverManager.getDriver().findElement(By.cssSelector(css));

        return webElement;
    }
}
