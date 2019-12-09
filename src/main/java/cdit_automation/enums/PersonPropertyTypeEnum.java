package cdit_automation.enums;

public enum PersonPropertyTypeEnum {
    RESIDENCE("RESIDENCE"),
    OWNERSHIP("OWNERSHIP");

    private String value;

    PersonPropertyTypeEnum(String s) {
        this.value = s;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
