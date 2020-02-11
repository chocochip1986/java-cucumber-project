package cdit_automation.data_helpers;

import cdit_automation.configuration.StepDefLevelTestContext;
import cdit_automation.data_helpers.batch_entities.MhaCeasedCitizenFileEntry;
import cdit_automation.data_helpers.factories.PersonFactory;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.models.*;
import cdit_automation.models.embeddables.BiTemporalData;
import cdit_automation.models.embeddables.BusinessTemporalData;
import cdit_automation.models.embeddables.DbTemporalData;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MhaCeasedCitizenFileDataPrep extends BatchFileDataPrep {

  private Random random = new Random();
  private BiTemporalData biTemporalData = new BiTemporalData();

  public void initDatabase(List<Map<String, String>> list, StepDefLevelTestContext testContext) {
    Map<String, String> map = list.get(0);
    List<PersonId> scPersonIds = populateSCs(parseStringSize(map.get("SingaporeCitizen")));
    List<PersonId> dcPersonIds = populateDCs(parseStringSize(map.get("DualCitizen")));
    List<String> availableNrics =
        Stream.concat(scPersonIds.stream(), dcPersonIds.stream())
            .map(PersonId::getNaturalId)
            .collect(Collectors.toList());
    List<CeasedCitizenValidated> ceasedCitizens =
        populateCeasedCitizens(parseStringSize(map.get("CeasedCitizen")), availableNrics);
    List<String> usedNrics =
        ceasedCitizens.stream().map(CeasedCitizenValidated::getNric).collect(Collectors.toList());
    availableNrics.removeIf(usedNrics::contains);

    testContext.set("availableNrics", availableNrics);
    testContext.set("presentCeasedCitizen", ceasedCitizens);
  }

  public List<String> createBodyOfTestScenarios(
      List<Map<String, String>> list, StepDefLevelTestContext testContext) {
    Map<String, String> map = list.get(0);
    List<String> availableNrics =
        testContext.get("availableNrics") == null
            ? new ArrayList<>()
            : testContext.get("availableNrics");
    List<CeasedCitizenValidated> presentCeasedCitizen =
        testContext.get("presentCeasedCitizen") == null
            ? new ArrayList<>()
            : testContext.get("presentCeasedCitizen");

    List<MhaCeasedCitizenFileEntry> citizens =
        getCitizens(parseStringSize(map.get("CeasedCitizen")), availableNrics);
    List<MhaCeasedCitizenFileEntry> repeatedCitizens =
        getRepeatedCitizens(
            parseStringSize(map.get("RepeatedCeasedCitizen")), presentCeasedCitizen);
    List<MhaCeasedCitizenFileEntry> emptyNameCitizens =
        getEmptyNameCitizens(parseStringSize(map.get("EmptyName")));
    List<MhaCeasedCitizenFileEntry> renunciationAfterCutOffDateCitizens =
        getRenunciationAfterCutOffDateCitizens(
            parseStringSize(map.get("RenunciationDateAfterCutOff")));
    List<MhaCeasedCitizenFileEntry> awardedCitizens =
        getAwardedSCCitizens(parseStringSize(map.get("AwardedSingaporeCitizen")), availableNrics);
    List<MhaCeasedCitizenFileEntry> duplicateCitizens =
        getDuplicateCitizens(
            parseStringSize(map.get("NumberOfDuplication")),
            Stream.concat(citizens.stream(), repeatedCitizens.stream())
                .collect(Collectors.toList()));
    List<MhaCeasedCitizenFileEntry> ceasedCitizenWithSGCountryCode =
            getCeasedCitizenWithSG(parseStringSize(map.get("CeasedCitizenWithSGCountryCode")));

    List<MhaCeasedCitizenFileEntry> ceasedCitizens =
        Stream.of(
                citizens,
                repeatedCitizens,
                emptyNameCitizens,
                renunciationAfterCutOffDateCitizens,
                awardedCitizens,
                duplicateCitizens,
                ceasedCitizenWithSGCountryCode)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    testContext.set("ceasedCitizens", ceasedCitizens);

    return ceasedCitizens.stream().map(MhaCeasedCitizenFileEntry::toString).collect(Collectors.toList());
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
            .gender(currentPersonDetail.getGender())
            .biTemporalData(newBiTemporalData)
            .build();
    personDetailRepo.save(newPersonDetail);
  }

  private void updateNationality(Batch batch, CeasedCitizenValidated ceasedCitizen, PersonId personId) {
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

      MhaCeasedCitizenFileEntry mhaCeasedCitizenFileEntry = MhaCeasedCitizenFileEntry.builder()
              .nric(nric.isEmpty() ? Phaker.validNric() : nric)
              .name(Phaker.validName())
              .nationality(Phaker.randomNonSGCountryCode())
              .citizenRenunciationDate(dateUtils.daysBeforeToday(15))
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
      MhaCeasedCitizenFileEntry mhaCeasedCitizenFileEntry = MhaCeasedCitizenFileEntry.builder()
              .nric(ceasedCitizens.get(i).getNric())
              .name(ceasedCitizens.get(i).getName())
              .nationality(Phaker.randomNonSGCountryCode())
              .citizenRenunciationDate(dateUtils.daysBeforeToday(15))
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
      MhaCeasedCitizenFileEntry mhaCeasedCitizenFileEntry = MhaCeasedCitizenFileEntry.builder()
              .nric(personId.getNaturalId())
              .name(personName.getName())
              .nationality("SG")
              .citizenRenunciationDate(dateUtils.daysBeforeToday(6))
              .build();
      batchFileDataWriter.chunkOrWrite(mhaCeasedCitizenFileEntry.toString());
      resultList.add(mhaCeasedCitizenFileEntry);
    }
    return resultList;
  }

  private List<MhaCeasedCitizenFileEntry> getEmptyNameCitizens(int numOfRecords) {
    List<MhaCeasedCitizenFileEntry> resultList = new ArrayList<>();
    for (int i = 0; i < numOfRecords; i++) {
      MhaCeasedCitizenFileEntry mhaCeasedCitizenFileEntry = MhaCeasedCitizenFileEntry.builder()
              .nric(Phaker.validNric())
              .name("")
              .nationality(Phaker.randomNonSGCountryCode())
              .citizenRenunciationDate(dateUtils.daysBeforeToday(6))
              .build();
      batchFileDataWriter.chunkOrWrite(mhaCeasedCitizenFileEntry.toString());
      resultList.add(mhaCeasedCitizenFileEntry);
    }
    return resultList;
  }

  private List<MhaCeasedCitizenFileEntry> getRenunciationAfterCutOffDateCitizens(int numOfRecords) {
    List<MhaCeasedCitizenFileEntry> resultList = new ArrayList<>();
    for (int i = 0; i < numOfRecords; i++) {
      MhaCeasedCitizenFileEntry mhaCeasedCitizenFileEntry = MhaCeasedCitizenFileEntry.builder()
              .nric(Phaker.validNric())
              .name(Phaker.validName())
              .nationality(Phaker.randomNonSGCountryCode())
              .citizenRenunciationDate(dateUtils.daysBeforeToday(10))
              .build();
      batchFileDataWriter.chunkOrWrite(mhaCeasedCitizenFileEntry.toString());
      resultList.add(mhaCeasedCitizenFileEntry);
    }
    return resultList;
  }

  private List<MhaCeasedCitizenFileEntry> getAwardedSCCitizens(int numOfRecords, List<String> nrics) {
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
      MhaCeasedCitizenFileEntry mhaCeasedCitizenFileEntry = MhaCeasedCitizenFileEntry.builder()
              .nric(nric.isEmpty() ? Phaker.validNric() : nric)
              .name(Phaker.validName())
              .nationality("SG")
              .citizenRenunciationDate(dateUtils.daysBeforeToday(6))
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
      citizens.forEach(c -> {
        List<MhaCeasedCitizenFileEntry> duplicateList = Collections.nCopies(numberOfDuplication - 1, c);
        for( MhaCeasedCitizenFileEntry dup : duplicateList ) {
          batchFileDataWriter.chunkOrWrite(dup.toString());
        }
        results.addAll(duplicateList);
      });
    }
    return results;
  }
}
