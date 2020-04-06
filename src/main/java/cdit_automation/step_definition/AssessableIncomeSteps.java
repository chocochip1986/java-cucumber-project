package cdit_automation.step_definition;

import cdit_automation.configuration.TestEnv;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.PersonIdTypeEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.FileReceived;
import cdit_automation.models.PersonId;
import cdit_automation.models.interfaces.ICustomIncomeRecord;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class AssessableIncomeSteps extends AbstractSteps {

  @Given("MHA send MHA_BULK_CITIZEN file on {word} with the following data:")
  public void mhaSendMHA_BULK_CITIZENFileOnWithTheFollowingData(
      String dateStr, DataTable dataTable) {
    List<String> formattedEntries = mhaBulkFileDataPrep.formatEntries(dataTable);
    mhaBulkFileDataPrep.writeToFile(dateStr, formattedEntries);
  }

  @And("I re-run the batch job")
  public void iReRunTheBatchJob() {
    FileReceived fileReceived = testContext.get("fileReceived");
    apiHelper.sendCallToTriggerBatchJob(fileReceived);
  }

  @Given(
      "the following person's details, insert into database manually, valid from {word} till infinity")
  public void theFollowingPersonSDetailsInsertIntoDatabaseManuallyValidFromTillInfinity(
      String validFromDateStr, DataTable dataTable) {
    irasAssessableIncomeFileDataPrep.insertPerson(dataTable, validFromDateStr);
  }

  @Given(
      "the following fin person's details, insert into database manually, valid from {word} till infinity")
  public void theFollowingFinPersonSDetailsInsertIntoDatabaseManuallyValidFromTillInfinity(
      String validFromDateStr, DataTable dataTable) {
    irasAssessableIncomeFileDataPrep.insertFinPerson(dataTable, validFromDateStr);
  }

  @And("I verify {word} {word} persisted correctly")
  public void iVerifyNRICSKPersistedCorrectly(String typeStr, String naturalId) {
    PersonId personId = irasAssessableIncomeFileDataPrep.getPersonIdBy(naturalId);
    testAssert.assertEquals(personId.getNaturalId(), naturalId, "NaturalId should be " + naturalId);
    testAssert.assertEquals(
        personId.getPersonIdType(),
        PersonIdTypeEnum.valueOf(typeStr),
        "PersonIdType should be " + PersonIdTypeEnum.NRIC);
  }

  @Given("MHA send MHA_DEATH_DATE file on {word} with the following data:")
  public void mhaSendMHA_DEATH_DATEFileOnWithTheFollowingData(String dateStr, DataTable dataTable) {
    List<String> formattedEntries = mhaDeathDateFileDataPrep.formatEntries(dataTable);
    mhaDeathDateFileDataPrep.writeToFile(dateStr, formattedEntries);
  }

  @Then("I update the {word} file received date to {word}")
  public void iUpdateTheMHA_DEATH_DATEFileReceivedDateTo(String fileTypeStr, String dateStr) {
    FileReceived fileReceived = testContext.get("fileReceived");
    irasAssessableIncomeFileDataPrep.updateFileReceivedDate(fileReceived, dateStr);
  }

  @Given("MHA send MHA_CEASED_CITIZEN file on {word} with the following data:")
  public void mhaSendMHA_CEASED_CITIZENFileOnWithTheFollowingData(
      String dateStr, DataTable dataTable) {
    List<String> formattedEntries = mhaCeasedCitizenFileDataPrep.formatEntries(dataTable);
    mhaCeasedCitizenFileDataPrep.writeToFile(dateStr, formattedEntries);
  }

  @Then("I retrieve the egress file")
  public void iRetrieveTheEgressFile() {
    if (testManager.getTestEnvironment().getEnv().equals(TestEnv.Env.LOCAL)) {
      ArrayList<String> r = new ArrayList<>();
      findEgressFolderFilePath(System.getProperty("user.home"), 0, r);
      this.testContext.set("egressFilePath", r.get(0));
      log.info("Egress file path: " + r.get(0));
    }

    if (testManager.getTestEnvironment().getEnv().equals(TestEnv.Env.QA)) {
      // retrieve file from S3
    }
  }

  private void findEgressFolderFilePath(String dirPath, int level, ArrayList<String> result) {
    File dir = new File(dirPath);
    File[] firstLevelFiles = dir.listFiles();
    if (firstLevelFiles != null && firstLevelFiles.length > 0) {
      for (File aFile : firstLevelFiles) {
        if (aFile.isDirectory()) {
          boolean containDatasourceKeyword = aFile.getName().contains("data-source");
          File egressDir =
              new File(aFile.getAbsolutePath() + File.separator + "egress-volume/irasnap/outbox");
          if (containDatasourceKeyword && egressDir.exists()) {
            File[] egressFile = egressDir.listFiles();
            if (egressFile != null && egressFile.length == 0) {
              throw new TestFailException("Unable to find egress file");
            } else {
              result.add(egressFile[0].getAbsolutePath());
            }
          } else {
            findEgressFolderFilePath(aFile.getAbsolutePath(), level + 1, result);
          }
        }
      }
    }
  }

  @Then("I verify it has the following data:")
  public void iVerifyItHasTheFollowingData(List<String> stringList) {
    testAssert.assertTrue(
        irasAssessableIncomeFileDataPrep.doesFileContainAllString(
            stringList, testContext.get("egressFilePath")),
        "Expected file to contain all string : " + stringList.toString());
  }

  @And("delete the egress file")
  public void deleteTheEgressFile() {
    testAssert.assertTrue(
        irasAssessableIncomeFileDataPrep.deleteFile(testContext.get("egressFilePath")),
        "Expected file to be deleted in path : " + testContext.get("egressFilePath"));
  }

  @Given("IRAS provide the assessable income file on {word} with the following data:")
  public void irasProvideTheAssessableIncomeFileOnWithTheFollowingData(
      String dateStr, DataTable dataTable) {
    List<String> formattedEntries = irasAssessableIncomeFileDataPrep.formatEntries(dataTable);
    irasAssessableIncomeFileDataPrep.writeToFile(
        dateStr, formattedEntries, FileTypeEnum.IRAS_BULK_AI);
  }

  @And(
      "I verify the following natural id's income status and value for year {int} as of {word} are as follows:")
  public void iVerifyTheFollowingNaturalIdSIncomeStatusAndValueForYearAsOfAreAsFollows(
      int year, String dateStr, DataTable dataTable) {
    List<Map<String, String>> dataRows = dataTable.asMaps(String.class, String.class);
    List<String> naturalIds =
        dataRows.stream().map(row -> row.get("NATURAL_ID")).collect(Collectors.toList());
    List<ICustomIncomeRecord> iCustomIncomeRecords =
        irasAssessableIncomeFileDataPrep.getIncomeByNaturalIdsAndYearAndAsOf(
            naturalIds, year, dateStr);
    dataRows.forEach(
        row -> {
          Optional<ICustomIncomeRecord> optPersistedRecord =
              iCustomIncomeRecords.stream()
                  .filter(c -> c.getNaturalId().equals(row.get("NATURAL_ID")))
                  .findFirst();
          if (optPersistedRecord.isPresent()) {
            ICustomIncomeRecord persistedRecord = optPersistedRecord.get();
            testAssert.assertEquals(
                persistedRecord.getNaturalId(),
                row.get("NATURAL_ID"),
                "Expected natural id to be equal. Got "
                    + persistedRecord.getNaturalId()
                    + " WANT ["
                    + row.get("NATURAL_ID")
                    + "]");

            testAssert.assertEquals(
                persistedRecord.getAssessableIncome(),
                new BigDecimal(row.get("ASSESSABLE_INCOME")),
                "Expected assessable income to be equal for NRIC :"
                    + row.get("NATURAL_ID")
                    + ". Got "
                    + persistedRecord.getAssessableIncome()
                    + ", Want "
                    + row.get("ASSESSABLE_INCOME"));

            testAssert.assertEquals(
                persistedRecord.getAssessableIncomeStatus().getValue(),
                row.get("STATUS"),
                "Expected assessable income status to be equal. Got "
                    + persistedRecord.getAssessableIncomeStatus()
                    + ", Want "
                    + row.get("STATUS"));
          }
        });
  }

  @Given("I triggered first bulk assessable income egress job on {word} of year {word}")
  public void iTriggeredFirstBulkAssessableIncomeEgressJobOnOfYear(
      String dateStr, String yearOfAssessment) {
    LocalDate localDate = dateUtils.parse(dateStr);
    apiHelper.sendCallToTriggerOutgoingIrasFirstBulkJob(localDate, yearOfAssessment);
  }

  @And("I triggered bulk assessable income egress job on {word}")
  public void iTriggeredBulkAssessableIncomeEgressJobOnOfYear(String dateStr) {
    apiHelper.sendCallToTriggerOutgoingIrasAiJob(dateUtils.parse(dateStr));
  }

  @Given("I triggered thrice monthly assessable income egress job on {word}")
  public void iTriggeredThriceMonthlyAssessableIncomeEgressJobOnOfYear(String dateStr) {
    LocalDate localDate = dateUtils.parse(dateStr);
    apiHelper.sendCallToTriggerOutgoingIrasTriMonthlyAiJob(localDate, false);
  }

  @Given(
      "IRAS provide the thrice monthly assessable income file on {word} with the following data:")
  public void irasProvideTheThriceMonthlyAssessableIncomeFileOnWithTheFollowingData(
      String dateStr, DataTable dataTable) {
    List<String> formattedEntries = irasAssessableIncomeFileDataPrep.formatEntries(dataTable);
    irasAssessableIncomeFileDataPrep.writeToFile(
        dateStr, formattedEntries, FileTypeEnum.IRAS_THRICE_MONTHLY_AI);
  }

  @Given(
      "I triggered first thrice monthly assessable income egress job for the month of June on {word}")
  public void iTriggeredFirstThriceMonthlyAssessableIncomeEgressJobForTheMonthOfJuneOn(
      String dateStr) {
    LocalDate localDate = dateUtils.parse(dateStr);
    apiHelper.sendCallToTriggerOutgoingIrasTriMonthlyAiJob(localDate, true);
  }

  @Given("MHA send MHA_NEW_CITIZEN file on {word} with the following data:")
  public void mhaSendMHA_NEW_CITIZENFileOnWithTheFollowingData(
      String dateStr, DataTable dataTable) {
    List<String> formattedEntries = mhaNewCitizenFileDataPrep.formatEntries(dataTable);
    mhaNewCitizenFileDataPrep.writeToFile(dateStr, formattedEntries);
  }

  @Given("the following details, insert income appeal cases into database:")
  public void theFollowingDetailsInsertIncomeAppealCasesIntoDatabase(DataTable dataTable) {
    irasAssessableIncomeFileDataPrep.insertAppealIncomes(dataTable);
  }

  @Given("IRAS return the assessable income for the following natural id:")
  public void irasReturnTheAssessableIncomeForTheFollowingNaturalId(DataTable dataTable) {
    List<Map<String, String>> dataRows = dataTable.asMaps(String.class, String.class);
    testContext.set("appealCase", dataRows);
  }

  @Then("I turn off the appeal flag")
  public void iTurnOffTheAppealFlag() {
    List<Map<String, String>> dataRows = testContext.get("appealCase");
    dataRows.stream().forEach(row -> {
      irasAssessableIncomeFileDataPrep.turnOffAppealCase(row.get("NATURAL_ID"), row.get("YEAR"));
    });
  }

  @Given("MHA send MHA_DUAL_CITIZEN on {word} with the following data:")
  public void mhaSendMHA_DUAL_CITIZENOnWithTheFollowingData(String dateStr, List<String> naturalIds) {
    batchFileDataWriter.begin(dateStr, FileTypeEnum.MHA_DUAL_CITIZEN, null);
    naturalIds.forEach(id -> batchFileDataWriter.chunkOrWrite(id));
    batchFileDataWriter.end();
  }
}
