package cdit_automation.enums;

public enum ShopHouseLQ {
    ONE_ROOM("1"),
    TWO_ROOM("2"),
    THREE_ROOM("3"),
    FOUR_ROOM("4"),
    FIVE_ROOM("5"),
    EXECUTIVE("E"),
    SPACE("");

    private String value;

    private ShopHouseLQ(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    // returns the relevant enum for a given string value
    public static ShopHouseLQ fromString(String str) {
        for (ShopHouseLQ enumVal : ShopHouseLQ.values()) {
            if (enumVal.getValue().equals(str)) {
                return enumVal;
            }
        }
        return null;
    }
}
