package cdit_automation.enums;

public enum HomeTypeEnum {
    COMMUNITY_GROUP_HOME("0"),
    COMMUNITY_HOSPITAL("1"),
    DISABILITY_HOME("2"),
    DISABILITY_HOSTEL("3"),
    HALFWAY_HOME("4"),
    NON_RESIDENTIAL_IN_AWWA_AND_HENDERSON("5"),
    SCORE_SHELTER("6"),
    SENIOR_GROUP_HOME("7"),
    SENIOR_GROUP_HOME_PROGRAMME_WITHIN_SHELTERED_HOME("8"),
    SHELTERED_HOME("9"),
    WELFARE_HOME("10");

    private String value;

    HomeTypeEnum(String s) {
        this.value = s;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String getValue() {
        return this.value;
    }

    public static HomeTypeEnum fromString(String str) {
        for (HomeTypeEnum enumVal : HomeTypeEnum.values()) {
            if (enumVal.getValue().equals(str)) {
                return enumVal;
            }
        }
        return null;
    }
}
