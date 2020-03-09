package cdit_automation.step_definition;

import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.automation.PropertyTypeEnum;
import cdit_automation.enums.automation.ResidencyEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.PersonId;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;

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
        //TODO
    }

    @Given("^(?:An|A) (singaporean|foreign) person ([A-Za-z]+) (owns|resides|owns and resides)(?: in)? a ([a-z]+) property$")
    public void personAOwnsAProperty(String residentialStatus, String person, String residency, String propertyType) {
        PropertyTypeEnum propertyTypeEnum = retrievePropertyOrError(propertyType);

        PersonId personId = residentialStatus.equals("singaporean") ? personFactory.createNewSCPersonId() : personFactory.createNewFRPersonId();

        addressFactory.createPropertyFor(personId.getPerson(), getOwnershipType(residency), propertyTypeEnum);
        testContext.set(person, personId);
    }

    @And("^([A-Za-z]+) (owns|resides)(?: in)? a ([a-z]+) property$")
    public void residesInAnotherProperty(String person, String residency, String propertyType) {
        PropertyTypeEnum propertyTypeEnum = retrievePropertyOrError(propertyType);

        PersonId personId = testContext.get(person);
        addressFactory.createPropertyFor(personId.getPerson(), getOwnershipType(residency), propertyTypeEnum);
    }

    private PropertyTypeEnum retrievePropertyOrError(String propertyType) {
        if ( PropertyTypeEnum.fromString(propertyType) == null ) {
            throw new TestFailException("Unsupported property type in PropertyTypeEnum");
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
}
