package cdit_automation.step_definition;

import cdit_automation.constants.Constants;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.models.Batch;
import cdit_automation.models.Nationality;
import cdit_automation.models.PersonId;
import cdit_automation.models.embeddables.BiTemporalData;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public class MhaNewCitizenSteps extends AbstractSteps {
  @Given("^a (MHA_NEW_CITIZEN) file with the following details$")
  public void aMHA_NEW_CITIZENFileWithTheFollowingDetails(FileTypeEnum fileTypeEnum, DataTable dataTable) {
    List<String> formattedEntries = this.mhaNewCitizenFileDataPrep.formatEntries(dataTable);
    mhaNewCitizenFileDataPrep.writeToFile(formattedEntries);
  }

  @Given("^a MHA_NEW_CITIZEN file contains a (.*) invalid date of run$")
  public void aMHA_NEW_CITIZENFileContainsAnInvalidDateOfRun(String problem, DataTable dataTable) {
    if (problem.equals("FutureDate")) {
      problem = dateUtils.daysAfterToday(1).format(Phaker.DATETIME_FORMATTER_YYYYMMDD);
    }
    List<String> formattedEntries = mhaNewCitizenFileDataPrep.formatEntries(dataTable);
    mhaNewCitizenFileDataPrep.writeToFile(problem, formattedEntries);
  }

  @And("^([a-z_]+) became a singapore citizen (\\d+) days ago$")
  public void personBecameASingaporeCitizenDaysAgo(String personName, int daysAgo) {
    PersonId personId = testContext.get(personName);

    LocalDate citizenshipAttainmentDate = dateUtils.daysBeforeToday(daysAgo);
    Batch batch = Batch.createCompleted();

    BiTemporalData nationalityBiTemporalData = BiTemporalData.create(
            dateUtils.beginningOfDayToTimestamp(citizenshipAttainmentDate.minusDays(1l)),
            Timestamp.valueOf(Constants.INFINITE_LOCAL_DATE_TIME));
    Nationality prevNationality = nationalityRepo.findNationalityByPerson(personId.getPerson());
    nationalityRepo.updateValidTill(dateUtils.endOfDayToTimestamp(citizenshipAttainmentDate.minusDays(1l)), prevNationality.getId());
    Nationality curNationality = Nationality.create(batch, personId.getPerson(), NationalityEnum.SINGAPORE_CITIZEN, nationalityBiTemporalData, dateUtils.beginningOfDayToTimestamp(citizenshipAttainmentDate), null);

    batchRepo.save(batch);
    nationalityRepo.save(curNationality);
  }
}
