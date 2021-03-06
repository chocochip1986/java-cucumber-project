package automation.pages;

import automation.enums.datasource.BatchStatusEnum;
import automation.models.datasource.Batch;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FileTrailPage extends AbstractPage {
    public final String BACK_BTN = "//button[contains(@class,'secondary-button')]/child::span[@class='secondary-button-text' and text()='BACK']";
    public final String REJECT_FILE_BTN = "//button[contains(@class,'secondary-button')]/child::span[@class='secondary-button-text' and text()='REJECT FILE']";
    public final String RE_VALIDATE_FILE_BTN = "//app-primary-button[@id='file-trail-load-button']/child::button";


    public final String FILE_TRAIL_PAGE = "//header[@class='header' and text()='File Trail']";
    public final String REASONABLENESS_TRENDING_PAGE = ".trending-page-link";

    public final String FILE_TRAIL_INCOMING_INFO_SUBHEADER = "//div[@class='header-title' and text()='Incoming Information']";
    public final String FILE_TRAIL_FORMAT_VALIDATION_INFO_SUBHEADER = "//div[@class='header-title' and text()='Format Validation Information']";
    public final String FILE_TRAIL_CONTENT_VALIDATION_INFO_SUBHEADER = "//div[@class='header-title' and text()='Content Validation Information']";
    public final String FILE_TRAIL_REASONABLENESS_SUBHEADER = "//div[@class='header-title' and text()='Reasonableness Trending Information']";
    public final String FILE_TRAIL_LOAD_VALIDATION_INFO_SUBHEADER = "//div[@class='header-title' and text()='Load Validation Information']";
    public final String FILE_TRAIL_REASONABLENESS_TRENDING_COUNT_LABEL = "//span[@id='trending-count']";

    public final String FILE_TRAIL_FORMAT_VALIDATION_PASSED_COUNT = "//span[@id='format-pass-count']";
    public final String FILE_TRAIL_FORMAT_VALIDATION_FAILED_COUNT = "//span[@id='format-fail-count']";
    public final String FILE_TRAIL_CONTENT_VALIDATION_PASSED_COUNT = "//span[@id='content-pass-count']";
    public final String FILE_TRAIL_CONTENT_VALIDATION_FAILED_COUNT = "//span[@id='content-fail-count']";

    public void clickBack() {
        pageUtils.click_on(BACK_BTN);
    }
    public void clickReasonablenessTrending() {
        pageUtils.click_on(REASONABLENESS_TRENDING_PAGE);
    }
    public void clickRejectFile() { pageUtils.click_on(REJECT_FILE_BTN); }
    public void clickRevalidateFile() { pageUtils.click_on(RE_VALIDATE_FILE_BTN); }

    public void verifyFileTrailPage(Batch batch) {
        testAssert.assertTrue(pageUtils.hasElement(FILE_TRAIL_PAGE), "File Trail page is not displayed!");
        verifyIncomingInfoCard();
        verifyFormatValidationCard();
        verifyContentValidationCard(batch);
        verifyReasonablenessCard(batch);
        verifyLoadCard(batch);
    }

    public void verifyTrendingRecordCount(long count) {
//        testAssert.assertEquals(pageUtils.findElement(FILE_TRAIL_REASONABLENESS_TRENDING_COUNT_LABEL).getText(), Long.toString(count), "Trending record count does not match!");
    }

    private void verifyIncomingInfoCard() {
        verifyCard(FILE_TRAIL_INCOMING_INFO_SUBHEADER);
    }

    private void verifyFormatValidationCard() {
        verifyCard(FILE_TRAIL_FORMAT_VALIDATION_INFO_SUBHEADER);
    }

    private void verifyContentValidationCard(Batch batch) {
        if ( batch.getStatus().equals(BatchStatusEnum.RAW_DATA) ||
                batch.getStatus().equals(BatchStatusEnum.BULK_CHECK_VALIDATED_DATA) ||
                batch.getStatus().equals(BatchStatusEnum.BULK_CHECK_VALIDATION_ERROR) ||
                batch.getStatus().equals(BatchStatusEnum.VALIDATED_TO_PREPARED_DATA) || batch.getStatus().equals(BatchStatusEnum.VALIDATED_TO_PREPARED_ERROR) ) {
            verifyCard(FILE_TRAIL_CONTENT_VALIDATION_INFO_SUBHEADER);
        }
    }

    private void verifyReasonablenessCard(Batch batch) {
        if ( batch.getStatus().equals(BatchStatusEnum.VALIDATED_TO_PREPARED_DATA) ||
                batch.getStatus().equals(BatchStatusEnum.ERROR_RATE) ||
                batch.getStatus().equals(BatchStatusEnum.ERROR_RATE_ERROR) ||
                batch.getStatus().equals(BatchStatusEnum.MAPPED_DATA) ||
                batch.getStatus().equals(BatchStatusEnum.BULK_MAPPED_DATA_ERROR) ||
                batch.getStatus().equals(BatchStatusEnum.BULK_MAPPED_DATA) ||
                batch.getStatus().equals(BatchStatusEnum.BULK_MAPPED_DATA_ERROR) ||
                batch.getStatus().equals(BatchStatusEnum.CLEANUP) ||
                batch.getStatus().equals(BatchStatusEnum.CLEANUP_ERROR) ) {
            verifyCard(FILE_TRAIL_REASONABLENESS_SUBHEADER);
        }
    }

    private void verifyLoadCard(Batch batch) {
        if ( batch.getStatus().equals(BatchStatusEnum.VALIDATED_TO_PREPARED_DATA) ||
                batch.getStatus().equals(BatchStatusEnum.ERROR_RATE) ||
                batch.getStatus().equals(BatchStatusEnum.ERROR_RATE_ERROR) ||
                batch.getStatus().equals(BatchStatusEnum.MAPPED_DATA) ||
                batch.getStatus().equals(BatchStatusEnum.BULK_MAPPED_DATA_ERROR) ||
                batch.getStatus().equals(BatchStatusEnum.BULK_MAPPED_DATA) ||
                batch.getStatus().equals(BatchStatusEnum.BULK_MAPPED_DATA_ERROR) ||
                batch.getStatus().equals(BatchStatusEnum.CLEANUP) ||
                batch.getStatus().equals(BatchStatusEnum.CLEANUP_ERROR) ) {
            verifyCard(FILE_TRAIL_LOAD_VALIDATION_INFO_SUBHEADER);
        }
    }

    private void verifyCard(String card) {
        boolean isFound = waitUntilCondition(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return pageUtils.hasElement(card);
            }
        });
        if (!isFound) {
            testAssert.assertTrue(isFound, "Unable to find header with css or xpath: "+card);
        }
    }

    public boolean isFormatPassedCountSpanExist() {
        return waitUntilCondition(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return pageUtils.hasElement(FILE_TRAIL_FORMAT_VALIDATION_PASSED_COUNT);
            }
        });
    }

    public String getFormatPassedCount() {
        return getCount(FILE_TRAIL_FORMAT_VALIDATION_PASSED_COUNT);
    }

    public boolean isFormatFailedCountSpanExist() {
        return waitUntilCondition(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return pageUtils.hasElement(FILE_TRAIL_FORMAT_VALIDATION_FAILED_COUNT);
            }
        });
    }

    public String getFormatFailedCount() {
        return getCount(FILE_TRAIL_FORMAT_VALIDATION_FAILED_COUNT);
    }

    public boolean isContentPassedCountSpanExist() {
        return waitUntilCondition(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return pageUtils.hasElement(FILE_TRAIL_CONTENT_VALIDATION_PASSED_COUNT);
            }
        });
    }

    public String getContentPassedCount() {
        return getCount(FILE_TRAIL_CONTENT_VALIDATION_PASSED_COUNT);
    }

    public boolean isContentFailedCountSpanExist() {
        return waitUntilCondition(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return pageUtils.hasElement(FILE_TRAIL_CONTENT_VALIDATION_FAILED_COUNT);
            }
        });
    }

    public String getContentFailedCount() {
        return getCount(FILE_TRAIL_CONTENT_VALIDATION_FAILED_COUNT);
    }

    public boolean isRejectFileButtonExist() {
        return waitUntilCondition(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return pageUtils.hasElement(REJECT_FILE_BTN);
            }
        });
    }

    public boolean isReValidateWithoutErrorRateButtonExist() {
        return waitUntilCondition(new Supplier<Boolean>() {
            @Override
            public Boolean get() { return pageUtils.hasElement(RE_VALIDATE_FILE_BTN); }
        });
    }
    
    private String getCount(String xpath) {
        pageUtils.waitForElementToHaveNumericalDigit(xpath);
        Optional<WebElement> targetOpt = Optional.ofNullable(pageUtils.findElement(xpath));
        return targetOpt.isPresent() ? targetOpt.get().getText() : "";
    }

}
