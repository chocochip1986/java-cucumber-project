package automation.step_definition.datasource;

import automation.constants.TestConstants;
import automation.data_helpers.datasource.MhaCeasedCitizenFileDataPrep;
import automation.data_helpers.datasource.batch_entities.MhaCeasedCitizenFileEntry;
import automation.data_setup.Phaker;
import automation.enums.datasource.FileTypeEnum;
import automation.enums.datasource.NationalityEnum;
import automation.models.datasource.Nationality;
import automation.models.datasource.PersonId;
import automation.models.datasource.PersonName;
import automation.utilities.DateUtils;
import automation.utilities.StringUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.sql.Timestamp;
import java.time.LocalDate;
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

  @Given("^the file has the following details with Header date of run (.*)$")
  public void theFileHasTheFollowingDetails(String dateOption, DataTable dataTable) {

      batchFileDataWriter.begin(getCeasedCitizenHeaderString(dateOption), FileTypeEnum.MHA_CEASED_CITIZEN, null);
      mhaCeasedCitizenFileDataPrep.createBodyOfTestScenarios(dataTable.asMaps(String.class, String.class), testContext);
      batchFileDataWriter.end();
  }
  
  @Given("^the mha ceased citizen file contains the following details with Header date of run (.*)$")
  public void theMhaCeasedCitizenFileContainsFollowingDetails(String dateOption, DataTable dataTable) {

      batchFileDataWriter.begin(getCeasedCitizenHeaderString(dateOption), FileTypeEnum.MHA_CEASED_CITIZEN, null);
      mhaCeasedCitizenFileDataPrep.createBodyOfSpecificTestScenarios(dataTable.asMaps(String.class, String.class), testContext);
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

  @And("I verify the previous nationality valid till timestamp is the day before renunciation date at 2359HR")
  public void iVerifyThePreviousNationalityValidTillTimestampIsTheRenunciationDateAtHR() {
    List<MhaCeasedCitizenFileEntry> ceasedCitizens = testContext.get("ceasedCitizens");
    ceasedCitizens.forEach(
        c -> {
          Date recordValidityDate =
              dateUtils.localDateToDate(
                      dateUtils.parse(c.getCitizenRenunciationDate()).minusDays(1));
          Timestamp expectedValidTill =
              dateUtils.endOfDayToTimestamp(
                      dateUtils.parse(c.getCitizenRenunciationDate()).minusDays(1));
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
              dateUtils.beginningOfDayToTimestamp(
                      dateUtils.parse(c.getCitizenRenunciationDate()));
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
                .citizenRenunciationDate(dateUtils.daysBeforeToday(daysAgo).format(DateUtils.DATETIME_FORMATTER_YYYYMMDD))
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

    @And("^MHA sends a ceased citizenship file stating that ([a-z_]+) renounced (?:her|his) citizenship (\\d+) days ago$")
    public void mhaSendsACeasedCitizenshipFileStatingThatPersonRenouncedHisCitizenshipDaysAgo(String personName, int ceasedSCdaysAgo) {
      PersonId personId = testContext.get(personName);
      LocalDate ceassationDate = dateUtils.daysBeforeToday(ceasedSCdaysAgo);

      batchFileDataWriter.begin(mhaCeasedCitizenFileDataPrep.generateSingleHeader(), FileTypeEnum.MHA_CEASED_CITIZEN, null);
      MhaCeasedCitizenFileEntry mhaCeasedCitizenFileEntry = 
              new MhaCeasedCitizenFileEntry(personId.getNaturalId(), personName, NationalityEnum.randomNonSGCountryCode().getValue(), ceassationDate.format(DateUtils.DATETIME_FORMATTER_YYYYMMDD));
      batchFileDataWriter.chunkOrWrite(mhaCeasedCitizenFileEntry.toString());
      batchFileDataWriter.end();
    }

    @And("^([a-z_]+) is a non singaporean since (\\d+) days ago$")
    public void personIsANonSingaporeanSinceDaysAgo(String personName, int daysAgo) {
      PersonId personId = testContext.get(personName);

      Nationality curNationality = nationalityRepo.findNationalityByPerson(personId.getPerson());
      testAssert.assertEquals(NationalityEnum.NON_SINGAPORE_CITIZEN, curNationality.getNationality(), "Person with "+personId.getNaturalId()+" is not a non-singaporean");
      Timestamp expectedValidFrom = dateUtils.beginningOfDayToTimestamp(dateUtils.daysBeforeToday(daysAgo));
      testAssert.assertEquals(expectedValidFrom, curNationality.getBiTemporalData().getBusinessTemporalData().getValidFrom(), "Person with "+personId.getNaturalId()+" is not a non-singaporean from "+expectedValidFrom.toString());
    }

    @Given("^([a-z_]+) who is (\\d+) years old attained his Singapore Citizenship (\\d+) days ago$")
    public void personAttainedHisSingaporeCitizenshipDaysAgo(String personName, int age, int daysAgo) {

      LocalDate birthDate = dateUtils.yearsBeforeToday(age);
      LocalDate attainmentDate = dateUtils.daysBeforeToday(daysAgo);
      PersonId personId = personFactory.createNewSCPersonId(birthDate, personName, Phaker.validNric(), attainmentDate);
      
      testContext.set(personName, personId);
    }

    @And("^I verify that the correct persons have ceased being singaporean$")
    public void iVerifyThatTheCorrectPersonsHaveCeasedBeingSingaporean() {
      List<MhaCeasedCitizenFileEntry> ceasedCases = testContext.get("citizens");

      for ( MhaCeasedCitizenFileEntry ceasedCase : ceasedCases ) {
          LocalDate ceassationDate = LocalDate.parse(ceasedCase.getCitizenRenunciationDate(), DateUtils.DATETIME_FORMATTER_YYYYMMDD);
          String nric = ceasedCase.getNric();
          PersonId personId = personIdRepo.findByNaturalId(nric);
          Nationality curNationality = nationalityRepo.findNationalityByPerson(personId.getPerson());

          testAssert.assertEquals(NationalityEnum.NON_SINGAPORE_CITIZEN, curNationality.getNationality(), "Person with "+nric+" is not a non-SC!");
          testAssert.assertEquals(dateUtils.beginningOfDayToTimestamp(ceassationDate), curNationality.getBiTemporalData().getBusinessTemporalData().getValidFrom(), "Person with "+nric+" has wrong validFrom date!");
          testAssert.assertEquals(dateUtils.beginningOfDayToTimestamp(ceassationDate), curNationality.getCitizenshipRenunciationDate(), "Person with "+nric+" has incorrect citizenship renunciation date!");

          Date validTill = new Date(dateUtils.beginningOfDayToTimestamp(ceassationDate.minusDays(1l)).getTime());
          Nationality prevNationality = nationalityRepo.findNationalityByPerson(personId.getPerson(), validTill);
          testAssert.assertEquals(NationalityEnum.SINGAPORE_CITIZEN, prevNationality.getNationality(), "Person with "+nric+" does not have the correct previous nationality!");
          testAssert.assertEquals(dateUtils.endOfDayToTimestamp(ceassationDate.minusDays(1l)), prevNationality.getBiTemporalData().getBusinessTemporalData().getValidTill(), "Person with "+nric+" did not cease being a singaporean correctly!");
      }
    }

    private String getCeasedCitizenHeaderString(String dateOption) {

        String headerString;

        switch (dateOption.toUpperCase()) {
            case TestConstants.OPTION_VALID:
                headerString = mhaBulkFileDataPrep.generateSingleHeader(dateUtils.now());
                break;
            case TestConstants.OPTION_INVALID:
                headerString = dateUtils.now().format(DateUtils.DATETIME_FORMATTER_DDMMYYYY);
                break;
            case TestConstants.OPTION_SPACES:
                headerString = StringUtils.rightPad(StringUtils.SPACE, 8);
                break;
            case TestConstants.OPTION_BLANK:
                headerString = StringUtils.EMPTY_STRING;
                break;
            case TestConstants.OPTION_FUTURE_DATE:
                headerString = Phaker.validFutureDate().format(DateUtils.DATETIME_FORMATTER_YYYYMMDD);
                break;
            default:
                headerString = dateOption;
        }

        testContext.set(
                MhaCeasedCitizenFileDataPrep.CEASED_CITIZEN_DATE_OF_RUN, headerString);

        return headerString;
    }
}
