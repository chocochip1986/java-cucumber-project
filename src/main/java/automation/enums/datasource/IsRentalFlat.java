package automation.enums.datasource;

public enum IsRentalFlat {
    YES("Y"),
    NO("N");

    private String value;

    private IsRentalFlat(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    // returns the relevant enum for a given string value
    public static IsRentalFlat fromString(String str) {
        for (IsRentalFlat enumVal : IsRentalFlat.values()) {
            if (enumVal.getValue().equals(str)) {
                return enumVal;
            }
        }
        return null;
    }
}
