package cdit_automation.data_helpers.factories;

import cdit_automation.data_setup.PhakAddress;
import cdit_automation.data_setup.data_setup_address.PhakAbstractAddress;
import cdit_automation.enums.FormatType;
import cdit_automation.enums.PersonPropertyTypeEnum;
import cdit_automation.enums.PreparedPropertyTypeEnum;
import cdit_automation.enums.PropertyType;
import cdit_automation.enums.automation.PropertyTypeEnum;
import cdit_automation.enums.automation.ResidencyEnum;
import cdit_automation.models.Batch;
import cdit_automation.models.Person;
import cdit_automation.models.PersonDetail;
import cdit_automation.models.PersonProperty;
import cdit_automation.models.Property;
import cdit_automation.models.PropertyDetail;
import cdit_automation.models.SpecialProperty;
import cdit_automation.models.embeddables.BiTemporalData;
import cdit_automation.models.embeddables.PersonPropertyId;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class AddressFactory extends AbstractFactory {
    @Getter
    @Setter
    private class AddressOptions {
        private PropertyTypeEnum propertyTypeEnum;
        private ResidencyEnum ownershipEnum;
        private PhakAbstractAddress phakAbstractAddress;

        public AddressOptions() {
            this.propertyTypeEnum = PropertyTypeEnum.pick();
            this.ownershipEnum = ResidencyEnum.pick();
            this.phakAbstractAddress = PhakAddress.suggestAnAddress(this.propertyTypeEnum);
        }

        public AddressOptions(PropertyTypeEnum propertyTypeEnum, ResidencyEnum ownershipEnum) {
            this.propertyTypeEnum = propertyTypeEnum;
            this.ownershipEnum = ownershipEnum;
            this.phakAbstractAddress = PhakAddress.suggestAnAddress(this.propertyTypeEnum);
        }

        public AddressOptions(PropertyTypeEnum propertyTypeEnum, ResidencyEnum ownershipEnum, PhakAbstractAddress phakAbstractAddress) {
            this.propertyTypeEnum = propertyTypeEnum;
            this.ownershipEnum = ownershipEnum;
            this.phakAbstractAddress = phakAbstractAddress;
        }
    }

    public PropertyDetail createPropertyFor(Person person, ResidencyEnum ownershipEnum, PropertyTypeEnum propertyTypeEnum) {
        AddressOptions addressOptions = new AddressOptions(propertyTypeEnum, ownershipEnum);
        return createProperty(person, addressOptions);
    }

    private PropertyDetail createProperty(Person person, AddressOptions addressOptions) {
        Batch batch = Batch.createCompleted();
        BiTemporalData biTemporalData = new BiTemporalData()
                .generateNewBiTemporalData(dateUtils.beginningOfDayToTimestamp(retrieveBirthDate(person)));
        Property property = Property.builder().build();
        PropertyDetail propertyDetail = createPropertyData(addressOptions, batch, property, biTemporalData);

        PersonPropertyId personPropertyId = PersonPropertyId.builder().personEntity(person).propertyEntity(propertyDetail.getProperty()).build();

        batchRepo.save(batch);
        propertyRepo.save(property);
        propertyDetailRepo.save(propertyDetail);

        if ( addressOptions.getPhakAbstractAddress().isSpecialProperty() ) {
            specialPropertyRepo.save(SpecialProperty.create(batch, property, addressOptions.getPhakAbstractAddress().getSpecialMappingEnum(), addressOptions.getPhakAbstractAddress().getHomeTypeEnum(), biTemporalData));
        }

        if ( addressOptions.ownershipEnum.equals(ResidencyEnum.BOTH) ) {
            personPropertyRepo.save(PersonProperty.create(batch, personPropertyId, PersonPropertyTypeEnum.RESIDENCE, biTemporalData));
            personPropertyRepo.save(PersonProperty.create(batch, personPropertyId, PersonPropertyTypeEnum.OWNERSHIP, biTemporalData));
        } else {
            if ( addressOptions.ownershipEnum.equals(ResidencyEnum.OWNERSHIP) ) {
                personPropertyRepo.save(PersonProperty.create(batch, personPropertyId, PersonPropertyTypeEnum.OWNERSHIP, biTemporalData));
            } else {
                personPropertyRepo.save(PersonProperty.create(batch, personPropertyId, PersonPropertyTypeEnum.RESIDENCE, biTemporalData));
            }
        }

        return propertyDetail;
    }

    private PropertyDetail createPropertyData(AddressOptions addressOptions, Batch batch, Property property, BiTemporalData biTemporalData) {
        PropertyDetail propertyDetail = PropertyDetail.create(batch, addressOptions.getPhakAbstractAddress().getUnitNo(), addressOptions.getPhakAbstractAddress().getBlockNo(), addressOptions.getPhakAbstractAddress().getFloorNo(),
                addressOptions.getPhakAbstractAddress().getBuildingName(), addressOptions.getPhakAbstractAddress().getStreetName(), null, addressOptions.getPhakAbstractAddress().getOldPostalCode(),
                addressOptions.getPhakAbstractAddress().getPostalCode(), PreparedPropertyTypeEnum.pick(), FormatType.MHA, property, biTemporalData);

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
