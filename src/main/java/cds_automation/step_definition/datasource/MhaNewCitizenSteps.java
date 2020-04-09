package cds_automation.step_definition.datasource;

import cds_automation.constants.datasource.Constants;
import cds_automation.data_setup.Phaker;
import cds_automation.enums.datasource.FileTypeEnum;
import cds_automation.enums.datasource.NationalityEnum;
import cds_automation.models.datasource.Batch;
import cds_automation.models.datasource.Nationality;
import cds_automation.models.datasource.PersonId;
import cds_automation.models.datasource.embeddables.BiTemporalData;
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
            dateUtils.beginningOfDayToTimestamp(citizenshipAttainmentDate),
            Timestamp.valueOf(Constants.INFINITE_LOCAL_DATE_TIME));
    Nationality prevNationality = nationalityRepo.findNationalityByPerson(personId.getPerson());
    nationalityRepo.updateValidTill(dateUtils.endOfDayToTimestamp(citizenshipAttainmentDate.minusDays(1l)), prevNationality.getId());
    Nationality curNationality = Nationality.create(batch, personId.getPerson(), NationalityEnum.SINGAPORE_CITIZEN, nationalityBiTemporalData, dateUtils.beginningOfDayToTimestamp(citizenshipAttainmentDate), null);

    batchRepo.save(batch);
    nationalityRepo.save(curNationality);
  }

  @Given("^([a-z_]+) who is (\\d+) years old became a new citizen (\\d+) days ago$")
  public void personWhoIsYearsOldBecameANewCitizenDaysAgo(String personName, int age, int daysAgo) {
    LocalDate birthDate = dateUtils.yearsBeforeToday(age);
    LocalDate citizenAttainmentDate = dateUtils.daysBeforeToday(daysAgo);
    PersonId personId = personFactory.createNewCitizenPersonId(personName, birthDate, citizenAttainmentDate);

    testContext.set(personName, personId);
  }
}
