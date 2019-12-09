package cdit_automation.enums;

public enum CeasedCitizenNationalityEnum {
  BLANK(""),
  SG("SG");

  private String value;

  CeasedCitizenNationalityEnum(String s) {
    this.value = s;
  }

  public String getValue() {
    return this.value;
  }

  @Override
  public String toString() {
    return this.value;
  }

  public static CeasedCitizenNationalityEnum fromString(String str) {

    for (CeasedCitizenNationalityEnum enumVal : CeasedCitizenNationalityEnum.values()) {
      if (enumVal.getValue().equals(str)) {
        return enumVal;
      }
    }
    return null;
  }
}
