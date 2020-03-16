package cdit_automation.enums;

public enum SpecialMappingEnum {
    PRISON_STAFF_QUARTERS_ONE_ROOM("PRISON_STAFF_QUARTERS_ONE_ROOM"),
    PRISON_STAFF_QUARTERS_WHOLE_UNIT("PRISON_STAFF_QUARTERS_WHOLE_UNIT"),
    LORONG_BUANGKOK("LORONG_BUANGKOK"),
    SPECIAL_ISLAND_STREET_CODE("SPECIAL_ISLAND_STREET_CODE"),
    SPECIAL_ISLAND_STREET_NAME("SPECIAL_ISLAND_STREET_NAME"),
    TAX_EXEMPTED_PROPERTIES("TAX_EXEMPTED_PROPERTIES"),
    TAX_EXEMPTED_NURSING_HOMES("TAX_EXEMPTED_NURSING_HOMES"),
    NURSING_HOMES("NURSING_HOMES");

    private String value;

    SpecialMappingEnum(String s) {
        this.value = s;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String getValue() {
        return this.value;
    }

    public static SpecialMappingEnum fromString(String str) {
        for (SpecialMappingEnum enumVal : SpecialMappingEnum.values()) {
            if (enumVal.getValue().equals(str)) {
                return enumVal;
            }
        }
        return null;
    }
}
