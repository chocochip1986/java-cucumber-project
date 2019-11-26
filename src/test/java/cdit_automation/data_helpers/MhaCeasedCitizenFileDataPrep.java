package cdit_automation.data_helpers;

import cdit_automation.configuration.StepDefLevelTestContext;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.CeasedCitizenNationalityEnum;
import cdit_automation.enums.CeasedCitizenNricCancelledStatusEnum;
import cdit_automation.models.CeasedCitizen;
import cdit_automation.models.PersonId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MhaCeasedCitizenFileDataPrep extends BatchFileDataPrep {

  @Autowired PersonIdService personIdService;

  public List<String> createBodyOfTestScenarios(
      List<Map<String, String>> list, StepDefLevelTestContext testContext) {
    Map<String, String> map = list.get(0);
    List<PersonId> scPersonIds = populateSCs(parseStringSize(map.get("PresentSingaporeCitizen")));
    List<PersonId> dcPersonIds = populateDCs(parseStringSize(map.get("PresentDualCitizen")));
    List<CeasedCitizen> ceasedSCs =
        populateCeasedCitizens(parseStringSize(map.get("CeasedSingaporeCitizen")), scPersonIds);
    List<CeasedCitizen> ceasedDCs =
        populateCeasedCitizens(parseStringSize(map.get("CeasedDualCitizen")), dcPersonIds);
    List<CeasedCitizen> emptyNameCeasedCitizens =
        populateEmptyNameCeasedCitizens(parseStringSize(map.get("EmptyName")));
    List<CeasedCitizen> renunciationDateAfterCutOffCeasedCitizens =
        populateRenunciationDateAfterCutOffCeasedCitizens(
            parseStringSize(map.get("RenunciationDateAfterCutOff")));
    List<CeasedCitizen> awardedSCs =
        populateAwardedSingaporeCitizens(
            parseStringSize(map.get("AwardedSingaporeCitizen")), scPersonIds);

    List<String> ceasedCitizenNrics = new ArrayList<>();
    ceasedSCs.forEach(x -> ceasedCitizenNrics.add(x.getNric()));
    ceasedDCs.forEach(x -> ceasedCitizenNrics.add(x.getNric()));
    emptyNameCeasedCitizens.forEach(x -> ceasedCitizenNrics.add(x.getNric()));
    renunciationDateAfterCutOffCeasedCitizens.forEach(x -> ceasedCitizenNrics.add(x.getNric()));
    awardedSCs.forEach(x -> ceasedCitizenNrics.add(x.getNric()));
    testContext.set("ceasedCitizenNrics", ceasedCitizenNrics);

    return Stream.of(
            ceasedSCs.stream().map(CeasedCitizen::toString).collect(Collectors.toList()),
            ceasedDCs.stream().map(CeasedCitizen::toString).collect(Collectors.toList()),
            emptyNameCeasedCitizens.stream()
                .map(CeasedCitizen::toString)
                .collect(Collectors.toList()),
            renunciationDateAfterCutOffCeasedCitizens.stream()
                .map(CeasedCitizen::toString)
                .collect(Collectors.toList()),
            awardedSCs.stream().map(CeasedCitizen::toString).collect(Collectors.toList()))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
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

  private List<CeasedCitizen> populateCeasedCitizens(int numOfRecords, List<PersonId> personIds) {
    List<CeasedCitizen> resultList = new ArrayList<>();
    int iterations =
        personIds.size() == 0 ? numOfRecords : Math.min(numOfRecords, personIds.size());
    for (int i = 0; i < iterations; i++) {
      PersonId personId = null;
      if (personIds.size() > 0) {
        personId = personIds.get(personIds.size() - 1 - i);
      }
      resultList.add(populateCeasedCitizen(personId, CeasedCitizenNationalityEnum.BLANK));
    }
    return resultList;
  }

  private CeasedCitizen populateCeasedCitizen(
      PersonId personId, CeasedCitizenNationalityEnum statusEnum) {
    return CeasedCitizen.builder()
        .name(Phaker.validName())
        .nric(personId == null ? Phaker.validNric() : personId.getNaturalId())
        .nationality(statusEnum)
        .nricCancelledStatus(CeasedCitizenNricCancelledStatusEnum.YES)
        .citizenRenunciationDate(LocalDate.now().minusDays(6))
        .build();
  }

  private List<CeasedCitizen> populateEmptyNameCeasedCitizens(int numOfRecords) {
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

  private List<CeasedCitizen> populateRenunciationDateAfterCutOffCeasedCitizens(int numOfRecords) {
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

  private List<CeasedCitizen> populateAwardedSingaporeCitizens(
      int numOfRecords, List<PersonId> personIds) {
    List<CeasedCitizen> resultList = new ArrayList<>();
    int iterations =
        personIds.size() == 0 ? numOfRecords : Math.min(numOfRecords, personIds.size());
    for (int i = 0; i < iterations; i++) {
      PersonId personId = null;
      if (personIds.size() > 0) {
        personId = personIds.get(personIds.size() - 1 - i);
      }
      resultList.add(populateCeasedCitizen(personId, CeasedCitizenNationalityEnum.SG));
    }
    return resultList;
  }
}
