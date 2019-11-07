package cdit_automation.step_definition;

import cdit_automation.asserts.Assert;
import cdit_automation.constants.Constants;
import cdit_automation.constants.ErrorMessageConstants;
import cdit_automation.data_helpers.BatchFileCreator;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.enums.PersonIdTypeEnum;
import cdit_automation.enums.RestrictedEnum;
import cdit_automation.exceptions.TestDataSetupErrorException;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.Batch;
import cdit_automation.models.ErrorMessage;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import cdit_automation.models.Nationality;
import cdit_automation.models.Person;
import cdit_automation.models.PersonId;
import cdit_automation.utilities.DateUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Ignore
public class MhaDualCitizenSteps extends AbstractSteps {

    @Given("^there (?:is|are) (\\d+) existing dual citizen(?:s)?$")
    public void createExistingDualCitizen(int numOfDualCitizens) {
        log.info("Creating "+numOfDualCitizens+" existing dual citizens");
        Map<String, PersonId> hashOfDCs = new HashMap<>();
        for ( int i = 0 ; i < numOfDualCitizens ; i++ ) {
            PersonId personId = personIdService.createDualCitizen();
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

            Assert.assertNotNull(actualPersonId, "No such person id db:" +expectedPersonId.getNaturalId());
            Assert.assertTrue(expectedPersonId.getNaturalId(), actualPersonId.getNaturalId(), "No such person in db!");
        }
    }

    @Given("the mha dual citizen file has the following details:")
    public void thatTheMhaDualCitizenFileHasTheFollowingDetails(DataTable table) throws IOException {
        List<Map<String, String>> list = table.asMaps(String.class, String.class);
        List<String> listOfNewDCs = new ArrayList<>();
        List<String> listOfExistingDCs = new ArrayList<>();
        List<String> listOfExpiredDCs = new ArrayList<>();
        for ( int i = 0 ; i < Integer.valueOf(list.get(0).get("NewDualCitizensInFile")) ; i++ ) {
            PersonId personId = personIdService.createNewSCPersonId();
            listOfNewDCs.add(personId.getNaturalId());
        }

        testContext.set("listOfNewDCs", listOfNewDCs);

        for ( int i = 0 ; i < Integer.valueOf(list.get(0).get("ExistingDualCitizensInFile")) ; i++ ) {
            PersonId existingDC = personIdService.createDualCitizen();
            listOfExistingDCs.add(existingDC.getNaturalId());
        }

        testContext.set("listOfExistingDCs", listOfNewDCs);

        for ( int i = 0 ; i < Integer.valueOf(list.get(0).get("ExpiredDualCitizens")) ; i++ ) {
            PersonId expiredDC = personIdService.createDualCitizen();
            listOfExpiredDCs.add(expiredDC.getNaturalId());
        }

        testContext.set("listOfExpiredDCs", listOfExpiredDCs);

        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_DUAL_CITIZEN);
        testContext.set("fileReceived", batchFileCreator.fileCreator(fileDetail, "mha_dual_citizen"));

        List<String> listOfIdentifiersToWriteToFile = Stream.of(listOfNewDCs, listOfExistingDCs).flatMap(Collection::stream).collect(Collectors.toList());

        listOfIdentifiersToWriteToFile.add(0, batchFileCreator.generateDoubleHeader());
        listOfIdentifiersToWriteToFile.add(String.valueOf(listOfNewDCs.size()+listOfExistingDCs.size()));
        batchFileCreator.writeToFile("mha_dual_citizen.txt", listOfIdentifiersToWriteToFile);

