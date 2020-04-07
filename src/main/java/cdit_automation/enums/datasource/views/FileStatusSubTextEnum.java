package cdit_automation.enums.datasource.views;

public enum FileStatusSubTextEnum {
  HUNDRED_PERCENT_PASS("100% Pass"),
  PROCESSING("Processing"),
  SYSTEM_ERROR("System Error"),
  EXCEEDED_ERROR_RATE("Exceeded Error Rate"),
  FAILED_FILE("Failed File"),
  REJECTED_FILE("Rejected File");

  private String value;

  FileStatusSubTextEnum(String s) {
    this.value = s;
  }

  public String getValue() {
    return this.value;
  }
}