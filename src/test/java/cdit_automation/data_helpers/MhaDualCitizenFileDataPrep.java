package cdit_automation.data_helpers;

import cdit_automation.data_setup.Phaker;
import cdit_automation.models.PersonId;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class MhaDualCitizenFileDataPrep {
    @Autowired
    protected PersonIdService personIdService;

    public MhaDualCitizenFileDataPrep() {

    }

    public List<String> bodyCreator(List<Map<String, String>> listOfScenarios) {
        List<String> listOfNricsForBatchFile = new ArrayList<>();
        //New Dual Citizen scenario
        int numOfNewDCs = parseStringSize(listOfScenarios.get(0).get("NewDualCitizensInFile"));
        //New Existing Dual Citizen scenario
        int numOfExistingDCs = parseStringSize(listOfScenarios.get(0).get("ExistingDualCitizensInFile"));
        int numOfExpiredDCs = parseStringSize(listOfScenarios.get(0).get("ExpiredDualCitizens"));
        int numOfDuplicatedNrics = parseStringSize(listOfScenarios.get(0).get("DuplicatedNrics"));
        int numOfInvalidNrics = parseStringSize(listOfScenarios.get(0).get("InvalidNrics"));

        listOfNricsForBatchFile.addAll(createListOfNewDualCitizens(numOfNewDCs));
        listOfNricsForBatchFile.addAll(createListOfExistingDualCitizens(numOfExistingDCs));
        listOfNricsForBatchFile.addAll(createListOfExistingDualCitizens(numOfExpiredDCs));
        listOfNricsForBatchFile.addAll(createDuplicatedValidNricEntries(numOfDuplicatedNrics));
        listOfNricsForBatchFile.addAll(createListOfInvalidNrics(numOfInvalidNrics));

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

    public List<String> createListOfInvalidNrics(int size) {
        List<String> listOfInvalidNrics = new ArrayList<>();
        for ( int i = 0 ; i < size ; i++ ) {
            listOfInvalidNrics.add(Faker.instance().bothify("?#######?"));
        }

        return listOfInvalidNrics;
    }

    public String createInvalidNric() {
        return "A1234567F";
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

    private int parseStringSize(String size) {
        try {
            return Integer.valueOf(size);
        } catch ( NumberFormatException e ) {
            //Fail silently
        }
        return 0;
    }
}
