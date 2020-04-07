package cdit_automation.step_definition.datasource;

import cdit_automation.enums.datasource.FileTypeEnum;
import cdit_automation.models.datasource.Batch;
import cdit_automation.pages.FileTrailPage;
import cdit_automation.pages.datasource.TrendingRecordsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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

    @When("^I click on the reject file button$")
    public void iClickOnRejectFileButton() {
        log.info("Clicking on reject file button...");
        fileTrailPage.clickRejectFile();
    }

    @When("^I click on the revalidate without error rate button$")
    public void iClickOnTheReValidateWithoutErrorRateButton() {
        log.info("Clicking on the re-validate without error rate button...");
        fileTrailPage.clickRevalidateFile();
    }

    @Then("^I verify that I see the file trail page$")
    public void iVerifyThatISeeTheFileTrailPage() {
        log.info("Verifying file trail page is displayed...");
        Batch batch = testContext.get("batch");
        fileTrailPage.verifyFileTrailPage(batch);
    }

    @Then("^I verify that I see the reject file button$")
    public void iVerifyThatISeeTheRejectFileButton() {
        log.info("Verifying reject file button is displayed...");
        boolean isExist = fileTrailPage.isRejectFileButtonExist();
        testAssert.assertTrue(isExist, "Unable to locate reject file button");
    }

    @Then("^I verify that I see the revalidate without error rate button$")
    public void iVerifyThatISeeTheReValidateWithoutErrorRateButton() {
        log.info("Verify re-validate without error rate button is displayed...");
        boolean isExist = fileTrailPage.isReValidateWithoutErrorRateButtonExist();
        testAssert.assertTrue(isExist, "Unable to locate re-validate w/o error rate button");
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

    @Then("^I verify (\\d+) records passed format validation$")
    public void iVerifyRecordsPassedFormatValidation(int count) {
        testAssert.assertTrue(
                fileTrailPage.isFormatPassedCountSpanExist(), "Format passed count span should exist");
        testAssert.assertEquals(
                String.valueOf(count), fileTrailPage.getFormatPassedCount(), "Unexpected format passed count");
    }

    @Then("^I verify (\\d+) records passed content validation$")
    public void iVerifyRecordsPassedContentValidation(int count) {
        testAssert.assertTrue(
                fileTrailPage.isContentPassedCountSpanExist(), "Content passed count span should exist");
        testAssert.assertEquals(
                String.valueOf(count), fileTrailPage.getContentPassedCount(), "Unexpected content passed count");
    }

    @Then("^I verify (\\d+) records failed format validation$")
    public void iVerifyRecordsFailedFormatValidation(int count) {
        testAssert.assertTrue(
                fileTrailPage.isFormatFailedCountSpanExist(), "Format failed count span should exist");
        testAssert.assertEquals(
                String.valueOf(count), fileTrailPage.getFormatFailedCount(), "Unexpected content passed count");
    }

    @Then("^I verify (\\d+) records failed content validation$")
    public void iVerifyRecordsFailedContentValidation(int count) {
        testAssert.assertTrue(
                fileTrailPage.isContentFailedCountSpanExist(), "Content failed count span should exist");
        testAssert.assertEquals(
                String.valueOf(count), fileTrailPage.getContentFailedCount(), "Unexpected content passed count");
    }
}
