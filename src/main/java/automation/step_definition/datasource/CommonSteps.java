package automation.step_definition.datasource;

import automation.aws.modules.Slack;
import automation.configuration.datasource.TestEnv;
import automation.enums.datasource.BatchStatusEnum;
import automation.enums.datasource.FileTypeEnum;
import automation.exceptions.TestFailException;
import automation.models.datasource.Batch;
import automation.models.datasource.ErrorMessage;
import automation.models.datasource.FileReceived;
import automation.models.datasource.Nationality;
import automation.models.datasource.PersonDetail;
import automation.models.datasource.PersonId;
import automation.models.datasource.PersonName;
import automation.models.datasource.PersonProperty;
import automation.models.datasource.Property;
import automation.models.datasource.PropertyDetail;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Ignore
public class CommonSteps extends AbstractSteps {

    @And("I open a new tab")
    public void iOpenANewTab() {
        pageUtils.openNewTab();
    }

    @And("I close current tab")
    public void iCloseCurrentTab() {
        pageUtils.closeTab();
    }

    @And("^the (Mha|Iras) (.*) batch job completes running with status (.*)$")
    public void theBatchJobCompletesRunning(String agencyName, String batchJobName, BatchStatusEnum expectedBatchStatus) {
        log.info("Veryfing that batch job ended with status: "+expectedBatchStatus);
        if (testContext.contains("fileReceived")) {
            FileReceived fileReceived = testContext.get("fileReceived");

            if ( testManager.getTestEnvironment().getEnv().equals(TestEnv.Env.QA) ) {
                Batch batch = batchRepo.findFirstByFileReceivedOrderByCreatedAtDesc(fileReceived);
                boolean isVerified = waitUntilConditionForBatch(new Predicate<Batch>(){
                    public boolean test(Batch batch) {
                        batch = batchRepo.findFirstByFileReceivedOrderByCreatedAtDesc(fileReceived);
                        if ( batch != null && batch.getStatus().equals(expectedBatchStatus) ) {
                            return Boolean.TRUE;
                        } else if ( batch != null && BatchStatusEnum.isBatchStatusAnErrorStatus(batch.getStatus()) ) {
                            return Boolean.TRUE;
                        } else {
                            return Boolean.FALSE;
                        }
                    }
                }, batch);
                batch = batchRepo.findFirstByFileReceivedOrderByCreatedAtDesc(fileReceived);
                if (batch == null) {
                    slack.sendToSlack(testManager.testEnv.getTopicArn(), "No batch record created for fileReceived record: "+fileReceived.getId().toString(), Slack.Level.NEUTRAL);
                    testAssert.assertNotNull(batch, "The "+batchJobName+" job from "+agencyName+" is null!!!");
                } else {
                    slack.sendToSlack(testManager.testEnv.getTopicArn(), String.format("Status:%s", batch.getStatus()), Slack.Level.NEUTRAL);
                    testAssert.assertEquals(expectedBatchStatus, batch.getStatus(), "The "+batchJobName+" job from "+agencyName+" did not complete!!!");
                }
                testContext.set("batch", batch);
            } else {
                Batch batch = batchRepo.findFirstByFileReceivedOrderByCreatedAtDesc(fileReceived);
                testAssert.assertEquals(expectedBatchStatus, batch.getStatus(), new Supplier<String>() {
                    @Override
                    public String get() {
                        List<ErrorMessage> errorMessages = errorMessageRepo.findByBatch(batch);
                        return errorMessages.isEmpty() ? "" : errorMessages.stream().map(errorMessage -> errorMessage.getMessage()+System.lineSeparator()).collect(Collectors.joining());
                    }
                });

                if (testContext.contains("batch")) {
                    testContext.replace("batch", batch);
                } else {
                    testContext.set("batch", batch);
                }
            }
        } else {
            throw new TestFailException("No batch job previously created!");
        }
    }

