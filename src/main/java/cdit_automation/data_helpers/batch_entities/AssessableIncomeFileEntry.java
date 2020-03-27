package cdit_automation.data_helpers.batch_entities;

import cdit_automation.utilities.StringUtils;

import java.util.Map;

public class AssessableIncomeFileEntry {
  private String recordType;
  private String naturalId;
  private String assessableIncome;
  private String resultIndicator;
  private String assessmentYear;

  public AssessableIncomeFileEntry(Map<String, String> row) {
    this.recordType = row.get("RECORD_TYPE");
    this.naturalId = row.get("NATURAL_ID");
    this.assessableIncome = resolveAssessableIncome(row.get("ASSESSABLE_INCOME"));
    this.resultIndicator = resolveResultIndicator(row.get("RESULT_INDICATOR"));
    this.assessmentYear = row.get("ASSESSMENT_YEAR");
  }
  
  private String resolveAssessableIncome(String str) {
    return str.equalsIgnoreCase("<BLANK>") ? "" : str;
  }
  
  private String resolveResultIndicator(String str) {
    return str.equalsIgnoreCase("<BLANK>") ? "" : str;
  }

  public String toRawString() {
    return StringUtils.rightPad(this.recordType, 1)
        + StringUtils.rightPad(this.naturalId, 9)
        + StringUtils.rightPad(this.assessableIncome, 10)
        + StringUtils.rightPad(this.resultIndicator, 2)
        + StringUtils.rightPad(this.assessmentYear, 4)
        + StringUtils.rightPad("", 24, StringUtils.SPACE);
  }
}
