package cdit_automation.data_helpers.factories;

import cdit_automation.data_setup.PhakAddress;
import cdit_automation.data_setup.data_setup_address.PhakAbstractAddress;
import cdit_automation.enums.FormatType;
import cdit_automation.enums.PersonPropertyTypeEnum;
import cdit_automation.enums.PropertyType;
import cdit_automation.enums.PropertyTypeEnum;
import cdit_automation.models.Batch;
import cdit_automation.models.Person;
import cdit_automation.models.PersonDetail;
import cdit_automation.models.PersonProperty;
import cdit_automation.models.Property;
import cdit_automation.models.PropertyDetail;
import cdit_automation.models.embeddables.BiTemporalData;
import cdit_automation.models.embeddables.PersonPropertyId;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AddressFactory extends AbstractFactory {
    @Getter
    @Setter
    private class AddressOptions {
        private PropertyTypeEnum propertyTypeEnum;
        private PersonPropertyTypeEnum ownershipEnum;
        private PhakAbstractAddress phakAbstractAddress;

        public AddressOptions() {
            this.propertyTypeEnum = PropertyTypeEnum.pick();
            this.ownershipEnum = PersonPropertyTypeEnum.pick();
            this.phakAbstractAddress = PhakAddress.suggestAnAddress(this.propertyTypeEnum);
        }

        public AddressOptions(PropertyTypeEnum propertyTypeEnum, PersonPropertyTypeEnum ownershipEnum) {
            this.propertyTypeEnum = propertyTypeEnum;
            this.ownershipEnum = ownershipEnum;
            this.phakAbstractAddress = PhakAddress.suggestAnAddress(this.propertyTypeEnum);
        }

        public AddressOptions(PropertyTypeEnum propertyTypeEnum, PersonPropertyTypeEnum ownershipEnum, PhakAbstractAddress phakAbstractAddress) {
            this.propertyTypeEnum = propertyTypeEnum;
            this.ownershipEnum = ownershipEnum;
            this.phakAbstractAddress = phakAbstractAddress;
        }
    }

    private PropertyDetail createPropertyFor(Person person, PersonPropertyTypeEnum ownershipEnum, PropertyTypeEnum propertyTypeEnum, PhakAbstractAddress phakAbstractAddress ) {
        AddressOptions addressOptions = new AddressOptions(propertyTypeEnum, ownershipEnum, phakAbstractAddress);
        Batch batch = Batch.createCompleted();
        BiTemporalData biTemporalData = new BiTemporalData()
                .generateNewBiTemporalData(dateUtils.beginningOfDayToTimestamp(retrieveBirthDate(person)));
        PropertyDetail propertyDetail = createPropertyData(addressOptions, batch, biTemporalData);

        PersonPropertyId personPropertyId = PersonPropertyId.builder().personEntity(person).propertyEntity(propertyDetail.getProperty()).build();
        PersonProperty personProperty = PersonProperty.create(batch, personPropertyId, addressOptions.ownershipEnum, biTemporalData);

        batchRepo.save(batch);
        propertyDetailRepo.save(propertyDetail);
        personPropertyRepo.save(personProperty);

        return propertyDetail;
    }

    private PropertyDetail createPropertyFor(Person person, PersonPropertyTypeEnum ownershipEnum, PropertyTypeEnum propertyTypeEnum) {
        AddressOptions addressOptions = new AddressOptions(propertyTypeEnum, ownershipEnum);
        Batch batch = Batch.createCompleted();
        BiTemporalData biTemporalData = new BiTemporalData()
                .generateNewBiTemporalData(dateUtils.beginningOfDayToTimestamp(retrieveBirthDate(person)));
        PropertyDetail propertyDetail = createPropertyData(addressOptions, batch, biTemporalData);

        PersonPropertyId personPropertyId = PersonPropertyId.builder().personEntity(person).propertyEntity(propertyDetail.getProperty()).build();
        PersonProperty personProperty = PersonProperty.create(batch, personPropertyId, addressOptions.ownershipEnum, biTemporalData);

        batchRepo.save(batch);
        propertyDetailRepo.save(propertyDetail);
        personPropertyRepo.save(personProperty);

        return propertyDetail;
    }

    private PropertyDetail createPropertyData(AddressOptions addressOptions, Batch batch, BiTemporalData biTemporalData) {
        Property property = Property.builder().build();
        PropertyDetail propertyDetail = PropertyDetail.create(batch, addressOptions.getPhakAbstractAddress().getUnitNo(), addressOptions.getPhakAbstractAddress().getBlockNo(), addressOptions.getPhakAbstractAddress().getFloorNo(),
                addressOptions.getPhakAbstractAddress().getBuildingName(), addressOptions.getPhakAbstractAddress().getStreetName(), null, addressOptions.getPhakAbstractAddress().getOldPostalCode(),
                addressOptions.getPhakAbstractAddress().getPostalCode(), PropertyType.pick(), FormatType.MHA, property, biTemporalData);

        return propertyDetail;
    }

    private LocalDate retrieveBirthDate(Person person) {
        PersonDetail personDetail = personDetailRepo.findByPerson(person);
        if ( personDetail == null ) {
            return LocalDate.now();
        } else {
            return personDetail.getDateOfBirth();
        }
    }
}
