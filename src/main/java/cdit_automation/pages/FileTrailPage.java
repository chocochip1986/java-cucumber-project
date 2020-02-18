package cdit_automation.pages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Slf4j
@Component
public class FileTrailPage extends AbstractPage {
    public final String BACK_BTN = "//button[@class='secondary-button']/child::span[@class='secondary-button-text' and text()='BACK']";
    public final String FILE_TRAIL_PAGE = "//header[@class='header' and text()='File Trail']";

    public final String FILE_TRAIL_INCOMING_INFO_SUBHEADER = "//div[@class='header-title' and text()='Incoming Information']";
    public final String FILE_TRAIL_FORMAT_VALIDATION_INFO_SUBHEADER = "//div[@class='header-title' and text()='Format Validation Information']";
    public final String FILE_TRAIL_CONTENT_VALIDATION_INFO_SUBHEADER = "//div[@class='header-title' and text()='Content Validation Information']";
    public final String FILE_TRAIL_REASONABLENESS_SUBHEADER = "//div[@class='header-title' and text()='Reasonableness Trending Information']";
    public final String FILE_TRAIL_LOAD_VALIDATION_INFO_SUBHEADER = "//div[@class='header-title' and text()='Load Validation Information']";

    public void clickBack() {
        pageUtils.click_on(BACK_BTN);
    }

    public void verifyFileTrailPage() {
        testAssert.assertTrue(pageUtils.hasElement(FILE_TRAIL_PAGE), "File Trail page is not displayed!");
        String[] arrayOfSubheaders = new String[]{FILE_TRAIL_INCOMING_INFO_SUBHEADER,
                FILE_TRAIL_FORMAT_VALIDATION_INFO_SUBHEADER,
                FILE_TRAIL_CONTENT_VALIDATION_INFO_SUBHEADER,
                FILE_TRAIL_REASONABLENESS_SUBHEADER,
                FILE_TRAIL_LOAD_VALIDATION_INFO_SUBHEADER};

        for ( String subheader : arrayOfSubheaders ) {
            boolean isFound = waitUntilCondition(new Supplier<Boolean>() {
                @Override
                public Boolean get() {
                    return pageUtils.hasElement(subheader);
                }
            });
            if (!isFound) {
                testAssert.assertTrue(isFound, "Unable to find header with css or xpath: "+subheader);
            }
        }
    }
}
