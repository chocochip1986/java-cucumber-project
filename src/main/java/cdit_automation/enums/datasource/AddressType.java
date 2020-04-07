package cdit_automation.enums.datasource;

public enum AddressType {
    RESIDENTIAL("S");

    private String value;

    private AddressType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    // returns the relevant enum for a given string value
    public static AddressType fromString(String str) {
        for (AddressType enumVal : AddressType.values()) {
            if (enumVal.getValue().equals(str)) {
                return enumVal;
            }
        }
        return null;
    }
}
