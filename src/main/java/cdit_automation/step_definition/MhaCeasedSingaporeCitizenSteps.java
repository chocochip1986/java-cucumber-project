package cdit_automation.step_definition;

import cdit_automation.asserts.Assert;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.models.CeasedCitizenValidated;
import cdit_automation.models.FileDetail;
import cdit_automation.models.Nationality;
import cdit_automation.models.PersonDetail;
import cdit_automation.models.PersonId;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
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
    FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_CEASED_CITIZEN);
    testContext.set("fileReceived", batchFileCreator.replaceFile(fileDetail, FileTypeEnum.MHA_CEASED_CITIZEN.getValue().toLowerCase()));

    List<String> listOfIdentifiersToWriteToFile = new ArrayList<>();
    LocalDate cutOffDate = dateUtils.daysBeforeToday(5);
    testContext.set("cutOffDate", cutOffDate);
    LocalDate extractionDate = dateUtils.daysBeforeToday(3);
    listOfIdentifiersToWriteToFile.add(
        mhaCeasedCitizenFileDataPrep.generateDoubleHeader(extractionDate, cutOffDate));
    List<String> body =
        mhaCeasedCitizenFileDataPrep.createBodyOfTestScenarios(
            dataTable.asMaps(String.class, String.class), testContext);
    listOfIdentifiersToWriteToFile.addAll(body);
    listOfIdentifiersToWriteToFile.add(String.valueOf(body.size()));
    batchFileCreator.writeToFile(FileTypeEnum.MHA_CEASED_CITIZEN.getValue().toLowerCase(), listOfIdentifiersToWriteToFile);
  }

  @And("^I verify the the people listed in the file have nationality of (.*)$")
  public void nationalityOfAllPersonShouldChangeToNON_SINGAPORE_CITIZEN(
      NationalityEnum nationalityEnum) {
    List<CeasedCitizenValidated> ceasedCitizens = testContext.get("ceasedCitizens");
    ceasedCitizens.forEach(
        c -> {
          PersonId p = personIdRepo.findByNaturalId(c.getNric());
          Nationality n = nationalityRepo.findNationalityByPerson(p.getPerson());
          Assert.assertEquals(
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
          Assert.assertEquals(
              status == 1,
              pd.getIsNricCancelled(),
              "Expecting person with nric : ["
                  + pi.getNaturalId()
                  + "] to have NRIC_CANCELLED_STATUS of "
                  + status);
        });
  }

  @And("I verify the previous nationality valid till timestamp is the renunciation date at 2359HR")
  public void iVerifyThePreviousNationalityValidTillTimestampIsTheRenunciationDateAtHR() {
    List<CeasedCitizenValidated> ceasedCitizens = testContext.get("ceasedCitizens");
    ceasedCitizens.forEach(
        c -> {
          Date recordValidityDate =
              dateUtils.localDateToDate(c.getCitizenRenunciationDate().minusDays(1));
          Timestamp expectedValidTill =
              dateUtils.endOfDayToTimestamp(c.getCitizenRenunciationDate());
          PersonId p = personIdRepo.findByNaturalId(c.getNric());
          Nationality n =
              nationalityRepo.findNationalityByPerson(p.getPerson(), recordValidityDate);
          Timestamp actualValidTill =
              n.getBiTemporalData().getBusinessTemporalData().getValidTill();
          Assert.assertEquals(
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
    List<CeasedCitizenValidated> ceasedCitizens = testContext.get("ceasedCitizens");
    ceasedCitizens.forEach(
        c -> {
          Timestamp expectedValidFrom =
              dateUtils.beginningOfDayToTimestamp(c.getCitizenRenunciationDate().plusDays(1));
          PersonId p = personIdRepo.findByNaturalId(c.getNric());
          Nationality n = nationalityRepo.findNationalityByPerson(p.getPerson());
          Timestamp actualValidFrom =
              n.getBiTemporalData().getBusinessTemporalData().getValidFrom();
          Assert.assertEquals(
              expectedValidFrom,
              actualValidFrom,
              "Expecting previous nationality valid till date to be : [ "
                  + expectedValidFrom
                  + " ] but retrieved : [ "
                  + actualValidFrom
                  + " ]");
        });
  }

  @And(
      "I verify the previous person detail valid till timestamp is the renunciation date at 2359HR")
  public void iVerifyThePreviousPersonDetailValidTillTimestampIsTheRenunciationDateAtHR() {
    List<CeasedCitizenValidated> ceasedCitizens = testContext.get("ceasedCitizens");
    ceasedCitizens.forEach(
        c -> {
          Date recordValidityDate =
              dateUtils.localDateToDate(c.getCitizenRenunciationDate().minusDays(1));
          Timestamp expectedValidTill =
              dateUtils.endOfDayToTimestamp(c.getCitizenRenunciationDate());
          PersonId pi = personIdRepo.findByNaturalId(c.getNric());
          PersonDetail pd = personDetailRepo.findByPerson(pi.getPerson(), recordValidityDate);
          Timestamp actualValidTill =
              pd.getBiTemporalData().getBusinessTemporalData().getValidTill();
          Assert.assertEquals(
              expectedValidTill,
              actualValidTill,
              "Expecting previous person detail valid till date to be : [ "
                  + expectedValidTill
                  + " ] but retrieved : [ "
                  + actualValidTill
                  + " ]");
        });
  }

  @And(
      "I verify the supersede person detail valid from timestamp is the day after renunciation date")
  public void iVerifyTheSupersedePersonDetailValidFromTimestampIsTheDayAfterRenunciationDate() {
    List<CeasedCitizenValidated> ceasedCitizens = testContext.get("ceasedCitizens");
    ceasedCitizens.forEach(
        c -> {
          Timestamp expectedValidFrom =
              dateUtils.beginningOfDayToTimestamp(c.getCitizenRenunciationDate().plusDays(1));
          PersonId pi = personIdRepo.findByNaturalId(c.getNric());
          PersonDetail pd = personDetailRepo.findByPerson(pi.getPerson());
          Timestamp actualValidFrom =
              pd.getBiTemporalData().getBusinessTemporalData().getValidFrom();
          Assert.assertEquals(
              expectedValidFrom,
              actualValidFrom,
              "Expecting previous nationality valid till date to be : [ "
                  + expectedValidFrom
                  + " ] but retrieved : [ "
                  + actualValidFrom
                  + " ]");
        });
  }
}
