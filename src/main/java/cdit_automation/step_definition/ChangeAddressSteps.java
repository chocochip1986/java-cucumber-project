package cdit_automation.step_definition;

import cdit_automation.constants.Constants;
import cdit_automation.data_setup.PhakAddress;
import cdit_automation.data_setup.data_setup_address.PhakAbstractAddress;
import cdit_automation.enums.AddressIndicatorEnum;
import cdit_automation.enums.FileTypeEnum;
import cdit_automation.enums.PersonPropertyTypeEnum;
import cdit_automation.enums.SpecialMappingEnum;
import cdit_automation.enums.automation.PropertyTypeEnum;
import cdit_automation.enums.automation.ResidencyEnum;
import cdit_automation.exceptions.TestFailException;
import cdit_automation.models.Batch;
import cdit_automation.models.PersonId;
import cdit_automation.models.PersonProperty;
import cdit_automation.models.PropertyDetail;
import cdit_automation.models.SpecialProperty;
import cdit_automation.models.embeddables.PersonPropertyId;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.groups.Tuple;
import org.junit.Ignore;

@Slf4j
@Ignore
public class ChangeAddressSteps extends AbstractSteps {

    @Given("^the mha change address file is empty$")
    public void theMhaChangeAddressFileIsEmpty() {
        batchFileDataWriter.begin(mhaChangeAddressDataPrep.generateSingleDateNoOfRecordsHeader(0), FileTypeEnum.MHA_CHANGE_ADDRESS, null);
        batchFileDataWriter.end();
    }

    @Given("the mha change address file contains the following details:")
    public void theMhaChangeAddressFileHasTheFollowingDetails(DataTable dataTable) {
        List<Map<String, String>> listOfChangeAddresses = dataTable.asMaps(String.class, String.class);
        int noOfRecords = listOfChangeAddresses.size();

        batchFileDataWriter.begin(mhaChangeAddressDataPrep.generateSingleDateNoOfRecordsHeader(noOfRecords), FileTypeEnum.MHA_CHANGE_ADDRESS, null);
        mhaChangeAddressFileDataPrep.createBodyOfTestScenarios(listOfChangeAddresses, testContext);
        batchFileDataWriter.end();
    }

    @Given("^(?:An|A)(?: (\\d+) year old)? (singaporean|foreign) person ([A-Za-z]+) (owns|resides|owns and resides)(?: in)? a ([a-z_]+) property ([a-z0-9]+)$")
    public void personAOwnsAProperty(Integer age, String residentialStatus, String person, String residency, String propertyType, String propertyName) {
        PropertyTypeEnum propertyTypeEnum = retrievePropertyOrError(propertyType);
        LocalDate birthDate = age == null ? dateUtils.yearsBeforeToday(40) : dateUtils.yearsBeforeToday(age);

        PersonId personId = residentialStatus.equals("singaporean") ? personFactory.createNewSCPersonId(birthDate, person) : personFactory.createNewFRPersonId(birthDate, person);
        if(!testContext.contains(propertyName)){
            testContext.set(propertyName, addressFactory.createPropertyFor(personId.getPerson(), getOwnershipType(residency), propertyTypeEnum));
        }
        else{
            PropertyDetail existingPropertyDetail = testContext.get(propertyName);
            addressFactory.createPersonPropertyWhenAddressExist(personId.getPerson(), existingPropertyDetail.getProperty(), getOwnershipType(residency));
        }
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
            "(\\d+) days ago$")
    public void theMhaChangeAddressFileContainsInfoThat(String personName, String prevIndicatorType, String prevPropertyName, String curIndicatorType, String curPropertyName, long daysAgo) {
        checkIfPersonExistsInTestContext(personName);
        checkIfPropertyExistsInTestContext(prevPropertyName);
        checkIfPropertyExistsInTestContext(curPropertyName);

        LocalDate date = dateUtils.daysBeforeToday(daysAgo);

        batchFileDataWriter.begin(mhaChangeAddressDataPrep.generateSingleDateNoOfRecordsHeader(date, 1), FileTypeEnum.MHA_CHANGE_ADDRESS, null);
        mhaChangeAddressFileDataPrep.createLineInBody(testContext.get(personName),
                addressIndicatorEnumFrom(prevIndicatorType),
                testContext.get(prevPropertyName),
                addressIndicatorEnumFrom(curIndicatorType),
                testContext.get(curPropertyName), dateUtils.daysBeforeToday(daysAgo));
        batchFileDataWriter.end();

        testContext.set("expectedNewAddress", testContext.get(curPropertyName));
    }

