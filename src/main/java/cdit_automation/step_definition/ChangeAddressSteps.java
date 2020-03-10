package cdit_automation.step_definition;

import cdit_automation.enums.AddressIndicatorEnum;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.PersonPropertyTypeEnum;
import cdit_automation.enums.automation.PropertyTypeEnum;
import cdit_automation.enums.automation.ResidencyEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.PersonId;
import cdit_automation.models.PersonProperty;
import cdit_automation.models.PropertyDetail;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Ignore
public class ChangeAddressSteps extends AbstractSteps{
    @Given("^the mha change address file is empty$")
    public void theMhaChangeAddressFileIsEmpty() {
        batchFileDataWriter.begin(mhaChangeAddressDataPrep.generateSingleDateNoOfRecordsHeader(0), FileTypeEnum.MHA_CHANGE_ADDRESS, null);
        batchFileDataWriter.end();
    }

    @Given("the mha change address file contains the following details:")
    public void theMhaChangeAddressFileHasTheFollowingDetails(DataTable dataTable) {
        List<Map<String, String>> listOfChangeAddresses = dataTable.asMaps(String.class, String.class);

        mhaChangeAddressFileDataPrep.createBodyOfTestScenarios(listOfChangeAddresses, testContext);
        //TODO
    }

    @Given("^(?:An|A) (singaporean|foreign) person ([A-Za-z]+) (owns|resides|owns and resides)(?: in)? a ([a-z_]+) property ([a-z0-9]+)$")
    public void personAOwnsAProperty(String residentialStatus, String person, String residency, String propertyType, String propertyName) {
        PropertyTypeEnum propertyTypeEnum = retrievePropertyOrError(propertyType);

        PersonId personId = residentialStatus.equals("singaporean") ? personFactory.createNewSCPersonId() : personFactory.createNewFRPersonId();

        testContext.set(propertyName, addressFactory.createPropertyFor(personId.getPerson(), getOwnershipType(residency), propertyTypeEnum));
        testContext.set(person, personId);
    }

    @And("^([A-Za-z]+) (owns|resides|owns and resides)(?: in)? a ([a-z]+) property ([a-z0-9]+)$")
    public void residesInAnotherProperty(String person, String residency, String propertyType, String propertyName) {
        PropertyTypeEnum propertyTypeEnum = retrievePropertyOrError(propertyType);

        PersonId personId = testContext.get(person);
        testContext.set(propertyName, addressFactory.createPropertyFor(personId.getPerson(), getOwnershipType(residency), propertyTypeEnum));
    }

    private PropertyTypeEnum retrievePropertyOrError(String propertyType) {
        if ( PropertyTypeEnum.fromString(propertyType) == null ) {
            throw new TestFailException("Unsupported property type in PropertyTypeEnum: "+propertyType);
        }
        return PropertyTypeEnum.fromString(propertyType);
    }

    private ResidencyEnum getOwnershipType(String ownershipType) {
        if ( ownershipType.equals("owns and resides") ) {
            return ResidencyEnum.BOTH;
        } else if ( ownershipType.equals("owns") ) {
            return ResidencyEnum.OWNERSHIP;
        } else {
            return ResidencyEnum.RESIDENCE;
        }
    }

    @And("^the mha change address file contains information that ([A-Za-z]+) changed from \\((mha_z|mha_c|nca)\\)([a-z0-9]+) to \\((mha_z|mha_c|nca)\\)([a-z0-9]+) " +
            "(\\d) days ago$")
    public void theMhaChangeAddressFileContainsInfoThat(String personName, String prevIndicatorType, String prevPropertyName, String curIndicatorType, String curPropertyName, long daysAgo) {
        checkIfPersonExistsInTestContext(personName);
        checkIfPropertyExistsInTestContext(prevPropertyName);
        checkIfPropertyExistsInTestContext(curPropertyName);

        batchFileDataWriter.begin(mhaChangeAddressDataPrep.generateSingleDateNoOfRecordsHeader(1), FileTypeEnum.MHA_CHANGE_ADDRESS, null);
        mhaChangeAddressFileDataPrep.createLineInBody(testContext.get(personName),
                addressIndicatorEnumFrom(prevIndicatorType),
                testContext.get(prevPropertyName),
                addressIndicatorEnumFrom(curIndicatorType),
                testContext.get(curPropertyName), dateUtils.daysBeforeToday(daysAgo));
        batchFileDataWriter.end();
    }

