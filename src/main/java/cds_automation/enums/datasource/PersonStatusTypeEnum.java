package cds_automation.enums.datasource;

public enum PersonStatusTypeEnum {
    
    NO_INTERACTION("NO_INTERACTION"),
    MERDEKA_GENERATION_PACKAGE("MERDEKA_GENERATION_PACKAGE");

    private String value;

    PersonStatusTypeEnum(String s) {
        this.value = s;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