    @And("^the mha change address file contains information that ([A-Za-z]+) changed from \\((mha_z|mha_c|nca)\\)([a-z0-9]+) to a new \\((mha_z|mha_c|nca)\\)([a-z_]+) property " +
            "(\\d) days ago$")
    public void theMhaChangeAddressFileContainsInfoThat2(String personName, String prevIndicatorType, String prevPropertyName, String curIndicatorType, String propertyType, int daysAgo) {
        checkIfPersonExistsInTestContext(personName);
        checkIfPropertyExistsInTestContext(prevPropertyName);
        PropertyTypeEnum propertyTypeEnum = retrievePropertyOrError(propertyType);

        PhakAbstractAddress phakAddress = PhakAddress.suggestAnAddress(propertyTypeEnum);

        batchFileDataWriter.begin(mhaChangeAddressDataPrep.generateSingleDateNoOfRecordsHeader(1), FileTypeEnum.MHA_CHANGE_ADDRESS, null);
        mhaChangeAddressFileDataPrep.createLineInBodyWithNewCurAddress(testContext.get(personName),
                addressIndicatorEnumFrom(prevIndicatorType),
                testContext.get(prevPropertyName),
                addressIndicatorEnumFrom(curIndicatorType),
                phakAddress, dateUtils.daysBeforeToday(daysAgo));
        batchFileDataWriter.end();

        testContext.set("expectedNewAddress", phakAddress);
    }

    @And("^the mha change address file contains information that ([A-Za-z]+) changed from a new \\((mha_z|mha_c|nca)\\)([a-z0-9]+) property to \\((mha_z|mha_c|nca)\\)([a-z_]+) " +
            "(\\d) days ago$")
    public void theMhaChangeAddressFileContainsInfoThat3(String personName, String prevIndicatorType, String prevPropertyType, String curIndicatorType, String curPropertyName, int daysAgo) {
        checkIfPersonExistsInTestContext(personName);
        checkIfPropertyExistsInTestContext(curPropertyName);
        PropertyTypeEnum propertyTypeEnum = retrievePropertyOrError(prevPropertyType);

        batchFileDataWriter.begin(mhaChangeAddressDataPrep.generateSingleDateNoOfRecordsHeader(1), FileTypeEnum.MHA_CHANGE_ADDRESS, null);
        mhaChangeAddressFileDataPrep.createLineInBodyWithNewPrevAddress(testContext.get(personName),
                addressIndicatorEnumFrom(prevIndicatorType),
                propertyTypeEnum,
                addressIndicatorEnumFrom(curIndicatorType),
                testContext.get(curPropertyName), dateUtils.daysBeforeToday(daysAgo));
        batchFileDataWriter.end();
    }

