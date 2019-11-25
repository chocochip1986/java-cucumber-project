package cdit_automation.data_helpers;

import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.CeasedCitizenNationalityEnum;
import cdit_automation.enums.CeasedCitizenNricCancelledStatusEnum;
import cdit_automation.models.CeasedCitizen;
import cdit_automation.models.PersonId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class MhaCeasedCitizenFileDataPrep extends BatchFileDataPrep {

  @Autowired PersonIdService personIdService;

  public List<PersonId> populateSingaporeCitizenInDB(int numOfRecords) {
    List<PersonId> result = new ArrayList<>();
    for (int i = 0; i < numOfRecords; i++) {
      PersonId personId = personIdService.createNewSCPersonId();
      result.add(personId);
    }
    return result;
  }

  public List<PersonId> populateDualCitizenInDB(int numOfRecords) {
    List<PersonId> result = new ArrayList<>();
    for (int i = 0; i < numOfRecords; i++) {
      PersonId personId = personIdService.createDualCitizen();
      result.add(personId);
    }
    return result;
  }

  public CeasedCitizen.CeasedCitizenBuilder ceasedSingaporeCitizenBuilder() {
    return ceasedCitizenBuilder().nationality(CeasedCitizenNationalityEnum.SG);
  }

  public CeasedCitizen.CeasedCitizenBuilder ceasedDualCitizenBuilder() {
    return ceasedCitizenBuilder().nationality(CeasedCitizenNationalityEnum.BLANK);
  }

  private CeasedCitizen.CeasedCitizenBuilder ceasedCitizenBuilder() {
    return CeasedCitizen.builder()
        .nric(Phaker.validNric())
        .name(Phaker.validName())
        .citizenRenunciationDate(
            LocalDate.now().minusDays(6))
        .nricCancelledStatus(CeasedCitizenNricCancelledStatusEnum.YES);
  }
}
