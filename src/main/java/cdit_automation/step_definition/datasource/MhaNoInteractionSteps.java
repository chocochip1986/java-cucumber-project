package cdit_automation.step_definition.datasource;

import cdit_automation.enums.datasource.FileTypeEnum;
import cdit_automation.enums.datasource.PersonStatusTypeEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.datasource.Batch;
import cdit_automation.models.datasource.FileReceived;
import cdit_automation.models.datasource.Person;
import cdit_automation.models.datasource.PersonId;
import cdit_automation.models.datasource.PersonStatus;
import cdit_automation.utilities.CommonUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

@Slf4j
@Ignore
public class MhaNoInteractionSteps extends AbstractSteps { 
    
  private static final String FILE_RECEIVED = "fileReceived";
  private static final String SAVED_PERSONS = "savedPersons";
  private static final String SAVED_PERSON_IDS = "savedPersonIds";
  private static final String PREPARED_PERSON_STATUS_LIST = "preparedPersonStatusList";

  private String headerDetail;
  private ArrayList<String> bodyDetail = new ArrayList<>();
  private String footerDetail;
    
  @Given("the file have the following header details:") 
  public void theFileHaveTheFollowingHeaderDetails(String headerString) {
      this.headerDetail = headerString;
  }

  @Given("the file have the following record details:")
  public void theFileHaveTheFollowingRecordDetails(DataTable dataTable) {

      List<Map<String, String>> dataMap = dataTable.asMaps(String.class, String.class);
      List<String> bodyString = mhaNoInteractionFileDataPrep.createBodyOfTestScenarios(dataMap);
      this.bodyDetail.addAll(bodyString);
  }
  
  @Given("the file have the following footer details:")
  public void theFileHasTheFollowingFooterDetails(String footerString) {
      this.footerDetail = footerString;
  }

  @Given("the database populated with the following person and person id details:")
  public void databasePopulatedWithTheFollowingPersonAndPersonIdDetails(DataTable dataTable) {

      log.info("Populating Person and PersonId table.");

      List<Person> personList = new ArrayList<>();
      List<PersonId> personIdList = new ArrayList<>();
      List<Map<String, String>> detailList = dataTable.asMaps(String.class, String.class);

      Batch batch = Batch.builder().build();
      batchRepo.save(batch);
      
      detailList.forEach(
              detail -> {
                  Person person = mhaNoInteractionFileDataPrep.getPerson();
                  personList.add(person);
                  personIdList.add(mhaNoInteractionFileDataPrep.getPersonId(detail, batch, person));
              });
       
      testContext.set(SAVED_PERSONS, personRepo.saveAll(personList));
      testContext.set(SAVED_PERSON_IDS, personIdRepo.saveAll(personIdList));
    }
    
  @And("the MHA no interaction file is created")
  public void theMhaNoInteractionFileIsCreated() {

      try {

          List<String> listOfIdentifiersToWriteToFile = new ArrayList<>();
          
          addToListIfNotNull(listOfIdentifiersToWriteToFile, headerDetail);
          listOfIdentifiersToWriteToFile.addAll(bodyDetail);
          addToListIfNotNull(listOfIdentifiersToWriteToFile, footerDetail);

          batchFileCreator.writeToFile(FileTypeEnum.MHA_NO_INTERACTION.getValue().toLowerCase(), listOfIdentifiersToWriteToFile);
          
      } catch (IOException ioe) {
          log.error(ioe.getMessage());
      }
  }
  
  @And("^I verify number of records in MHA no interaction validated table is (\\d)")
  public void verifyNumberOfRecordsInNoInteractionValidatedTable(long noOfRecords) {

      log.info("Verifying number of records in No Interaction Validated table: " + noOfRecords);
      if (testContext.contains(FILE_RECEIVED)) {
          
          FileReceived fileReceived = testContext.get(FILE_RECEIVED);
          Batch batch = batchRepo.findFirstByFileReceivedOrderByCreatedAtDesc(fileReceived);
          
          long count = noInteractionValidatedRepo.countAllByBatch(batch);
          testAssert.assertEquals(noOfRecords, count, "The expected number of record(s) does not match!!!");
          
      } else {
          throw new TestFailException("No batch job previously created!");
      }
  }
  
  @And("I verify that the person status is updated correctly")
  public void verifyThePersonStatusUpdatedCorrectly() {

      log.info("Verifying the Person Status.");

      if (testContext.contains(FILE_RECEIVED)) {

          List<PersonId> personIdList = testContext.get(SAVED_PERSON_IDS);
          List<PersonStatus> personStatusList = new ArrayList<>();
          
          personIdList.forEach(
                  personId -> {
                      PersonStatus ps = personStatusRepo.getByPersonAndType(personId.getPerson(), PersonStatusTypeEnum.NO_INTERACTION);
                      personStatusList.add(ps);
                      testAssert.assertEquals(PersonStatusTypeEnum.NO_INTERACTION,
                              ps.getType(),
                              "Expecting person with nric : ["
                                      + personId.getNaturalId()
                                      + "] to have status of : "
                                      + PersonStatusTypeEnum.NO_INTERACTION);
                  });

          testContext.set(PREPARED_PERSON_STATUS_LIST, personStatusList);

      } else {
          throw new TestFailException("No batch job previously created!");
      }
  }

  @And("I remove the test data from the prepared database")
  public void removeTheTestDataFromPreparedDatabase() {

      log.info("Removing test data from prepared database.");

      personStatusRepo.deleteAll(testContext.get(PREPARED_PERSON_STATUS_LIST));
      personIdRepo.deleteAll(testContext.get(SAVED_PERSON_IDS));
      personRepo.deleteAll(testContext.get(SAVED_PERSONS));
  }
    
  private void addToListIfNotNull(List<String> writeToFileList, String detailString) {
      
      if (detailString != null) {
          writeToFileList.add(CommonUtils.emptyStringIfInputIsKeywordBlank(detailString));
      }
  }
}