    @And("^the error message is (.*)$")
    public void theErrorMessageIs(String errorMsg) {
        FileReceived fileReceived = testContext.get("fileReceived");
        Batch batch = testContext.get("batch");
        if ( batch == null ) {
            throw new TestFailException("No batch record is found in the testContext!!!");
        }
        log.info("Verifying that the batch job ("+batch.getId()+") contains a corresponding error message record with error: "+errorMsg);

        List<ErrorMessage> errorMessage = errorMessageRepo.findByBatch(batch);

        testAssert.assertEquals(true, errorMessage.stream().filter(errMsg -> errMsg.getMessage().equals(errorMsg)).findFirst().isPresent(), "No such error message is found for batch: "+batch.getId());
    }

    @And("^the error message contains (.*)$")
    public void theErrorMessageContains(String errorMsg) {
        FileReceived fileReceived = testContext.get("fileReceived");
        Batch batch = testContext.get("batch");
        if ( batch == null ) {
            throw new TestFailException("No batch record is found in the testContext!!!");
        }
        log.info("Verifying that the batch job ("+batch.getId()+") contains a corresponding error message record with error: "+errorMsg);

        List<ErrorMessage> errorMessage = errorMessageRepo.findByBatch(batch);

        Arrays.asList(errorMsg.split(",")).forEach(m -> {
            testAssert.assertTrue(
                    errorMessage.stream().anyMatch(
                            errMsg -> errMsg.getMessage().equalsIgnoreCase(m.trim()) ||
                                    errMsg.getMessage().matches(".*" + m.trim() + ".*") ||
                                    errMsg.getMessage().contains(m.trim())),
                    "No such error message [" + m.trim() + "] is found for batch: " + batch.getId());
        });
    }

    @And("I verify that the following error message appeared:")
    public void iVerifyThatTheFollowingErrorMessageAppeared(DataTable table) {

        Batch batch = batchRepo.findFirstByFileReceivedOrderByCreatedAtDesc(testContext.get("fileReceived"));
        List<String> errorMessages =
                errorMessageRepo.findByBatch(batch).stream()
                        .map(ErrorMessage::getMessage)
                        .collect(Collectors.toList());

        List<Map<String, String>> dataMap = table.asMaps(String.class, String.class);
        dataMap
                .forEach(
                        m -> {
                            int expectedMessageCount = parseStringSize((String) m.get("Count"));
                            String expectedErrorMessage = m.get("Message").toString();
                            testAssert.assertEquals(
                                    (long) expectedMessageCount,
                                    errorMessages.stream()
                                            .filter(z -> z.equalsIgnoreCase(expectedErrorMessage) ||
                                                    z.matches(".*"+expectedErrorMessage+".*") ||
                                                    z.contains(expectedErrorMessage))
                                            .count(),
                                    "Unexpected repetition of [ " + expectedErrorMessage + " ] error message");
                            errorMessages.removeIf(e -> e.equalsIgnoreCase(expectedErrorMessage) ||
                                    e.matches(".*"+expectedErrorMessage+".*") ||
                                    e.contains(expectedErrorMessage));
                        });

        testAssert.assertEquals(Collections.emptyList(), errorMessages, "Unexpected error message found!");
    }

    @And("there are no error messages")
    public void thereAreNoErrorMessages() {
        log.info("Verifying that are no error messages...");
        FileReceived fileReceived = testContext.get("fileReceived");
        if ( fileReceived == null ) {
            throw new TestFailException("No file received record!");
        }

        Batch batch = batchRepo.findFirstByFileReceivedOrderByCreatedAtDesc(fileReceived);
        if ( batch == null ) {
            throw new TestFailException("No batch record created for fileReceived record: "+fileReceived.getId());
        }

        List<ErrorMessage> errorMessages = errorMessageRepo.findByBatch(batch);

        testAssert.assertTrue(errorMessages.isEmpty(), "There are error messages for batch: "+batch.getId());
    }

