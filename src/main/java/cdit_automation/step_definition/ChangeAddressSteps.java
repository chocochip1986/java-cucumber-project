package cdit_automation.step_definition;

import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.PropertyTypeEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.PersonId;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.But;
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

    @Given("the mha change address file has the following details:")
    public void theMhaChangeAddressFileHasTheFollowingDetails(DataTable dataTable) {
    }

    @Given("^(?:An|A) (singaporean|foreign) person ([A-Za-z]+) (owns|resides (?:in)) a ([A-Z]+) property$")
    public void personAOwnsAProperty(String residentialStatus, String person, String residency, String propertyType) {
        if (PropertyTypeEnum.fromString(propertyType) == null ) {
            throw new TestFailException("Unsupported property type in PropertyTypeEnum");
        }

        PersonId personId;
        if ( residentialStatus.equals("singaporean") ) {
            personId = personFactory.createNewSCPersonId();
        } else {
            personId = personFactory.createNewFRPersonId();
        }

        testContext.set(person, personId);

    }

    @But("^resides in another ([A-Z]+) property$")
    public void residesInAnotherProperty() {
    }
}
