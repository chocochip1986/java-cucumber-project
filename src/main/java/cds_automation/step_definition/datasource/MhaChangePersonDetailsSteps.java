package cds_automation.step_definition.datasource;

import cds_automation.enums.datasource.FileTypeEnum;
import cds_automation.enums.datasource.MhaChangePersonDetailsEnum;
import cds_automation.models.datasource.Gender;
import cds_automation.models.datasource.PersonDetail;
import cds_automation.models.datasource.PersonName;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class MhaChangePersonDetailsSteps extends AbstractSteps {
  @Given("^the mha change in person details file is empty$")
  public void theMhaChangeInPersonDetailsFileIsEmpty() {
    log.info("Creating an empty person details file file...");
    batchFileDataWriter.begin(
        mhaChangePersonDetailsDataPrep.generateSingleHeader(),
        FileTypeEnum.MHA_PERSON_DETAIL_CHANGE,
        null);
    batchFileDataWriter.end();
  }

  @Given("the mha person details file has the following details:")
  public void theMhaPersonDetailsFileHasTheFollowingDetails(DataTable dataTable)
      throws IOException {
    batchFileDataWriter.begin(
        mhaChangePersonDetailsDataPrep.generateSingleHeader(),
        FileTypeEnum.MHA_PERSON_DETAIL_CHANGE,
        null);

    List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
    mhaChangePersonDetailsDataPrep.createBodyOfTestScenarios(list);

    batchFileDataWriter.end();
  }

  @Given("^the mha person details file is being created$")
  public void theMhaPersonDetailsFileIsBeingCreated() {
    batchFileDataWriter.begin(
        mhaChangePersonDetailsDataPrep.generateSingleHeader(),
        FileTypeEnum.MHA_PERSON_DETAIL_CHANGE,
        null);
  }

  @And("^the person with nric ([ST]\\d{7}[A-Z]) does not have an existing (Person_Detail|Person_Name|Person_Gender) record$")
  public void thePersonWithDoesNotHaveAnExistingPersonDetailRecord(String nric, MhaChangePersonDetailsEnum missingType) {
    if (missingType.equals(MhaChangePersonDetailsEnum.Person_Detail)) {
      personDetailRepo.deleteByNaturalId(nric);
    } else if (missingType.equals(MhaChangePersonDetailsEnum.Person_Name)) {
      personNameRepo.deleteByNaturalId(nric);
    } else if (missingType.equals(MhaChangePersonDetailsEnum.Person_Gender)) {
      genderRepo.deleteByNaturalId(nric);
    }
  }

  @And("^the corresponding (Person_Detail|Person_Name|Person_Gender) record for person with nric ([ST]\\d{7}[A-Z]) now reflects the new value (.*)$")
  public void theCorrespondingData_item_changed_catRecordForPersonWithNricNricNowReflectsTheNewValue(MhaChangePersonDetailsEnum changedType, String nric, String newValue) {
    if (changedType.equals(MhaChangePersonDetailsEnum.Person_Detail)) {
      PersonDetail personDetail = personDetailRepo.findByNaturalId(nric);
      testAssert.assertEquals(
          personDetail.getDateOfBirth().format(dateUtils.DATETIME_FORMATTER_YYYYMMDD),
          newValue,
          "Person Detail has not been updated for nric: " + nric);
    } else if (changedType.equals(MhaChangePersonDetailsEnum.Person_Name)) {
      PersonName personName = personNameRepo.findByNaturalId(nric);
      testAssert.assertEquals(
          personName.getName(), newValue, "Person Name has not been updated for nric: " + nric);
    } else if (changedType.equals(MhaChangePersonDetailsEnum.Person_Gender)) {
      Gender personGender = genderRepo.findByNaturalId(nric);
      testAssert.assertEquals(
          personGender.getGenderEnum().getValue(),
          newValue,
          "Gender has not been updated for nric: " + nric);
    }
  }

  @When("^the corresponding records for person with nric ([ST]\\d{7}[A-Z]) are now valid from the new value (.*)$")
  public void theCorrespondingRecordsForPersonAreNowValidFromTheNewValue(String nric, String newValue) {
    Timestamp newDob =
        Timestamp.valueOf(
            LocalDate.parse(newValue, dateUtils.DATETIME_FORMATTER_YYYYMMDD).atStartOfDay());
    Timestamp personDetailValidFrom =
        personDetailRepo
            .findByNaturalId(nric)
            .getBiTemporalData()
            .getBusinessTemporalData()
            .getValidFrom();
    Timestamp personNameValidFrom =
        personNameRepo
            .findByNaturalId(nric)
            .getBiTemporalData()
            .getBusinessTemporalData()
            .getValidFrom();
    Timestamp genderValidFrom =
        genderRepo
            .findByNaturalId(nric)
            .getBiTemporalData()
            .getBusinessTemporalData()
            .getValidFrom();
    Timestamp nationaltyValidFrom =
        nationalityRepo
            .findByNaturalId(nric)
            .getBiTemporalData()
            .getBusinessTemporalData()
            .getValidFrom();
    Timestamp personIdValidFrom =
        personIdRepo
            .findByNaturalId(nric)
            .getBiTemporalData()
            .getBusinessTemporalData()
            .getValidFrom();

    List<Timestamp> validFroms =
        Arrays.asList(
            personDetailValidFrom,
            personNameValidFrom,
            genderValidFrom,
            nationaltyValidFrom,
            personIdValidFrom);
    for (Timestamp validFrom : validFroms) {
      testAssert.assertEquals(
          validFrom, newDob, "'Valid from' in other tables has not been updated correctly");
    }
  }
}
