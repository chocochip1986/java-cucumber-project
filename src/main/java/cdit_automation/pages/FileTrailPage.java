package cdit_automation.pages;

import cdit_automation.enums.BatchStatusEnum;
import cdit_automation.models.Batch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Slf4j
@Component
public class FileTrailPage extends AbstractPage {
    public final String BACK_BTN = "//button[@class='secondary-button']/child::span[@class='secondary-button-text' and text()='BACK']";
    public final String FILE_TRAIL_PAGE = "//header[@class='header' and text()='File Trail']";
    public final String REASONABLENESS_TRENDING_PAGE = "//a[text()='Details of trending records']";

    public final String FILE_TRAIL_INCOMING_INFO_SUBHEADER = "//div[@class='header-title' and text()='Incoming Information']";
    public final String FILE_TRAIL_FORMAT_VALIDATION_INFO_SUBHEADER = "//div[@class='header-title' and text()='Format Validation Information']";
    public final String FILE_TRAIL_CONTENT_VALIDATION_INFO_SUBHEADER = "//div[@class='header-title' and text()='Content Validation Information']";
    public final String FILE_TRAIL_REASONABLENESS_SUBHEADER = "//div[@class='header-title' and text()='Reasonableness Trending Information']";
    public final String FILE_TRAIL_LOAD_VALIDATION_INFO_SUBHEADER = "//div[@class='header-title' and text()='Load Validation Information']";

    public void clickBack() {
        pageUtils.click_on(BACK_BTN);
    }
    public void clickReasonablenessTrending() {
        pageUtils.click_on(REASONABLENESS_TRENDING_PAGE);
    }

    public void verifyFileTrailPage(Batch batch) {
        testAssert.assertTrue(pageUtils.hasElement(FILE_TRAIL_PAGE), "File Trail page is not displayed!");
        verifyIncomingInfoCard();
        verifyFormatValidationCard();
        verifyContentValidationCard(batch);
        verifyReasonablenessCard(batch);
        verifyLoadCard(batch);
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
}
