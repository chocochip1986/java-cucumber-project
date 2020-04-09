package automation.enums.datasource;

public enum SpringJobStatusEnum {
  ABANDONED("ABANDONED"),
  COMPLETED("COMPLETED"),
  FAILED("FAILED"),
  STARTED("STARTED"),
  STARTING("STARTING"),
  STOPPED("STOPPED"),
  STOPPING("STOPPING"),
  UNKNOWN("UNKNOWN");

  private String value;

  SpringJobStatusEnum(String s) {
    this.value = s;
  }
}
