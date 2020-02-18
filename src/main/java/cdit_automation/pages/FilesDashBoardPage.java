package cdit_automation.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Component
public class FilesDashBoardPage extends AbstractPage {
    public final String FILE_TRAIL_BUTTONS = ".traffic-button-cell";
    public final String FILES_DASHBOARD_SUBTITLE = "//div[@class='table-title dark-gray-font' and text()='Files Dashboard']";

    public final String BODY = "//tbody[@role='rowgroup']/child::mat-row[@role='row']";

    public void accessRandomFileTrail() {
        WebElement webElement = pageUtils.findFirstElement(FILE_TRAIL_BUTTONS);
        webElement.click();
    }

    public void verifyFilesDashboardPage() {
        testAssert.assertTrue(pageUtils.hasElement(FILES_DASHBOARD_SUBTITLE), "Files Dashboard page is not displayed!!!");
    }

    public void verifyFilesExistsInTable() {
        boolean existsRows = waitUntilCondition(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                List<WebElement> listOfRows = pageUtils.findAllWebElements(BODY);
                if ( !listOfRows.isEmpty() ) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        testAssert.assertTrue(existsRows, "No files were displayed on the Files Dashboard!!!");
    }
}
