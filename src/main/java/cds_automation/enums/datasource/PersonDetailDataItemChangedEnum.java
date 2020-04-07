package cds_automation.enums.datasource;

import java.util.Random;

public enum PersonDetailDataItemChangedEnum {
    GENDER("S"),
    NAME("N"),
    DATE_OF_BIRTH("B");

    private String value;

    PersonDetailDataItemChangedEnum(String s) {
        this.value = s;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String getValue() {
        return this.value;
    }

    public static PersonDetailDataItemChangedEnum fromString(String str) {
        for (PersonDetailDataItemChangedEnum enumVal : PersonDetailDataItemChangedEnum.values()) {
            if (enumVal.getValue().equals(str)) {
                return enumVal;
            }
        }
        return null;
    }

    public static PersonDetailDataItemChangedEnum pick() {
        return PersonDetailDataItemChangedEnum.values()[new Random().nextInt(PersonDetailDataItemChangedEnum.values().length)];
    }
}
