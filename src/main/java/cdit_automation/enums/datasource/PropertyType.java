package cdit_automation.enums.datasource;

import java.util.Random;

public enum PropertyType {
    HDB("H"),
    NON_HDB("N"),
    PREWAR_SIT_PSA("P"),
    ISLAND_ADDRESS("ISLAND_ADDRESS"),
    LORONG_BUANGKOK_ADDRESS("LORONG_BUANGKOK_ADDRESS");

    private String value;

    PropertyType(String value) {
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

    public static PropertyType pick() {
        return PropertyType.values()[new Random().nextInt(PropertyType.values().length)];
    }
}
