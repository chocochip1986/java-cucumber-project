package cdit_automation.data_helpers.batch_entities;

import cdit_automation.enums.datasource.NcaAddressTypeEnum;
import cdit_automation.utilities.StringUtils;

public class NcaAddressFileEntry extends AddressFileEntry {
    public NcaAddressFileEntry (String unitNo, String floorNo, String blockNo, String streetCode, String oldPostalCode, String postalCode) {
        this.unitNo = unitNo == null ? EMPTY_STRING : unitNo;
        this.floorNo = floorNo == null ? EMPTY_STRING : floorNo;
        this.blockNo = blockNo == null ? EMPTY_STRING : blockNo;
        this.streetCode = streetCode == null ? EMPTY_STRING : streetCode;
        this.streetName = "";
        this.buildingName = "";
        this.oldPostalCode = oldPostalCode == null ? EMPTY_STRING : oldPostalCode;
        this.postalCode = postalCode == null ? EMPTY_STRING : postalCode;
        this.addressType = NcaAddressTypeEnum.RESIDENTIAL.getValue();
    }

    public NcaAddressFileEntry (String unitNo, String floorNo, String blockNo, String streetCode, String oldPostalCode, String postalCode, String addressType) {
        this.unitNo = unitNo == null ? EMPTY_STRING : unitNo;
        this.floorNo = floorNo == null ? EMPTY_STRING : floorNo;
        this.blockNo = blockNo == null ? EMPTY_STRING : blockNo;
        this.streetCode = streetCode == null ? EMPTY_STRING : streetCode;
        this.streetName = "";
        this.buildingName = "";
        this.oldPostalCode = oldPostalCode == null ? EMPTY_STRING : oldPostalCode;
        this.postalCode = postalCode == null ? EMPTY_STRING : postalCode;
        this.addressType = getAddressType(addressType);
    }

    public String toString() {
        return StringUtils.leftPad(addressType, 1)+
                StringUtils.leftPad(blockNo, 10)+
                StringUtils.leftPad(streetCode, 6)+
                StringUtils.leftPad(floorNo, 3)+
                StringUtils.leftPad(unitNo, 5)+
                StringUtils.leftPad(oldPostalCode, 4)+
                StringUtils.leftPad(postalCode, 6)+
                StringUtils.leftPad(SPACE, 55);
    }
    
    private String getAddressType(String inputType) {
        
        if (inputType == null || inputType.trim().isEmpty()) {
            return NcaAddressTypeEnum.RESIDENTIAL.getValue();
        }
        
        return inputType;
    }
}
