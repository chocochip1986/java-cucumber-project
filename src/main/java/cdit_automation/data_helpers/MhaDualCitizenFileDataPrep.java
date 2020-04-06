package cdit_automation.data_helpers;

import cdit_automation.configuration.StepDefLevelTestContext;
import cdit_automation.data_setup.Phaker;
import cdit_automation.models.PersonId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.constraints.Positive;
import org.springframework.stereotype.Component;

@Component
public class MhaDualCitizenFileDataPrep extends BatchFileDataPrep {

    public MhaDualCitizenFileDataPrep() {

    }

    public List<String> bodyCreator(List<Map<String, String>> list, StepDefLevelTestContext testContext) {
        List<String> listOfNewDCs = createListOfNewDualCitizens(parseStringSize(list.get(0).get("NewDualCitizensInFile")));
        List<String> listOfExistingDCs = createListOfExistingDualCitizens(parseStringSize(list.get(0).get("ExistingDualCitizensInFile")));
        List<String> listOfExpiredDCs = createListOfExpiredDualCitizens(parseStringSize(list.get(0).get("ExpiredDualCitizens")));
        List<String> listOfDuplicatedNrics = createDuplicatedValidNricEntries(parseStringSize(list.get(0).get("DuplicatedNrics")));
        List<String> listOfInvalidNrics = createListOfInvalidNrics(parseStringSize(list.get(0).get("InvalidNrics")));
        List<String> listOfNonExistentNrics = createListOfNonExistentNrics(parseStringSize(list.get(0).get("NonExistentNrics")));

        testContext.set("listOfNewDCs", listOfNewDCs);
        testContext.set("listOfExistingDCs", listOfExistingDCs);
        testContext.set("listOfExpiredDCs", listOfExpiredDCs);
        testContext.set("listOfDuplicatedNrics", listOfDuplicatedNrics);
        testContext.set("listOfInvalidNrics", listOfInvalidNrics);
        testContext.set("listOfNonExistentNrics", listOfNonExistentNrics);

        return Stream.of(listOfNewDCs, listOfExistingDCs, listOfDuplicatedNrics, listOfInvalidNrics, listOfNonExistentNrics).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public List<String> createListOfNonExistentNrics(int numOfNonExistentNrics) {
        List<String> nonExistentNricsList = new ArrayList<>();
        for ( int i = 0 ; i < numOfNonExistentNrics ; i++ ) {
            String nric = Phaker.validNric();
            nonExistentNricsList.add(nric);
            batchFileDataWriter.chunkOrWrite(nric);
        }
        return nonExistentNricsList;
    }

    public List<String> createDuplicatedValidNricEntries(int numOfDuplicatedEntries) {
        List<String> duplicatedList = new ArrayList<>();
        PersonId personId = personFactory.createNewSCPersonId();

        for ( int i = 0 ; i < numOfDuplicatedEntries ; i++ ) {
            duplicatedList.addAll(new ArrayList<>(Arrays.asList(personId.getNaturalId(), personId.getNaturalId())));
            batchFileDataWriter.chunkOrWrite(personId.getNaturalId());
        }

        return duplicatedList;
    }

    public List<String> createDuplicatedValidNricEntries() {
        PersonId personId = personFactory.createNewSCPersonId();
        List<String> duplicateedList = new ArrayList<>(Arrays.asList(personId.getNaturalId(), personId.getNaturalId()));
        batchFileDataWriter.chunkOrWrite(personId.getNaturalId());
        batchFileDataWriter.chunkOrWrite(personId.getNaturalId());

        return duplicateedList;
    }

    public String createNewDualCitizen(String nric) {
        batchFileDataWriter.chunkOrWrite(nric);
        return nric;
    }

    public List<String> createListOfNewDualCitizens(@Positive int numOfNewDCs) {
        List<String> listOfNewDCs = new ArrayList<>();
        for ( int i = 0 ; i < numOfNewDCs ; i++ ) {
            PersonId personId = personFactory.createNewSCPersonId();
            String inputLine = personId.getNaturalId();
            listOfNewDCs.add(inputLine);
            batchFileDataWriter.chunkOrWrite(inputLine);
        }
        return listOfNewDCs;
    }

    public List<String> createListOfExpiredDualCitizens(@Positive int numOfExistingDCs) {
        List<String> listOfExistingDCs = new ArrayList<>();
        for ( int i = 0 ; i < numOfExistingDCs ; i++ ) {
            PersonId existingDC = personFactory.createDualCitizen();
            listOfExistingDCs.add(existingDC.getNaturalId());
        }
        return listOfExistingDCs;
    }

    public List<String> createListOfExistingDualCitizens(@Positive int numOfExistingDCs) {
        List<String> listOfExistingDCs = new ArrayList<>();
        for ( int i = 0 ; i < numOfExistingDCs ; i++ ) {
            PersonId existingDC = personFactory.createDualCitizen();
            String inputLine = existingDC.getNaturalId();
            listOfExistingDCs.add(inputLine);
            batchFileDataWriter.chunkOrWrite(inputLine);
        }
        return listOfExistingDCs;
    }

    public List<String> createListOfInvalidNrics(int size) {
        List<String> listOfInvalidNrics = new ArrayList<>();
        for ( int i = 0 ; i < size ; i++ ) {
            String inputLine = Phaker.invalidNric();
            batchFileDataWriter.chunkOrWrite(inputLine);
            listOfInvalidNrics.add(inputLine);
        }

        return listOfInvalidNrics;
    }
}
