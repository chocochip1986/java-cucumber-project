package cdit_automation.step_definition;

import cdit_automation.asserts.Assert;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.CeasedCitizenNationalityEnum;
import cdit_automation.enums.CeasedCitizenNricCancelledStatusEnum;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.models.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class MhaCeasedSingaporeCitizenSteps extends AbstractSteps {

  @Given("the file contain a record that is already exist in the system")
  public void theFileContainARecordThatIsAlreadyExistInTheSystem() throws IOException {
    FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_CEASED_CITIZEN);
    testContext.set("fileReceived", batchFileCreator.fileCreator(fileDetail, "mha_ceased_citizen"));
    PersonId personId = mhaCeasedCitizenFileDataPrep.populateSCs(1).get(0);
    Batch batch = new Batch();
    batch.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
    batchRepo.save(batch);
    CeasedCitizen ceasedCitizen =
        CeasedCitizen.builder()
            .batch(batch)
            .name(Phaker.validName())
            .nationality(CeasedCitizenNationalityEnum.SG)
            .citizenRenunciationDate(dateUtils.daysBeforeToday(30))
            .nric(personId.getNaturalId())
            .nricCancelledStatus(CeasedCitizenNricCancelledStatusEnum.YES)
            .build();
    ceasedCitizenRepo.save(ceasedCitizen);

    List<String> content = new ArrayList<>();
    content.add(mhaCeasedCitizenFileDataPrep.generateDoubleHeader());
    content.add(ceasedCitizen.toString());
    content.add("1");
    batchFileCreator.writeToFile("mha_ceased_citizen.txt", content);
  }

  @Given("the file contain duplicate record")
  public void theFileContainDuplicateRecord() {}

  @Given("the file has the following details:")
  public void theFileHasTheFollowingDetails(DataTable dataTable) throws IOException {
    FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_CEASED_CITIZEN);
    testContext.set("fileReceived", batchFileCreator.fileCreator(fileDetail, "mha_ceased_citizen"));

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
    batchFileCreator.writeToFile("mha_ceased_citizen.txt", listOfIdentifiersToWriteToFile);
  }

  @And("^I verify the batch error message is (.*)")
  public void theBatchErrorMessage(String errorMsg) {
    Batch batch = batchRepo.findByFileReceivedOrderByCreatedAtDesc(testContext.get("fileReceived"));
    Assert.assertEquals(
        true,
        errorMessageRepo.findByBatch(batch).stream()
            .anyMatch(errorMessage -> errorMessage.getMessage().equalsIgnoreCase(errorMsg)),
        "Expecting [" + errorMsg + "] to be thrown");
  }

  @And("^I verify the the people listed in the file have nationality of (.*)$")
  public void nationalityOfAllPersonShouldChangeToNON_SINGAPORE_CITIZEN(
      NationalityEnum nationalityEnum) {
    List<String> ceasedCitizenNrics = testContext.get("ceasedCitizenNrics");
    LocalDate cutOffDate = testContext.get("cutOffDate");
    ceasedCitizenNrics.forEach(
        nric -> {
          PersonId p = personIdRepo.findByNaturalId(nric);
          Nationality n = nationalityRepo.findNationalityByPerson(p.getPerson());
          Assert.assertEquals(
              nationalityEnum,
              n.getNationality(),
              "Expecting person with nric : ["
                  + p.getNaturalId()
                  + "] to have nationality of : "
                  + nationalityEnum);

          //          Timestamp timestamp =
          // n.getBiTemporalData().getBusinessTemporalData().getValidFrom();
          //          Timestamp timestamp1 = dateUtils.beginningOfDayToTimestamp(cutOffDate);
          //
          //          Assert.assertEquals(
          //              timestamp1,
          //              timestamp,
          //              "Expecting supersede nationality's valid from date to be : [ "
          //                  + timestamp1
          //                  + " ] but retrieved : [ "
          //                  + timestamp
          //                  + " ]");

          //          Nationality oldNationality =
          //              nationalityRepo.findNationalityByPerson(
          //                  p.getPerson(), dateUtils.localDateToDate(cutOffDate.minusDays(1)));
          //          Assert.assertEquals(
          //              dateUtils.endOfDayToTimestamp(cutOffDate.minusDays(1)),
          //
          // oldNationality.getBiTemporalData().getBusinessTemporalData().getValidTill(),
          //              "Expecting old nationality valid till date to be : [ "
          //                  + dateUtils.endOfDayToTimestamp(cutOffDate.minusDays(1))
          //                  + " ] but retrieved : [ "
          //                  +
          // oldNationality.getBiTemporalData().getBusinessTemporalData().getValidTill()
          //                  + " ]");
        });
  }

  @And("I verify the the people listed in the file have NRIC_CANCELLED_STATUS of {int}")
  public void iVerifyTheThePeopleListedInTheFileHaveNRIC_CANCELLED_STATUSOf(int status) {
    List<String> ceasedCitizenNrics = testContext.get("ceasedCitizenNrics");
    ceasedCitizenNrics.forEach(
        nric -> {
          PersonId pi = personIdRepo.findByNaturalId(nric);
          PersonDetail pd =
              personDetailRepo.findCurrentPersonDetailByPerson(
                  pi.getPerson(), dateUtils.localDateToDate(dateUtils.now()));
          Assert.assertEquals(
              status == 1,
              pd.getIsNricCancelled(),
              "Expecting person with nric : ["
                  + pi.getNaturalId()
                  + "] to have NRIC_CANCELLED_STATUS of "
                  + status);
        });
  }

  @And("I verify the old nationality [VALID_TILL] timestamp is a day before cut off date at 2359HR")
  public void iVerifyTheOldNationalityVALID_TILLTimestampIsADayBeforeCutOffDateAtHR() {
    LocalDate cutOffDate = testContext.get("cutOffDate");
    Date recordValidityDate = dateUtils.localDateToDate(cutOffDate.minusDays(1));
    Timestamp expectedValidTill = dateUtils.endOfDayToTimestamp(cutOffDate.minusDays(1));

    List<String> nrics = testContext.get("ceasedCitizenNrics");
    nrics.forEach(
        nric -> {
          PersonId p = personIdRepo.findByNaturalId(nric);
          Nationality n =
              nationalityRepo.findNationalityByPerson(p.getPerson(), recordValidityDate);
          Timestamp actualValidTill =
              n.getBiTemporalData().getBusinessTemporalData().getValidTill();
          Assert.assertEquals(
              expectedValidTill,
              actualValidTill,
              "Expecting old nationality valid till date to be : [ "
                  + expectedValidTill
                  + " ] but retrieved : [ "
                  + actualValidTill
                  + " ]");
        });
  }

  @And("I verify the supersede nationality [VALID_FROM] date is cut off date at 0000HR")
  public void iVerifyTheSupersedeNationalityVALID_FROMDateIsCutOffDateAtHR() {
    LocalDate cutOffDate = testContext.get("cutOffDate");
    Timestamp expectedValidFrom = dateUtils.beginningOfDayToTimestamp(cutOffDate);

    List<String> nrics = testContext.get("ceasedCitizenNrics");
    nrics.forEach(
        nric -> {
          PersonId p = personIdRepo.findByNaturalId(nric);
          Nationality n =
              nationalityRepo.findNationalityByPerson(p.getPerson());
          Timestamp actualValidFrom =
              n.getBiTemporalData().getBusinessTemporalData().getValidFrom();
          Assert.assertEquals(
              expectedValidFrom,
              actualValidFrom,
              "Expecting old nationality valid till date to be : [ "
                  + expectedValidFrom
                  + " ] but retrieved : [ "
                  + actualValidFrom
                  + " ]");
        });
  }
}
