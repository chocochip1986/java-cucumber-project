package cds_automation.enums.datasource;

public enum PersonIdTypeEnum {
    NRIC("NRIC"),
    FIN("FIN"),
    PP("PP");

    private String value;

    PersonIdTypeEnum(String s) {
        this.value = s;
    }
}
