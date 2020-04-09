package automation.enums.datasource;

public enum ResultIndicatorEnum {
  ID_NOT_EXIST_IN_DB("00"),
  FINALISED_ASSESSMENT_FOUND("01"),
  NO_ASSESSMENT_RECORD_FOUND_RETURN_ISSUED("02"),
  INVALID_ID("03"),
  NO_ASSESSMENT_RECORD_FOUND_NO_RETURN_ISSUED("04"),
  DEFAULT_ASSESSMENT_FOUND("05"),
  YEAR_OF_ASSESSMENT_OUT_OF_RANGE("06");

  private String value;

  ResultIndicatorEnum(String s) {
    this.value = s;
  }

  public String getValue() {
    return this.value;
  }

  // returns the relevant enum for a given string value
  public static ResultIndicatorEnum fromString(String str) {
    for (ResultIndicatorEnum enumVal : ResultIndicatorEnum.values()) {
      if (enumVal.getValue().equals(str)) {
        return enumVal;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return this.value;
  }
}
