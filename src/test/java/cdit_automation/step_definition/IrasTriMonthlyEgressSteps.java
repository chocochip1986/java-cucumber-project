package cdit_automation.step_definition;

import cdit_automation.asserts.Assert;
import cdit_automation.enums.AssessableIncomeStatus;
import cdit_automation.enums.NationalityEnum;
import cdit_automation.enums.PersonIdTypeEnum;
import cdit_automation.models.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IrasTriMonthlyEgressSteps extends AbstractSteps {

  @Then("check PersonId object created as specified")
  public void checkPersonIdObjectCreatedAsSpecified() {
    PersonId personId = testContext.get("personId");
    Assert.assertEquals("S9999999A", personId.getNaturalId(), "asd");
    Assert.assertEquals("NRIC", personId.getPersonIdType().toString(), "asd");
  }

  @Then("check PersonName object created as specified")
  public void checkPersonNameObjectCreatedAsSpecified() {
    PersonName personName = testContext.get("personName");
    Assert.assertEquals("Muthu", personName.getName(), "Name are not equal");
  }

  @Then("check PersonDetail object created as specified")
  public void checkPersonDetailObjectCreatedAsSpecified() {
    PersonDetail personDetail = testContext.get("personDetail");
    Assert.assertEquals(
        LocalDate.parse("20201212", DateTimeFormatter.ofPattern("yyyyMMdd")),
        personDetail.getDateOfDeath(),
        "Death date are not equal");
  }

  @Then("check Nationality object created as specified")
  public void checkNationalityObjectCreatedAsSpecified() {
    Nationality nationality = testContext.get("nationality");
    Assert.assertEquals(
        NationalityEnum.SINGAPORE_CITIZEN,
        nationality.getNationality(),
        "Expecting nationality of [SINGAPORE_CITIZEN|PERMANENT_RESIDENT]");
    Assert.assertEquals(
        Timestamp.valueOf(
            LocalDate.parse("20191010", DateTimeFormatter.ofPattern("yyyyMMdd"))
                .atTime(LocalTime.MAX)),
        nationality.getCitizenshipRenunciationDate(),
        "Renunciation Date not same");
  }

  @Then("check Income object created as specified")
  public void checkIncomeObjectCreatedAsSpecified() {
    Optional<Income> incomeOpt = testContext.get("income");
    if(incomeOpt.isPresent()) {
      Assert.assertEquals(
          AssessableIncomeStatus.PENDING_ASSESSMENT,
          incomeOpt.get().getAssessableIncomeStatus(),
          "Unexpect AI status");
      Assert.assertEquals(Short.valueOf("2018"), incomeOpt.get().getYear(), "Unexpect AI year");
      Assert.assertEquals(
          BigDecimal.valueOf(1000000), incomeOpt.get().getAssessableIncome(), "Unexpect AI Income");
    }
  }

  @Given("the following detail, generate all necessary objects")
  public void theFollowingDetailGenerateAllNecessaryObjects(DataTable dataTable) {
    List<Map<String, String>> listOfMap = dataTable.asMaps(String.class, String.class);
    Person p = irasTriMonthlyEgressDataPrep.getPersonOpt().get();
    PersonId personId = irasTriMonthlyEgressDataPrep.getPersonIdOpt(listOfMap.get(0), p).get();
    PersonName personName = irasTriMonthlyEgressDataPrep.getPersonNameOpt(listOfMap.get(0), p).get();
    PersonDetail personDetail = irasTriMonthlyEgressDataPrep.getPersonDetailOpt(listOfMap.get(0), p).get();
    Nationality nationality = irasTriMonthlyEgressDataPrep.getNationalityOpt(listOfMap.get(0), p).get();
    Optional<Income> incomeOpt = irasTriMonthlyEgressDataPrep.getIncomeOpt(listOfMap.get(0), p);

    testContext.set("personId", personId);
    testContext.set("personName", personName);
    testContext.set("personDetail", personDetail);
    testContext.set("nationality", nationality);
    testContext.set("income", incomeOpt);
  }

  @Then("check the nric in PersonId object is valid")
  public void checkTheNricInPersonIdObjectIsValid() {
    PersonId personId = testContext.get("personId");
    PersonIdTypeEnum idTypeEnum = personId.getPersonIdType();
    boolean isSingaporeCitizen = idTypeEnum.equals(PersonIdTypeEnum.NRIC);
    Pattern nricPattern = Pattern.compile("^[ST]\\d{7}[A-Z]$");
    Pattern finPattern = Pattern.compile("^[FG]\\d{7}[A-Z]$");
    Matcher nricMatcher = nricPattern.matcher(personId.getNaturalId());
    Matcher finMatcher = finPattern.matcher(personId.getNaturalId());
    Assert.assertEquals(
        true,
        isSingaporeCitizen ? nricMatcher.find() : finMatcher.find(),
        "Invalid natural id generated");
  }

  @Then("check the name in PersonId object is not AUTO or Null")
  public void checkTheNameInPersonIdObjectIsNotAUTOOrNull() {
    PersonName personName = testContext.get("personName");
    Assert.assertEquals(
        false,
        personName.getName().isEmpty(),
        "Invalid name generated: [ " + personName.getName() + " ]");
  }

  @Then("check the renunciation date in Nationality object is equals to {word}")
  public void checkTheRenunciationDateInNationalityObjectIsEqualsToCitizenshipRenunciationDate(
      String dateStr) {
    Nationality nationality = testContext.get("nationality");
    boolean isValidDate = dateStr.matches("^[0-9]{8}$");
    Timestamp expectTimestamp =
        isValidDate
            ? Timestamp.valueOf(
                LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd"))
                    .atTime(LocalTime.MAX))
            : null;
    if (nationality.getCitizenshipRenunciationDate() != null) {
      Assert.assertEquals(
          expectTimestamp,
          nationality.getCitizenshipRenunciationDate(),
          "Invalid renunciation date");
    } else {
      Assert.assertEquals("-", dateStr, "Expecting only value of - have no renunciation date");
    }
  }

  @Then("check the renunciation date in Income object is equals to {word}")
  public void checkTheRenunciationDateInIncomeObjectIsEqualsToAIStatus(String aiStatus) {
    Optional<Income> income = testContext.get("income");
    if (aiStatus.matches("^[^-]+")) {
      Assert.assertEquals(
          aiStatus, income.get().getAssessableIncomeStatus().getValue(), "AI Status don't match");
    } else {
      Assert.assertEquals("-", aiStatus, "Expecting only value of - have no income");
    }
  }

  @Then("check the income year in Income object is equals to {word}")
  public void checkTheIncomeYearInIncomeObjectIsEqualsToAIStatus(String aiYear) {
    Optional<Income> income = testContext.get("income");
    if (aiYear.matches("^[^-]+")) {
      Assert.assertEquals(
          Short.valueOf(aiYear), income.get().getYear(), "AI Year don't match");
    } else {
      Assert.assertEquals("-", aiYear, "Expecting only value of - have no income year");
    }
  }
}
