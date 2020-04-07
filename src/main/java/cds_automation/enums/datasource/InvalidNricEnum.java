package cds_automation.enums.datasource;

public enum InvalidNricEnum {
    EMPTY("EMPTY"),
    EMPTY_SPACE("EMPTY_SPACE"),
    INVALID("INVALID"),
    SHORT("SHORT"),
    S555("S555"),
    S888("S888");

    private String value;

    InvalidNricEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static InvalidNricEnum fromString(String str) {
        for (InvalidNricEnum enumVal : InvalidNricEnum.values()) {
            if (enumVal.getValue().equals(str)) {
                return enumVal;
            }
        }
        return null;
    }
}