        testContext.set("listOfIdentifiersToWriteToFile", listOfIdentifiersToWriteToFile);
    }

    @Then("I verify that there are new dual citizen in datasource db")
    public void iVerifyThatThereAreNewDualCitizensInDatasourceDb() {
        List<String> listOfNewDCs = testContext.get("listOfNewDCs");

        log.info("Verifying that all new dual citizens are in datasource"+listOfNewDCs.toArray().toString());

        List<PersonId> personIds = Collections.emptyList();
        Date now = dateUtils.localDateToDate(dateUtils.now());
        for(String identifier : listOfNewDCs) {
            personIds = personIdRepo.findDualCitizen(identifier, now, now);

            Assert.assertEquals(1, personIds.size(), "Person with nric: "+identifier+" is not a dual citizen!");

        }

    }

    @Then("I verify that no changes were made to existing dual citizens")
    public void iVerifyThatNoChangesWereMadeToExistingDualCitizens() {
        List<String> listOfExistingDCs = testContext.get("listOfExistingDCs");

        log.info("Verifying that no changes were made to all existing dual citizens who appear in the MHA dual citizen file.");

        List<PersonId> personIds;
        Date now = dateUtils.localDateToDate(dateUtils.now());
        for(String identifier : listOfExistingDCs) {
            personIds = personIdRepo.findDualCitizen(identifier, now, now);

            Assert.assertEquals(1, personIds.size(), "Person with nric: "+identifier+" has been modified!");
        }
    }


    @Then("I verify that the dual citizens who are not in the file will be Singaporeans")
    public void iVerifyThatTheDualCitizensWhoAreNotInTheFileWillBeSingaporeans() {
        List<String> listOfExpiredDCs = testContext.get("listOfExpiredDCs");

        log.info("Verifying that existing dual citizens who did not appear in the file will become Singaporeans");

        List<PersonId> personIds;
        Date now = dateUtils.localDateToDate(dateUtils.now());
        for(String identifier : listOfExpiredDCs) {
            personIds = personIdRepo.findDualCitizen(identifier, now, now);

            Assert.assertEquals(0, personIds.size(), "Person with nric: "+identifier+" has an error!");

            PersonId personId = personIdRepo.findCurrentPersonIdByIdentifier(identifier, PersonIdTypeEnum.NRIC, now);
            Nationality currentNationality = nationalityRepo.findCurrentNationalityByPerson(personId.getPerson(), now);

            Assert.assertEquals(NationalityEnum.SINGAPORE_CITIZEN, currentNationality.getNationality(), "Person with nric "+identifier+" is not converted to Singaporean!");
        }
    }

    @Given("the mha dual citizen file has an invalid nric")
    public void theMhaDualCitizenFileHasAnInvalidNric() throws IOException{
        log.info("Creating an invalid nric entry in MHA dual citizen file");
        String invalidNric = "A1234567C";

        List<String> listOfIdentifiersToWriteToFile = new ArrayList<>();
        List<String> body = new ArrayList<>();
        body.add(invalidNric);

        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_DUAL_CITIZEN);
        testContext.set("fileReceived", batchFileCreator.fileCreator(fileDetail, "mha_dual_citizen"));

        listOfIdentifiersToWriteToFile.add(0, batchFileCreator.generateDoubleHeader());
        listOfIdentifiersToWriteToFile.addAll(body);
        listOfIdentifiersToWriteToFile.add(String.valueOf(body.size()));

        batchFileCreator.writeToFile("mha_dual_citizen.txt", listOfIdentifiersToWriteToFile);

        testContext.set("invalidNric", invalidNric);
    }

    @Then("I verify that there is an error message for invalid nric")
    public void iVerifyThatThereIsAnErrorMessageForInvalidNric() {
        log.info("Verifying that there is an error message for invalid nric");

        FileReceived fileReceived = testContext.get("fileReceived");

        List<Batch> batches = batchRepo.findByFileReceivedOrderByCreatedAtDesc(fileReceived);
        Batch batch = batches.get(0);

        List<ErrorMessage> errorMessages = errorMessageRepo.findByBatch(batch);

        Assert.assertEquals(true, errorMessages.stream().anyMatch(errorMessage -> errorMessage.getMessage().equals(ErrorMessageConstants.INVALID_NRIC_FORMAT)), "No invalid nric error message found!");
    }
}