    @And("^the mha change address file contains information that ([A-Za-z]+) changed from \\((mha_z|mha_c|nca)\\)([a-z0-9]+) to a new \\((mha_z|mha_c|nca)\\)([a-z_]+) property " +
            "(\\d) days ago$")
    public void theMhaChangeAddressFileContainsInfoThat2(String personName, String prevIndicatorType, String prevPropertyName, String curIndicatorType, String propertyType, int daysAgo) {
        checkIfPersonExistsInTestContext(personName);
        checkIfPropertyExistsInTestContext(prevPropertyName);
        PropertyTypeEnum propertyTypeEnum = retrievePropertyOrError(propertyType);

        batchFileDataWriter.begin(mhaChangeAddressDataPrep.generateSingleDateNoOfRecordsHeader(1), FileTypeEnum.MHA_CHANGE_ADDRESS, null);
        mhaChangeAddressFileDataPrep.createLineInBodyWithNewCurAddress(testContext.get(personName),
                addressIndicatorEnumFrom(prevIndicatorType),
                testContext.get(prevPropertyName),
                addressIndicatorEnumFrom(curIndicatorType),
                propertyTypeEnum, dateUtils.daysBeforeToday(daysAgo));
        batchFileDataWriter.end();
    }

    private AddressIndicatorEnum addressIndicatorEnumFrom(String indType){
        if ( indType.equals("mha_z") ) {
            return AddressIndicatorEnum.MHA_Z;
        } else if ( indType.equals("mha_c") ) {
            return AddressIndicatorEnum.MHA_C;
        } else {
            return AddressIndicatorEnum.NCA;
        }
    }

    @Then("^([A-Za-z]+) resides in ([a-z0-9]+) from (\\d+) days ago$")
    public void residesInAddressFromDaysAgo(String personName, String propertyName, int daysAgo) {
        checkIfPersonExistsInTestContext(personName);
        checkIfPropertyExistsInTestContext(propertyName);

        PersonId personId = testContext.get(personName);
        PropertyDetail propertyDetail = testContext.get(propertyName);
        PersonProperty personProperty = personPropertyRepo.findByPersonAndPropertyAndType(personId.getPerson(), propertyDetail.getProperty(), PersonPropertyTypeEnum.RESIDENCE);

        testAssert.assertNotNull(personProperty, "no person property record indicating that "+personName+" of "+personId.getNaturalId()+" resides in "+propertyName);
        testAssert.assertEquals(dateUtils.beginningOfDayToTimestamp(dateUtils.daysBeforeToday(daysAgo)),
                personProperty.getBiTemporalData().getBusinessTemporalData().getValidFrom(),
                personName+" ("+personId.getNaturalId()+") does not reside in the address ("+propertyDetail.toString()+") from the address change date!");
    }

    private void checkIfPersonExistsInTestContext(String personName) {
        if ( testContext.doNotContain(personName) ) {
            throw new TestFailException("No such person registered in testContext: "+personName+". Please create person in another step definition.");
        }
    }

    private void checkIfPropertyExistsInTestContext(String propertyName) {
        if ( testContext.doNotContain(propertyName) ) {
            throw new TestFailException("No such property registered in testContext: "+propertyName+". Please create property in another step definition.");
        }
    }

    @Then("^([A-Za-z]+) does not reside in ([a-z0-9]+) since (\\d+) days ago$")
    public void johnDoesNotResideInAbeSinceDaysAgo(String personName, String propertyName, int daysAgo) {
        checkIfPersonExistsInTestContext(personName);
        checkIfPropertyExistsInTestContext(propertyName);

        PersonId personId = testContext.get(personName);
        PropertyDetail propertyDetail = testContext.get(propertyName);
        Date date = dateUtils.localDateToDate(dateUtils.daysBeforeToday(daysAgo));
        PersonProperty personProperty = personPropertyRepo.findByPersonAndType(personId.getPerson(), PersonPropertyTypeEnum.RESIDENCE);
        PropertyDetail actualPropertyDetail = propertyDetailRepo.findByProperty(personProperty.getIdentifier().getPropertyEntity());

        testAssert.assertNotEquals(propertyDetail.getId(), actualPropertyDetail.getId(), personName+" ("+personId.getNaturalId()+") is still living in property"+propertyName+" => "+propertyDetail.toString());
    }
}
