package automation.data_helpers.datasource.factories;

import automation.data_setup.PhakAddress;
import automation.data_setup.data_setup_address.PhakAbstractAddress;
import automation.enums.datasource.FormatType;
import automation.enums.datasource.PersonPropertyTypeEnum;
import automation.enums.datasource.PreparedPropertyTypeEnum;
import automation.enums.datasource.SpecialMappingEnum;
import automation.enums.automation.PropertyTypeEnum;
import automation.enums.automation.ResidencyEnum;
import automation.models.datasource.Batch;
import automation.models.datasource.Nationality;
import automation.models.datasource.Person;
import automation.models.datasource.PersonDetail;
import automation.models.datasource.PersonProperty;
import automation.models.datasource.Property;
import automation.models.datasource.PropertyDetail;
import automation.models.datasource.SpecialMapping;
import automation.models.datasource.SpecialProperty;
import automation.models.datasource.embeddables.BiTemporalData;
import automation.models.datasource.embeddables.PersonPropertyId;

import java.sql.Timestamp;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

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

    public PropertyDetail createPropertyFor(Person person, ResidencyEnum ownershipEnum) {
        return createPropertyFor(person, ownershipEnum, PropertyTypeEnum.pick());
    }

    public PropertyDetail createPropertyFor(Person person, ResidencyEnum ownershipEnum, PropertyTypeEnum propertyTypeEnum) {
        AddressOptions addressOptions = new AddressOptions(propertyTypeEnum, ownershipEnum);
        return createProperty(person, addressOptions);
    }

    public void createPersonPropertyWhenAddressExist(Person person, Property property, ResidencyEnum residencyEnum){
        Batch batch = Batch.builder().build();
        batchRepo.save(batch);

        PersonPropertyId personPropertyId = PersonPropertyId.builder()
                .personEntity(person)
                .propertyEntity(property)
                .validFrom(dateUtils.beginningOfDayToTimestamp(retrieveBirthDate(person)))
                .type(PersonPropertyTypeEnum.OWNERSHIP)
                .build();

        if ( residencyEnum.equals(ResidencyEnum.BOTH) ) {
            PersonPropertyId personPropertyId2 = PersonPropertyId.builder()
                    .personEntity(person)
                    .propertyEntity(property)
                    .validFrom(dateUtils.beginningOfDayToTimestamp(retrieveBirthDate(person)))
                    .type(PersonPropertyTypeEnum.RESIDENCE)
                    .build();
            personPropertyRepo.save(PersonProperty.create(batch, personPropertyId, Timestamp.valueOf("9999-12-31 23:59:59")));
            personPropertyRepo.save(PersonProperty.create(batch, personPropertyId2, Timestamp.valueOf("9999-12-31 23:59:59")));
        } else {
            if ( residencyEnum.equals(ResidencyEnum.OWNERSHIP) ) {
                personPropertyRepo.save(PersonProperty.create(batch, personPropertyId, Timestamp.valueOf("9999-12-31 23:59:59")));
            } else {
                personPropertyId.setType(PersonPropertyTypeEnum.RESIDENCE);
                personPropertyRepo.save(PersonProperty.create(batch, personPropertyId, Timestamp.valueOf("9999-12-31 23:59:59")));
            }
        }
    }

    private PropertyDetail createProperty(Person person, AddressOptions addressOptions) {
        Batch batch = Batch.createCompleted();
        BiTemporalData biTemporalData = new BiTemporalData()
                .generateNewBiTemporalData(dateUtils.beginningOfDayToTimestamp(retrieveBirthDate(person)));
        Property property = Property.builder().build();
        PropertyDetail propertyDetail = createPropertyData(addressOptions, batch, property, biTemporalData);

        Nationality nationality = nationalityRepo.findNationalityByPerson(person);

        PersonPropertyId personPropertyId = PersonPropertyId.builder()
                .personEntity(person)
                .propertyEntity(propertyDetail.getProperty())
                .validFrom(nationality.getCitizenshipAttainmentDate() == null ?
                        dateUtils.beginningOfDayToTimestamp(retrieveBirthDate(person)) :
                        nationality.getCitizenshipAttainmentDate())
                .type(PersonPropertyTypeEnum.OWNERSHIP)
                .build();

        batchRepo.save(batch);
        propertyRepo.save(property);
        propertyDetailRepo.save(propertyDetail);

        if ( addressOptions.getPhakAbstractAddress().isSpecialProperty() ) {
            specialPropertyRepo.save(SpecialProperty.create(batch, property, addressOptions.getPhakAbstractAddress().getSpecialMappingEnum(), addressOptions.getPhakAbstractAddress().getHomeTypeEnum(), biTemporalData));
            if ( addressOptions.getPhakAbstractAddress().getSpecialMappingEnum().equals(SpecialMappingEnum.LORONG_BUANGKOK) ) {
                specialMappingRepo.save(SpecialMapping.createLorongBuangkok(addressOptions.getPhakAbstractAddress().getPostalCode(), biTemporalData));
            }
        }

        if ( addressOptions.ownershipEnum.equals(ResidencyEnum.BOTH) ) {
            PersonPropertyId personPropertyId2 = PersonPropertyId.builder()
                    .personEntity(person)
                    .propertyEntity(property)
                    .validFrom(dateUtils.beginningOfDayToTimestamp(retrieveBirthDate(person)))
                    .type(PersonPropertyTypeEnum.RESIDENCE)
                    .build();
            personPropertyRepo.save(PersonProperty.create(batch, personPropertyId, Timestamp.valueOf("9999-12-31 23:59:59")));
            personPropertyRepo.save(PersonProperty.create(batch, personPropertyId2, Timestamp.valueOf("9999-12-31 23:59:59")));
        } else {
            if ( addressOptions.ownershipEnum.equals(ResidencyEnum.OWNERSHIP) ) {
                personPropertyRepo.save(PersonProperty.create(batch, personPropertyId, Timestamp.valueOf("9999-12-31 23:59:59")));
            } else {
                personPropertyId.setType(PersonPropertyTypeEnum.RESIDENCE);
                personPropertyRepo.save(PersonProperty.create(batch, personPropertyId, Timestamp.valueOf("9999-12-31 23:59:59")));
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
