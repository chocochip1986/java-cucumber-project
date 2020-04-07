package cds_automation.enums.datasource;

import java.util.Random;

public enum GenderEnum {
    MALE("M"),
    FEMALE("F"),
    UNKNOWN("U");

    private String value;

    GenderEnum(String s) {
        this.value = s;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String getValue() {
        return this.value;
    }

    public static GenderEnum fromString(String str) {
        for (GenderEnum enumVal : GenderEnum.values()) {
            if (enumVal.getValue().equals(str)) {
                return enumVal;
            }
        }
        return null;
    }

    public static GenderEnum pick() {
        return GenderEnum.values()[new Random().nextInt(GenderEnum.values().length)];
    }

    public static String invalidGender() {
        return "A";
    }
}
