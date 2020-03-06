package cdit_automation.data_helpers.batch_entities;

import cdit_automation.utilities.StringUtils;

import java.util.Map;

public class MhaNewCitizenFileEntry {
  private String nric;
  private String fin;
  private String name;
  private String dateOfBirth;
  private String gender;
  private String oldAddressIndicator;
  private String oldAddressIndicator2;
  private String oldAddressRaw;
  private String newAddressIndicator;
  private String newAddressIndicator2;
  private String newAddressRaw;
  private String newInvalidAddressTag;
  private String dateOfAddressChange;
  private String citizenshipAttainmentIssueDate;

  public MhaNewCitizenFileEntry(Map<String, String> row) {
    this.nric = row.get("NRIC");
    this.fin = row.get("FIN");
    this.name = row.get("NAME");
    this.dateOfBirth = row.get("DOB");
    this.gender = row.get("GENDER");
    this.oldAddressIndicator = row.get("OLD_ADDR_IND");
    this.oldAddressIndicator2 = row.get("OLD_ADDR_IND2");
    this.oldAddressRaw = row.get("OLD_ADDR");
    this.newAddressIndicator = row.get("NEW_ADDR_IND");
    this.newAddressIndicator2 = row.get("NEW_ADDR_IND2");
    this.newAddressRaw = row.get("NEW_ADDR");
    this.newInvalidAddressTag =
        row.get("NEW_INVALID_ADDR_TAG") != null ? row.get("NEW_INVALID_ADDR_TAG") : "";
    this.dateOfAddressChange = row.get("DATE_OF_ADDR_CHANGE");
    this.citizenshipAttainmentIssueDate = row.get("CTZ_ATT_DATE");
  }

  public String toRawString() {
    return StringUtils.rightPad(this.nric, 9, StringUtils.SPACE)
        + StringUtils.rightPad(this.fin, 9, StringUtils.SPACE)
        + StringUtils.rightPad(this.name, 66, StringUtils.SPACE)
        + StringUtils.rightPad(this.dateOfBirth, 8, StringUtils.SPACE)
        + StringUtils.rightPad(this.gender, 1, StringUtils.SPACE)
        + StringUtils.rightPad(this.oldAddressIndicator, 1, StringUtils.SPACE)
        + StringUtils.rightPad(this.oldAddressIndicator2, 1, StringUtils.SPACE)
        + StringUtils.rightPad(this.oldAddressRaw, 89, StringUtils.SPACE)
        + StringUtils.rightPad(this.newAddressIndicator, 1, StringUtils.SPACE)
        + StringUtils.rightPad(this.newAddressIndicator2, 1, StringUtils.SPACE)
        + StringUtils.rightPad(this.newAddressRaw, 89, StringUtils.SPACE)
        + StringUtils.rightPad(this.newInvalidAddressTag, 1, StringUtils.SPACE)
        + StringUtils.rightPad(this.dateOfAddressChange, 8, StringUtils.SPACE)
        + StringUtils.rightPad(this.citizenshipAttainmentIssueDate, 8, StringUtils.SPACE);
  }
}
