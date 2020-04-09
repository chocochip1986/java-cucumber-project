package automation.data_helpers.datasource.batch_entities;

import automation.enums.datasource.MhaAddressTypeEnum;
import automation.utilities.StringUtils;

public class MhaAddressFileEntry extends AddressFileEntry {
    public MhaAddressFileEntry(String unitNo, String floorNo, String blockNo, String streetName, String buildingName, String oldPostalCode, String postalCode) {
        this.unitNo = unitNo == null ? EMPTY_STRING : unitNo;
        this.floorNo = floorNo == null ? EMPTY_STRING : floorNo;
        this.blockNo = blockNo == null ? EMPTY_STRING : blockNo;
        this.streetCode = "";
        this.streetName = streetName == null ? EMPTY_STRING : streetName;
        this.buildingName = buildingName == null ? EMPTY_STRING : buildingName;
        this.oldPostalCode = oldPostalCode == null ? EMPTY_STRING : oldPostalCode;
        this.postalCode = postalCode == null ? EMPTY_STRING : postalCode;
        this.addressType = MhaAddressTypeEnum.pick().getValue();
    }

    public MhaAddressFileEntry(String unitNo, String floorNo, String blockNo, String streetName, String buildingName, String oldPostalCode, String postalCode, String addressType) {
        this.unitNo = unitNo == null ? EMPTY_STRING : unitNo;
        this.floorNo = floorNo == null ? EMPTY_STRING : floorNo;
        this.blockNo = blockNo == null ? EMPTY_STRING : blockNo;
        this.streetCode = "";
        this.streetName = streetName == null ? EMPTY_STRING : streetName;
        this.buildingName = buildingName == null ? EMPTY_STRING : buildingName;
        this.oldPostalCode = oldPostalCode == null ? EMPTY_STRING : oldPostalCode;
        this.postalCode = postalCode == null ? EMPTY_STRING : postalCode;
        this.addressType = getAddressType(addressType);
    }
    
    public String toString() {
        return StringUtils.leftPad(addressType, 1)+
                StringUtils.leftPad(blockNo, 10)+
                StringUtils.leftPad(streetName, 32)+
                StringUtils.leftPad(floorNo, 2)+
                StringUtils.leftPad(unitNo, 5)+
                StringUtils.leftPad(buildingName, 30)+
                StringUtils.leftPad(oldPostalCode, 4)+
                StringUtils.leftPad(postalCode, 6);
    }
    
    private String getAddressType(String inputType) {

        if (inputType == null || inputType.trim().isEmpty()) {
            return MhaAddressTypeEnum.pick().getValue();
        }

        return inputType;
    }
}
