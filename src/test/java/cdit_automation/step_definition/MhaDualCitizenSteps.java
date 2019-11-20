package cdit_automation.step_definition;

import cdit_automation.asserts.Assert;
import cdit_automation.constants.ErrorMessageConstants;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.enums.PersonIdTypeEnum;
import cdit_automation.exceptions.TestDataSetupErrorException;
import cdit_automation.models.Batch;
import cdit_automation.models.ErrorMessage;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import cdit_automation.models.Nationality;
import cdit_automation.models.PersonId;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Ignore;

import java.io.IOException;
import java.util.ArrayList;
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
        List<String> listOfNewDCs = mhaDualCitizenFileDataPrep.createListOfNewDualCitizens(parseStringSize(list.get(0).get("NewDualCitizensInFile")));
        List<String> listOfExistingDCs = mhaDualCitizenFileDataPrep.createListOfExistingDualCitizens(parseStringSize(list.get(0).get("ExistingDualCitizensInFile")));
        List<String> listOfExpiredDCs = mhaDualCitizenFileDataPrep.createListOfExistingDualCitizens(parseStringSize(list.get(0).get("ExpiredDualCitizens")));
        List<String> listOfDuplicatedNrics = mhaDualCitizenFileDataPrep.createDuplicatedValidNricEntries(parseStringSize(list.get(0).get("DuplicatedNrics")));
        List<String> listOfInvalidNrics = mhaDualCitizenFileDataPrep.createListOfInvalidNrics(parseStringSize(list.get(0).get("InvalidNrics")));

        testContext.set("listOfNewDCs", listOfNewDCs);
        testContext.set("listOfExistingDCs", listOfNewDCs);
        testContext.set("listOfExpiredDCs", listOfExpiredDCs);
        testContext.set("listOfDuplicatedNrics", listOfDuplicatedNrics);
        testContext.set("listOfInvalidNrics", listOfInvalidNrics);

        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_DUAL_CITIZEN);
        testContext.set("fileReceived", batchFileCreator.fileCreator(fileDetail, "mha_dual_citizen"));

        List<String> listOfIdentifiersToWriteToFile = Stream.of(listOfNewDCs, listOfExistingDCs, listOfDuplicatedNrics, listOfInvalidNrics).flatMap(Collection::stream).collect(Collectors.toList());

        listOfIdentifiersToWriteToFile.add(0, mhaDualCitizenFileDataPrep.generateDoubleHeader());
        listOfIdentifiersToWriteToFile.add(String.valueOf(listOfNewDCs.size()+listOfExistingDCs.size()+listOfInvalidNrics.size()+listOfDuplicatedNrics.size()));
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
            personIds = personIdRepo.findDualCitizen(identifier);

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
            personIds = personIdRepo.findDualCitizen(identifier);

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
            personIds = personIdRepo.findDualCitizen(identifier);

            Assert.assertEquals(0, personIds.size(), "Person with nric: "+identifier+" has an error!");

            PersonId personId = personIdRepo.findPersonByNaturalId(identifier);
            Nationality currentNationality = nationalityRepo.findCurrentNationalityByPerson(personId.getPerson(), now);

            Assert.assertEquals(NationalityEnum.SINGAPORE_CITIZEN, currentNationality.getNationality(), "Person with nric "+identifier+" is not converted to Singaporean!");
        }
    }

    @Given("the mha dual citizen file has an invalid nric")
    public void theMhaDualCitizenFileHasAnInvalidNric() throws IOException{
        log.info("Creating an invalid nric entry in MHA dual citizen file");
        String invalidNric = mhaDualCitizenFileDataPrep.createInvalidNric();

        List<String> listOfIdentifiersToWriteToFile = new ArrayList<>();
        List<String> body = new ArrayList<>();
        body.add(invalidNric);

        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_DUAL_CITIZEN);
        testContext.set("fileReceived", batchFileCreator.fileCreator(fileDetail, "mha_dual_citizen"));

        listOfIdentifiersToWriteToFile.add(0, mhaDualCitizenFileDataPrep.generateDoubleHeader());
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

    @Given("the mha dual citizen file have duplicate nric record")
    public void theMhaDualCitizenFileHaveDuplicateNricRecord() throws IOException {
        log.info("Creating an duplicate nric entry in MHA dual citizen file");


        List<String> listOfIdentifiersToWriteToFile = new ArrayList<>();
        List<String> body = mhaDualCitizenFileDataPrep.createDuplicatedValidNricEntries();

        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_DUAL_CITIZEN);
        testContext.set("fileReceived", batchFileCreator.fileCreator(fileDetail, "mha_dual_citizen"));

        listOfIdentifiersToWriteToFile.add(0, mhaDualCitizenFileDataPrep.generateDoubleHeader());
        listOfIdentifiersToWriteToFile.addAll(body);
        listOfIdentifiersToWriteToFile.add(String.valueOf(body.size()));

        batchFileCreator.writeToFile("mha_dual_citizen.txt", listOfIdentifiersToWriteToFile);

        testContext.set("duplicateNric", body.get(0));
    }

    @Given("the mha dual citizen file is empty")
    public void theMhaDualCitizenFileIsEmpty() throws IOException {
        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_DUAL_CITIZEN);
        testContext.set("fileReceived", batchFileCreator.fileCreator(fileDetail, "mha_dual_citizen"));

        List<String> listOfIdentifiersToWriteToFile = new ArrayList<>();;
        List<String> body = Lists.emptyList();

        listOfIdentifiersToWriteToFile.add(mhaDualCitizenFileDataPrep.generateDoubleHeader());
        listOfIdentifiersToWriteToFile.addAll(body);
        listOfIdentifiersToWriteToFile.add(String.valueOf(body.size()));

        batchFileCreator.writeToFile("mha_dual_citizen.txt", listOfIdentifiersToWriteToFile);
    }

    @Given("the mha dual citizen file has a cut-off date in the future")
    public void theMhaDualCitizenFileHasACutOffDateInTheFuture() throws IOException {
        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_DUAL_CITIZEN);
        testContext.set("fileReceived", batchFileCreator.fileCreator(fileDetail, "mha_dual_citizen"));

        List<String> listOfIdentifiersToWriteToFile = new ArrayList<>();;
        List<String> body = Lists.emptyList();

        listOfIdentifiersToWriteToFile.add(mhaDualCitizenFileDataPrep.generateDoubleHeader(dateUtils.daysAfterToday(1), dateUtils.daysAfterToday(1)));
        listOfIdentifiersToWriteToFile.addAll(body);
        listOfIdentifiersToWriteToFile.add(String.valueOf(body.size()));

        batchFileCreator.writeToFile("mha_dual_citizen.txt", listOfIdentifiersToWriteToFile);
    }
}
