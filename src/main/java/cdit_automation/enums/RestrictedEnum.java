package cdit_automation.enums;

public enum RestrictedEnum {
    NORMAL("NORMAL"),
    STAFF("STAFF"),
    VIP("VIP");

    private String value;

    RestrictedEnum(String s) {
        this.value = s;
    }

    @Override
    public String toString() {
        return this.value;
    }
}