package cdit_automation.pages;

import cdit_automation.enums.datasource.BatchStatusEnum;
import cdit_automation.enums.datasource.views.FileStatusSubTextEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.datasource.FileReceived;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FilesDashBoardPage extends AbstractPage {
    public final String FILE_TRAIL_BUTTONS = ".traffic-button-cell";
    public final String FILES_DASHBOARD_SUBTITLE = "//div[@class='table-title dark-gray-font' and text()='Files Dashboard']";
    public final String FILES_DASHBOARD_TABLE = ".mat-table tbody mat-row";

    public final String BODY = "//tbody[@role='rowgroup']/child::mat-row[@role='row']";

    public final String COLUMN_CURRENTSTATUS_SUBTEXT = "//mat-row[@role='row']/child::mat-cell[contains(@class, 'cdk-column-currentStatus')]//*[@class='traffic-button-cell-subText']";

    public final String PAGINATION_SELECTED_PAGE = ".mat-paginator-page-size mat-form-field span.mat-select-value-text span";
    public final String PAGINATION_SELECTED_PAGE_INNER_SPAN = "//div[contains(@class, 'mat-form-field-infix')]/child::mat-select/child::div/child::div[1]/child::span/child::span";
    public final String PAGE_MAX_SIZE = "//div[@class='mat-paginator-range-actions']/child::div[@class='mat-paginator-range-label']";

    public final String ITEM_PER_PAGE_SELECT = "//div[contains(@class, 'mat-form-field-infix')]/child::mat-select";
    public final String ITEMS_PER_PAGE_OPTIONS = "//div[contains(@class, 'mat-select-panel')]/child::mat-option";
    public final String ITEMS_PER_PAGE_OPTIONS_TEXT = "//span[contains(@class, 'mat-option-text')]";

    public final String PAGE_NEXT_BTN = ".mat-paginator-navigation-next";
    public final String PAGE_BACK_BTN = ".mat-paginator-navigation-previous";
    
    public static final String MDT_TABLE_CELL_MDT_TEXT_CELL_DIV = "mdt-table-cell mdt-text-cell div";
    public static final String CURRENT_STATUS_MAIN_TEXT = ".traffic-button-cell-text";
    public static final String CURRENT_STATUS_SUB_TEXT = ".traffic-button-cell-subText";

    public void accessRandomFileTrail() {
        WebElement webElement = pageUtils.findFirstElement(FILE_TRAIL_BUTTONS);
        webElement.click();
    }

    public void verifyFilesDashboardPage() {
        boolean isLoaded = waitUntilCondition(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return pageUtils.hasElement(FILES_DASHBOARD_SUBTITLE);
            }
        });
        
        testAssert.assertTrue(isLoaded, "Files Dashboard page is not displayed!!!");
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

    public void searchForFile(FileReceived fileReceived) {
        WebElement targetFileTrailWebElement = findFileReceivedWebElement(fileReceived);
        if ( targetFileTrailWebElement == null ) {
            throw new TestFailException("Unable to find file: "+fileReceived.getFilePath());
        }
        WebElement webElement = targetFileTrailWebElement.findElement(By.tagName("button"));
        webElement.click();
    }

    public WebElement findFileReceivedWebElement(FileReceived fileReceived) {
        
        WebElement itemsPerPageWebElement = pageUtils.findElement(PAGINATION_SELECTED_PAGE_INNER_SPAN);
        WebElement maxPageWebElement = pageUtils.findElement(PAGE_MAX_SIZE);
        Pattern pattern = Pattern.compile("^.* of (\\d+)$");
        Matcher matcher = pattern.matcher(maxPageWebElement.getText());
        WebElement targetFileTrailWebElement = null;
        if ( matcher.find() ) {
            int maxPageTraversalCount = deriveMaxPages(Double.parseDouble(itemsPerPageWebElement.getText()), Double.parseDouble(matcher.group(1)));
            for (int i = 0 ; i < maxPageTraversalCount ; i++) {
                List<WebElement> rowsOfFiles = pageUtils.findAllWebElements(BODY);
                validateFilesExists(rowsOfFiles);
                for ( WebElement rowWebElement : rowsOfFiles ) {
                    List<WebElement> cellWebElements = rowWebElement.findElements(By.cssSelector(".mat-cell"));
                    if (cellWebElements.get(0).findElement(By.cssSelector(MDT_TABLE_CELL_MDT_TEXT_CELL_DIV)).getText().equals(fileReceived.getFileDetail().getAgency().getValue()) && 
                            cellWebElements.get(1).findElement(By.cssSelector(MDT_TABLE_CELL_MDT_TEXT_CELL_DIV)).getText().matches("^"+fileReceived.getFileDetail().getFileName()+".*$") &&
                            cellWebElements.get(4).findElement(By.cssSelector(MDT_TABLE_CELL_MDT_TEXT_CELL_DIV)).getText().equals(fileReceived.getReceivedTimestamp().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                    ) {
                        targetFileTrailWebElement = rowWebElement;
                        break;
                    }
                }
                if ( targetFileTrailWebElement == null ) {
                    traverseToNextFileDashBoardPage(rowsOfFiles.get(0));
                }
            }
        }
        
        return targetFileTrailWebElement;
    }

    public void traverseToPreviousFileDashBoardPage() {
        pageUtils.click_on(PAGE_BACK_BTN);
    }

    public void traverseToNextFileDashBoardPage(WebElement staleWebElement) {
        pageUtils.asyncClickOn(PAGE_NEXT_BTN, staleWebElement);
    }

    private void validateFilesExists(List<WebElement> webElements) {
        if (!checkIfFilesExists(webElements)) {
            throw new TestFailException("There are no files displayed!");
        }
    }

    private boolean checkIfFilesExists(List<WebElement> webElements) {
        return !webElements.isEmpty();
    }

    private int deriveMaxPages(double itemsPerPage, double numberOfFiles) {
        return (int)Math.ceil( numberOfFiles / itemsPerPage );
    }
}
