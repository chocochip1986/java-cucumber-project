package cdit_automation.enums;

import java.util.Random;

public enum YesNoTypeEnum {
    YES("Y"),
    NO("N");

    private String value;

    YesNoTypeEnum(String s) {
        this.value = s;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String getValue() {
        return this.value;
    }

    // returns the relevant enum for a given string value
    public static YesNoTypeEnum fromString(String str) {
        for (YesNoTypeEnum enumVal : YesNoTypeEnum.values()) {
            if (enumVal.getValue().equals(str)) {
                return enumVal;
            }
        }
        return null;
    }

    public static YesNoTypeEnum random() {
        return YesNoTypeEnum.values()[new Random().nextInt(YesNoTypeEnum.values().length)];
    }
}
