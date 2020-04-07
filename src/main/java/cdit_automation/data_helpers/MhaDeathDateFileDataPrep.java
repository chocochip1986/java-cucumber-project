package cdit_automation.data_helpers;

import cdit_automation.configuration.StepDefLevelTestContext;
import cdit_automation.data_helpers.batch_entities.MhaDeathBroadcastFileEntry;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.datasource.FileTypeEnum;
import cdit_automation.models.datasource.PersonDetail;
import cdit_automation.models.datasource.PersonId;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;

import io.cucumber.datatable.DataTable;
import org.springframework.stereotype.Component;

@Component
public class MhaDeathDateFileDataPrep extends BatchFileDataPrep {

    public List<String> createBodyOfTestScenarios(List<Map<String, String>> list, StepDefLevelTestContext testContext, Timestamp receivedTimestamp) {
        List<String> listOfInvalidNrics = createListWithInvalidNrics(parseStringSize(list.get(0).get("InvalidNrics")));
        List<String> listOfDuplicatedEntries = createListOfDuplicatedEntries(parseStringSize(list.get(0).get("DuplicatedEntries")));
        List<String> listOfDuplicatedNricOnlyEntries = createListOfDuplicatedNricOnlyEntries(parseStringSize(list.get(0).get("PartialDuplicates")));
        List<String> listOfValidSCDeathCases = createListOfValidSCDeathCases(parseStringSize(list.get(0).get("ValidSCDeathCases")), receivedTimestamp.toLocalDateTime().toLocalDate());
        List<String> listOfValidPPDeathCases = createListOfValidPPDeathCases(parseStringSize(list.get(0).get("ValidPPDeathCases")), receivedTimestamp.toLocalDateTime().toLocalDate());
        List<String> listOfValidFRDeathCases = createListOfValidFRDeathCases(parseStringSize(list.get(0).get("ValidFRDeathCases")), receivedTimestamp.toLocalDateTime().toLocalDate());
        List<String> listOfPplDeathDateEarlierThanBirthDate = createListOfPplDeathDateEarlierThanBirthDate(parseStringSize(list.get(0).get("DeathDateEarlierThanBirthDate")));
        List<String> listOfPplWhoAreAlreadyDead = createListOfPplWhoAreAlreadyDead(parseStringSize(list.get(0).get("PplWhoAreAlreadyDead")), receivedTimestamp.toLocalDateTime().toLocalDate());
        List<String> listOfPplWithFutureDeathDates = createListOfPplWithFutureDeathDates(parseStringSize(list.get(0).get("PplWithFutureDeathDates")));
        List<String> listOfHybridDuplicateEntries = createListOfHybridDuplicateEntries(parseStringSize(list.get(0).get("HybridDuplicates")));

        testContext.set("listOfInvalidNrics", listOfInvalidNrics);
        testContext.set("listOfDuplicatedEntries", listOfDuplicatedEntries);
        testContext.set("listOfDuplicatedNricOnlyEntries", listOfDuplicatedNricOnlyEntries);
        testContext.set("listOfValidSCDeathCases", listOfValidSCDeathCases);
        testContext.set("listOfValidPPDeathCases", listOfValidPPDeathCases);
        testContext.set("listOfValidFRDeathCases", listOfValidFRDeathCases);
        testContext.set("listOfPplDeathDateEarlierThanBirthDate", listOfPplDeathDateEarlierThanBirthDate);
        testContext.set("listOfPplWhoAreAlreadyDead", listOfPplWhoAreAlreadyDead);
        testContext.set("listOfPplWithFutureDeathDates", listOfPplWithFutureDeathDates);
        testContext.set("listOfHybridDuplicateEntries", listOfHybridDuplicateEntries);

        List<String> body = Stream.of(listOfInvalidNrics,
                listOfDuplicatedEntries,
                listOfDuplicatedNricOnlyEntries,
                listOfValidSCDeathCases,
                listOfValidPPDeathCases,
                listOfValidFRDeathCases,
                listOfPplWhoAreAlreadyDead,
                listOfPplWithFutureDeathDates,
                listOfHybridDuplicateEntries,
                listOfPplDeathDateEarlierThanBirthDate).flatMap(Collection::stream).collect(Collectors.toList());

        return body;
    }

