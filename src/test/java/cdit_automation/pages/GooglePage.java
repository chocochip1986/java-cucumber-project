package cdit_automation.pages;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
public class GooglePage extends AbstractPage{
    public static final String GOOGLE_URL = "https://www.google.com.sg/";
    public static final String GOOGLE_SEARCH_BAR = "//input[@class='FFgLFyf gsfi']";

    public void visitSearchPage() {
        driverManager.getDriver().get(GOOGLE_URL);
        WebElement searchBarElement = pageUtils.findElement(GOOGLE_SEARCH_BAR);

        assertNotNull(searchBarElement);
        assertTrue(searchBarElement.isDisplayed());
    }

    public void enterSearchKeyWords(String words) {
        WebElement searchBarElement = pageUtils.findElement(GOOGLE_SEARCH_BAR);
    }
}
