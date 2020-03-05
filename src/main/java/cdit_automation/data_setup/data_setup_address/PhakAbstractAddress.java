package cdit_automation.data_setup.data_setup_address;


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

    protected Class klass;

    public PhakAbstractAddress(String unitNo, String blockNo, String floorNo, String streetName, String buildingName, String oldPostalCode, String postalCode, Class klass) {
        this.unitNo = unitNo;
        this.blockNo = blockNo;
        this.floorNo = floorNo;
        this.streetName = streetName;
        this.buildingName = buildingName;
        this.oldPostalCode = oldPostalCode;
        this.postalCode = postalCode;
        this.klass = klass;
    }
}