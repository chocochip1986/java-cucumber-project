package cdit_automation.pages;

import cdit_automation.enums.BatchStatusEnum;
import cdit_automation.enums.views.FileStatusSubTextEnum;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Component
public class FilesDashBoardPage extends AbstractPage {
    public final String FILE_TRAIL_BUTTONS = ".traffic-button-cell";
    public final String FILES_DASHBOARD_SUBTITLE = "//div[@class='table-title dark-gray-font' and text()='Files Dashboard']";
    public final String FILES_DASHBOARD_TABLE = ".mat-table tbody mat-row";

    public final String BODY = "//tbody[@role='rowgroup']/child::mat-row[@role='row']";

    public final String COLUMN_CURRENTSTATUS_SUBTEXT = "//mat-row[@role='row']/child::mat-cell[contains(@class, 'cdk-column-currentStatus')]//*[@class='traffic-button-cell-subText']";

    public final String PAGINATION_SELECTED_PAGE = ".mat-paginator-page-size mat-form-field span.mat-select-value-text span";
    public final String PAGE_MAX_SIZE = "//div[@class='mat-paginator-range-actions']/child::div[@class='mat-paginator-range-label']";

    public final String ITEM_PER_PAGE_SELECT = "//div[contains(@class, 'mat-form-field-infix')]/child::mat-select";
    public final String ITEMS_PER_PAGE_OPTIONS = "//div[contains(@class, 'mat-select-panel')]/child::mat-option";
    public final String ITEMS_PER_PAGE_OPTIONS_TEXT = "//span[contains(@class, 'mat-option-text')]";

    public final String PAGE_NEXT_BTN = ".mat-paginator-navigation-next";
    public final String PAGE_BACK_BTN = ".mat-paginator-navigation-previous";

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

    public void verifyCorrectNumberOfRowsInTable(int expectedNumOfRows) {
        boolean correctRows = waitUntilCondition(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                List<WebElement> listOfRows = pageUtils.findAllWebElements(BODY);

                WebElement selectedNumOfPage = pageUtils.findFirstElement(PAGINATION_SELECTED_PAGE);
                int pageSize = Integer.valueOf(selectedNumOfPage.getText());

                if ( (expectedNumOfRows <= pageSize && listOfRows.size() <= pageSize)
                        && expectedNumOfRows == listOfRows.size() ) {
                    return true;
                } else {
                    log.error("Assertion Failed: \nExpected: {} \nActual: {}", expectedNumOfRows, pageSize);
                    return false;
                }
            }
        });

        testAssert.assertTrue(correctRows, "Incorrect number of rows displayed on Files Dashboard!!");
    }

    public void verifyCorrectCurrentStatusGenerated(){
        boolean correctCurrentStatus = waitUntilCondition(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                Map<FileStatusSubTextEnum, BatchStatusEnum> currentStatusToBatchStatusMap =
                        testContext.get("currentStatusToBatchStatusMap");

                List<WebElement> currentStatusSubText = pageUtils.findAllWebElements(COLUMN_CURRENTSTATUS_SUBTEXT);

                return currentStatusSubText.stream()
                        .map(WebElement::getText)
                        .collect(Collectors.toList())
                        .containsAll(
                                currentStatusToBatchStatusMap.keySet().stream()
                                .map(FileStatusSubTextEnum::getValue)
                                .collect(Collectors.toList())
                        );
            }
        });

        testAssert.assertTrue(correctCurrentStatus, "Incorrect Current Status displayed on Files Dashboard!");
    }

    public void selectItemsPerPage(int itemsPerPageOption) {
        boolean selectionExists = waitUntilCondition(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                // Get & Click Dropdown Element
                WebElement optionSelect = pageUtils.findElement(ITEM_PER_PAGE_SELECT);
                if (optionSelect == null) {
                    log.error("Assertion Failed: Failed to find Items Per Page dropdown");
                    return false;
                }
                optionSelect.click();

                // Get Dropdown Element options
                List<WebElement> optionList = pageUtils.findWebElementsByXpath(ITEMS_PER_PAGE_OPTIONS);
                for(WebElement option : optionList) {
                    WebElement optionSpan = option.findElement(By.xpath("." + ITEMS_PER_PAGE_OPTIONS_TEXT));

                    // Click specified Dropdown option
                    Integer optionTextValue = Integer.valueOf(optionSpan.getText());
                    if(optionTextValue == itemsPerPageOption) {
                        option.click();
                        return true;
                    }
                }
                log.error("Assertion Failed: Failed to find Items Per Page option '{}' in dropdown", itemsPerPageOption);
                return false;
            }
        });

        testAssert.assertTrue(selectionExists, "Items Per Page option does not exist on Files Dashboard!");
    }

    public void verifyEmptyDashboard() {
        testAssert.assertTrue(pageUtils.hasNoElement(FILES_DASHBOARD_TABLE), "File Dashboard is not empty!");
        WebElement webElement = pageUtils.findElement(PAGE_MAX_SIZE);
        testAssert.assertTrue(webElement.getText().matches("^0 of 0$"), "Page size displayed in File Dashboard is not correct!");
        testAssert.assertFalse(pageUtils.findElement(PAGE_NEXT_BTN).isEnabled(), "File Dashboard Next Button is clickable!!!");
        testAssert.assertFalse(pageUtils.findElement(PAGE_BACK_BTN).isEnabled(), "File Dashboard Back Button is clickable!!!");
    }
}
