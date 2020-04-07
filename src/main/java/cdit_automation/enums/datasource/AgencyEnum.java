package cdit_automation.enums.datasource;

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

    public static AgencyEnum fromString(String name) {
        for (AgencyEnum enumVal : AgencyEnum.values()) {
            if (enumVal.getValue().equals(name)) {
                return enumVal;
            }
        }
        return null;
    }
}