    public List<String> createListWithInvalidNrics(int numOfInvalidNrics) {
        List<String> listOfInvalidNrics = new ArrayList<>();

        for ( int i = 0 ; i < numOfInvalidNrics ; i++ ) {
            String inputLine = generateRandomDeathDateRecord(Phaker.invalidNric());
            listOfInvalidNrics.add(inputLine);
            batchFileDataWriter.chunkOrWrite(inputLine);
        }

        return listOfInvalidNrics;
    }

    public List<String> createListOfDuplicatedEntries(int numOfDuplicatedEntries) {
        List<String> listOfDuplicatedEntries = new ArrayList<>();

        for ( int i = 0 ; i < numOfDuplicatedEntries ; i++ ) {
            String dupNric = Phaker.validNric();
            String randomDeathDate = randomDeathDate();
            String inputLine = dupNric+randomDeathDate;

            listOfDuplicatedEntries.add(inputLine);
            listOfDuplicatedEntries.add(inputLine);

            batchFileDataWriter.chunkOrWrite(inputLine);
            batchFileDataWriter.chunkOrWrite(inputLine);

            generateAndSavePersonIdForDeathDateFile(dupNric, randomDeathDate);
        }

        return listOfDuplicatedEntries;
    }

    public List<String> createListOfDuplicatedNricOnlyEntries(int numOfDuplicatedEntries) {
        List<String> listOfDuplicatedEntries = new ArrayList<>();

        for ( int i = 0 ; i < numOfDuplicatedEntries ; i++ ) {
            String dupNric = Phaker.validNric();
            String inputLine1 = generateRandomDeathDateRecord(dupNric);
            String inputLine2 = generateRandomDeathDateRecord(dupNric);
            listOfDuplicatedEntries.add(inputLine1);
            listOfDuplicatedEntries.add(inputLine2);

            batchFileDataWriter.chunkOrWrite(inputLine1);
            batchFileDataWriter.chunkOrWrite(inputLine2);
        }

        return listOfDuplicatedEntries;
    }

    public List<String> createListOfHybridDuplicateEntries(int numOfDuplicatedEntries) {

        List<String> listOfDuplicatedEntries = new ArrayList<>();

        for (int i = 0 ; i < numOfDuplicatedEntries ; i++) {
            
            String dupNric = Phaker.validNric();
            String inputLine1 = generateRandomDeathDateRecord(dupNric);
            String inputLine2 = generateRandomDeathDateRecord(dupNric);
            
            listOfDuplicatedEntries.add(inputLine1);
            listOfDuplicatedEntries.add(inputLine1);
            listOfDuplicatedEntries.add(inputLine2);

            batchFileDataWriter.chunkOrWrite(inputLine1);
            batchFileDataWriter.chunkOrWrite(inputLine1);
            batchFileDataWriter.chunkOrWrite(inputLine2);
        }

        return listOfDuplicatedEntries;
    }

    public List<String> createListOfValidSCDeathCases(int numOfValidDeathCases, LocalDate fileReceivedDate) {
        List<String> listOfValidDeathCases = new ArrayList<>();

        for ( int i = 0 ; i < numOfValidDeathCases ; i++ ) {
            PersonId personId = personFactory.createNewSCPersonId();
            PersonDetail personDetail = personDetailRepo.findByPerson(personId.getPerson());
            String inputLine = personId.getNaturalId()+randomDeathDate(fileReceivedDate, personDetail.getDateOfBirth());
            listOfValidDeathCases.add(inputLine);
            batchFileDataWriter.chunkOrWrite(inputLine);
        }

        return listOfValidDeathCases;
    }

    public List<String> createListOfValidPPDeathCases(int numOfValidDeathCases, LocalDate fileReceivedDate) {
        List<String> listOfValidDeathCases = new ArrayList<>();

        for ( int i = 0 ; i < numOfValidDeathCases ; i++ ) {
            PersonId personId = personFactory.createNewPPPersonId();
            PersonDetail personDetail = personDetailRepo.findByPerson(personId.getPerson());
            String inputLine = personId.getNaturalId()+randomDeathDate(fileReceivedDate, personDetail.getDateOfBirth());
            listOfValidDeathCases.add(inputLine);
            batchFileDataWriter.chunkOrWrite(inputLine);
        }

        return listOfValidDeathCases;
    }

