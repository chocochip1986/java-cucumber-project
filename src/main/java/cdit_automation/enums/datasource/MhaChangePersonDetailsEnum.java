package cdit_automation.enums.datasource;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum MhaChangePersonDetailsEnum {
  Person_Detail("PD"),
  Person_Name("PN"),
  Person_Gender("PG");

  private String value;

  MhaChangePersonDetailsEnum(String s) {
    this.value = s;
  }

  public String getValue() {
    return this.value;
  }

  public static MhaChangePersonDetailsEnum fromString(String str) {
    for (MhaChangePersonDetailsEnum enumVal : MhaChangePersonDetailsEnum.values()) {
      if (enumVal.getValue().equals(str)) {
        return enumVal;
      }
    }
    return null;
  }
}
