package cdit_automation.enums;

public enum Gender {
    MALE("M"),
    FEMALE("F"),
    UNKNOWN("U");

    private String value;

    Gender(String s) {
        this.value = s;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String getValue() {
        return this.value;
    }

    public static Gender fromString(String str) {
        for (Gender enumVal : Gender.values()) {
            if (enumVal.getValue().equals(str)) {
                return enumVal;
            }
        }
        return null;
    }
}
