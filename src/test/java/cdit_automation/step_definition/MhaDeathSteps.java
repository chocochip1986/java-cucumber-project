package cdit_automation.step_definition;

import cdit_automation.asserts.Assert;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import cdit_automation.models.PersonDetail;
import cdit_automation.models.PersonId;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.assertj.core.util.Lists;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        testContext.set("listOfInvalidNrics", listOfInvalidNrics);
        testContext.set("listOfDuplicatedEntries", listOfDuplicatedEntries);
        testContext.set("listOfDuplicatedNricOnlyEntries", listOfDuplicatedNricOnlyEntries);
        testContext.set("listOfValidSCDeathCases", listOfValidSCDeathCases);
        testContext.set("listOfValidPPDeathCases", listOfValidPPDeathCases);

        List<String> listOfIdentifiersToWriteToFile = new ArrayList<>();;
        List<String> body = Stream.of(listOfInvalidNrics,
                listOfDuplicatedEntries,
                listOfDuplicatedNricOnlyEntries,
                listOfValidSCDeathCases,
                listOfValidPPDeathCases).flatMap(Collection::stream).collect(Collectors.toList());

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
        List<String> listOfPeopleForValidation = Stream.of(listOfValidSCs, listOfValidPPs).flatMap(Collection::stream).collect(Collectors.toList());

        for ( int i = 0 ; i < listOfPeopleForValidation.size() ; i++ ) {
            String nric = listOfPeopleForValidation.get(i).substring(0,9);

            PersonId personId = personIdRepo.findByNaturalId(nric);
            PersonDetail personDetail = personDetailRepo.findByPerson(personId.getPerson(), dateUtils.localDateToDate(dateUtils.now()));

            Assert.assertNotNull(personDetail, "No valid person detail record for: "+personId.getNaturalId());

            LocalDate expectedDeathDate = LocalDate.parse(listOfPeopleForValidation.get(i).substring(9), Phaker.DATETIME_FORMATTER_YYYYMMDD);

            Assert.assertEquals(expectedDeathDate, personDetail.getDateOfDeath(), "Person with identifier, "+personId.getNaturalId()+", does not have the correct death date!");
        }
    }
}
