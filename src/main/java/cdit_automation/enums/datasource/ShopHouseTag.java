package cdit_automation.enums.datasource;

public enum ShopHouseTag {
    OWNED_BY_HDB("Y"),
    NOT_OWNED_BY_HDB("N"),
    NOT_SHOP_HOUSE("");

    private String value;

    private ShopHouseTag(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    // returns the relevant enum for a given string value
    public static ShopHouseTag fromString(String str) {
        for (ShopHouseTag tag : ShopHouseTag.values()) {
            if (tag.getValue().equals(str)) {
                return tag;
            }
        }
        return null;
    }
}
