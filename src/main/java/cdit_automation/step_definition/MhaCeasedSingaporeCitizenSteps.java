package cdit_automation.step_definition;

import cdit_automation.asserts.Assert;
import cdit_automation.data_helpers.batch_entities.MhaCeasedCitizenFileEntry;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.models.CeasedCitizenValidated;
import cdit_automation.models.Nationality;
import cdit_automation.models.PersonDetail;
import cdit_automation.models.PersonId;
import cdit_automation.models.PersonName;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Ignore
public class MhaCeasedSingaporeCitizenSteps extends AbstractSteps {

  @Given("the database populated with the following data:")
  public void theDatabasePopulatedWithTheFollowingData(DataTable dataTable) {
    List<Map<String, String>> map = dataTable.asMaps(String.class, String.class);
    mhaCeasedCitizenFileDataPrep.initDatabase(
        dataTable.asMaps(String.class, String.class), testContext);
  }

  @Given("the file has the following details:")
  public void theFileHasTheFollowingDetails(DataTable dataTable) throws IOException {
    batchFileDataWriter.begin(mhaBulkFileDataPrep.generateSingleHeader(), FileTypeEnum.MHA_CEASED_CITIZEN, null);

    mhaCeasedCitizenFileDataPrep.createBodyOfTestScenarios(
        dataTable.asMaps(String.class, String.class), testContext);
    batchFileDataWriter.end();
  }

  @And("^I verify the the people listed in the file have nationality of (.*)$")
  public void nationalityOfAllPersonShouldChangeToNON_SINGAPORE_CITIZEN(
      NationalityEnum nationalityEnum) {
    List<MhaCeasedCitizenFileEntry> ceasedCitizens = testContext.get("ceasedCitizens");
    ceasedCitizens.forEach(
        c -> {
          PersonId p = personIdRepo.findByNaturalId(c.getNric());
          Nationality n = nationalityRepo.findNationalityByPerson(p.getPerson());
            testAssert.assertEquals(
              nationalityEnum,
              n.getNationality(),
              "Expecting person with nric : ["
                  + p.getNaturalId()
                  + "] to have nationality of : "
                  + nationalityEnum);
        });
  }

  @And("I verify the the people listed in the file have nric cancelled status of {int}")
  public void iVerifyTheThePeopleListedInTheFileHaveNRIC_CANCELLED_STATUSOf(int status) {
    List<CeasedCitizenValidated> ceasedCitizens = testContext.get("ceasedCitizens");
    ceasedCitizens.forEach(
        c -> {
          PersonId pi = personIdRepo.findByNaturalId(c.getNric());
          PersonDetail pd = personDetailRepo.findByPerson(pi.getPerson());
            testAssert.assertEquals(
              status == 1,
              pd.getIsNricCancelled(),
              "Expecting person with nric : ["
                  + pi.getNaturalId()
                  + "] to have NRIC_CANCELLED_STATUS of "
                  + status);
        });
  }

  @And("I verify the previous nationality valid till timestamp is the day before renunciation date at 2359HR")
  public void iVerifyThePreviousNationalityValidTillTimestampIsTheRenunciationDateAtHR() {
    List<MhaCeasedCitizenFileEntry> ceasedCitizens = testContext.get("ceasedCitizens");
    ceasedCitizens.forEach(
        c -> {
          Date recordValidityDate =
              dateUtils.localDateToDate(c.getCitizenRenunciationDate().minusDays(1));
          Timestamp expectedValidTill =
              dateUtils.endOfDayToTimestamp(c.getCitizenRenunciationDate().minusDays(1));
          PersonId p = personIdRepo.findByNaturalId(c.getNric());
          Nationality n =
              nationalityRepo.findNationalityByPerson(p.getPerson(), recordValidityDate);
          Timestamp actualValidTill =
              n.getBiTemporalData().getBusinessTemporalData().getValidTill();
            testAssert.assertEquals(
              expectedValidTill,
              actualValidTill,
              "Expecting previous nationality valid till date to be : [ "
                  + expectedValidTill
                  + " ] but retrieved : [ "
                  + actualValidTill
                  + " ]");
        });
  }