    @And("^the mha change address file contains information that ([A-Za-z]+) changed from a new \\((mha_z|mha_c|nca)\\)([a-z0-9]+) property to a new \\((mha_z|mha_c|nca)\\)([a-z_]+) property " +
            "(\\d) days ago$")
    public void theMhaChangeAddressFileContainsInfoThat4(String personName, String prevIndicatorType, String prevPropertyName, String curIndicatorType, String curPropertyName, int daysAgo) {
        checkIfPersonExistsInTestContext(personName);
        PropertyTypeEnum prevPropertyTypeEnum = retrievePropertyOrError(prevPropertyName);
        PropertyTypeEnum curPropertyTypeEnum = retrievePropertyOrError(curPropertyName);

        batchFileDataWriter.begin(mhaChangeAddressDataPrep.generateSingleDateNoOfRecordsHeader(1), FileTypeEnum.MHA_CHANGE_ADDRESS, null);
        mhaChangeAddressFileDataPrep.createLineInBodyWithNewPrevAndCurAddress(testContext.get(personName),
                addressIndicatorEnumFrom(prevIndicatorType),
                prevPropertyTypeEnum,
                addressIndicatorEnumFrom(curIndicatorType),
                curPropertyTypeEnum,
                dateUtils.daysBeforeToday(daysAgo));
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
        PersonProperty personProperty = personPropertyRepo.findByPersonAndPropertyAndType(personId.getPerson(), propertyDetail.getProperty(), PersonPropertyTypeEnum.RESIDENCE.name());

        testAssert.assertNotNull(personProperty, "no person property record indicating that "+personName+" of "+personId.getNaturalId()+" resides in "+propertyName);
        testAssert.assertEquals(dateUtils.beginningOfDayToTimestamp(dateUtils.daysBeforeToday(daysAgo)),
                personProperty.getIdentifier().getValidFrom(),
                personName+" ("+personId.getNaturalId()+") does not reside in the address ("+propertyDetail.toString()+") from the address change date!");
    }

    @Then("^([A-Za-z]+) resides in the new ([a-z0-9]+) from (\\d+) days ago$")
    public void residesInNewAddressFromDaysAgo(String personName, String propertyName, int daysAgo) {
        checkIfPersonExistsInTestContext(personName);

        PersonId personId = testContext.get(personName);
        PhakAbstractAddress expectedPhakAddress = testContext.get("expectedNewAddress");
        PersonProperty actualPersonProperty = personPropertyRepo.findByPersonAndType(personId.getPerson(), PersonPropertyTypeEnum.RESIDENCE.name());
        PropertyDetail actualPropertyDetail = propertyDetailRepo.findByAddress(expectedPhakAddress.getUnitNo(),
                expectedPhakAddress.getBlockNo(),
                expectedPhakAddress.getFloorNo(),
                expectedPhakAddress.getBuildingName(),
                expectedPhakAddress.getStreetName(),expectedPhakAddress.getStreetCode(), expectedPhakAddress.getOldPostalCode(), expectedPhakAddress.getPostalCode());

        testAssert.assertNotNull(actualPersonProperty, "no person property record indicating that "+personName+" of "+personId.getNaturalId()+" resides in "+propertyName);
        testAssert.assertEquals(dateUtils.beginningOfDayToTimestamp(dateUtils.daysBeforeToday(daysAgo)),
                actualPersonProperty.getIdentifier().getValidFrom(),
                personName+" ("+personId.getNaturalId()+") does not reside in the address ("+expectedPhakAddress.toString()+") from the address change date!");
        testAssert.assertNotNull(actualPropertyDetail, "The new "+propertyName+" at "+expectedPhakAddress.toString()+" is not being persisted in PropertyDetail");
        testAssert.assertEquals(Tuple.tuple(expectedPhakAddress.getUnitNo(),
                expectedPhakAddress.getFloorNo(),
                expectedPhakAddress.getBlockNo(),
                expectedPhakAddress.getStreetCode(),
                expectedPhakAddress.getStreetName(),
                expectedPhakAddress.getBuildingName(),
                expectedPhakAddress.getOldPostalCode(),
                expectedPhakAddress.getPostalCode()),
                actualPropertyDetail, "No Property Detail record created!", "unit", "floor", "blockNumber", "streetCode", "streetName", "buildingName", "postalCode", "newPostalCode", "");
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
        PersonProperty personProperty = personPropertyRepo.findByPersonAndType(personId.getPerson(), PersonPropertyTypeEnum.RESIDENCE.name(), date);
        PropertyDetail actualPropertyDetail = propertyDetailRepo.findByProperty(personProperty.getIdentifier().getPropertyEntity());

        testAssert.assertEquals(propertyDetail.getId(), actualPropertyDetail.getId(), personName+" ("+personId.getNaturalId()+") is still living in property"+propertyName+" => "+propertyDetail.toString());
    }

