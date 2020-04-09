package automation.step_definition.datasource;

import automation.constants.datasource.Constants;
import automation.constants.datasource.ErrorMessageConstants;
import automation.constants.TestConstants;
import automation.data_helpers.datasource.batch_entities.MhaDualCitizenFileEntry;
import automation.enums.datasource.FileTypeEnum;
import automation.enums.datasource.InvalidDateOfRunEnum;
import automation.enums.datasource.InvalidNricEnum;
import automation.enums.datasource.NationalityEnum;
import automation.exceptions.TestDataSetupErrorException;
import automation.models.datasource.Batch;
import automation.models.datasource.ErrorMessage;
import automation.models.datasource.FileReceived;
import automation.models.datasource.Nationality;
import automation.models.datasource.PersonId;
import automation.models.datasource.embeddables.BiTemporalData;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class MhaDualCitizenSteps extends AbstractSteps {

    @Given("^there (?:is|are) (\\d+) existing dual citizen(?:s)?$")
    public void createExistingDualCitizen(int numOfDualCitizens) {
        log.info("Creating "+numOfDualCitizens+" existing dual citizens");
        Map<String, PersonId> hashOfDCs = new HashMap<>();
        for ( int i = 0 ; i < numOfDualCitizens ; i++ ) {
            PersonId personId = personFactory.createDualCitizen();
            hashOfDCs.put("personId" + i, personId);
        }

        testContext.set("hashOfDCs", hashOfDCs);
    }

    @And("^I verify that the dual citizens exists$")
    public void iVerifyThatTheDualCitizensExists() {
        log.info("Verifying that the dual citizens exists");
        if ( testContext.doNotContain("hashOfDCs") ) {
            throw new TestDataSetupErrorException("No such variable as hashOfDCs stored in TestContext!");
        }

        Map<String, PersonId> hashOfDCs = testContext.get("hashOfDCs");
        for ( int i = 0 ; i < hashOfDCs.keySet().size() ; i++ ) {
            PersonId expectedPersonId = hashOfDCs.get("personId"+ i);
            PersonId actualPersonId = personIdRepo.findByNaturalId(expectedPersonId.getNaturalId());

            testAssert.assertNotNull(actualPersonId, "No such person id db:" +expectedPersonId.getNaturalId());
            testAssert.assertEquals(expectedPersonId.getNaturalId(), actualPersonId.getNaturalId(), "No such person in db!");
        }
    }

    @Given("the mha dual citizen file has the following details:")
    public void thatTheMhaDualCitizenFileHasTheFollowingDetails(DataTable table) {
        LocalDate runDate =  TestConstants.DEFAULT_EXTRACTION_DATE;
        batchFileDataWriter.begin(runDate.format(dateUtils.DATETIME_FORMATTER_YYYYMMDD), FileTypeEnum.MHA_DUAL_CITIZEN, null);
        List<Map<String, String>> list = table.asMaps(String.class, String.class);
        List<String> body = mhaDualCitizenFileDataPrep.bodyCreator(list, testContext);

        testContext.set("listOfIdentifiersToWriteToFile", body);
        testContext.set("runDate", runDate);
        batchFileDataWriter.end();
    }

    @Then("I verify that there are new dual citizen in datasource db")
    public void iVerifyThatThereAreNewDualCitizensInDatasourceDb() {
        List<String> listOfNewDCs = testContext.get("listOfNewDCs");

        log.info("Verifying that all new dual citizens are in datasource"+ Arrays.toString(listOfNewDCs.toArray()));

        List<PersonId> personIds = Collections.emptyList();
        LocalDate runDate = testContext.get("runDate");
        for(String identifier : listOfNewDCs) {
            personIds = personIdRepo.findDualCitizen(identifier);

            testAssert.assertEquals(1, personIds.size(), "Person with nric: "+identifier+" is not a dual citizen!");

            Nationality nationality = nationalityRepo.findNationalityByPerson(personIds.get(0).getPerson());
            testAssert.assertNotNull(nationality, "No nationality record exists for nric "+identifier);
            testAssert.assertEquals(dateUtils.beginningOfDayToTimestamp(runDate), nationality.getBiTemporalData().getBusinessTemporalData().getValidFrom(), "Person with "+identifier+" did not begin his/her DC nationality from "+runDate.toString());
            Nationality prevNationality = nationalityRepo.findNationalityByPerson(personIds.get(0).getPerson(), dateUtils.localDateToDate(runDate.minusDays(1L)));
            testAssert.assertEquals(dateUtils.endOfDayToTimestamp(runDate.minusDays(1L)), prevNationality.getBiTemporalData().getBusinessTemporalData().getValidTill(), "Person with "+identifier+" did not end his/her previous nationality on "+runDate.minusDays(1L).toString());
        }

    }

    @Then("I verify that no changes were made to existing dual citizens")
    public void iVerifyThatNoChangesWereMadeToExistingDualCitizens() {
        List<String> listOfExistingDCs = testContext.get("listOfExistingDCs");

        log.info("Verifying that no changes were made to all existing dual citizens who appear in the MHA dual citizen file.");

        List<PersonId> personIds;
        for(String identifier : listOfExistingDCs) {
            personIds = personIdRepo.findDualCitizen(identifier);

            testAssert.assertEquals(1, personIds.size(), "Person with nric: "+identifier+" has been modified!");
        }
    }


    @Then("I verify that the dual citizens who are not in the file will be Singaporeans")
    public void iVerifyThatTheDualCitizensWhoAreNotInTheFileWillBeSingaporeans() {
        List<String> listOfExpiredDCs = testContext.get("listOfExpiredDCs");

        log.info("Verifying that existing dual citizens who did not appear in the file will become Singaporeans");

        List<PersonId> personIds;
        LocalDate runDate = testContext.get("runDate");
        for(String identifier : listOfExpiredDCs) {
            personIds = personIdRepo.findDualCitizen(identifier);

            testAssert.assertEquals(0, personIds.size(), "Person with nric: "+identifier+" has an error!");

            PersonId personId = personIdRepo.findPersonByNaturalId(identifier);
            Nationality currentNationality = nationalityRepo.findNationalityByPerson(personId.getPerson());

            testAssert.assertEquals(NationalityEnum.SINGAPORE_CITIZEN, currentNationality.getNationality(), "Person with nric "+identifier+" is not converted to Singaporean!");
            testAssert.assertEquals(dateUtils.beginningOfDayToTimestamp(runDate), currentNationality.getBiTemporalData().getBusinessTemporalData().getValidFrom(), "Person with nric "+identifier+" did not start his SG citizenship from "+runDate.toString());

            Nationality prevNationality = nationalityRepo.findNationalityByPerson(personId.getPerson(), dateUtils.localDateToDate(runDate.minusDays(1L)));
            testAssert.assertEquals(dateUtils.endOfDayToTimestamp(runDate.minusDays(1L)), prevNationality.getBiTemporalData().getBusinessTemporalData().getValidTill(), "Person with nric "+identifier+" did not end his DC nationality on "+runDate.minusDays(1L).toString());
        }
    }

    @Given("the mha dual citizen file has duplicate nric record")
    public void theMhaDualCitizenFileHasDuplicateNricRecord(DataTable table) {
        log.info("Creating an duplicate nric entry in MHA dual citizen file");
        LocalDate runDate =  TestConstants.DEFAULT_EXTRACTION_DATE;
        batchFileDataWriter.begin(runDate.format(dateUtils.DATETIME_FORMATTER_YYYYMMDD), FileTypeEnum.MHA_DUAL_CITIZEN, null);
        List<Map<String, String>> list = table.asMaps(String.class, String.class);
        List<String> body = mhaDualCitizenFileDataPrep.bodyCreator(list, testContext);
        testContext.set("listOfIdentifiersToWriteToFile", body);
        testContext.set("runDate", runDate);
        batchFileDataWriter.end();
    }

    @Given("the mha dual citizen file is empty")
    public void theMhaDualCitizenFileIsEmpty() {
        batchFileDataWriter.begin(mhaDualCitizenFileDataPrep.generateSingleHeader(), FileTypeEnum.MHA_DUAL_CITIZEN, null);
        batchFileDataWriter.end();
    }

    @Given("the mha dual citizen file has a run date in the future")
    public void theMhaDualCitizenFileHasACutOffDateInTheFuture(DataTable dataTable) {
        List<Map<String, String>> body = dataTable.asMaps(String.class, String.class);

        batchFileDataWriter.begin(mhaDualCitizenFileDataPrep.generateSingleHeader(dateUtils.daysAfterToday(1)), FileTypeEnum.MHA_DUAL_CITIZEN, null);
        mhaDualCitizenFileDataPrep.bodyCreator(body, testContext);
        batchFileDataWriter.end();
    }

    @Given("^([a-z_]+) was a dual citizen (\\d+) days ago$")
    public void personWasADualCitizenDaysAgo(String personName, int daysAgo) {
        PersonId personId = personFactory.createDualCitzenTurnSC(dateUtils.daysBeforeToday(daysAgo));
        testContext.set(personName, personId);
    }

    @And("^([a-z_]+) became a dual citizen (\\d+) days ago$")
    public void personBecameADualCitizenDaysAgo(String personName, int daysAgo) {
        batchFileDataWriter.begin(mhaDualCitizenFileDataPrep.generateSingleHeader(dateUtils.daysBeforeToday(daysAgo)), FileTypeEnum.MHA_DUAL_CITIZEN, null);
        PersonId personId = testContext.get(personName);
        MhaDualCitizenFileEntry mhaDualCitizenFileEntry = MhaDualCitizenFileEntry.builder().identiifer(personId.getNaturalId()).build();
        batchFileDataWriter.chunkOrWrite(mhaDualCitizenFileEntry.toString());
        batchFileDataWriter.end();
    }

    @And("^I verify that ([a-z_]+) is a dual citizen (\\d+) days ago$")
    public void iVerifyThatPersonIsADualCitizenDaysAgo(String personName, int daysAgo) {
        PersonId personId = testContext.get(personName);
        Nationality curNationality = nationalityRepo.findNationalityByPerson(personId.getPerson(),
                dateUtils.localDateToDate(dateUtils.daysBeforeToday(daysAgo)));
        Nationality prevNationality = nationalityRepo.findNationalityByPerson(personId.getPerson(),
                dateUtils.localDateToDate(dateUtils.daysBeforeToday(daysAgo).minusDays(1)));

        testAssert.assertNotNull(curNationality, personName+" ("+personId.getNaturalId()+") does not have a current nationality!");
        testAssert.assertEquals(NationalityEnum.DUAL_CITIZENSHIP, curNationality.getNationality(), personName+" ("+personId.getNaturalId()+") is currently NOT a dual citizen!");
        testAssert.assertEquals(dateUtils.beginningOfDayToTimestamp(dateUtils.daysBeforeToday(daysAgo)),
                curNationality.getBiTemporalData().getBusinessTemporalData().getValidFrom(),
                personName+" ("+personId.getNaturalId()+") did not start being dual citizen on "+dateUtils.beginningOfDayToTimestamp(dateUtils.daysBeforeToday(daysAgo)));
        testAssert.assertNotNull(prevNationality, personName+" ("+personId.getNaturalId()+") does not have a previous nationality!");
        testAssert.assertEquals(NationalityEnum.SINGAPORE_CITIZEN, prevNationality.getNationality(), personName+" ("+personId.getNaturalId()+") was previously NOT a singaporean!");
        testAssert.assertEquals(dateUtils.endOfDayToTimestamp(dateUtils.daysBeforeToday(daysAgo)),
                prevNationality.getBiTemporalData().getBusinessTemporalData().getValidTill(),
                personName+" ("+personId.getNaturalId()+") did not end his/her singaporean nationality on "+dateUtils.endOfDayToTimestamp(dateUtils.daysBeforeToday(daysAgo)));

    }

    @And("no update is done for these nrics")
    public void noUpdateIsDoneForTheseNrics() {
        List<String> nrics = testContext.get("listOfNonExistentNrics");
        for (String nric: nrics) {
            PersonId personId = personIdRepo.findByNaturalId(nric);
            testAssert.assertNull(personId, "There exists a person with such an nric "+nric);
        }
    }

    @Given("^([a-z_]+) who is (\\d+) years old had (?:his|her) citizenship renounced ([0-9]+) days ago$")
    public void personHadHisHerCitizenshipRenouncedDaysAgo(String personName, int age, int daysAgo) {
        LocalDate renunciationDate = dateUtils.daysBeforeToday(daysAgo);
        LocalDate birthDate = dateUtils.yearsBeforeToday(age);
        PersonId personId = personFactory.createCeasedCitizen(personName, birthDate, renunciationDate);

        testContext.set(personName, personId);
    }

    @And("^the mha dual citizen file sends information that ([a-z_]+) is a dual citizen (\\d+) days ago$")
    public void theMhaDualCitizenFileSendsInformationThatPersonIsADualCitizenDaysAgo(String personName, int daysAgo) {
        LocalDate runDate = dateUtils.daysBeforeToday(daysAgo);
        batchFileDataWriter.begin(mhaDualCitizenFileDataPrep.generateSingleHeader(runDate), FileTypeEnum.MHA_DUAL_CITIZEN, null);

        PersonId personId = testContext.get(personName);
        mhaDualCitizenFileDataPrep.createNewDualCitizen(personId.getNaturalId());
        batchFileDataWriter.end();
    }

    @Then("^([a-z_]+) remains a non singaporean$")
    public void janeRemainsANonSingaporean(String personName) {
        PersonId personId = testContext.get(personName);

        Nationality currentNationality = nationalityRepo.findNationalityByPerson(personId.getPerson());
        testAssert.assertEquals(NationalityEnum.NON_SINGAPORE_CITIZEN, currentNationality.getNationality(), "Person with "+personId.getNaturalId()+" is not a non_singaporean!");
    }

    @And("^([a-z_]+) is a dual citizen(?: with a citizenship attainment date dating (\\d+) days ago)?$")
    public void personIsADualCitizen(String personName, Integer daysAgo) {
        PersonId personId = testContext.get(personName);

        Nationality curNationality = nationalityRepo.findNationalityByPerson(personId.getPerson());
        testAssert.assertEquals(NationalityEnum.DUAL_CITIZENSHIP, curNationality.getNationality(), "Person with "+personId.getNaturalId()+" is not a dual citizen!");

        if ( daysAgo != null ) {
            Timestamp expectedCitizenshipAttainmentDate = dateUtils.beginningOfDayToTimestamp(dateUtils.daysBeforeToday(daysAgo));
            testAssert.assertEquals(expectedCitizenshipAttainmentDate, curNationality.getCitizenshipAttainmentDate(), "Person with "+personId.getNaturalId()+" does not have a citizenship attainment date of "+expectedCitizenshipAttainmentDate);
        }
    }

    @And("^mha states that ([a-z_]+) is a dual citizen since (\\d+) days ago$")
    public void mhaStatesThatPersonIsADualCitizenSinceDaysAgo(String personName, int daysAgo) {
        PersonId personId = testContext.get(personName);

        Nationality prevNationality = nationalityRepo.findNationalityByPerson(personId.getPerson());
        Date validTill = new Date(dateUtils.endOfDayToTimestamp(dateUtils.daysBeforeToday(daysAgo).minusDays(1L)).getTime());
        nationalityRepo.updateValidTill(validTill, prevNationality.getId());

        Batch batch = Batch.createCompleted();
        BiTemporalData biTemporalData = BiTemporalData.create(dateUtils.beginningOfDayToTimestamp(dateUtils.daysBeforeToday(daysAgo)), Timestamp.valueOf(Constants.INFINITE_LOCAL_DATE_TIME));
        Nationality curNationality = Nationality.create(batch, personId.getPerson(), NationalityEnum.DUAL_CITIZENSHIP, biTemporalData, prevNationality.getCitizenshipAttainmentDate(), prevNationality.getCitizenshipRenunciationDate());

        batchRepo.save(batch);
        nationalityRepo.save(curNationality);
    }

    @And("^mha sends a dual citizen file without ([a-z_]+) in it (\\d+) days ago$")
    public void mhaSendsADualCitizenFileWithoutPersonInItDaysAgo(String personName, int daysAgo) {
        LocalDate runDate = dateUtils.daysBeforeToday(daysAgo);
        batchFileDataWriter.begin(mhaDualCitizenFileDataPrep.generateSingleHeader(runDate), FileTypeEnum.MHA_DUAL_CITIZEN, null);
        //Putting a random valid DC into the file to prevent failure
        mhaDualCitizenFileDataPrep.createListOfNewDualCitizens(1);
        batchFileDataWriter.end();
    }

    @Then("^([a-z_]+) is a singaporean from (\\d+) days ago$")
    public void personIsASingaporeanFromDaysAgo(String personName, int daysAgo) {
        PersonId personId = testContext.get(personName);

        Timestamp validFrom = dateUtils.beginningOfDayToTimestamp(dateUtils.daysBeforeToday(daysAgo));
        Nationality curNationality = nationalityRepo.findNationalityByPerson(personId.getPerson());
        testAssert.assertEquals(NationalityEnum.SINGAPORE_CITIZEN, curNationality.getNationality(), "Person with "+personId.getNaturalId()+" is not a Singaporean!");
        testAssert.assertEquals(validFrom, curNationality.getBiTemporalData().getBusinessTemporalData().getValidFrom(), "Person with "+personId.getNaturalId()+" did not become a Singaporean on "+validFrom.toString());

        Date validTill = new Date(dateUtils.beginningOfDayToTimestamp(dateUtils.daysBeforeToday(daysAgo).minusDays(1L)).getTime());
        Nationality prevNationality = nationalityRepo.findNationalityByPerson(personId.getPerson(), validTill);
        testAssert.assertEquals(dateUtils.endOfDayToTimestamp(dateUtils.daysBeforeToday(daysAgo).minusDays(1L)), prevNationality.getBiTemporalData().getBusinessTemporalData().getValidTill(), "Person with "+personId.getNaturalId()+" did not end his/her previous nationality on "+validTill.toString());
    }

    @Given("^the mha dual citizen file contains invalid date of run and date of run is (EMPTY|EMPTY_SPACE|INVALID_FORMAT|FUTURE_DATE)$")
    public void theMhaDualCitizenFileContainsInvalidDateOfRunAndDateOfRunIs(InvalidDateOfRunEnum type) throws IOException{
        log.info("Creating an invalid Date of Run ({}) header in MHA Dual Citizen file", type);

        List<String> listOfIdentifiersToWriteToFile = new ArrayList<>();
        List<String> body = new ArrayList<>();

        String validNric = mhaDualCitizenFileDataPrep.createValidNric();
        body.add(validNric);

        listOfIdentifiersToWriteToFile.add(0, mhaDualCitizenFileDataPrep.generateInvalidSingleHeader(type));
        listOfIdentifiersToWriteToFile.addAll(body);
        listOfIdentifiersToWriteToFile.add(String.valueOf(body.size()));

        batchFileCreator.writeToFile(FileTypeEnum.MHA_DUAL_CITIZEN.getValue().toLowerCase(), listOfIdentifiersToWriteToFile);
        testContext.set("validNric", validNric);
    }

    @Then("I verify that there is an error message for wrong header length")
    public void iVerifyThatThereIsAnErrorMessageForWrongHeaderLength() {
        log.info("Verifying that there is an error message for wrong header length");
        FileReceived fileReceived = testContext.get("fileReceived");
        Batch batch = batchRepo.findFirstByFileReceivedOrderByCreatedAtDesc(fileReceived);
        List<ErrorMessage> errorMessages = errorMessageRepo.findByBatch(batch);
        testAssert.assertEquals(true, errorMessages.stream().anyMatch(
            errorMessage -> errorMessage.getMessage()
                .contains(ErrorMessageConstants.HEADER_LENGTH_ERROR)),
                "No invalid Date of Run error message found.");
    }

    @Then("I verify that there is an error message for wrong date format")
    public void iVerifyThatThereIsAnErrorMessageForWrongDateFormat() {
        log.info("Verifying that there is an error message for wrong date format");
        FileReceived fileReceived = testContext.get("fileReceived");
        Batch batch = batchRepo.findFirstByFileReceivedOrderByCreatedAtDesc(fileReceived);
        List<ErrorMessage> errorMessages = errorMessageRepo.findByBatch(batch);
        testAssert.assertEquals(true, errorMessages.stream().anyMatch(
            errorMessage -> errorMessage.getMessage()
                .contains("Must be in yyyyMMdd date format.")),
                "No invalid Date of Run error message found.");
    }

    @Then("I verify that there is an error message for future date")
    public void iVerifyThatThereIsAnErrorMessageForFutureDate() {
        log.info("Verifying that there is an error message for future date");
        FileReceived fileReceived = testContext.get("fileReceived");
        Batch batch = batchRepo.findFirstByFileReceivedOrderByCreatedAtDesc(fileReceived);
        List<ErrorMessage> errorMessages = errorMessageRepo.findByBatch(batch);
        testAssert.assertEquals(true, errorMessages.stream().anyMatch(
            errorMessage -> errorMessage.getMessage()
                .contains(ErrorMessageConstants.EXTRACTION_DATE_AFTER_FILE_RECEIVED_DATE)),
                "No invalid Date of Run error message found.");
    }

    @Given("^the mha dual citizen file has (EMPTY|EMPTY_SPACE|INVALID|SHORT|S555|S888) nric$")
    public void theMhaDualCitizenFileHasAnNric(InvalidNricEnum type) throws IOException {
        log.info("Creating an invalid NRIC entry ({}) in MHA Dual Citizen file", type);
        List<String> listOfIdentifiersToWriteToFile = new ArrayList<>();
        List<String> body = new ArrayList<>();

        String invalidNric = mhaDualCitizenFileDataPrep.generateInvalidNric(type);
        body.add(invalidNric);

        listOfIdentifiersToWriteToFile.add(0, mhaDualCitizenFileDataPrep.generateSingleHeader());
        listOfIdentifiersToWriteToFile.addAll(body);
        listOfIdentifiersToWriteToFile.add(String.valueOf(body.size()));

        batchFileCreator.writeToFile(FileTypeEnum.MHA_DUAL_CITIZEN.getValue().toLowerCase(), listOfIdentifiersToWriteToFile);
        testContext.set("invalidNric", invalidNric);
    }

    @Then("I verify that the is an error message for wrong body length")
    public void iVerifyThatTheIsAnErrorMessageForWrongBodyLength() {
        log.info("Verifying that there is an error message for wrong body length");
        FileReceived fileReceived = testContext.get("fileReceived");
        Batch batch = batchRepo.findFirstByFileReceivedOrderByCreatedAtDesc(fileReceived);
        List<ErrorMessage> errorMessages = errorMessageRepo.findByBatch(batch);
        testAssert.assertEquals(true, errorMessages.stream().anyMatch(
            errorMessage -> errorMessage.getMessage()
                .contains(ErrorMessageConstants.BODY_LENGTH_ERROR)),
                "No invalid NRIC error message found.");
    }

    @Then("I verify that the is an error message for null or blank nric")
    public void iVerifyThatTheIsAnErrorMessageForNullOrBlankNric() {
        log.info("Verifying that there is an error message for null or blank nric");
        FileReceived fileReceived = testContext.get("fileReceived");
        Batch batch = batchRepo.findFirstByFileReceivedOrderByCreatedAtDesc(fileReceived);
        List<ErrorMessage> errorMessages = errorMessageRepo.findByBatch(batch);
        testAssert.assertEquals(true, errorMessages.stream().anyMatch(
            errorMessage -> errorMessage.getMessage()
                .contains(ErrorMessageConstants.NRIC_IS_BLANK)),
                "No invalid NRIC error message found.");
    }

    @Then("I verify that there is an error message for invalid nric")
    public void iVerifyThatThereIsAnErrorMessageForInvalidNric() {
        log.info("Verifying that there is an error message for invalid nric");
        FileReceived fileReceived = testContext.get("fileReceived");
        Batch batch = batchRepo.findFirstByFileReceivedOrderByCreatedAtDesc(fileReceived);
        List<ErrorMessage> errorMessages = errorMessageRepo.findByBatch(batch);
        testAssert.assertEquals(true, errorMessages.stream().anyMatch(
            errorMessage -> errorMessage.getMessage()
                .contains(ErrorMessageConstants.MUST_BE_VALID_NRIC_IN_VALID_FORMAT)),
                "No invalid NRIC error message found!");
    }

    @Then("I verify that there is a warning message for duplicate nric")
    public void iVerifyThatThereIsAWarningMessageForDuplicateNric() {
        log.info("Verifying that there is a warning message for duplicate nric");
        FileReceived fileReceived = testContext.get("fileReceived");
        Batch batch = batchRepo.findFirstByFileReceivedOrderByCreatedAtDesc(fileReceived);
        List<ErrorMessage> errorMessages = errorMessageRepo.findByBatch(batch);
        testAssert.assertEquals(true, errorMessages.stream().anyMatch(
            errorMessage -> errorMessage.getMessage()
                .contains(ErrorMessageConstants.COMPLETELY_DUPLICATE_RECORD_FOUND_ERROR_MESSAGE)),
                "No duplicate NRIC warning message found!");
    }

    @Given("^([a-z_]+) who is (\\d+) years old converted to a dual citizen (\\d+) days ago$")
    public void personConvertedToADualCitizenDaysAgo(String personName, int age, int daysAgo) {
        LocalDate birthDate = dateUtils.yearsBeforeToday(age);
        LocalDate runDate = dateUtils.daysBeforeToday(daysAgo);
        PersonId personId = personFactory.createSCTurnDualCitizen(personName, birthDate, runDate);
        testContext.set(personName, personId);
    }

    @And("^MHA dual citizen file contains ([a-z_]+) nric$")
    public void mhaDualCitizenFileContainsNric(String personName) {
        batchFileDataWriter.begin(mhaDualCitizenFileDataPrep.generateSingleHeader(LocalDate.now()), FileTypeEnum.MHA_DUAL_CITIZEN, null);
        PersonId personId = testContext.get(personName);
        MhaDualCitizenFileEntry mhaDualCitizenFileEntry = MhaDualCitizenFileEntry.builder().identiifer(personId.getNaturalId()).build();
        testContext.set("nric", personId.getNaturalId());
        batchFileDataWriter.chunkOrWrite(mhaDualCitizenFileEntry.toString());
        batchFileDataWriter.end();
    }

    @Then("^I verify that ([a-z_]+) nationality was not updated in datasource db$")
    public void iVerifyThatNationalityIsNotUpdateInDatasourceDb(String personName) {
        log.info("Verifying that the person nationality was not updated");
        FileReceived fileReceived = testContext.get("fileReceived");
        Batch batch = batchRepo.findFirstByFileReceivedOrderByCreatedAtDesc(fileReceived);
        PersonId personId = testContext.get(personName);
        Nationality nationality = nationalityRepo.findNationalityByPerson(personId.getPerson());
        testAssert.assertNotEquals(nationality.getBatch().getId(), batch.getId(),
            "Person nationality was updated by this batch");
    }

    @And("MHA dual citizen file does not contains person nric")
    public void mhaDualCitizenFileDoesNotContainsNric() {
        batchFileDataWriter.begin(mhaDualCitizenFileDataPrep.generateSingleHeader(LocalDate.now()), FileTypeEnum.MHA_DUAL_CITIZEN, null);
        mhaDualCitizenFileDataPrep.createListOfNewDualCitizens(1);
        batchFileDataWriter.end();
    }

    @Then("^I verify that ([a-z_]+) nationality was updated in datasource db$")
    public void iVerifyThatJohnNationalityWasUpdatedInDatasourceDb(String personName) {
        log.info("Verifying that the person nationality was updated");
        FileReceived fileReceived = testContext.get("fileReceived");
        Batch batch = batchRepo.findFirstByFileReceivedOrderByCreatedAtDesc(fileReceived);
        log.info("Batch Id: {}", batch.getId());
        PersonId personId = testContext.get(personName);
        Nationality nationality = nationalityRepo.findNationalityByPerson(personId.getPerson());
        log.info("Nationality Id: {}", nationality.getId());
        testAssert.assertEquals(nationality.getBatch().getId(), batch.getId(),
            "Person nationality was updated by this batch");
        testAssert.assertEquals(nationality.getNationality(), NationalityEnum.SINGAPORE_CITIZEN,
            "Person was not updated to a Singapore Citizen by this batch");
    }
}
