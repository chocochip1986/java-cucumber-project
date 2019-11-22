package cdit_automation.enums;

public enum PropertyType {
    HDB("H"),
    NON_HDB("N"),
    PREWAR_SIT_PSA("P"),
    ISLAND_ADDRESS("ISLAND_ADDRESS"),
    LORONG_BUANGKOK_ADDRESS("LORONG_BUANGKOK_ADDRESS");

    private String value;

    private PropertyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    // returns the relevant enum for a given string value
    public static PropertyType fromString(String str) {
        for (PropertyType pt : PropertyType.values()) {
            if (pt.getValue().equals(str)) {
                return pt;
            }
        }
        return null;
    }
}
