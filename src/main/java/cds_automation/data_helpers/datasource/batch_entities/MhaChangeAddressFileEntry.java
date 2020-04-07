package cds_automation.data_helpers.datasource.batch_entities;

import cds_automation.enums.datasource.AddressIndicatorEnum;
import cds_automation.utilities.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class MhaChangeAddressFileEntry {
    private String idNumber;
    private AddressIndicatorEnum oldAddressIndicator;
    private AddressFileEntry oldAddressFileEntry;
    private AddressIndicatorEnum newAddressIndicator;
    private AddressFileEntry newAddressFileEntry;
    private String addressChangedDate;

    public MhaChangeAddressFileEntry(String idNumber, AddressIndicatorEnum oldAddressIndicator, AddressFileEntry oldAddressFileEntry, AddressIndicatorEnum newAddressIndicator, AddressFileEntry newAddressFileEntry, String addressChangedDate) {
        this.idNumber = idNumber;
        this.oldAddressIndicator = oldAddressIndicator;
        this.oldAddressFileEntry = oldAddressFileEntry;
        this.newAddressIndicator = newAddressIndicator;
        this.newAddressFileEntry = newAddressFileEntry;
        this.addressChangedDate = addressChangedDate;
    }

    public String toString() {
        return StringUtils.leftPad(idNumber, 9)+
                StringUtils.leftPad(newAddressIndicator.getValue(), 1)+
                newAddressFileEntry.toString()+
                addressChangedDate+
                StringUtils.leftPad(oldAddressIndicator.getValue(), 1)+
                oldAddressFileEntry.toString()+" ";
    }
}
