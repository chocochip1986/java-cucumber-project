package cdit_automation.step_definition;

import cdit_automation.data_helpers.batch_entities.MhaDualCitizenFileEntry;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.exceptions.TestDataSetupErrorException;
import cdit_automation.models.Batch;
import cdit_automation.models.ErrorMessage;
import cdit_automation.models.FileReceived;
import cdit_automation.models.Nationality;
import cdit_automation.models.PersonId;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import java.io.IOException;
import java.util.ArrayList;
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
            hashOfDCs.put("personId"+String.valueOf(i), personId);
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
            PersonId expectedPersonId = hashOfDCs.get("personId"+String.valueOf(i));
            PersonId actualPersonId = personIdRepo.findByNaturalId(expectedPersonId.getNaturalId());

            testAssert.assertNotNull(actualPersonId, "No such person id db:" +expectedPersonId.getNaturalId());
            testAssert.assertEquals(expectedPersonId.getNaturalId(), actualPersonId.getNaturalId(), "No such person in db!");
        }
    }

    @Given("the mha dual citizen file has the following details:")
    public void thatTheMhaDualCitizenFileHasTheFollowingDetails(DataTable table) throws IOException {
        batchFileDataWriter.begin(mhaDualCitizenFileDataPrep.generateSingleHeader(), FileTypeEnum.MHA_DUAL_CITIZEN, null);
        List<Map<String, String>> list = table.asMaps(String.class, String.class);
        List<String> body = mhaDualCitizenFileDataPrep.bodyCreator(list, testContext);

        testContext.set("listOfIdentifiersToWriteToFile", body);
        batchFileDataWriter.end();
    }

    @Then("I verify that there are new dual citizen in datasource db")
    public void iVerifyThatThereAreNewDualCitizensInDatasourceDb() {
        List<String> listOfNewDCs = testContext.get("listOfNewDCs");

        log.info("Verifying that all new dual citizens are in datasource"+listOfNewDCs.toArray().toString());

        List<PersonId> personIds = Collections.emptyList();
        Date now = dateUtils.localDateToDate(dateUtils.now());
        for(String identifier : listOfNewDCs) {
            personIds = personIdRepo.findDualCitizen(identifier);

            testAssert.assertEquals(1, personIds.size(), "Person with nric: "+identifier+" is not a dual citizen!");

        }

    }

    @Then("I verify that no changes were made to existing dual citizens")
    public void iVerifyThatNoChangesWereMadeToExistingDualCitizens() {
        List<String> listOfExistingDCs = testContext.get("listOfExistingDCs");

        log.info("Verifying that no changes were made to all existing dual citizens who appear in the MHA dual citizen file.");

        List<PersonId> personIds;
        Date now = dateUtils.localDateToDate(dateUtils.now());
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
        Date now = dateUtils.localDateToDate(dateUtils.now());
        for(String identifier : listOfExpiredDCs) {
            personIds = personIdRepo.findDualCitizen(identifier);

            testAssert.assertEquals(0, personIds.size(), "Person with nric: "+identifier+" has an error!");

            PersonId personId = personIdRepo.findPersonByNaturalId(identifier);
            Nationality currentNationality = nationalityRepo.findNationalityByPerson(personId.getPerson());

            testAssert.assertEquals(NationalityEnum.SINGAPORE_CITIZEN, currentNationality.getNationality(), "Person with nric "+identifier+" is not converted to Singaporean!");
        }
    }

    @Given("the mha dual citizen file has an invalid nric")
    public void theMhaDualCitizenFileHasAnInvalidNric() throws IOException{
        log.info("Creating an invalid nric entry in MHA dual citizen file");
        String invalidNric = mhaDualCitizenFileDataPrep.createInvalidNric();

        List<String> listOfIdentifiersToWriteToFile = new ArrayList<>();
        List<String> body = new ArrayList<>();
        body.add(invalidNric);

        listOfIdentifiersToWriteToFile.add(0, mhaDualCitizenFileDataPrep.generateSingleHeader());
        listOfIdentifiersToWriteToFile.addAll(body);
        listOfIdentifiersToWriteToFile.add(String.valueOf(body.size()));

        batchFileCreator.writeToFile(FileTypeEnum.MHA_DUAL_CITIZEN.getValue().toLowerCase(), listOfIdentifiersToWriteToFile);

        testContext.set("invalidNric", invalidNric);
    }

    @Then("I verify that there is an error message for invalid nric")
    public void iVerifyThatThereIsAnErrorMessageForInvalidNric() {
        log.info("Verifying that there is an error message for invalid nric");

        FileReceived fileReceived = testContext.get("fileReceived");

        Batch batch = batchRepo.findFirstByFileReceivedOrderByCreatedAtDesc(fileReceived);

        List<ErrorMessage> errorMessages = errorMessageRepo.findByBatch(batch);

        testAssert.assertEquals(true,
                errorMessages.stream().anyMatch(errorMessage -> errorMessage.getMessage().matches(".*Must be valid NRIC in format.*")),
                "No invalid nric error message found!");
    }

    @Given("the mha dual citizen file have duplicate nric record")
    public void theMhaDualCitizenFileHaveDuplicateNricRecord() throws IOException {
        log.info("Creating an duplicate nric entry in MHA dual citizen file");

        List<String> listOfIdentifiersToWriteToFile = new ArrayList<>();
        List<String> body = mhaDualCitizenFileDataPrep.createDuplicatedValidNricEntries();

        listOfIdentifiersToWriteToFile.add(0, mhaDualCitizenFileDataPrep.generateDoubleHeader());
        listOfIdentifiersToWriteToFile.addAll(body);
        listOfIdentifiersToWriteToFile.add(String.valueOf(body.size()));

        batchFileCreator.writeToFile(FileTypeEnum.MHA_DUAL_CITIZEN.getValue().toLowerCase(), listOfIdentifiersToWriteToFile);

        testContext.set("duplicateNric", body.get(0));
    }

    @Given("the mha dual citizen file is empty")
    public void theMhaDualCitizenFileIsEmpty() throws IOException {
        batchFileDataWriter.begin(mhaDualCitizenFileDataPrep.generateSingleHeader(), FileTypeEnum.MHA_DUAL_CITIZEN, null);
        batchFileDataWriter.end();
    }

    @Given("the mha dual citizen file has a run date in the future")
    public void theMhaDualCitizenFileHasACutOffDateInTheFuture(DataTable dataTable) throws IOException {
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
}
