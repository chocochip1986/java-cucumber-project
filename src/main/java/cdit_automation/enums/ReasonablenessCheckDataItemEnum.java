package cdit_automation.enums;

import lombok.Getter;

@Getter
public enum ReasonablenessCheckDataItemEnum {
    // MHA bulk new 13 year old annual file
    NO_OF_NEW_THIRTEEN_YEAR_OLD("No of new 13 year old");

    private String value;

    ReasonablenessCheckDataItemEnum(String value) {
        this.value = value;
    }

    public static ReasonablenessCheckDataItemEnum fromString(String str) {
        for (ReasonablenessCheckDataItemEnum enumVal : ReasonablenessCheckDataItemEnum.values()) {
            if (enumVal.getValue().equals(str)) {
                return enumVal;
            }
        }

        return null;
    }
}
