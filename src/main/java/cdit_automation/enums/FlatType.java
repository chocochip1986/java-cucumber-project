package cdit_automation.enums;

public enum FlatType {
    ONE_ROOM("1"),
    TWO_ROOM("2"),
    THREE_ROOM("3"),
    FOUR_ROOM("4"),
    FIVE_ROOM("5"),
    EXECUTIVE("E"),
    MULTI_GENERATION("8"),
    SPACE("");

    private String value;

    private FlatType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    // returns the relevant enum for a given string value
    public static FlatType fromString(String str) {
        for (FlatType ft : FlatType.values()) {
            if (ft.getValue().equals(str)) {
                return ft;
            }
        }
        return null;
    }
}
