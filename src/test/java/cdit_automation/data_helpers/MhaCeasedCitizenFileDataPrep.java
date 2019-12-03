package cdit_automation.data_helpers;

import cdit_automation.configuration.StepDefLevelTestContext;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.CeasedCitizenNationalityEnum;
import cdit_automation.enums.CeasedCitizenNricCancelledStatusEnum;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.models.*;
import cdit_automation.models.embeddables.BiTemporalData;
import cdit_automation.models.embeddables.BusinessTemporalData;
import cdit_automation.models.embeddables.DbTemporalData;
import cdit_automation.repositories.BatchRepo;
import cdit_automation.repositories.CeasedCitizenRepo;
import cdit_automation.repositories.NationalityRepo;
import cdit_automation.repositories.PersonIdRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MhaCeasedCitizenFileDataPrep extends BatchFileDataPrep {

  @Autowired BatchRepo batchRepo;
  @Autowired CeasedCitizenRepo ceasedCitizenRepo;
  @Autowired PersonIdRepo personIdRepo;
  @Autowired NationalityRepo nationalityRepo;

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
    List<CeasedCitizen> ceasedCitizens =
        populateCeasedCitizens(parseStringSize(map.get("CeasedCitizen")), availableNrics);
    List<String> usedNrics =
        ceasedCitizens.stream().map(CeasedCitizen::getNric).collect(Collectors.toList());
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
    List<CeasedCitizen> presentCeasedCitizen =
        testContext.get("presentCeasedCitizen") == null
            ? new ArrayList<>()
            : testContext.get("presentCeasedCitizen");

    List<CeasedCitizen> citizens =
        getCitizens(parseStringSize(map.get("CeasedCitizen")), availableNrics);
    List<CeasedCitizen> repeatedCitizens =
        getRepeatedCitizens(
            parseStringSize(map.get("RepeatedCeasedCitizen")), presentCeasedCitizen);
    List<CeasedCitizen> emptyNameCitizens =
        getEmptyNameCitizens(parseStringSize(map.get("EmptyName")));
    List<CeasedCitizen> renunciationAfterCutOffDateCitizens =
        getRenunciationAfterCutOffDateCitizens(
            parseStringSize(map.get("RenunciationDateAfterCutOff")));
    List<CeasedCitizen> awardedCitizens =
        getAwardedSCCitizens(parseStringSize(map.get("AwardedSingaporeCitizen")), availableNrics);
    List<CeasedCitizen> duplicateCitizens =
        getDuplicateCitizens(
            parseStringSize(map.get("NumberOfDuplication")),
            Stream.concat(citizens.stream(), repeatedCitizens.stream())
                .collect(Collectors.toList()));

    List<CeasedCitizen> ceasedCitizens =
        Stream.of(
                citizens,
                repeatedCitizens,
                emptyNameCitizens,
                renunciationAfterCutOffDateCitizens,
                awardedCitizens,
                duplicateCitizens)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    testContext.set("ceasedCitizens", ceasedCitizens);

    return ceasedCitizens.stream().map(CeasedCitizen::toString).collect(Collectors.toList());
  }

  public List<PersonId> populateSCs(int numOfRecords) {
    List<PersonId> result = new ArrayList<>();
    for (int i = 0; i < numOfRecords; i++) {
      PersonId personId = personIdService.createNewSCPersonId();
      result.add(personId);
    }
    return result;
  }

  private List<PersonId> populateDCs(int numOfRecords) {
    List<PersonId> result = new ArrayList<>();
    for (int i = 0; i < numOfRecords; i++) {
      PersonId personId = personIdService.createDualCitizen();
      result.add(personId);
    }
    return result;
  }

  private List<CeasedCitizen> populateCeasedCitizens(int quantity, List<String> nrics) {
    Random random = new Random();
    List<String> clonedNrics = new ArrayList<>(nrics);
    List<CeasedCitizen> resultList = new ArrayList<>();
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

      CeasedCitizen ceasedCitizen =
          CeasedCitizen.builder()
              .batch(b)
              .name(Phaker.validName())
              .nationality(CeasedCitizenNationalityEnum.BLANK)
              .citizenRenunciationDate(dateUtils.daysBeforeToday(30))
              .nric(nric)
              .nricCancelledStatus(CeasedCitizenNricCancelledStatusEnum.YES)
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
      Batch batch, CeasedCitizen ceasedCitizen, PersonId personId) {
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
      Batch batch, CeasedCitizen ceasedCitizen, PersonId personId) {
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
            .isNricCancelled(ceasedCitizen.getNricCancelledStatus().getBooleanValue())
            .biTemporalData(newBiTemporalData)
            .build();
    personDetailRepo.save(newPersonDetail);
  }

  private void updateNationality(Batch batch, CeasedCitizen ceasedCitizen, PersonId personId) {
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
      Batch batch, CeasedCitizen ceasedCitizen, PersonId personId) {
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

  private List<CeasedCitizen> getCitizens(int quantity, List<String> nrics) {
    List<String> clonedNrics = new ArrayList<>(nrics);
    List<CeasedCitizen> resultList = new ArrayList<>();
    int iterations = clonedNrics.isEmpty() ? quantity : Math.min(quantity, clonedNrics.size());
    for (int i = 0; i < iterations; i++) {
      String nric = "";
      if (clonedNrics.size() > 0) {
        int randomIndex = random.nextInt(clonedNrics.size());
        nric = clonedNrics.get(randomIndex);
        clonedNrics.remove(randomIndex);
      }

      resultList.add(
          CeasedCitizen.builder()
              .name(Phaker.validName())
              .nationality(CeasedCitizenNationalityEnum.BLANK)
              .citizenRenunciationDate(dateUtils.daysBeforeToday(15))
              .nric(nric.isEmpty() ? Phaker.validNric() : nric)
              .nricCancelledStatus(CeasedCitizenNricCancelledStatusEnum.YES)
              .build());
    }
    return resultList;
  }

  private List<CeasedCitizen> getRepeatedCitizens(
      int quantity, List<CeasedCitizen> ceasedCitizens) {
    List<CeasedCitizen> resultList = new ArrayList<>();
    int iterations =
        ceasedCitizens.isEmpty() ? quantity : Math.min(quantity, ceasedCitizens.size());
    for (int i = 0; i < iterations; i++) {
      resultList.add(
          CeasedCitizen.builder()
              .name(ceasedCitizens.get(i).getName())
              .nationality(CeasedCitizenNationalityEnum.BLANK)
              .citizenRenunciationDate(dateUtils.daysBeforeToday(15))
              .nric(ceasedCitizens.get(i).getNric())
              .nricCancelledStatus(CeasedCitizenNricCancelledStatusEnum.YES)
              .build());
    }
    return resultList;
  }

  private List<CeasedCitizen> getEmptyNameCitizens(int numOfRecords) {
    List<CeasedCitizen> resultList = new ArrayList<>();
    for (int i = 0; i < numOfRecords; i++) {
      resultList.add(
          CeasedCitizen.builder()
              .name("")
              .nric(Phaker.validNric())
              .nationality(CeasedCitizenNationalityEnum.BLANK)
              .nricCancelledStatus(CeasedCitizenNricCancelledStatusEnum.YES)
              .citizenRenunciationDate(LocalDate.now().minusDays(6))
              .build());
    }
    return resultList;
  }

  private List<CeasedCitizen> getRenunciationAfterCutOffDateCitizens(int numOfRecords) {
    List<CeasedCitizen> resultList = new ArrayList<>();
    for (int i = 0; i < numOfRecords; i++) {
      resultList.add(
          CeasedCitizen.builder()
              .name(Phaker.validName())
              .nric(Phaker.validNric())
              .nationality(CeasedCitizenNationalityEnum.BLANK)
              .nricCancelledStatus(CeasedCitizenNricCancelledStatusEnum.YES)
              .citizenRenunciationDate(LocalDate.now().plusDays(10))
              .build());
    }
    return resultList;
  }

  private List<CeasedCitizen> getAwardedSCCitizens(int numOfRecords, List<String> nrics) {
    List<CeasedCitizen> resultList = new ArrayList<>();
    List<String> clonedNrics = new ArrayList<>(nrics);
    int iterations = nrics.size() == 0 ? numOfRecords : Math.min(numOfRecords, nrics.size());
    for (int i = 0; i < iterations; i++) {
      String nric = "";
      if (clonedNrics.size() > 0) {
        int randomIndex = random.nextInt(clonedNrics.size());
        nric = clonedNrics.get(randomIndex);
        clonedNrics.remove(randomIndex);
      }
      resultList.add(
          CeasedCitizen.builder()
              .name(Phaker.validName())
              .nric(nric.isEmpty() ? Phaker.validNric() : nric)
              .nationality(CeasedCitizenNationalityEnum.SG)
              .nricCancelledStatus(CeasedCitizenNricCancelledStatusEnum.YES)
              .citizenRenunciationDate(LocalDate.now().minusDays(6))
              .build());
    }
    return resultList;
  }

  private List<CeasedCitizen> getDuplicateCitizens(
      int numberOfDuplication, List<CeasedCitizen> citizens) {
    List<CeasedCitizen> results = new ArrayList<>();
    if (numberOfDuplication != 0) {
      citizens.forEach(c -> results.addAll(Collections.nCopies(numberOfDuplication - 1, c)));
    }
    return results;
  }
}
