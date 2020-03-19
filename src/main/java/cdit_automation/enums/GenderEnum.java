package cdit_automation.enums;

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

    public static String invalidGender() {
        return "A";
    }
}
