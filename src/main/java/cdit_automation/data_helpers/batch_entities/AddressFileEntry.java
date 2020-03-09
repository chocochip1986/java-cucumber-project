package cdit_automation.data_helpers.batch_entities;

public abstract class AddressFileEntry {
    protected final String SPACE = " ";
    protected final String EMPTY_STRING = "";
    protected String addressType;
    protected String unitNo;
    protected String blockNo;
    protected String floorNo;
    protected String streetName;
    protected String streetCode;
    protected String buildingName;
    protected String oldPostalCode;
    protected String postalCode;

    public abstract String toString();
}
