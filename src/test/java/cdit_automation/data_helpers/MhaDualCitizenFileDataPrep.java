package cdit_automation.data_helpers;

import cdit_automation.configuration.StepDefLevelTestContext;
import cdit_automation.models.PersonId;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MhaDualCitizenFileDataPrep extends BatchFileDataPrep {

    public MhaDualCitizenFileDataPrep() {

    }

    public List<String> bodyCreator(List<Map<String, String>> list, StepDefLevelTestContext testContext) {
        List<String> listOfNewDCs = createListOfNewDualCitizens(parseStringSize(list.get(0).get("NewDualCitizensInFile")));
        List<String> listOfExistingDCs = createListOfExistingDualCitizens(parseStringSize(list.get(0).get("ExistingDualCitizensInFile")));
        List<String> listOfExpiredDCs = createListOfExistingDualCitizens(parseStringSize(list.get(0).get("ExpiredDualCitizens")));
        List<String> listOfDuplicatedNrics = createDuplicatedValidNricEntries(parseStringSize(list.get(0).get("DuplicatedNrics")));
        List<String> listOfInvalidNrics = createListOfInvalidNrics(parseStringSize(list.get(0).get("InvalidNrics")));

        testContext.set("listOfNewDCs", listOfNewDCs);
        testContext.set("listOfExistingDCs", listOfExistingDCs);
        testContext.set("listOfExpiredDCs", listOfExpiredDCs);
        testContext.set("listOfDuplicatedNrics", listOfDuplicatedNrics);
        testContext.set("listOfInvalidNrics", listOfInvalidNrics);

        List<String> listOfNricsForBatchFile = Stream.of(listOfNewDCs, listOfExistingDCs, listOfDuplicatedNrics, listOfInvalidNrics).flatMap(Collection::stream).collect(Collectors.toList());

        return listOfNricsForBatchFile;
    }

    public List<String> createDuplicatedValidNricEntries(int numOfDuplicatedEntries) {
        List<String> duplicatedList = new ArrayList<>();
        for ( int i = 0 ; i < numOfDuplicatedEntries ; i++ ) {
            PersonId personId = personIdService.createNewSCPersonId();
            duplicatedList.addAll(new ArrayList<>(Arrays.asList(personId.getNaturalId(), personId.getNaturalId())));
        }

        return duplicatedList;
    }

    public List<String> createDuplicatedValidNricEntries() {
        PersonId personId = personIdService.createNewSCPersonId();
        List<String> duplicateedList = new ArrayList<>(Arrays.asList(personId.getNaturalId(), personId.getNaturalId()));

        return duplicateedList;
    }

    public List<String> createListOfNewDualCitizens(@Positive int numOfNewDCs) {
        List<String> listOfNewDCs = new ArrayList<>();
        for ( int i = 0 ; i < numOfNewDCs ; i++ ) {
            PersonId personId = personIdService.createNewSCPersonId();
            listOfNewDCs.add(personId.getNaturalId());
        }
        return listOfNewDCs;
    }

    public List<String> createListOfExistingDualCitizens(@Positive int numOfExistingDCs) {
        List<String> listOfExistingDCs = new ArrayList<>();
        for ( int i = 0 ; i < numOfExistingDCs ; i++ ) {
            PersonId existingDC = personIdService.createDualCitizen();
            listOfExistingDCs.add(existingDC.getNaturalId());
        }
        return listOfExistingDCs;
    }
}