    @And("^I verify that person with ([S|T|F|G][0-9]{7}[A-Z]) (is|is not) persisted in Datasource$")
    public void iVerifyThatPersonWithFXIsPersistedInDatasource(String identifier, String persisted) {
        log.info("Verifying records of person with " + identifier + " " + persisted + " persisted.");
        PersonId personId = personIdRepo.findByNaturalId(identifier);

        // If "is not" persisted, check personId is null. Without personId, nothing can be created.
        if (persisted.equalsIgnoreCase("is not")) {
            testAssert.assertNull(personId, "PersonId record found for "+identifier);
            return;
        }

        // Otherwise, check all relevant data is created.
        testAssert.assertNotNull(personId, "No PersonId record for "+identifier);
        testAssert.assertNotNull(personId.getPerson(), "No Person record for "+identifier);

        Nationality nationality = nationalityRepo.findNationalityByPerson(personId.getPerson());
        testAssert.assertNotNull(nationality, "No Nationality record for "+personId.getNaturalId());

        PersonName personName = personNameRepo.findByPerson(personId.getPerson());
        testAssert.assertNotNull(personName, "No PersonName record for "+personId.getNaturalId());

        PersonDetail personDetail = personDetailRepo.findByPerson(personId.getPerson());
        testAssert.assertNotNull(personDetail, "No Person Detail record for "+personId.getNaturalId());

        PersonProperty personProperty = personPropertyRepo.findByPerson(personId.getPerson());
        testAssert.assertNotNull(personProperty, "No Person Property record for "+personId.getNaturalId());

        Property property = propertyRepo.findByPropertyId(personProperty.getIdentifier().getPropertyEntity().getPropertyId());
        testAssert.assertNotNull(property, "No property record for "+personId.getNaturalId());

        PropertyDetail propertyDetail = propertyDetailRepo.findByProperty(property);
        testAssert.assertNotNull(propertyDetail, "No property detail for "+personId.getNaturalId());
    }

    @And("^I will receive another file")
    public void iWillReceiveAnotherFile() throws IOException {
        File outputArtifactDir = new File(testManager.getOutputArtifactsDir().getFileName().toString());
        String[] dirFiles;
        if(outputArtifactDir.isDirectory()){
            dirFiles = outputArtifactDir.list();
            for (int i = 0; i < dirFiles.length; i++) {
                File dirFile = new File(outputArtifactDir, dirFiles[i]);
                if(!dirFile.isDirectory()){
                    dirFile.delete();
                }
            }
        }
    }

    @Given("^the (MHA_BULK_CITIZEN|MHA_NEW_CITIZEN|MHA_NO_INTERACTION|MHA_CHANGE_ADDRESS|MHA_DUAL_CITIZEN|MHA_PERSON_DETAIL_CHANGE|MHA_DEATH_DATE|MHA_CEASED_CITIZEN|IRAS_BULK_AI|IRAS_THRICE_MONTHLY_AI) file is empty$")
    public void theFileIsEmpty(FileTypeEnum fileTypeEnum) {
        try {
            batchFileCreator.writeToFile(fileTypeEnum.getValue().toLowerCase(), Collections.emptyList());
        } catch(IOException ioe) {
            throw new TestFailException("Unable to create empty file for " + fileTypeEnum.getValue());
        }
    }

    @And("^I verify number of records in Incoming Record table is (\\d)")
    public void verifyNumberOfRecordsInIncomingRecordTable(long noOfRecords) {

        log.info("Verifying number of records in Incoming Record table: " + noOfRecords);
        if (testContext.contains("fileReceived")) {

            FileReceived fileReceived = testContext.get("fileReceived");
            Batch batch = batchRepo.findFirstByFileReceivedOrderByCreatedAtDesc(fileReceived);
            
            long count = incomingRecordRepo.countAllByBatch(batch);
            testAssert.assertEquals(noOfRecords, count, "The expected number of record(s) does not match!" );

        } else {
            throw new TestFailException("No File Received previously created!");
        }
    }
}
