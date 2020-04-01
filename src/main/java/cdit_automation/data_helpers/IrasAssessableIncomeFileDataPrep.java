package cdit_automation.data_helpers;

import cdit_automation.data_helpers.batch_entities.AssessableIncomeFileEntry;
import cdit_automation.data_helpers.batch_entities.MhaBulkCitizenFileEntry;
import cdit_automation.enums.AssessableIncomeStatus;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.enums.PersonIdTypeEnum;
import cdit_automation.models.*;
import cdit_automation.models.embeddables.BiTemporalData;
import cdit_automation.models.embeddables.BusinessTemporalData;
import cdit_automation.models.interfaces.ICustomIncomeRecord;
import cdit_automation.repositories.*;
import cdit_automation.utilities.StringUtils;
import io.cucumber.datatable.DataTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class IrasAssessableIncomeFileDataPrep extends BatchFileDataPrep {

  private FileReceivedRepo fileReceivedRepo;
  private PersonRepo personRepo;
  private IncomeRepo incomeRepo;

  @Autowired
  public IrasAssessableIncomeFileDataPrep(
      FileReceivedRepo fileReceivedRepo, PersonRepo personRepo, IncomeRepo incomeRepo) {
    this.fileReceivedRepo = fileReceivedRepo;
    this.personRepo = personRepo;
    this.incomeRepo = incomeRepo;
  }

  public void updateFileReceivedDate(FileReceived fileReceived, String dateStr) {
    LocalDate localDate = dateUtils.parse(dateStr);
    fileReceived.setReceivedTimestamp(dateUtils.endOfDayToTimestamp(localDate));
    fileReceivedRepo.save(fileReceived);
  }

  public void insertPerson(DataTable dataTable, String validFromDateStr) {
    List<Map<String, String>> dataRows = dataTable.asMaps(String.class, String.class);
    List<MhaBulkCitizenFileEntry> entries =
        dataRows.stream().map(MhaBulkCitizenFileEntry::new).collect(Collectors.toList());
    BiTemporalData biTemporalData =
        new BiTemporalData()
            .generateNewBiTemporalData(
                dateUtils.beginningOfDayToTimestamp(dateUtils.parse(validFromDateStr)));
    Batch batch = Batch.createCompleted();
    batchRepo.save(batch);
    entries.forEach(
        entry -> {
          Person person = Person.create();
          personRepo.save(person);
          PersonName personName = PersonName.create(batch, person, entry.getName(), biTemporalData);
          personNameRepo.save(personName);
          PersonId personId =
              PersonId.create(batch, PersonIdTypeEnum.NRIC, person, entry.getNric(), biTemporalData);
          personIdRepo.save(personId);
          PersonDetail personDetail =
              PersonDetail.create(
                  batch, person, dateUtils.parse(entry.getDateOfBirth()), null, biTemporalData);
          personDetailRepo.save(personDetail);
          Nationality nationality =
              Nationality.create(
                  batch,
                  person,
                  NationalityEnum.SINGAPORE_CITIZEN,
                  biTemporalData,
                  dateUtils.endOfDayToTimestamp(
                      dateUtils.parse(entry.getCitizenshipAttainmentIssueDate())));
          nationalityRepo.save(nationality);
        });
  }

  public PersonId getPersonIdBy(String naturalId) {
    return personIdRepo.findByNaturalId(naturalId);
  }

  public void insertFinPerson(DataTable dataTable, String validFromDateStr) {
    List<Map<String, String>> dataRows = dataTable.asMaps(String.class, String.class);
    List<MhaBulkCitizenFileEntry> entries =
        dataRows.stream().map(MhaBulkCitizenFileEntry::new).collect(Collectors.toList());
    BiTemporalData biTemporalData =
        new BiTemporalData()
            .generateNewBiTemporalData(
                dateUtils.beginningOfDayToTimestamp(dateUtils.parse(validFromDateStr)));
    Batch batch = Batch.createCompleted();
    batchRepo.save(batch);
    entries.forEach(
        entry -> {
          Person person = Person.create();
          personRepo.save(person);
          PersonId personId =
              PersonId.create(batch, PersonIdTypeEnum.FIN, person, entry.getFin(), biTemporalData);
          personIdRepo.save(personId);
        });
  }

  public boolean doesFileContainAllString(List<String> stringList, String filePath) {
    Path path = Paths.get(filePath);
    try (Stream<String> lines = Files.lines(path)) {
      return lines.map(o -> stringList.contains(o.trim())).reduce(true, (a, b) -> a && b);
    } catch (IOException e) {
      log.error("Unable to find file with path :" + filePath);
      log.error(e.getMessage());
    }
    return false;
  }

  public boolean deleteFile(String egressFilePath) {
    Path path = Paths.get(egressFilePath);
    try {
      return Files.deleteIfExists(path);
    } catch (DirectoryNotEmptyException e) {
      log.error("File is a directory and expect directory to be empty.");
      log.error(e.getMessage());
    } catch (IOException e) {
      log.error(e.getMessage());
    }
    return false;
  }

  public void writeToFile(String dateStr, List<String> entries, FileTypeEnum fileTypeEnum) {
    String header =
        StringUtils.rightPad(
            "0" + generateSingleHeader(dateUtils.parse(dateStr)), 50, StringUtils.SPACE);
    batchFileDataWriter.begin(header, fileTypeEnum, 10);
    entries.forEach(s -> batchFileDataWriter.chunkOrWrite(s));
    batchFileDataWriter.end();
  }

  public List<String> formatEntries(DataTable dataTable) {
    List<Map<String, String>> dataRows = dataTable.asMaps(String.class, String.class);
    List<AssessableIncomeFileEntry> entries =
        dataRows.stream().map(AssessableIncomeFileEntry::new).collect(Collectors.toList());
    return entries.stream()
        .map(AssessableIncomeFileEntry::toRawString)
        .collect(Collectors.toList());
  }

  public List<ICustomIncomeRecord> getIncomeByNaturalIdsAndYearAndAsOf(
      List<String> naturalIds, int year, String dateStr) {
    return incomeRepo.findIncomeByNaturalIdsAndYearAndAsOf(naturalIds, year, dateStr);
  }

  public void insertAppealIncomes(DataTable dataTable) {
    Batch newBatch = Batch.createCompleted();
    batchRepo.save(newBatch);

    List<Map<String, String>> dataRows = dataTable.asMaps(String.class, String.class);

    List<Income> appealIncomes =
        dataRows.stream()
            .map(row -> createAppealIncome(row, newBatch))
            .collect(Collectors.toList());
    incomeRepo.saveAll(appealIncomes);

    List<Income> supersededIncomes =
            dataRows.stream()
                    .map(this::supersedeIncome)
                    .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                    .collect(Collectors.toList());
    incomeRepo.saveAll(supersededIncomes);
  }

  private Optional<Income> supersedeIncome(Map<String, String> row) {
    Optional<Income> incomeOptional =
        incomeRepo.findIncomeByNaturalIdAndYear(
            row.get("NATURAL_ID"), Integer.parseInt(row.get("APPEAL_YEAR")), row.get("APPEAL_DATE"));

    if (!incomeOptional.isPresent()) {
      return Optional.empty();
    }

    Income result = incomeOptional.get();
    BusinessTemporalData newBusinessTemporalData =
        result
            .getBiTemporalData()
            .getBusinessTemporalData()
            .newValidTill(
                dateUtils.endOfDayToTimestamp(
                    dateUtils.parse(row.get("APPEAL_DATE")).minusDays(1)));

    BiTemporalData newBiTemporalData =
        BiTemporalData.builder()
            .businessTemporalData(newBusinessTemporalData)
            .dbTemporalData(result.getBiTemporalData().getDbTemporalData())
            .build();

    result.setBiTemporalData(newBiTemporalData);
    return Optional.of(result);
  }

  public Income createAppealIncome(Map<String, String> row, Batch batch) {
    PersonId personId = personIdRepo.findByNaturalId(row.get("NATURAL_ID"));
    Person person = personId.getPerson();
    BiTemporalData biTemporalData =
        new BiTemporalData()
            .generateNewBiTemporalData(
                dateUtils.beginningOfDayToTimestamp(dateUtils.parse(row.get("APPEAL_DATE"))));
    Income result = Income.builder()
        .batch(batch)
        .assessableIncomeStatus(AssessableIncomeStatus.NEW_APPEAL_CASE)
        .biTemporalData(biTemporalData)
        .year(Short.valueOf(row.get("APPEAL_YEAR")))
        .person(person)
        .isAppealCase(true)
        .build();

    incomeRepo.findIncomeByNaturalIdAndYear(
            row.get("NATURAL_ID"),
            Integer.parseInt(row.get("APPEAL_YEAR")), row.get("APPEAL_DATE")
    ).ifPresent(oldIncome -> {
      result.setAssessableIncome(oldIncome.getAssessableIncome());
      result.setAssessableIncomeStatus(oldIncome.getAssessableIncomeStatus());
    });

    return result;
  }

  public void turnOffAppealCase(String natural_id, String year) {
    Income appealIncome = incomeRepo.findAppealIncomeByNaturalIdAndYear(natural_id, year);
    appealIncome.setAppealCase(false);
    incomeRepo.save(appealIncome);
  }
}
