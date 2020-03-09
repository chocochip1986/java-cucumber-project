package cdit_automation.data_helpers.batch_entities;

import cdit_automation.enums.MhaAddressTypeEnum;
import cdit_automation.utilities.StringUtils;

public class MhaOverseasFileEntry extends AddressFileEntry {
    public MhaOverseasFileEntry(String unitNo, String floorNo, String blockNo, String streetName, String buildingName, String oldPostalCode, String postalCode) {
        this.unitNo = unitNo == null ? EMPTY_STRING : unitNo;
        this.floorNo = floorNo == null ? EMPTY_STRING : floorNo;
        this.blockNo = blockNo == null ? EMPTY_STRING : blockNo;
        this.streetCode = "";
        this.streetName = streetName == null ? EMPTY_STRING : streetName;
        this.buildingName = buildingName == null ? EMPTY_STRING : buildingName;
        this.oldPostalCode = oldPostalCode == null ? EMPTY_STRING : oldPostalCode;
        this.postalCode = postalCode == null ? EMPTY_STRING : postalCode;
        this.addressType = MhaAddressTypeEnum.OVERSEAS_ADDRESS.getValue();
    }

    @Override
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
}
