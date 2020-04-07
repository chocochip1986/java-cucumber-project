package cdit_automation.data_helpers;

import cdit_automation.configuration.StepDefLevelTestContext;
import cdit_automation.constants.TestConstants;
import cdit_automation.data_helpers.batch_entities.MhaCeasedCitizenFileEntry;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.datasource.FileTypeEnum;
import cdit_automation.enums.datasource.NationalityEnum;
import cdit_automation.models.datasource.Batch;
import cdit_automation.models.datasource.CeasedCitizenValidated;
import cdit_automation.models.datasource.Nationality;
import cdit_automation.models.datasource.PersonDetail;
import cdit_automation.models.datasource.PersonId;
import cdit_automation.models.datasource.PersonName;
import cdit_automation.models.datasource.embeddables.BiTemporalData;
import cdit_automation.models.datasource.embeddables.BusinessTemporalData;
import cdit_automation.models.datasource.embeddables.DbTemporalData;
import cdit_automation.utilities.CommonUtils;
import cdit_automation.utilities.StringUtils;
import io.cucumber.datatable.DataTable;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MhaCeasedCitizenFileDataPrep extends BatchFileDataPrep {

  private static final String SINGAPORE_CITIZEN = "SingaporeCitizen";
  private static final String DUAL_CITIZEN = "DualCitizen";
  private static final String CEASED_CITIZEN = "CeasedCitizen";
  private static final String AVAILABLE_NRICS = "availableNrics";
  private static final String PRESENT_CEASED_CITIZEN = "presentCeasedCitizen";
  private static final String REPEATED_CEASED_CITIZEN = "RepeatedCeasedCitizen";
  private static final String EMPTY_NAME = "EmptyName";
  private static final String RENUNCIATION_DATE_AFTER_CUT_OFF = "RenunciationDateAfterCutOff";
  private static final String AWARDED_SINGAPORE_CITIZEN = "AwardedSingaporeCitizen";
  private static final String NUMBER_OF_DUPLICATION = "NumberOfDuplication";
  private static final String NUMBER_OF_PARTIAL_DUPLICATION = "NumberOfPartialDuplication";
  private static final String CEASED_CITIZEN_WITH_SG_COUNTRY_CODE = "CeasedCitizenWithSGCountryCode";
  private static final String CEASED_CITIZEN_WHO_IS_NON_SG = "CeasedCitizenWhoIsNonSG";

  private static final String FIELD_NRIC = "Nric";
  private static final String FIELD_NAME = "Name";
  private static final String FIELD_NATIONALITY = "Nationality";
  private static final String FIELD_CESSATION_DATE = "Cessation Date";
  
  private Random random = new Random();
  private BiTemporalData biTemporalData = new BiTemporalData();

  public void initDatabase(List<Map<String, String>> list, StepDefLevelTestContext testContext) {
    Map<String, String> map = list.get(0);
    List<PersonId> scPersonIds = populateSCs(parseStringSize(map.get(SINGAPORE_CITIZEN)));
    List<PersonId> dcPersonIds = populateDCs(parseStringSize(map.get(DUAL_CITIZEN)));
    List<String> availableNrics =
        Stream.concat(scPersonIds.stream(), dcPersonIds.stream())
            .map(PersonId::getNaturalId)
            .collect(Collectors.toList());
    List<CeasedCitizenValidated> ceasedCitizens =
        populateCeasedCitizens(parseStringSize(map.get(CEASED_CITIZEN)), availableNrics);
    List<String> usedNrics =
        ceasedCitizens.stream().map(CeasedCitizenValidated::getNric).collect(Collectors.toList());
    availableNrics.removeIf(usedNrics::contains);

    testContext.set(AVAILABLE_NRICS, availableNrics);
    testContext.set(PRESENT_CEASED_CITIZEN, ceasedCitizens);
  }

  public List<String> createBodyOfTestScenarios(
      List<Map<String, String>> list, StepDefLevelTestContext testContext) {
    Map<String, String> map = list.get(0);
    List<String> availableNrics =
        testContext.get(AVAILABLE_NRICS) == null
            ? new ArrayList<>()
            : testContext.get(AVAILABLE_NRICS);
    List<CeasedCitizenValidated> presentCeasedCitizen =
        testContext.get(PRESENT_CEASED_CITIZEN) == null
            ? new ArrayList<>()
            : testContext.get(PRESENT_CEASED_CITIZEN);

    List<MhaCeasedCitizenFileEntry> citizens =
        getCitizens(parseStringSize(map.get(CEASED_CITIZEN)), availableNrics);
    List<MhaCeasedCitizenFileEntry> repeatedCitizens =
        getRepeatedCitizens(
            parseStringSize(map.get(REPEATED_CEASED_CITIZEN)), presentCeasedCitizen);
    List<MhaCeasedCitizenFileEntry> emptyNameCitizens =
        getEmptyNameCitizens(parseStringSize(map.get(EMPTY_NAME)));
    List<MhaCeasedCitizenFileEntry> renunciationAfterCutOffDateCitizens =
        getRenunciationAfterCutOffDateCitizens(
            parseStringSize(map.get(RENUNCIATION_DATE_AFTER_CUT_OFF)));
    List<MhaCeasedCitizenFileEntry> awardedCitizens =
        getAwardedSCCitizens(parseStringSize(map.get(AWARDED_SINGAPORE_CITIZEN)), availableNrics);
    List<MhaCeasedCitizenFileEntry> duplicateCitizens =
        getDuplicateCitizens(
            parseStringSize(map.get(NUMBER_OF_DUPLICATION)),
            Stream.concat(citizens.stream(), repeatedCitizens.stream())
                .collect(Collectors.toList()));
    List<MhaCeasedCitizenFileEntry> partialDuplicateCitizens = 
        getPartialDuplicateCitizens(
             parseStringSize(map.get(NUMBER_OF_PARTIAL_DUPLICATION)),
             Stream.concat(citizens.stream(), repeatedCitizens.stream())
                 .collect(Collectors.toList()));
    List<MhaCeasedCitizenFileEntry> ceasedCitizenWithSGCountryCode =
        getCeasedCitizenWithSG(parseStringSize(map.get(CEASED_CITIZEN_WITH_SG_COUNTRY_CODE)));
    List<MhaCeasedCitizenFileEntry> ceasedCitizenWhoIsNonSG =
        getCeasedCitizenWhoHasAForeignNationality(
            parseStringSize(map.get(CEASED_CITIZEN_WHO_IS_NON_SG)));

    List<MhaCeasedCitizenFileEntry> ceasedCitizens =
        Stream.of(
                citizens,
                repeatedCitizens,
                emptyNameCitizens,
                renunciationAfterCutOffDateCitizens,
                awardedCitizens,
                duplicateCitizens,
                partialDuplicateCitizens,
                ceasedCitizenWithSGCountryCode,
                ceasedCitizenWhoIsNonSG)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    testContext.set("ceasedCitizens", ceasedCitizens);

    return ceasedCitizens.stream()
        .map(MhaCeasedCitizenFileEntry::toString)
        .collect(Collectors.toList());
  }

  public void createBodyOfTestScenarios(List<Map<String, String>> listOfScenarios) {

    for (Map<String, String> scenario: listOfScenarios) {
      
      MhaCeasedCitizenFileEntry mhaCeasedCitizenFileEntry = 
              MhaCeasedCitizenFileEntry
                      .builder()
                      .nric(CommonUtils.nricFieldOptions(scenario.get(FIELD_NRIC)))
                      .name(CommonUtils.nameFieldOptions(scenario.get(FIELD_NAME)))
                      .nationality(nationalityFieldOptions(scenario.get(FIELD_NATIONALITY)))
                      .citizenRenunciationDate(renunciationDateFieldOptions(scenario.get(FIELD_CESSATION_DATE)))
                      .build();
      
      batchFileDataWriter.chunkOrWrite(mhaCeasedCitizenFileEntry.toString());
    }
  }

  public List<PersonId> populateSCs(int numOfRecords) {
    List<PersonId> result = new ArrayList<>();
    for (int i = 0; i < numOfRecords; i++) {
      PersonId personId = personFactory.createNewSCPersonId();
      result.add(personId);
    }
    return result;
  }

  private List<PersonId> populateDCs(int numOfRecords) {
    List<PersonId> result = new ArrayList<>();
    for (int i = 0; i < numOfRecords; i++) {
      PersonId personId = personFactory.createDualCitizen();
      result.add(personId);
    }
    return result;
  }

  private List<CeasedCitizenValidated> populateCeasedCitizens(int quantity, List<String> nrics) {
    Random random = new Random();
    List<String> clonedNrics = new ArrayList<>(nrics);
    List<CeasedCitizenValidated> resultList = new ArrayList<>();
    int iterations = clonedNrics.size() == 0 ? quantity : Math.min(quantity, clonedNrics.size());

    Batch b = new Batch();
    b.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
    batchRepo.save(b);

    for (int i = 0; i < iterations; i++) {
      String nric = "";
      if (clonedNrics.size() > 0) {
        int randomIndex = random.nextInt(clonedNrics.size());
        nric = clonedNrics.get(randomIndex);
        clonedNrics.remove(randomIndex);
      }

      CeasedCitizenValidated ceasedCitizen =
          CeasedCitizenValidated.builder()
              .batch(b)
              .name(Phaker.validName())
              .nationality(Phaker.randomNonSGCountryCode())
              .citizenRenunciationDate(dateUtils.daysBeforeToday(30))
              .nric(nric)
              .isMappable(true)
              .build();
      ceasedCitizenRepo.save(ceasedCitizen);
      PersonId currentPersonId = personIdRepo.findPersonByNaturalId(nric);
      if (currentPersonId != null) {
        updatePersonDetailRecord(b, ceasedCitizen, currentPersonId);
        updateNationality(b, ceasedCitizen, currentPersonId);
        generateNewPersonRecord(b, ceasedCitizen, currentPersonId);
        generateNewNationalityRecord(b, ceasedCitizen, currentPersonId);
      }
      resultList.add(ceasedCitizen);
    }
    return resultList;
  }

  private void updatePersonDetailRecord(
      Batch batch, CeasedCitizenValidated ceasedCitizen, PersonId personId) {
    PersonDetail currentPersonDetail = personDetailRepo.findByPerson(personId.getPerson());
    BiTemporalData currentPersonBiTemporalData = currentPersonDetail.getBiTemporalData();
    BusinessTemporalData newCurrentPersonBusinessTemporalData =
        currentPersonBiTemporalData
            .getBusinessTemporalData()
            .newValidTill(
                dateUtils.endOfDayToTimestamp(ceasedCitizen.getCitizenRenunciationDate()));
    currentPersonBiTemporalData.setBusinessTemporalData(newCurrentPersonBusinessTemporalData);
    currentPersonBiTemporalData.setDbTemporalData(new DbTemporalData());
    personDetailRepo.save(currentPersonDetail);
  }

  private void generateNewPersonRecord(
      Batch batch, CeasedCitizenValidated ceasedCitizen, PersonId personId) {
    PersonDetail currentPersonDetail =
        personDetailRepo.findByPerson(
            personId.getPerson(),
            dateUtils.beginningOfDayToTimestamp(
                ceasedCitizen.getCitizenRenunciationDate().minusDays(1)));
    BiTemporalData newBiTemporalData =
        biTemporalData.generateNewBiTemporalData(
            dateUtils.beginningOfDayToTimestamp(
                ceasedCitizen.getCitizenRenunciationDate().plusDays(1)));
    PersonDetail newPersonDetail =
        PersonDetail.builder()
            .batch(batch)
            .person(currentPersonDetail.getPerson())
            .dateOfBirth(currentPersonDetail.getDateOfBirth())
            .dateOfDeath(currentPersonDetail.getDateOfDeath())
            .biTemporalData(newBiTemporalData)
            .build();
    personDetailRepo.save(newPersonDetail);
  }

  private void updateNationality(
      Batch batch, CeasedCitizenValidated ceasedCitizen, PersonId personId) {
    Nationality currentNationality = nationalityRepo.findNationalityByPerson(personId.getPerson());
    BiTemporalData currentNationalityBiTemporalData = currentNationality.getBiTemporalData();
    BusinessTemporalData newCurrentNationalityBusinessTemporalDate =
        currentNationalityBiTemporalData
            .getBusinessTemporalData()
            .newValidTill(
                dateUtils.endOfDayToTimestamp(ceasedCitizen.getCitizenRenunciationDate()));
    currentNationalityBiTemporalData.setBusinessTemporalData(
        newCurrentNationalityBusinessTemporalDate);
    currentNationalityBiTemporalData.setDbTemporalData(new DbTemporalData());
    nationalityRepo.save(currentNationality);
  }

  private void generateNewNationalityRecord(
      Batch batch, CeasedCitizenValidated ceasedCitizen, PersonId personId) {
    Nationality currentNationality =
        nationalityRepo.findNationalityByPerson(
            personId.getPerson(),
            dateUtils.beginningOfDayToTimestamp(
                ceasedCitizen.getCitizenRenunciationDate().minusDays(1)));
    BiTemporalData newBiTemporalData =
        biTemporalData.generateNewBiTemporalData(
            dateUtils.beginningOfDayToTimestamp(
                ceasedCitizen.getCitizenRenunciationDate().plusDays(1)));
    Nationality newNationality =
        Nationality.builder()
            .batch(batch)
            .person(currentNationality.getPerson())
            .nationality(NationalityEnum.NON_SINGAPORE_CITIZEN)
            .citizenshipAttainmentDate(currentNationality.getCitizenshipAttainmentDate())
            .citizenshipRenunciationDate(
                dateUtils.endOfDayToTimestamp(ceasedCitizen.getCitizenRenunciationDate()))
            .biTemporalData(newBiTemporalData)
            .build();
    nationalityRepo.save(newNationality);
  }

  private List<MhaCeasedCitizenFileEntry> getCitizens(int quantity, List<String> nrics) {
    List<String> clonedNrics = new ArrayList<>(nrics);
    List<MhaCeasedCitizenFileEntry> resultList = new ArrayList<>();
    int iterations = clonedNrics.isEmpty() ? quantity : Math.min(quantity, clonedNrics.size());
    for (int i = 0; i < iterations; i++) {
      String nric = "";
      if (clonedNrics.size() > 0) {
        int randomIndex = random.nextInt(clonedNrics.size());
        nric = clonedNrics.get(randomIndex);
        clonedNrics.remove(randomIndex);
      }

      MhaCeasedCitizenFileEntry mhaCeasedCitizenFileEntry =
          MhaCeasedCitizenFileEntry.builder()
              .nric(nric.isEmpty() ? Phaker.validNric() : nric)
              .name(Phaker.validName())
              .nationality(Phaker.randomNonSGCountryCode())
              .citizenRenunciationDate(dateUtils.daysBeforeToday(15).format(dateUtils.DATETIME_FORMATTER_YYYYMMDD))
              .build();
      batchFileDataWriter.chunkOrWrite(mhaCeasedCitizenFileEntry.toString());
      resultList.add(mhaCeasedCitizenFileEntry);
    }
    return resultList;
  }

  private List<MhaCeasedCitizenFileEntry> getRepeatedCitizens(
      int quantity, List<CeasedCitizenValidated> ceasedCitizens) {
    List<MhaCeasedCitizenFileEntry> resultList = new ArrayList<>();
    int iterations =
        ceasedCitizens.isEmpty() ? quantity : Math.min(quantity, ceasedCitizens.size());
    for (int i = 0; i < iterations; i++) {
      MhaCeasedCitizenFileEntry mhaCeasedCitizenFileEntry =
          MhaCeasedCitizenFileEntry.builder()
              .nric(ceasedCitizens.get(i).getNric())
              .name(ceasedCitizens.get(i).getName())
              .nationality(Phaker.randomNonSGCountryCode())
              .citizenRenunciationDate(dateUtils.daysBeforeToday(15).format(dateUtils.DATETIME_FORMATTER_YYYYMMDD))
              .build();
      batchFileDataWriter.chunkOrWrite(mhaCeasedCitizenFileEntry.toString());
      resultList.add(mhaCeasedCitizenFileEntry);
    }
    return resultList;
  }

  private List<MhaCeasedCitizenFileEntry> getCeasedCitizenWhoHasAForeignNationality(
      int numOfRecords) {
    List<MhaCeasedCitizenFileEntry> resultList = new ArrayList<>();
    for (int i = 0; i < numOfRecords; i++) {
      PersonId personId = personFactory.createNewSCPersonId();
      nationalityRepo.updateNationality(
          NationalityEnum.randomNonSGCountryCode(), personId.getPerson());

      PersonName personName = personNameRepo.findByPerson(personId.getPerson());
      MhaCeasedCitizenFileEntry mhaCeasedCitizenFileEntry =
          MhaCeasedCitizenFileEntry.builder()
              .nric(personId.getNaturalId())
              .name(personName.getName())
              .nationality(Phaker.randomNonSGCountryCode())
              .citizenRenunciationDate(dateUtils.daysBeforeToday(6).format(dateUtils.DATETIME_FORMATTER_YYYYMMDD))
              .build();
      batchFileDataWriter.chunkOrWrite(mhaCeasedCitizenFileEntry.toString());
      resultList.add(mhaCeasedCitizenFileEntry);
    }
    return resultList;
  }

  private List<MhaCeasedCitizenFileEntry> getCeasedCitizenWithSG(int numOfRecords) {
    List<MhaCeasedCitizenFileEntry> resultList = new ArrayList<>();
    for (int i = 0; i < numOfRecords; i++) {
      PersonId personId = personFactory.createNewSCPersonId();
      PersonName personName = personNameRepo.findByPerson(personId.getPerson());
      MhaCeasedCitizenFileEntry mhaCeasedCitizenFileEntry =
          MhaCeasedCitizenFileEntry.builder()
              .nric(personId.getNaturalId())
              .name(personName.getName())
              .nationality("SG")
              .citizenRenunciationDate(dateUtils.daysBeforeToday(6).format(dateUtils.DATETIME_FORMATTER_YYYYMMDD))
              .build();
      batchFileDataWriter.chunkOrWrite(mhaCeasedCitizenFileEntry.toString());
      resultList.add(mhaCeasedCitizenFileEntry);
    }
    return resultList;
  }

  private List<MhaCeasedCitizenFileEntry> getEmptyNameCitizens(int numOfRecords) {
    List<MhaCeasedCitizenFileEntry> resultList = new ArrayList<>();
    for (int i = 0; i < numOfRecords; i++) {
      MhaCeasedCitizenFileEntry mhaCeasedCitizenFileEntry =
          MhaCeasedCitizenFileEntry.builder()
              .nric(Phaker.validNric())
              .name("")
              .nationality(Phaker.randomNonSGCountryCode())
              .citizenRenunciationDate(dateUtils.daysBeforeToday(6).format(dateUtils.DATETIME_FORMATTER_YYYYMMDD))
              .build();
      batchFileDataWriter.chunkOrWrite(mhaCeasedCitizenFileEntry.toString());
      resultList.add(mhaCeasedCitizenFileEntry);
    }
    return resultList;
  }

  private List<MhaCeasedCitizenFileEntry> getRenunciationAfterCutOffDateCitizens(int numOfRecords) {
    List<MhaCeasedCitizenFileEntry> resultList = new ArrayList<>();
    for (int i = 0; i < numOfRecords; i++) {
      MhaCeasedCitizenFileEntry mhaCeasedCitizenFileEntry =
          MhaCeasedCitizenFileEntry.builder()
              .nric(Phaker.validNric())
              .name(Phaker.validName())
              .nationality(Phaker.randomNonSGCountryCode())
              .citizenRenunciationDate(dateUtils.daysAfterToday(1).format(dateUtils.DATETIME_FORMATTER_YYYYMMDD))
              .build();
      batchFileDataWriter.chunkOrWrite(mhaCeasedCitizenFileEntry.toString());
      resultList.add(mhaCeasedCitizenFileEntry);
    }
    return resultList;
  }

  private List<MhaCeasedCitizenFileEntry> getAwardedSCCitizens(
      int numOfRecords, List<String> nrics) {
    List<MhaCeasedCitizenFileEntry> resultList = new ArrayList<>();
    List<String> clonedNrics = new ArrayList<>(nrics);
    int iterations = nrics.size() == 0 ? numOfRecords : Math.min(numOfRecords, nrics.size());
    for (int i = 0; i < iterations; i++) {
      String nric = "";
      if (clonedNrics.size() > 0) {
        int randomIndex = random.nextInt(clonedNrics.size());
        nric = clonedNrics.get(randomIndex);
        clonedNrics.remove(randomIndex);
      }
      MhaCeasedCitizenFileEntry mhaCeasedCitizenFileEntry =
          MhaCeasedCitizenFileEntry.builder()
              .nric(nric.isEmpty() ? Phaker.validNric() : nric)
              .name(Phaker.validName())
              .nationality("SG")
              .citizenRenunciationDate(dateUtils.daysBeforeToday(6).format(dateUtils.DATETIME_FORMATTER_YYYYMMDD))
              .build();
      batchFileDataWriter.chunkOrWrite(mhaCeasedCitizenFileEntry.toString());
      resultList.add(mhaCeasedCitizenFileEntry);
    }
    return resultList;
  }

  private List<MhaCeasedCitizenFileEntry> getDuplicateCitizens(
      int numberOfDuplication, List<MhaCeasedCitizenFileEntry> citizens) {
    List<MhaCeasedCitizenFileEntry> results = new ArrayList<>();
    if (numberOfDuplication != 0) {
      citizens.forEach(
          c -> {
            List<MhaCeasedCitizenFileEntry> duplicateList =
                Collections.nCopies(numberOfDuplication - 1, c);
            for (MhaCeasedCitizenFileEntry dup : duplicateList) {
              batchFileDataWriter.chunkOrWrite(dup.toString());
            }
            results.addAll(duplicateList);
          });
    }
    return results;
  }

  private List<MhaCeasedCitizenFileEntry> getPartialDuplicateCitizens(
          int numberOfDuplication, List<MhaCeasedCitizenFileEntry> citizens) {
    
    List<MhaCeasedCitizenFileEntry> results = new ArrayList<>();
    
    if (numberOfDuplication != 0) {
      citizens.forEach(
              c -> {
                List<MhaCeasedCitizenFileEntry> duplicateList =
                        Collections.nCopies(numberOfDuplication - 1, c);
                for (MhaCeasedCitizenFileEntry dup : duplicateList) {
                  
                  // randomize name & renunciation date
                  dup.setName(Phaker.validName());
                  dup.setCitizenRenunciationDate(
                          dateUtils.yearsBeforeDate(
                                  dateUtils.parse(dup.getCitizenRenunciationDate()), random.nextInt(10))
                                  .format(dateUtils.DATETIME_FORMATTER_YYYYMMDD));
                  
                  batchFileDataWriter.chunkOrWrite(dup.toString());
                }
                results.addAll(duplicateList);
              });
    }
    return results;
  }

  public List<String> formatEntries(DataTable dataTable) {
    List<Map<String, String>> dataRows = dataTable.asMaps(String.class, String.class);
    List<MhaCeasedCitizenFileEntry> entries =
        dataRows.stream().map(MhaCeasedCitizenFileEntry::new).collect(Collectors.toList());
    return entries.stream().map(MhaCeasedCitizenFileEntry::toString).collect(Collectors.toList());
  }

  public void writeToFile(String dateStr, List<String> entries) {
    LocalDate localDate = dateUtils.parse(dateStr);
    batchFileDataWriter.begin(generateSingleHeader(localDate), FileTypeEnum.MHA_CEASED_CITIZEN, 10);
    entries.forEach(entry -> batchFileDataWriter.chunkOrWrite(entry));
    batchFileDataWriter.end();
  }

  private String nationalityFieldOptions(String option) {

    String nationality;

    switch (option.toUpperCase()) {
      case TestConstants.OPTION_VALID:
        nationality = Phaker.randomNonSGCountryCode();
        break;
      case TestConstants.OPTION_INVALID:
        nationality = Phaker.SG;
        break;
      case TestConstants.OPTION_SPACES:
        nationality = StringUtils.rightPad(StringUtils.SPACE, 2);
        break;
      default:
        nationality = option;
    }

    return nationality;
  }

  private String renunciationDateFieldOptions(String dateOption) {

    String renunciationDate;

    switch (dateOption.toUpperCase()) {
      case TestConstants.OPTION_VALID:
        renunciationDate = dateUtils.now().format(dateUtils.DATETIME_FORMATTER_YYYYMMDD);
        break;
      case TestConstants.OPTION_INVALID:
        renunciationDate = dateUtils.now().format(dateUtils.DATETIME_FORMATTER_DDMMYYYY);
        break;
      case TestConstants.OPTION_SPACES:
        renunciationDate = StringUtils.rightPad(StringUtils.SPACE, 8);
        break;
      case TestConstants.OPTION_BLANK:
        renunciationDate = StringUtils.EMPTY_STRING;
        break;
      case TestConstants.OPTION_FUTURE_DATE:
        renunciationDate = Phaker.validFutureDate().format(dateUtils.DATETIME_FORMATTER_YYYYMMDD);
        break;
      default:
        renunciationDate = dateOption;
    }

    return renunciationDate;
  }
}
