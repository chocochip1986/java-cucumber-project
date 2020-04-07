package cdit_automation.enums.datasource;

public enum AnnualValueStatus {
    AV_ASSESSED("AV_ASSESSED"),
    AV_ASSESSED_0_TO_13K("AV_ASSESSED_0_TO_13K"),
    AV_ASSESSED_13K_TO_21K("AV_ASSESSED_13K_TO_21K"),
    AV_ASSESSED_MORE_THAN_21K("AV_ASSESSED_MORE_THAN_21K"),
    AV_PENDING("AV_PENDING"),
    AV_CANNOT_BE_DETERMINED("AV_CANNOT_BE_DETERMINED");

    private String value;

    private AnnualValueStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
