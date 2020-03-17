package cdit_automation.data_setup.data_setup_address;


import cdit_automation.enums.HomeTypeEnum;
import cdit_automation.enums.SpecialMappingEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhakAbstractAddress {
    protected String unitNo;
    protected String blockNo;
    protected String floorNo;
    protected String streetName;
    protected String buildingName;
    protected String oldPostalCode;
    protected String postalCode;
    protected String streetCode;

    protected Class klass;
    protected boolean isSpecialProperty;
    protected SpecialMappingEnum specialMappingEnum;
    protected HomeTypeEnum homeTypeEnum;

    public PhakAbstractAddress(String unitNo, String blockNo, String floorNo, String streetName, String buildingName, String oldPostalCode, String postalCode, String streetCode, Class klass) {
        this.unitNo = unitNo;
        this.blockNo = blockNo;
        this.floorNo = floorNo;
        this.streetName = streetName;
        this.buildingName = buildingName;
        this.oldPostalCode = oldPostalCode;
        this.postalCode = postalCode;
        this.streetCode = streetCode;
        this.klass = klass;
        isSpecialProperty = false;
        specialMappingEnum = null;
        homeTypeEnum = null;
    }
}