package cdit_automation.step_definition;

import cdit_automation.asserts.Assert;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.exceptions.TestDataSetupErrorException;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.Batch;
import cdit_automation.models.FileDetail;
import cdit_automation.models.FileReceived;
import cdit_automation.models.PersonId;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
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
            listOfNewDCs.add(Phaker.validNric());
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
        File file = new File("src/test/resources/artifacts/mha_dual_citizen.txt");
        if ( !file.exists() ) {
            throw new TestFailException("No such file in path src/test/resources/artifacts/mha_dual_citizen.txt");
        }
        FileReceived fileReceived = FileReceived.builder()
                .receivedTimestamp(Timestamp.valueOf(LocalDateTime.now()))
                .filePath(file.getAbsolutePath())
                .fileDetail(fileDetail)
                .build();
        fileReceived = fileReceivedRepo.save(fileReceived);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        List<String> listOfIdentifiersToWriteToFile = Stream.of(listOfNewDCs, listOfExistingDCs).flatMap(Collection::stream).collect(Collectors.toList());

        String extractionDate = dateUtils.daysBeforeToday(5).format(dateTimeFormatter);
        String cutOffDate = extractionDate;
        listOfIdentifiersToWriteToFile.add(0, extractionDate+cutOffDate);
        listOfIdentifiersToWriteToFile.add(String.valueOf(listOfNewDCs.size()+listOfExistingDCs.size()));
        batchFileCreator.writeToFile("mha_dual_citizen.txt", listOfIdentifiersToWriteToFile);

        testContext.set("listOfIdentifiersToWriteToFile", listOfIdentifiersToWriteToFile);
        testContext.set("fileReceived", fileReceived);
    }
}
