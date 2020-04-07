package cds_automation.pages;

import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Component
public class GooglePage extends AbstractPage{
    public static final String GOOGLE_URL = "https://www.google.com.sg/";
    public static final String GOOGLE_SEARCH_BAR = "//input[@type='text']";
    public static final String GOOGLE_SEARCH_BUTTON = "//div[@class='FPdoLc VlcLAe']//child::input[@value='Google Search']";
    public static final String GOOGLE_SEARCH_RESULTS_PAGE = "//div[@id='resultStats']";

    public void visitSearchPage() {
        driverManager.getDriver().get(GOOGLE_URL);
        WebElement searchBarElement = pageUtils.findElement(GOOGLE_SEARCH_BAR);

        assertNotNull(searchBarElement);
        assertTrue(searchBarElement.isDisplayed());
    }

    public void enterSearchKeyWords(String words) {
        pageUtils.setText(GOOGLE_SEARCH_BAR, words);
    }

    public void search() {
        pageUtils.click_on(GOOGLE_SEARCH_BUTTON);
    }

    public void verifySearchSucceeded() {
        WebElement webElement = pageUtils.findElement(GOOGLE_SEARCH_RESULTS_PAGE);

        assertNotNull(webElement);
        assertTrue(webElement.isDisplayed());
    }
}
