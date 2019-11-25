package cdit_automation.step_definition;

import cdit_automation.asserts.Assert;
import cdit_automation.data_setup.Phaker;
import cdit_automation.enums.CeasedCitizenNationalityEnum;
import cdit_automation.enums.CeasedCitizenNricCancelledStatusEnum;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.models.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class MhaCeasedSingaporeCitizenSteps extends AbstractSteps {

  @Given("the ceased sc file contain a person detail record not found in system")
  public void theCeasedScFileContainAPersonDetailRecordNotFoundInSystem() throws IOException {
    FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_CEASED_CITIZEN);
    testContext.set("fileReceived", batchFileCreator.fileCreator(fileDetail, "mha_ceased_citizen"));
    List<String> content = new ArrayList<>();
    content.add(mhaCeasedCitizenFileDataPrep.generateDoubleHeader());
    CeasedCitizen citizen = mhaCeasedCitizenFileDataPrep.ceasedSingaporeCitizenBuilder().build();
    content.add(citizen.toString());
    content.add("1");
    batchFileCreator.writeToFile("mha_ceased_citizen.txt", content);
  }

  @Given("the ceased sc file contain a record that is already exist in the system")
  public void theCeasedScFileContainARecordThatIsAlreadyExistInTheSystem() throws IOException {
    FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_CEASED_CITIZEN);
    testContext.set("fileReceived", batchFileCreator.fileCreator(fileDetail, "mha_ceased_citizen"));
    PersonId personId = mhaCeasedCitizenFileDataPrep.populateSingaporeCitizenInDB(1).get(0);
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

  @Given("the ceased sc file contain duplicate record")
  public void theCeasedScFileContainDuplicateRecord() {}

  @Given("the ceased sc file contain an invalid name length of zero")
  public void theCeasedScFileContainAnInvalidNameLengthOfZero() throws IOException {
    FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_CEASED_CITIZEN);
    testContext.set("fileReceived", batchFileCreator.fileCreator(fileDetail, "mha_ceased_citizen"));
    PersonId personId = mhaCeasedCitizenFileDataPrep.populateSingaporeCitizenInDB(1).get(0);
    CeasedCitizen ceasedCitizen =
        mhaCeasedCitizenFileDataPrep
            .ceasedSingaporeCitizenBuilder()
            .name("")
            .nric(personId.getNaturalId())
            .build();
    List<String> content = new ArrayList<>();
    content.add(mhaCeasedCitizenFileDataPrep.generateDoubleHeader());
    content.add(ceasedCitizen.toString());
    content.add("1");
    batchFileCreator.writeToFile("mha_ceased_citizen.txt", content);
  }

  @Given(
      "the ceased sc file contain an invalid renunciation date that is after cut off date with no attainment date")
  public void
      theCeasedScFileContainAnInvalidRenunciationDateThatIsAfterCutOffDateWithNoAttainmentDate()
          throws IOException {
    FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_CEASED_CITIZEN);
    testContext.set("fileReceived", batchFileCreator.fileCreator(fileDetail, "mha_ceased_citizen"));

    PersonId personId = mhaCeasedCitizenFileDataPrep.populateSingaporeCitizenInDB(1).get(0);
    CeasedCitizen ceasedCitizen =
        mhaCeasedCitizenFileDataPrep
            .ceasedSingaporeCitizenBuilder()
            .citizenRenunciationDate(LocalDate.now().plusDays(6))
            .build();
    List<String> content = new ArrayList<>();
    content.add(mhaCeasedCitizenFileDataPrep.generateDoubleHeader());
    content.add(ceasedCitizen.toString());
    content.add("1");
    batchFileCreator.writeToFile("mha_ceased_citizen.txt", content);
  }

  @Given("the ceased sc file has the following details:")
  public void theCeasedScFileHasTheFollowingDetails(DataTable dataTable) throws IOException {
    FileDetail fileDetail = fileDetailRepo.findByFileEnum(FileTypeEnum.MHA_CEASED_CITIZEN);
    testContext.set("fileReceived", batchFileCreator.fileCreator(fileDetail, "mha_ceased_citizen"));

    List<Map<String, Integer>> list = dataTable.asMaps(String.class, Integer.class);
    int numOfSingaporeCitizen = list.get(0).get("SINGAPORE CITIZEN");
    int numOfDualCitizen = list.get(0).get("DUAL CITIZEN");

    List<PersonId> singaporeCitizenPersonIds =
        mhaCeasedCitizenFileDataPrep.populateSingaporeCitizenInDB(numOfSingaporeCitizen);
    Stream<CeasedCitizen> scCeasedCitizen =
        singaporeCitizenPersonIds.stream()
            .map(
                personId ->
                    mhaCeasedCitizenFileDataPrep
                        .ceasedSingaporeCitizenBuilder()
                        .nric(personId.getNaturalId())
                        .build());

    List<PersonId> dualCitizenPersonIds =
        mhaCeasedCitizenFileDataPrep.populateDualCitizenInDB(numOfDualCitizen);
    Stream<CeasedCitizen> dcCeasedCitizen =
        dualCitizenPersonIds.stream()
            .map(
                personId ->
                    mhaCeasedCitizenFileDataPrep
                        .ceasedDualCitizenBuilder()
                        .nric(personId.getNaturalId())
                        .build());

    List<PersonId> personIds = new ArrayList<>();
    personIds.addAll(singaporeCitizenPersonIds);
    personIds.addAll(dualCitizenPersonIds);
    testContext.set("personIds", personIds);

    List<String> content = new ArrayList<>();
    content.add(mhaCeasedCitizenFileDataPrep.generateDoubleHeader());
    Stream.concat(scCeasedCitizen, dcCeasedCitizen)
        .forEach(ceasedCitizen -> content.add(ceasedCitizen.toString()));
    content.add(String.valueOf(singaporeCitizenPersonIds.size() + dualCitizenPersonIds.size()));
    batchFileCreator.writeToFile("mha_ceased_citizen.txt", content);
  }

  @And("the batch error message is (.*)")
  public void theBatchErrorMessage(String errorMsg) {
    Batch batch = batchRepo.findByFileReceivedOrderByCreatedAtDesc(testContext.get("fileReceived"));
    Assert.assertEquals(
        true,
        errorMessageRepo.findByBatch(batch).stream()
            .anyMatch(errorMessage -> errorMessage.getMessage().equalsIgnoreCase(errorMsg)),
        "Expecting [" + errorMsg + "] to be thrown");
  }

  @And("^nationality of all person should change to (.*)$")
  public void nationalityOfAllPersonShouldChangeToNON_SINGAPORE_CITIZEN(
      NationalityEnum nationalityEnum) {
    List<PersonId> personIds = testContext.get("personIds");
    personIds.forEach(
        personId -> {
          Nationality n = nationalityRepo.findNationalityByPerson(personId.getPerson());
          Assert.assertEquals(
              nationalityEnum,
              n.getNationality(),
              "Expecting person with nric : ["
                  + personId.getNaturalId()
                  + "] to have nationality of : "
                  + nationalityEnum);
        });
  }

  @And("nric cancelled status change to {int}")
  public void nricCancelledStatusChangeTo(int status) {
    List<PersonId> personIds = testContext.get("personIds");
    personIds.forEach(
        personId -> {
          PersonDetail personDetail =
              personDetailRepo.findCurrentPersonDetailByPerson(
                  personId.getPerson(), dateUtils.localDateToDate(dateUtils.now()));
          Assert.assertEquals(
              status == 1,
              personDetail.getIsNricCancelled(),
              "Expecting person with nric : ["
                  + personId.getNaturalId()
                  + "] to have NRIC_CANCELLED_STATUS of "
                  + status);
        });
  }
}