package cdit_automation.enums.datasource;

import java.util.Random;

public enum PersonPropertyTypeEnum {
    RESIDENCE("RESIDENCE"),
    OWNERSHIP("OWNERSHIP");

    private String value;

    PersonPropertyTypeEnum(String s) {
        this.value = s;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static PersonPropertyTypeEnum pick() {
        return PersonPropertyTypeEnum.values()[new Random().nextInt(PersonPropertyTypeEnum.values().length)];
    }
}
