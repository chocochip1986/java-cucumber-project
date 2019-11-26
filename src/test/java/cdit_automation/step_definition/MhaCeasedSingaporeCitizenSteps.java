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
    listOfIdentifiersToWriteToFile.add(mhaCeasedCitizenFileDataPrep.generateDoubleHeader());
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
}
