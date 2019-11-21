package cdit_automation.step_definition;

import cdit_automation.asserts.Assert;
import cdit_automation.constants.ErrorMessageConstants;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.models.Batch;
import cdit_automation.models.DeathDateValidated;
import cdit_automation.models.ErrorMessage;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import cdit_automation.models.PersonDetail;
import cdit_automation.models.PersonId;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Ignore;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Ignore
public class MhaDeathSteps extends AbstractSteps {
    @Given("^the mha death file is empty$")
    public void theMhaDeathFileIsEmpty() throws IOException {
        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_DUAL_CITIZEN);
        testContext.set("fileReceived", batchFileCreator.fileCreator(fileDetail, "mha_death_date"));

        List<String> listOfIdentifiersToWriteToFile = new ArrayList<>();;
        List<String> body = Lists.emptyList();

        listOfIdentifiersToWriteToFile.add(mhaDualCitizenFileDataPrep.generateDoubleHeader());
        listOfIdentifiersToWriteToFile.addAll(body);
        listOfIdentifiersToWriteToFile.add(String.valueOf(body.size()));

        batchFileCreator.writeToFile("mha_death_date.txt", listOfIdentifiersToWriteToFile);
    }

    @Given("^the mha death file has the following details:$")
    public void theMhaDeathFileHasTheFollowingDetails(DataTable table) throws IOException {
        FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_DEATH_DATE);
        FileReceived fileReceived = batchFileCreator.fileCreator(fileDetail, "mha_death_date");
        testContext.set("fileReceived", fileReceived);

        List<Map<String, String>> list = table.asMaps(String.class, String.class);
        List<String> listOfInvalidNrics = mhaDeathDateFileDataPrep.createListWithInvalidNrics(parseStringSize(list.get(0).get("InvalidNrics")));
        List<String> listOfDuplicatedEntries = mhaDeathDateFileDataPrep.createListOfDuplicatedEntries(parseStringSize(list.get(0).get("DuplicatedEntries")));
        List<String> listOfDuplicatedNricOnlyEntries = mhaDeathDateFileDataPrep.createListOfDuplicatedNricOnlyEntries(parseStringSize(list.get(0).get("DuplicatedNricOnlyEntries")));
        List<String> listOfValidSCDeathCases = mhaDeathDateFileDataPrep.createListOfValidSCDeathCases(parseStringSize(list.get(0).get("ValidSCDeathCases")), fileReceived.getReceivedTimestamp().toLocalDateTime().toLocalDate());
        List<String> listOfValidPPDeathCases = mhaDeathDateFileDataPrep.createListOfValidPPDeathCases(parseStringSize(list.get(0).get("ValidPPDeathCases")), fileReceived.getReceivedTimestamp().toLocalDateTime().toLocalDate());
        List<String> listOfValidFRDeathCases = mhaDeathDateFileDataPrep.createListOfValidFRDeathCases(parseStringSize(list.get(0).get("ValidFRDeathCases")), fileReceived.getReceivedTimestamp().toLocalDateTime().toLocalDate());
        List<String> listOfPplDeathDateEarlierThanBirthDate = mhaDeathDateFileDataPrep.createListOfPplDeathDateEarlierThanBirthDate(parseStringSize(list.get(0).get("DeathDateEarlierThanBirthDate")));
        List<String> listOfPplWhoAreAlreadyDead = mhaDeathDateFileDataPrep.createListOfPplWhoAreAlreadyDead(parseStringSize(list.get(0).get("PplWhoAreAlreadyDead")), fileReceived.getReceivedTimestamp().toLocalDateTime().toLocalDate());
        List<String> listOfPplWithFutureDeathDates = mhaDeathDateFileDataPrep.createListOfPplWithFutureDeathDates(parseStringSize(list.get(0).get("PplWithFutureDeathDates")));

        testContext.set("listOfInvalidNrics", listOfInvalidNrics);
        testContext.set("listOfDuplicatedEntries", listOfDuplicatedEntries);
        testContext.set("listOfDuplicatedNricOnlyEntries", listOfDuplicatedNricOnlyEntries);
        testContext.set("listOfValidSCDeathCases", listOfValidSCDeathCases);
        testContext.set("listOfValidPPDeathCases", listOfValidPPDeathCases);
        testContext.set("listOfValidFRDeathCases", listOfValidFRDeathCases);
        testContext.set("listOfPplDeathDateEarlierThanBirthDate", listOfPplDeathDateEarlierThanBirthDate);
        testContext.set("listOfPplWhoAreAlreadyDead", listOfPplWhoAreAlreadyDead);
        testContext.set("listOfPplWithFutureDeathDates", listOfPplWithFutureDeathDates);

        List<String> listOfIdentifiersToWriteToFile = new ArrayList<>();;
        List<String> body = Stream.of(listOfInvalidNrics,
                listOfDuplicatedEntries,
                listOfDuplicatedNricOnlyEntries,
                listOfValidSCDeathCases,
                listOfValidPPDeathCases,
                listOfValidFRDeathCases,
                listOfPplWhoAreAlreadyDead,
                listOfPplWithFutureDeathDates,
                listOfPplDeathDateEarlierThanBirthDate).flatMap(Collection::stream).collect(Collectors.toList());

        listOfIdentifiersToWriteToFile.add(mhaDeathDateFileDataPrep.generateDoubleHeader());
        listOfIdentifiersToWriteToFile.addAll(body);
        listOfIdentifiersToWriteToFile.add(String.valueOf(body.size()));

        batchFileCreator.writeToFile("mha_death_date.txt", listOfIdentifiersToWriteToFile);

        testContext.set("listOfIdentifiersToWriteToFile", listOfIdentifiersToWriteToFile);
    }

    @Then("I verify that the people listed in the death file have the correct death dates")
    public void iVerifyThatThePeopleListedInTheDeathFileHaveTheCorrectDeathDates() {
        List<String> listOfValidSCs = testContext.get("listOfValidSCDeathCases");
        List<String> listOfValidPPs = testContext.get("listOfValidPPDeathCases");
        List<String> listOfValidFRs = testContext.get("listOfValidFRDeathCases");
        List<String> listOfPeopleForValidation = Stream.of(listOfValidSCs,
                listOfValidPPs,
                listOfValidFRs).flatMap(Collection::stream).collect(Collectors.toList());

        for ( int i = 0 ; i < listOfPeopleForValidation.size() ; i++ ) {
            String nric = listOfPeopleForValidation.get(i).substring(0,9);

            PersonId personId = personIdRepo.findByNaturalId(nric);
            PersonDetail personDetail = personDetailRepo.findByPerson(personId.getPerson());

            Assert.assertNotNull(personDetail, "No valid person detail record for: "+personId.getNaturalId());

            LocalDate expectedDeathDate = LocalDate.parse(listOfPeopleForValidation.get(i).substring(9), Phaker.DATETIME_FORMATTER_YYYYMMDD);

            Assert.assertEquals(expectedDeathDate, personDetail.getDateOfDeath(), "Person with identifier, "+personId.getNaturalId()+", does not have the correct death date!");
        }
    }

    @Then("^I verify that there is an error message for invalid death dates$")
    public void iVerifyThatTheseErroneousRecordsHaveTheErrorMessage() {
        log.info("Verifying that there is an error message for invalid death dates");

        FileReceived fileReceived = testContext.get("fileReceived");
        Batch batch = batchRepo.findByFileReceivedOrderByCreatedAtDesc(fileReceived);

        List<String> listOfPpl = testContext.get("listOfPplDeathDateEarlierThanBirthDate");
        for ( int i = 0 ; i < listOfPpl.size() ; i++ ) {
            String nric = listOfPpl.get(i).substring(0,9);
            DeathDateValidated deathDateValidated = deathDateValidatedRepo.findByNricAndBatch(nric, batch);
            ErrorMessage errorMessage = errorMessageRepo.findByValidatedIdAndValidatedType(deathDateValidated.getId(), ErrorMessage.ValidatedTypes.DEATH_DATE);

            Assert.assertNotNull(errorMessage, "No error messages at all!");
            Assert.assertEquals(ErrorMessageConstants.DEATH_BEFORE_BIRTH, errorMessage.getMessage(), "Error message contains incorrect message!");
        }
    }

    @Then("I verify that there is an error message for existing death case")
    public void iVerifyThatThereIsAnErrorMessageForExistingDeathCase() {
        log.info("Verify that there is an error message for existing death case");

        FileReceived fileReceived = testContext.get("fileReceived");
        Batch batch = batchRepo.findByFileReceivedOrderByCreatedAtDesc(fileReceived);

        List<String> listOfPpl = testContext.get("listOfPplWhoAreAlreadyDead");
        for ( int i = 0 ; i < listOfPpl.size() ; i++ ) {
            String nric = listOfPpl.get(i).substring(0,9);
            DeathDateValidated deathDateValidated = deathDateValidatedRepo.findByNricAndBatch(nric, batch);
            ErrorMessage errorMessage = errorMessageRepo.findByValidatedIdAndValidatedType(deathDateValidated.getId(), ErrorMessage.ValidatedTypes.DEATH_DATE);

            Assert.assertNotNull(errorMessage, "No error messages at all!");
            Assert.assertEquals(ErrorMessageConstants.MAP_TO_PREPARED_DATA_ERROR, errorMessage.getMessage(), "Error message contains incorrect message!");
        }
    }

    @Then("I verify that there is an error message for future death date case")
    public void iVerifyThatThereIsAnErrorMessageForFutureDeathDateCase() {
        log.info("Verify that there is an error message for future death date case");

        FileReceived fileReceived = testContext.get("fileReceived");
        Batch batch = batchRepo.findByFileReceivedOrderByCreatedAtDesc(fileReceived);

        List<String> listOfPpl = testContext.get("listOfPplWithFutureDeathDates");
        for ( int i = 0 ; i < listOfPpl.size() ; i++ ) {
            String nric = listOfPpl.get(i).substring(0,9);
            DeathDateValidated deathDateValidated = deathDateValidatedRepo.findByNricAndBatch(nric, batch);
            ErrorMessage errorMessage = errorMessageRepo.findByValidatedIdAndValidatedType(deathDateValidated.getId(), ErrorMessage.ValidatedTypes.DEATH_DATE);

            Assert.assertNotNull(errorMessage, "No error messages at all!");
            Assert.assertEquals(ErrorMessageConstants.MAP_TO_PREPARED_DATA_ERROR, errorMessage.getMessage(), "Error message contains incorrect message!");
        }
    }
}
