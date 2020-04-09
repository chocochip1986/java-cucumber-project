package automation.enums.datasource;

public enum HdbActionCode {
    NEW_FLATS("A"),
    DEMOLISHED_FLATS("D"),
    CHANGES_IN_DETAILS_FOR_SAME_ADDR("U"),
    BULK_FILE("");

    private String value;

    private HdbActionCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    // returns the relevant enum for a given string value
    public static HdbActionCode fromString(String str) {
        for (HdbActionCode enumVal : HdbActionCode.values()) {
            if (enumVal.getValue().equals(str)) {
                return enumVal;
            }
        }
        return null;
    }
}
