package cdit_automation.pages.datasource;

import cdit_automation.pages.AbstractPage;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Slf4j
@Component
public class TrendingRecordsPage extends AbstractPage {
    private final String PAGE_TITLE = "//div[contains(@class, 'table-title') and text()='Details of Trending Records']";

    public void verifyLoaded() {
        boolean isLoaded = waitUntilCondition(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return pageUtils.hasElement(PAGE_TITLE);
            }
        });
        testAssert.assertTrue(isLoaded, "Trending Records Page is not displayed!");
    }
}
