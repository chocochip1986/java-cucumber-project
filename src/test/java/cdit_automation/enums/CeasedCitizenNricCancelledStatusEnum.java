package cdit_automation.enums;

public enum CeasedCitizenNricCancelledStatusEnum {
  YES("Y");

  private String value;

  CeasedCitizenNricCancelledStatusEnum(String s) {
    this.value = s;
  }

  public String getValue() {
    return this.value;
  }

  @Override
  public String toString() {
    return this.value;
  }

  public static CeasedCitizenNricCancelledStatusEnum fromString(String str) {

    for (CeasedCitizenNricCancelledStatusEnum enumVal :
        CeasedCitizenNricCancelledStatusEnum.values()) {
      if (enumVal.getValue().equals(str)) {
        return enumVal;
      }
    }

    return null;
  }

  public boolean getBooleanValue() {
    return this.value.equalsIgnoreCase(CeasedCitizenNricCancelledStatusEnum.YES.value);
  }
}
