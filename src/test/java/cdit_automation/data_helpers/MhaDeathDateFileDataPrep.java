package cdit_automation.data_helpers;

import cdit_automation.configuration.StepDefLevelTestContext;
import cdit_automation.data_setup.Phaker;
import cdit_automation.models.FileReceived;
import cdit_automation.models.PersonDetail;
import cdit_automation.models.PersonId;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MhaDeathDateFileDataPrep extends BatchFileDataPrep {

    public List<String> createBodyOfTestScenarios(List<Map<String, String>> list, StepDefLevelTestContext testContext, FileReceived fileReceived) {
        List<String> listOfInvalidNrics = createListWithInvalidNrics(parseStringSize(list.get(0).get("InvalidNrics")));
        List<String> listOfDuplicatedEntries = createListOfDuplicatedEntries(parseStringSize(list.get(0).get("DuplicatedEntries")));
        List<String> listOfDuplicatedNricOnlyEntries = createListOfDuplicatedNricOnlyEntries(parseStringSize(list.get(0).get("DuplicatedNricOnlyEntries")));
        List<String> listOfValidSCDeathCases = createListOfValidSCDeathCases(parseStringSize(list.get(0).get("ValidSCDeathCases")), fileReceived.getReceivedTimestamp().toLocalDateTime().toLocalDate());
        List<String> listOfValidPPDeathCases = createListOfValidPPDeathCases(parseStringSize(list.get(0).get("ValidPPDeathCases")), fileReceived.getReceivedTimestamp().toLocalDateTime().toLocalDate());
        List<String> listOfValidFRDeathCases = createListOfValidFRDeathCases(parseStringSize(list.get(0).get("ValidFRDeathCases")), fileReceived.getReceivedTimestamp().toLocalDateTime().toLocalDate());
        List<String> listOfPplDeathDateEarlierThanBirthDate = createListOfPplDeathDateEarlierThanBirthDate(parseStringSize(list.get(0).get("DeathDateEarlierThanBirthDate")));
        List<String> listOfPplWhoAreAlreadyDead = createListOfPplWhoAreAlreadyDead(parseStringSize(list.get(0).get("PplWhoAreAlreadyDead")), fileReceived.getReceivedTimestamp().toLocalDateTime().toLocalDate());
        List<String> listOfPplWithFutureDeathDates = createListOfPplWithFutureDeathDates(parseStringSize(list.get(0).get("PplWithFutureDeathDates")));

        testContext.set("listOfInvalidNrics", listOfInvalidNrics);
        testContext.set("listOfDuplicatedEntries", listOfDuplicatedEntries);
        testContext.set("listOfDuplicatedNricOnlyEntries", listOfDuplicatedNricOnlyEntries);
        testContext.set("listOfValidSCDeathCases", listOfValidSCDeathCases);
        testContext.set("listOfValidPPDeathCases", listOfValidPPDeathCases);
        testContext.set("listOfValidFRDeathCases", listOfValidFRDeathCases);
        testContext.set("listOfPplDeathDateEarlierThanBirthDate", listOfPplDeathDateEarlierThanBirthDate);
        testContext.set("listOfPplWhoAreAlreadyDead", listOfPplWhoAreAlreadyDead);
        testContext.set("listOfPplWithFutureDeathDates", listOfPplWithFutureDeathDates);

        List<String> body = Stream.of(listOfInvalidNrics,
                listOfDuplicatedEntries,
                listOfDuplicatedNricOnlyEntries,
                listOfValidSCDeathCases,
                listOfValidPPDeathCases,
                listOfValidFRDeathCases,
                listOfPplWhoAreAlreadyDead,
                listOfPplWithFutureDeathDates,
                listOfPplDeathDateEarlierThanBirthDate).flatMap(Collection::stream).collect(Collectors.toList());

        return body;
    }

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
            PersonId personId = personFactory.createNewSCPersonId();
            PersonDetail personDetail = personDetailRepo.findByPerson(personId.getPerson());
            listOfValidDeathCases.add(personId.getNaturalId()+randomDeathDate(fileReceivedDate, personDetail.getDateOfBirth()));
        }

        return listOfValidDeathCases;
    }

    public List<String> createListOfValidPPDeathCases(int numOfValidDeathCases, LocalDate fileReceivedDate) {
        List<String> listOfValidDeathCases = new ArrayList<>();

        for ( int i = 0 ; i < numOfValidDeathCases ; i++ ) {
            PersonId personId = personFactory.createNewPPPersonId();
            PersonDetail personDetail = personDetailRepo.findByPerson(personId.getPerson());
            listOfValidDeathCases.add(personId.getNaturalId()+randomDeathDate(fileReceivedDate, personDetail.getDateOfBirth()));
        }

        return listOfValidDeathCases;
    }

    public List<String> createListOfValidFRDeathCases(int numOfValidDeathCases, LocalDate fileReceivedDate) {
        List<String> listOfValidDeathCases = new ArrayList<>();

        for ( int i = 0 ; i < numOfValidDeathCases ; i++ ) {
            PersonId personId = personFactory.createNewFRPersonId();
            PersonDetail personDetail = personDetailRepo.findByPerson(personId.getPerson());
            listOfValidDeathCases.add(personId.getNaturalId()+randomDeathDate(fileReceivedDate, personDetail.getDateOfBirth()));
        }

        return listOfValidDeathCases;
    }

    public List<String> createListOfPplDeathDateEarlierThanBirthDate(int numOfPpl) {
        List<String> listOfPpl = new ArrayList<>();

        for ( int i = 0 ; i < numOfPpl ; i++ ) {
            PersonId personId = personFactory.createNewSCPersonId();
            PersonDetail personDetail = personDetailRepo.findByPerson(personId.getPerson());
            personDetailRepo.updateBirthDateForPerson(dateUtils.now().minusDays(20), personDetail.getPerson());

            listOfPpl.add(personId.getNaturalId()+dateUtils.now().minusDays(21).format(Phaker.DATETIME_FORMATTER_YYYYMMDD));
        }

        return listOfPpl;
    }

    public List<String> createListOfPplWhoAreAlreadyDead(int numOfPpl, LocalDate fileReceivedDate) {
        List<String> listOfPpl = new ArrayList<>();

        for ( int i = 0 ; i < numOfPpl ; i++ ) {
            PersonId personId = personFactory.createNewSCPersonId();
            PersonDetail personDetail = personDetailRepo.findByPerson(personId.getPerson());
            personDetailRepo.updateDeathDateForPerson(Phaker.validDate(personDetail.getDateOfBirth(), dateUtils.now()), personId.getPerson());

            listOfPpl.add(personId.getNaturalId()+randomDeathDate(fileReceivedDate, personDetail.getDateOfBirth()));
        }

        return listOfPpl;
    }

    public List<String> createListOfPplWithFutureDeathDates(int numOfPpl) {
        List<String> listOfPpl = new ArrayList<>();

        for ( int i = 0 ; i < numOfPpl ; i++ ) {
            PersonId personId = personFactory.createNewSCPersonId();

            listOfPpl.add(personId.getNaturalId()+dateUtils.tomorrow().format(Phaker.DATETIME_FORMATTER_YYYYMMDD));
        }

        return listOfPpl;
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
