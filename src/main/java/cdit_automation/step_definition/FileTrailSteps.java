package cdit_automation.step_definition;

import cdit_automation.enums.FileTypeEnum;
import cdit_automation.models.Batch;
import cdit_automation.pages.FileTrailPage;
import cdit_automation.pages.datasource.TrendingRecordsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Ignore
public class FileTrailSteps extends AbstractSteps {
    @Autowired
    FileTrailPage fileTrailPage;

    @Autowired
    TrendingRecordsPage trendingRecordsPage;

    @And("^I click on the back button$")
    public void iClickOnBackButton() {
        log.info("Clicking on Back button...");
        fileTrailPage.clickBack();
    }

    @Then("I verify that I see the file trail page")
    public void iVerifyThatISeeTheFileTrailPage() {
        log.info("Verifying file trail page is displayed...");
        Batch batch = testContext.get("batch");
        fileTrailPage.verifyFileTrailPage(batch);
    }

    @And("^I click on the reasonableness trending link$")
    public void iClickOnReasonablenessTrendingLink() {
        log.info("Clicking on reasonableness trending link...");
        fileTrailPage.clickReasonablenessTrending();
        trendingRecordsPage.verifyLoaded();
    }

    @Then("^I verify that content validation count matches Datasource validated record count$")
    public void iVerifyThatContentValidationMatchesDatasourceValidatedRecords(){
        log.info("Verifying content validation count matches data persisted in Datasource validated table...");
        Batch batch = testContext.get("batch");
        FileTypeEnum fileTypeEnum = batch.getFileReceived().getFileDetail().getFileEnum();
        long validatedCount = jpaRepoFactory.countValidatedRecordsBasedOnFileTypeAndBatch(fileTypeEnum, batch);
        fileTrailPage.verifyTrendingRecordCount(validatedCount);
    }

    @Then("I verify {int} records passed format validation")
    public void iVerifyRecordsPassedFormatValidation(int count) {
        testAssert.assertTrue(
                fileTrailPage.isFormatPassedCountSpanExist(), "Format passed count span should exist");
        testAssert.assertEquals(
                String.valueOf(count), fileTrailPage.getFormatPassedCount(), "Unexpected format passed count");
    }

    @Then("I verify {int} records passed content validation")
    public void iVerifyRecordsPassedContentValidation(int count) {
        testAssert.assertTrue(
                fileTrailPage.isContentPassedCountSpanExist(), "Content passed count span should exist");
        testAssert.assertEquals(
                String.valueOf(count), fileTrailPage.getContentPassedCount(), "Unexpected content passed count");
    }

    @Then("I verify {int} records failed format validation")
    public void iVerifyRecordsFailedFormatValidation(int count) {
        testAssert.assertTrue(
                fileTrailPage.isFormatFailedCountSpanExist(), "Format failed count span should exist");
        testAssert.assertEquals(
                String.valueOf(count), fileTrailPage.getFormatFailedCount(), "Unexpected content passed count");
    }

    @Then("I verify {int} records failed content validation")
    public void iVerifyRecordsFailedContentValidation(int count) {
        testAssert.assertTrue(
                fileTrailPage.isContentFailedCountSpanExist(), "Content failed count span should exist");
        testAssert.assertEquals(
                String.valueOf(count), fileTrailPage.getContentFailedCount(), "Unexpected content passed count");
    }
}
