package cdit_automation.data_helpers.factories;

import cdit_automation.data_setup.PhakAddress;
import cdit_automation.data_setup.data_setup_address.PhakAbstractAddress;
import cdit_automation.enums.PersonPropertyTypeEnum;
import cdit_automation.enums.PropertyTypeEnum;
import cdit_automation.models.Batch;
import cdit_automation.models.Person;
import cdit_automation.models.PersonDetail;
import cdit_automation.models.PersonProperty;
import cdit_automation.models.Property;
import cdit_automation.models.PropertyDetail;
import cdit_automation.models.embeddables.PersonPropertyId;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

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
        return createPropertyData(addressOptions);
    }

    private PropertyDetail createPropertyFor(Person person, PersonPropertyTypeEnum ownershipEnum, PropertyTypeEnum propertyTypeEnum) {
        AddressOptions addressOptions = new AddressOptions(propertyTypeEnum, ownershipEnum);
        PersonPropertyId personPropertyId = PersonPropertyId.builder().build();
        PersonProperty personProperty = PersonProperty.create(batch, addressOptions.getPropertyTypeEnum());

        PropertyDetail propertyDetail = createPropertyData(addressOptions);

        return propertyDetail;
    }

    private PropertyDetail createPropertyData(AddressOptions addressOptions) {
        Batch batch = Batch.createCompleted();
        Property property = Property.builder().build();
        PropertyDetail propertyDetail = PropertyDetail.create();

    }
}