  @And("I verify the supersede nationality valid from timestamp is the day after renunciation date")
  public void iVerifyTheSupersedeNationalityValidFromTimestampIsTheDayAfterRenunciationDate() {
    List<MhaCeasedCitizenFileEntry> ceasedCitizens = testContext.get("ceasedCitizens");
    ceasedCitizens.forEach(
        c -> {
          Timestamp expectedValidFrom =
              dateUtils.beginningOfDayToTimestamp(c.getCitizenRenunciationDate());
          PersonId p = personIdRepo.findByNaturalId(c.getNric());
          Nationality n = nationalityRepo.findNationalityByPerson(p.getPerson());
          Timestamp actualValidFrom =
              n.getBiTemporalData().getBusinessTemporalData().getValidFrom();
            testAssert.assertEquals(
              expectedValidFrom,
              actualValidFrom,
              "Expecting previous nationality valid till date to be : [ "
                  + expectedValidFrom
                  + " ] but retrieved : [ "
                  + actualValidFrom
                  + " ]");
        });
  }

    @Given("^that ([a-z_]+) was convert from a dual citizen to a singaporean (\\d+) days ago$")
    public void thatPersonWasConvertFromADualCitizenToASCDaysAgo(String personName, int daysAgo) {
      PersonId personId = personFactory.createDualCitzenTurnSC(dateUtils.daysBeforeToday(daysAgo));
      testContext.set(personName, personId);
    }

    @And("^([a-z_]+)'s citizenship ceased (\\d+) days ago$")
    public void hisCitizenshipCeasedDaysAgo(String personName, int daysAgo) {
        batchFileDataWriter.begin(mhaCeasedCitizenFileDataPrep.generateSingleHeader(), FileTypeEnum.MHA_CEASED_CITIZEN, null);
        PersonId personId = testContext.get(personName);
        PersonName personName1 = personNameRepo.findByPerson(personId.getPerson());
        MhaCeasedCitizenFileEntry mhaCeasedCitizenFileEntry = MhaCeasedCitizenFileEntry.builder()
                .nric(personId.getNaturalId())
                .name(personName1.getName())
                .nationality(NationalityEnum.US.getValue())
                .citizenRenunciationDate(dateUtils.daysBeforeToday(daysAgo))
                .build();
        batchFileDataWriter.chunkOrWrite(mhaCeasedCitizenFileEntry.toString());
        batchFileDataWriter.end();
    }

    @And("^I verify that ([a-z_]+) is not a citizen (\\d+) days ago$")
    public void iVerifyThatPersonIsNotACitizenDaysAgo(String personName, int daysAgo) {
      PersonId personId = testContext.get(personName);
      Nationality curNationality = nationalityRepo.findNationalityByPerson(personId.getPerson(),
              dateUtils.localDateToDate(dateUtils.daysBeforeToday(daysAgo)));
      Nationality prevNationality = nationalityRepo.findNationalityByPerson(personId.getPerson(),
              dateUtils.localDateToDate(dateUtils.daysBeforeToday(daysAgo).minusDays(1)));

      testAssert.assertNotNull(curNationality, "No current nationality for "+personName+" ("+personId.getNaturalId()+")");
      testAssert.assertNotEquals(curNationality.getNationality(), NationalityEnum.SINGAPORE_CITIZEN.getValue(), personName+" ("+personId.getNaturalId()+") still has a singaporean nationality!");
      testAssert.assertEquals(dateUtils.beginningOfDayToTimestamp(dateUtils.daysBeforeToday(daysAgo)),
              curNationality.getBiTemporalData().getBusinessTemporalData().getValidFrom(), personName+" ("+personId.getNaturalId()+") did not start from "+dateUtils.beginningOfDayToTimestamp(dateUtils.daysBeforeToday(daysAgo)));
      testAssert.assertNotNull(prevNationality, "No previous nationality for "+personName+" ("+personId.getNaturalId()+")");
      testAssert.assertEquals(prevNationality.getNationality().getValue(), NationalityEnum.SINGAPORE_CITIZEN.getValue(), personName+" ("+personId.getNaturalId()+") was not singaporean!");
      testAssert.assertEquals(dateUtils.endOfDayToTimestamp(dateUtils.daysBeforeToday(daysAgo).minusDays(1)),
              prevNationality.getBiTemporalData().getBusinessTemporalData().getValidTill(),
              personName+" ("+personId.getNaturalId()+") did not end his/her singaporean nationality on "+dateUtils.endOfDayToTimestamp(dateUtils.daysBeforeToday(daysAgo).minusDays(1)));
    }
}
