package automation.enums.datasource;

public enum InvalidDateOfRunEnum {
    EMPTY("EMPTY"),
    EMPTY_SPACE("EMPTY_SPACE"),
    INVALID_FORMAT("INVALID_FORMAT"),
    FUTURE_DATE("FUTURE_DATE");

    private String value;

    InvalidDateOfRunEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static InvalidDateOfRunEnum fromString(String str) {
        for (InvalidDateOfRunEnum enumVal : InvalidDateOfRunEnum.values()) {
            if (enumVal.getValue().equals(str)) {
                return enumVal;
            }
        }
        return null;
    }
}
