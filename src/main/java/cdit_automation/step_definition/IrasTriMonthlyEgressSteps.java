package cdit_automation.step_definition;

import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.Income;
import cdit_automation.models.Nationality;
import cdit_automation.models.Person;
import cdit_automation.models.PersonDetail;
import cdit_automation.models.PersonId;
import cdit_automation.models.PersonName;
import cdit_automation.utilities.StringUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class IrasTriMonthlyEgressSteps extends AbstractSteps {

  @Given("the following person's details, populate into the database")
  public void theFollowingPersonSDetailsPopulateIntoTheDatabase(DataTable dataTable) {
    log.info("Creating Person, PersonId, PersonDetail and Nationality object");
    List<Person> persons = new ArrayList<>();
    List<PersonName> personNames = new ArrayList<>();
    List<PersonId> personIds = new ArrayList<>();
    List<PersonDetail> personDetails = new ArrayList<>();
    List<Nationality> nationalities = new ArrayList<>();
    List<Map<String, String>> listOfMap = dataTable.asMaps(String.class, String.class);
    irasTriMonthlyEgressDataPrep.createBatch();
    listOfMap.forEach(
        m -> {
          Person person = irasTriMonthlyEgressDataPrep.getPerson();
          persons.add(person);
          personNames.add(irasTriMonthlyEgressDataPrep.getPersonName(m, person));
          personIds.add(irasTriMonthlyEgressDataPrep.getPersonId(m, person));
          personDetails.add(irasTriMonthlyEgressDataPrep.getPersonDetail(m, person));
          nationalities.add(irasTriMonthlyEgressDataPrep.getNationality(m, person));
        });

    testContext.set("savedPersons", personRepo.saveAll(persons));
    testContext.set("savedPersonNames", personNameRepo.saveAll(personNames));
    testContext.set("savedPersonIds", personIdRepo.saveAll(personIds));
    testContext.set("savedPersonDetails", personDetailRepo.saveAll(personDetails));
    testContext.set("savedNationalities", nationalityRepo.saveAll(nationalities));
    testContext.set("persons", persons);
  }

  @Given("the income details for each person referenced by index, populate into the database")
  public void theIncomeDetailsForEachPersonReferencedByIndexPopulateIntoTheDatabase(
      DataTable dataTable) {
    log.info("Creating income object");
    List<Person> persons = testContext.get("persons");
    List<Income> incomes = new ArrayList<>();
    List<Map<String, String>> listOfMap = dataTable.asMaps(String.class, String.class);
    listOfMap.forEach(
        m -> {
          incomes.add(irasTriMonthlyEgressDataPrep.getIncome(m, persons));
        });
    testContext.set("savedIncomes", incomeRepo.saveAll(incomes));
  }

  @And("I remove all test data from database")
  public void iRemoveAllTestDataFromDatabase() {
    log.info("Remove all test data in database");
    incomeRepo.deleteAll(testContext.get("savedIncomes"));
    nationalityRepo.deleteAll(testContext.get("savedNationalities"));
    personNameRepo.deleteAll(testContext.get("savedPersonNames"));
    personDetailRepo.deleteAll(testContext.get("savedPersonDetails"));
    personIdRepo.deleteAll(testContext.get("savedPersonIds"));
    personRepo.deleteAll(testContext.get("savedPersons"));
  }

  @Then("the IRAS AI tri monthly file should be created")
  public void theIRASAITriMonthlyFileShouldBeCreated() {
    log.info("Verify that the IRAS AI tri monthly file is created...");
    Path filePath = testContext.get("filePath");
    boolean isFound =
        waitUntilCondition(
            new Supplier<Boolean>() {
              public Boolean get() {
                if (Paths.get(filePath.toString(), "input_trimonthly_assessable_income.txt")
                    .toFile()
                    .exists()) {
                  return Boolean.TRUE;
                } else {
                  return Boolean.FALSE;
                }
              }
            });

    if (!isFound) {
      throw new TestFailException(
          "The file input_trimonthly_assessable_income.txt does not exists at "
              + filePath.toAbsolutePath().toString());
    }
  }

  @And("I verify the content of the tri monthly file should be as follows:")
  public void iVerifyTheContentOfTheTriMonthlyFileShouldBeAsFollows(DataTable dataTable) {
    log.info(
        "Verify the content of the tri monthly file should contain the expected record(s) user entered");
    Path filePath = testContext.get("filePath");
    List<Map<String, String>> listOfMap = dataTable.asMaps(String.class, String.class);
    listOfMap.stream()
        .map(m -> StringUtils.rightPad(m.get("NaturalId"), 17) + m.get("Year"))
        .forEach(
            r -> {
              try (Stream<String> lines =
                  Files.lines(
                      Paths.get(filePath.toString(), "input_trimonthly_assessable_income.txt"))) {
                testAssert.assertEquals(
                    true,
                    lines.anyMatch(s -> s.contains(r)),
                    "Missing record in file : [" + r + " ]");
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
  }
}