    public List<String> createListOfValidFRDeathCases(int numOfValidDeathCases, LocalDate fileReceivedDate) {
        List<String> listOfValidDeathCases = new ArrayList<>();

        for ( int i = 0 ; i < numOfValidDeathCases ; i++ ) {
            PersonId personId = personFactory.createNewFRPersonId();
            PersonDetail personDetail = personDetailRepo.findByPerson(personId.getPerson());
            String inputLine = personId.getNaturalId()+randomDeathDate(fileReceivedDate, personDetail.getDateOfBirth());
            listOfValidDeathCases.add(inputLine);
            batchFileDataWriter.chunkOrWrite(inputLine);
        }

        return listOfValidDeathCases;
    }

    public List<String> createListOfPplDeathDateEarlierThanBirthDate(int numOfPpl) {
        List<String> listOfPpl = new ArrayList<>();

        for ( int i = 0 ; i < numOfPpl ; i++ ) {
            PersonId personId = personFactory.createNewSCPersonId();
            PersonDetail personDetail = personDetailRepo.findByPerson(personId.getPerson());
            personDetailRepo.updateBirthDateForPerson(dateUtils.now().minusDays(20), personDetail.getPerson());
            String inputLine = personId.getNaturalId()+dateUtils.now().minusDays(21).format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
            listOfPpl.add(inputLine);
            batchFileDataWriter.chunkOrWrite(inputLine);
        }

        return listOfPpl;
    }

    public List<String> createListOfPplWhoAreAlreadyDead(int numOfPpl, LocalDate fileReceivedDate) {
        List<String> listOfPpl = new ArrayList<>();

        for ( int i = 0 ; i < numOfPpl ; i++ ) {
            PersonId personId = personFactory.createNewSCPersonId();
            PersonDetail personDetail = personDetailRepo.findByPerson(personId.getPerson());
            personDetailRepo.updateDeathDateForPerson(Phaker.validDate(personDetail.getDateOfBirth(), dateUtils.now()), personId.getPerson());
            String inputLine = personId.getNaturalId()+randomDeathDate(fileReceivedDate, personDetail.getDateOfBirth());
            listOfPpl.add(inputLine);
            batchFileDataWriter.chunkOrWrite(inputLine);
        }

        return listOfPpl;
    }

    public List<String> createListOfPplWithFutureDeathDates(int numOfPpl) {
        List<String> listOfPpl = new ArrayList<>();

        for ( int i = 0 ; i < numOfPpl ; i++ ) {
            PersonId personId = personFactory.createNewSCPersonId();
            String inputLine = personId.getNaturalId()+dateUtils.tomorrow().format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
            listOfPpl.add(inputLine);
            batchFileDataWriter.chunkOrWrite(inputLine);
        }

        return listOfPpl;
    }

    public List<String> formatEntries(DataTable dataTable) {
        List<Map<String, String>> dataRows = dataTable.asMaps(String.class, String.class);
        List<MhaDeathBroadcastFileEntry> entries = dataRows.stream().map(MhaDeathBroadcastFileEntry::new).collect(Collectors.toList());
        return entries.stream().map(MhaDeathBroadcastFileEntry::toRawString).collect(Collectors.toList());
    }

    public void writeToFile(String dateStr, List<String> entries) {
        LocalDate localDate = dateUtils.parse(dateStr);
        batchFileDataWriter.begin(generateSingleHeader(localDate), FileTypeEnum.MHA_DEATH_DATE, 10);
        entries.forEach(entry -> batchFileDataWriter.chunkOrWrite(entry));
        batchFileDataWriter.end();
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

    // Date of Birth will be generated based on Death Date - 1 year
    private void generateAndSavePersonIdForDeathDateFile(String nric, String deathDateString) {
        
        LocalDate deathDate = dateUtils.parse(deathDateString);
        LocalDate dateOfBirth = dateUtils.yearsBeforeDate(deathDate, 1);
        
        PersonId personId = personFactory.createNewSCPersonId(dateOfBirth, Phaker.validName(), nric);
        personIdRepo.save(personId);
    }

    // Generate record with random death date for input nric
    private String generateRandomDeathDateRecord(String nric) {
        return nric + randomDeathDate();
    }
}
