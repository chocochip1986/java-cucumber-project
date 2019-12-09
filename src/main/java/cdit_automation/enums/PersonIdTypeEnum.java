package cdit_automation.enums;

public enum PersonIdTypeEnum {
    NRIC("NRIC"),
    FIN("FIN"),
    PP("PP");

    private String value;

    PersonIdTypeEnum(String s) {
        this.value = s;
    }
}