    @And("^([A-Za-z]+) resided in a ([a-z_]+) property ([a-z0-9]+) (\\d+) days ago$")
    public void personResidedNAHdbPropertyXyzDaysAgo(String personName, String propertyType, String propertyName, int daysAgo) {
        checkIfPersonExistsInTestContext(personName);
        PropertyTypeEnum propertyTypeEnum = retrievePropertyOrError(propertyType);

        PersonId personId = testContext.get(personName);
        testContext.set(propertyName, addressFactory.createPropertyFor(personId.getPerson(), ResidencyEnum.RESIDENCE, propertyTypeEnum));
    }

    @And("^mha change address file states that ([A-Za-z]+) moved from ([a-z_]+) to ([a-z_]+) (\\d+) days ago$")
    public void mhaChangeAddressFileStatesThatPersonMovedFromPropertyToPropertyDaysAgo(String personName, String prevProperty, String curProperty, int daysAgo) {
        checkIfPersonExistsInTestContext(personName);
        checkIfPropertyExistsInTestContext(prevProperty);
        checkIfPropertyExistsInTestContext(curProperty);

        PersonId personId = testContext.get(personName);
        LocalDate addressChangeDate = dateUtils.daysBeforeToday(daysAgo);

        PropertyDetail prevPropertyDetail = testContext.get(prevProperty);
        PersonProperty prevPersonProperty = personPropertyRepo.findByPersonAndPropertyAndType(personId.getPerson(),
                prevPropertyDetail.getProperty(),
                PersonPropertyTypeEnum.RESIDENCE.name());
        PropertyDetail curPropertyDetail = testContext.get(curProperty);
        PersonProperty curPersonProperty = personPropertyRepo.findByPersonAndPropertyAndType(personId.getPerson(),
                curPropertyDetail.getProperty(),
                PersonPropertyTypeEnum.RESIDENCE.name());
        personPropertyRepo.updateValidTIll(prevPersonProperty.getIdentifier().getPersonEntity(),
                prevPersonProperty.getIdentifier().getPropertyEntity(), dateUtils.endOfDayToTimestamp(addressChangeDate.minusDays(1)));


        if ( curPersonProperty == null ) {
            Batch batch = batchRepo.save(Batch.createCompleted());
            PersonPropertyId personPropertyId = PersonPropertyId.builder()
                    .personEntity(personId.getPerson())
                    .propertyEntity(curPropertyDetail.getProperty())
                    .type(PersonPropertyTypeEnum.RESIDENCE)
                    .validFrom(dateUtils.beginningOfDayToTimestamp(addressChangeDate))
                    .build();

            personPropertyRepo.save(PersonProperty.createPersonProperty(batch, personPropertyId, Timestamp.valueOf(Constants.INFINITE_LOCAL_DATE_TIME)));
        } else {
            personPropertyRepo.updateValidFrom(curPersonProperty.getIdentifier().getPersonEntity(),
                    curPersonProperty.getIdentifier().getPropertyEntity(),
                    dateUtils.beginningOfDayToTimestamp(addressChangeDate));
        }

    }

