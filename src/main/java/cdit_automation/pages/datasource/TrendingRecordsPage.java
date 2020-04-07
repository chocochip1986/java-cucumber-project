package cdit_automation.pages.datasource;

import cdit_automation.enums.datasource.FileTypeEnum;
import cdit_automation.enums.datasource.ReasonablenessCheckDataItemEnum;
import cdit_automation.models.datasource.Batch;
import cdit_automation.models.datasource.ReasonablenessCheckStatistic;
import cdit_automation.pages.AbstractPage;
import java.util.List;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrendingRecordsPage extends AbstractPage {
    private final String PAGE_TITLE = "//div[contains(@class, 'table-title') and text()='Details of Trending Records']";
    private final String TABLE_ROWS = "//tbody[@role='rowgroup']/child::mat-row[@role='row']";
    private final String ROW_RELATIVE_PATH_TO_DESCRIPTION = "//mat-cell/child::mdt-table-cell/child::mdt-text-cell/child::div[text()[contains(., '<Stats Description>')]]";
    private final String ROW_RELATIVE_PATH_TO_CURRENT_RUN_STATS = "//mat-cell[<index>]/child::mdt-table-cell/child::mdt-text-cell/child::div";

    private final String DASH = "-";

    public void verifyLoaded() {
        boolean isLoaded = waitUntilCondition(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return pageUtils.hasElement(PAGE_TITLE);
            }
        });
        testAssert.assertTrue(isLoaded, "Trending Records Page is not displayed!");
    }

    public void verifyStatisticsForFile(FileTypeEnum fileTypeEnum, List<Batch> listOfExpectedBatches) {
        List<WebElement> webElementsStatisticRows = pageUtils.findAllWebElements(TABLE_ROWS);;

        for ( int i = 0 ; i < listOfExpectedBatches.size() ; i++ ) {
            for ( WebElement webElementStatisticRow : webElementsStatisticRows ) {
                WebElement webElement = webElementStatisticRow.findElement(By.xpath(ROW_RELATIVE_PATH_TO_DESCRIPTION.replace("<Stats Description>", ReasonablenessCheckDataItemEnum.NO_OF_NEW_THIRTEEN_YEAR_OLD.getValue())));
                if ( webElement != null ) {
                    Batch batch = listOfExpectedBatches.get(i);
                    ReasonablenessCheckStatistic stats =
                            reasonablenessCheckStatisticRepo.findByBatchAndDataItemAndDataCollectedDate(batch,
                                    ReasonablenessCheckDataItemEnum.NO_OF_NEW_THIRTEEN_YEAR_OLD.getValue(),
                                    batch.getCreatedAt());

                    WebElement webElementCurrentRunStats = webElementStatisticRow.findElement(By.xpath(ROW_RELATIVE_PATH_TO_CURRENT_RUN_STATS.replace("<index>", String.valueOf(i+2))));
                    if ( webElementCurrentRunStats != null ) {
                        if ( stats == null ) {
                            testAssert.assertEquals(DASH, webElementCurrentRunStats.getText(), "Stats are not the same!");
                        } else {
                            testAssert.assertEquals(stats.getDataItemValue(), webElementCurrentRunStats.getText(), "Stats are not the same!");
                        }
                    }
                }
            }
        }
    }
}
