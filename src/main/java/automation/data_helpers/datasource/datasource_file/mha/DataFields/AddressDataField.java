package automation.data_helpers.datasource.datasource_file.mha.DataFields;

import automation.data_helpers.datasource.datasource_file.DataField;
import automation.data_helpers.datasource.datasource_file.mha.DataFields.MhaAddress.MhaAddressField;
import automation.data_helpers.datasource.datasource_file.mha.DataFields.NcaAddress.NcaAddressField;
import automation.enums.datasource.AddressIndicatorEnum;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class AddressDataField extends DataField {

    public AddressDataField() {
        final String addressIndicator = AddressIndicatorEnum.pick().getValue();
        if(addressIndicator.equalsIgnoreCase("C")) {
            this.value = addressIndicator + new MhaAddressField(true).toRawString();
        }
        else if (addressIndicator.equalsIgnoreCase("Z") ){
            this.value = addressIndicator + new MhaAddressField(false).toRawString();
        }
        else {
            this.value = addressIndicator + new NcaAddressField().toRawString();
        }
    }

    @Override
    public String name() {
        return "address";
    }

    @Override
    public int length() {
        return 91;
    }

    @Override
    public String toRawString() {
        return this.value;
    }
}