    @And("^([A-Za-z]+) had lived in a ([a-z_]+) property ([a-z_]+) from ([0-9]+\\s(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s[0-9]{4}) to ([0-9]+\\s(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s[0-9]{4})$")
    public void personHadLivedInAPropertyFromYearsAgoToYearsAgo(String personName, String propertyType, String propertyName, String validFromString, String validTillString) {
        checkIfPersonExistsInTestContext(personName);
        PropertyTypeEnum propertyTypeEnum = retrievePropertyOrError(propertyType);

        PersonId personId = testContext.get(personName);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMM uuuu");
        LocalDate validFrom = LocalDate.parse(validFromString, format);
        LocalDate validTill = LocalDate.parse(validTillString, format);

        List<PersonProperty> originalPersonProperties = personPropertyRepo.findAllResidencesByPersonOrderByValidFromDesc(personId.getPerson());

        testContext.set(propertyName, addressFactory.createPropertyFor(personId.getPerson(), ResidencyEnum.RESIDENCE, propertyTypeEnum));
        PropertyDetail propertyDetail = testContext.get(propertyName);

        personPropertyRepo.updateValidFromAndValidTill(personId.getPerson(),
                propertyDetail.getProperty(),
                dateUtils.beginningOfDayToTimestamp(validFrom),
                dateUtils.endOfDayToTimestamp(validTill));
        PersonProperty personProperty = personPropertyRepo.findByPersonAndPropertyAndType(
                personId.getPerson(),
                propertyDetail.getProperty(),
                PersonPropertyTypeEnum.RESIDENCE.name(),
                dateUtils.beginningOfDayToTimestamp(validFrom));
        personPropertyTimelineReconstruction.reconstructResidenceTimelineFor(personId.getPerson(), originalPersonProperties, personProperty);
    }

    @And("^([A-Za-z]+) resides in the lorong buangkok special property$")
    public void personResidesInTheSpecialProperty(String personName) {
        checkIfPersonExistsInTestContext(personName);

        PersonId personId = testContext.get(personName);

        PropertyDetail propertyDetail = testContext.get("expectedNewAddress");
        testAssert.assertNotNull(propertyDetail, "No property details created for new address!");

        PersonProperty personProperty = personPropertyRepo.findByPersonAndPropertyAndType(personId.getPerson(), propertyDetail.getProperty(), PersonPropertyTypeEnum.RESIDENCE.name());
        testAssert.assertNotNull(personProperty, "No person property record created for "+personName+" ("+personId.getNaturalId()+"). This means said person does not live there!");

        SpecialProperty specialProperty = specialPropertyRepo.findByProperty(propertyDetail.getProperty());
        testAssert.assertNotNull(specialProperty, "New property for "+personName+" ("+personId.getNaturalId()+") is not tagged as a special property!");
        testAssert.assertEquals(SpecialMappingEnum.LORONG_BUANGKOK, specialProperty.getMainType(), "Special Property type is incorrect!");
    }

    @And("^([A-Za-z]+) resides in the new lorong buangkok special property$")
    public void personResidesInTheNewSpecialProperty(String personName) {
        checkIfPersonExistsInTestContext(personName);

        PersonId personId = testContext.get(personName);
        PhakAbstractAddress phakAbstractAddress = testContext.get("expectedNewAddress");

        PropertyDetail propertyDetail = propertyDetailRepo.findByAddress(phakAbstractAddress.getUnitNo(), phakAbstractAddress.getBlockNo(), phakAbstractAddress.getFloorNo(), null, phakAbstractAddress.getStreetName(), null, phakAbstractAddress.getOldPostalCode(), phakAbstractAddress.getPostalCode());
        testAssert.assertNotNull(propertyDetail, "No property details created for new address!");

        PersonProperty personProperty = personPropertyRepo.findByPersonAndPropertyAndType(personId.getPerson(), propertyDetail.getProperty(), PersonPropertyTypeEnum.RESIDENCE.name());
        testAssert.assertNotNull(personProperty, "No person property record created for "+personName+" ("+personId.getNaturalId()+"). This means said person does not live there!");

        SpecialProperty specialProperty = specialPropertyRepo.findByProperty(propertyDetail.getProperty());
        testAssert.assertNotNull(specialProperty, "New property for "+personName+" ("+personId.getNaturalId()+") is not tagged as a special property!");
        testAssert.assertEquals(phakAbstractAddress.getSpecialMappingEnum(), specialProperty.getMainType(), "Special Property type is incorrect!");
    }
}
