package cdit_automation.enums;

public enum AgencyEnum {
    HDB("HDB"),
    MSF("MSF"),
    MHA("MHA"),
    IRAS("IRAS"),
    SINGPOST("SINGPOST"),
    CPFB("CPFB");

    private String value;

    AgencyEnum(String s) {
        this.value = s;
    }

    public String getValue() {
        return this.value;
    }
}
