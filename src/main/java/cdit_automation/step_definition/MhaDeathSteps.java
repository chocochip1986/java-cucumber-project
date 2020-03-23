package cdit_automation.step_definition;

import cdit_automation.constants.ErrorMessageConstants;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.models.Batch;
import cdit_automation.models.DeathDateValidated;
import cdit_automation.models.ErrorMessage;
import cdit_automation.models.FileReceived;
import cdit_automation.models.PersonDetail;
import cdit_automation.models.PersonId;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class MhaDeathSteps extends AbstractSteps {
    @Given("^the mha death file is empty$")
    public void theMhaDeathFileIsEmpty() throws IOException {
        batchFileDataWriter.begin(mhaChangePersonDetailsDataPrep.generateSingleHeader(), FileTypeEnum.MHA_DEATH_DATE, null);
        batchFileDataWriter.end();
    }

    @Given("^the mha death file has the following details:$")
    public void theMhaDeathFileHasTheFollowingDetails(DataTable table) throws IOException {
        batchFileDataWriter.begin(mhaChangePersonDetailsDataPrep.generateSingleHeader(), FileTypeEnum.MHA_DEATH_DATE, null);
        Timestamp receivedTimestamp = dateUtils.beginningOfDayToTimestamp(dateUtils.now());

        List<Map<String, String>> list = table.asMaps(String.class, String.class);
        mhaDeathDateFileDataPrep.createBodyOfTestScenarios(list, testContext, receivedTimestamp);

        batchFileDataWriter.end();
        testContext.set("receivedTimestamp", receivedTimestamp);
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

            testAssert.assertNotNull(personDetail, "No valid person detail record for: "+personId.getNaturalId());

            LocalDate expectedDeathDate = LocalDate.parse(listOfPeopleForValidation.get(i).substring(9), Phaker.DATETIME_FORMATTER_YYYYMMDD);

            testAssert.assertEquals(expectedDeathDate, personDetail.getDateOfDeath(), "Person with identifier, "+personId.getNaturalId()+", does not have the correct death date!");
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

            testAssert.assertNotNull(errorMessage, "No error messages at all!");
            testAssert.assertEquals(ErrorMessageConstants.DEATH_BEFORE_BIRTH, errorMessage.getMessage(), "Error message contains incorrect message!");
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

            testAssert.assertNotNull(errorMessage, "No error messages at all!");
            testAssert.assertEquals(ErrorMessageConstants.MAP_TO_PREPARED_DATA_ERROR, errorMessage.getMessage(), "Error message contains incorrect message!");
        }
    }
}
