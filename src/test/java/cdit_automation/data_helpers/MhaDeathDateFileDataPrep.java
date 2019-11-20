package cdit_automation.data_helpers;

import cdit_automation.data_setup.Phaker;
import cdit_automation.models.PersonDetail;
import cdit_automation.models.PersonId;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MhaDeathDateFileDataPrep extends BatchFileDataPrep {

    public List<String> createListWithInvalidNrics(int numOfInvalidNrics) {
        List<String> listOfInvalidNrics = new ArrayList<>();

        for ( int i = 0 ; i < numOfInvalidNrics ; i++ ) {
            listOfInvalidNrics.add(Phaker.invalidNric()+randomDeathDate());
        }

        return listOfInvalidNrics;
    }

    public List<String> createListOfDuplicatedEntries(int numOfDuplicatedEntries) {
        List<String> listOfDuplicatedEntries = new ArrayList<>();

        for ( int i = 0 ; i < numOfDuplicatedEntries ; i++ ) {
            String dupNric = Phaker.validNric();
            String randomDeathDate = randomDeathDate();
            listOfDuplicatedEntries.add(dupNric+randomDeathDate);
            listOfDuplicatedEntries.add(dupNric+randomDeathDate);
        }

        return listOfDuplicatedEntries;
    }

    public List<String> createListOfDuplicatedNricOnlyEntries(int numOfDuplicatedEntries) {
        List<String> listOfDuplicatedEntries = new ArrayList<>();

        for ( int i = 0 ; i < numOfDuplicatedEntries ; i++ ) {
            String dupNric = Phaker.validNric();
            listOfDuplicatedEntries.add(dupNric+randomDeathDate());
            listOfDuplicatedEntries.add(dupNric+randomDeathDate());
        }

        return listOfDuplicatedEntries;
    }

    public List<String> createListOfValidSCDeathCases(int numOfValidDeathCases, LocalDate fileReceivedDate) {
        List<String> listOfValidDeathCases = new ArrayList<>();

        for ( int i = 0 ; i < numOfValidDeathCases ; i++ ) {
            PersonId personId = personIdService.createNewSCPersonId();
            PersonDetail personDetail = personDetailRepo.findByPerson(personId.getPerson(), dateUtils.localDateToDate(dateUtils.now()));
            listOfValidDeathCases.add(personId.getNaturalId()+randomDeathDate(fileReceivedDate, personDetail.getDateOfBirth()));
        }

        return listOfValidDeathCases;
    }

    public List<String> createListOfValidPPDeathCases(int numOfValidDeathCases, LocalDate fileReceivedDate) {
        List<String> listOfValidDeathCases = new ArrayList<>();

        for ( int i = 0 ; i < numOfValidDeathCases ; i++ ) {
            PersonId personId = personIdService.createNewPPPersonId();
            PersonDetail personDetail = personDetailRepo.findByPerson(personId.getPerson(), dateUtils.localDateToDate(dateUtils.now()));
            listOfValidDeathCases.add(personId.getNaturalId()+randomDeathDate(fileReceivedDate, personDetail.getDateOfBirth()));
        }

        return listOfValidDeathCases;
    }

    public List<String> createListOfValidFRDeathCases(int numOfValidDeathCases, LocalDate fileReceivedDate) {
        List<String> listOfValidDeathCases = new ArrayList<>();

        for ( int i = 0 ; i < numOfValidDeathCases ; i++ ) {
            PersonId personId = personIdService.createNewFRPersonId();
            PersonDetail personDetail = personDetailRepo.findByPerson(personId.getPerson(), dateUtils.localDateToDate(dateUtils.now()));
            listOfValidDeathCases.add(personId.getNaturalId()+randomDeathDate(fileReceivedDate, personDetail.getDateOfBirth()));
        }

        return listOfValidDeathCases;
    }

    private String randomDeathDate(@NotNull LocalDate fileReceviedDate, @NotNull LocalDate birthDate) {
        LocalDate within30DaysAgo = dateUtils.daysBeforeDate(fileReceviedDate, 30L);
        LocalDate[] dates = new LocalDate[]{within30DaysAgo, birthDate};

        LocalDate maximumDate = Arrays.stream(dates).max(LocalDate::compareTo).get();

        return Phaker.validDateFromRange(maximumDate, dateUtils.now()).format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
    }

    private String randomDeathDate() {
        return Phaker.validPastDate().format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
    }
}
