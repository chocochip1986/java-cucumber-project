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
  private String oldAddressType;
  private String oldAddressRaw;
  private String newAddressIndicator;
  private String newAddressType;
  private String newAddressRaw;
  private String newInvalidAddressTag;
  private String dateOfAddressChange;
  private String citizenshipAttainmentIssueDate;

  public MhaNewCitizenFileEntry(Map<String, String> row) {
    this.nric = StringUtils.returnEmptyStringIfNull(row.get("NRIC"));
    this.fin = StringUtils.returnEmptyStringIfNull(row.get("FIN"));
    this.name = StringUtils.returnEmptyStringIfNull(row.get("NAME"));
    this.dateOfBirth = StringUtils.returnEmptyStringIfNull(row.get("DOB"));
    this.gender = StringUtils.returnEmptyStringIfNull(row.get("GENDER"));
    this.oldAddressIndicator = StringUtils.returnEmptyStringIfNull(row.get("OLD_ADDR_IND"));
    this.oldAddressType = StringUtils.returnEmptyStringIfNull(row.get("OLD_ADDR_TYPE"));
    this.oldAddressRaw = StringUtils.returnEmptyStringIfNull(row.get("OLD_ADDR"));
    this.newAddressIndicator = StringUtils.returnEmptyStringIfNull(row.get("NEW_ADDR_IND"));
    this.newAddressType = StringUtils.returnEmptyStringIfNull(row.get("NEW_ADDR_TYPE"));
    this.newAddressRaw = StringUtils.returnEmptyStringIfNull(row.get("NEW_ADDR"));
    this.newInvalidAddressTag = StringUtils.returnEmptyStringIfNull(row.get("NEW_INVALID_ADDR_TAG"));
    this.dateOfAddressChange = StringUtils.returnEmptyStringIfNull(row.get("DATE_OF_ADDR_CHANGE"));
    this.citizenshipAttainmentIssueDate = StringUtils.returnEmptyStringIfNull(row.get("CTZ_ATT_DATE"));
  }

  public String toRawString() {
    return StringUtils.rightPad(this.nric, 9, StringUtils.SPACE)
        + StringUtils.rightPad(this.fin, 9, StringUtils.SPACE)
        + StringUtils.rightPad(this.name, 66, StringUtils.SPACE)
        + StringUtils.rightPad(this.dateOfBirth, 8, StringUtils.SPACE)
        + StringUtils.rightPad(this.gender, 1, StringUtils.SPACE)
        + StringUtils.rightPad(this.oldAddressIndicator, 1, StringUtils.SPACE)
        + StringUtils.rightPad(this.oldAddressType, 1, StringUtils.SPACE)
        + StringUtils.rightPad(this.oldAddressRaw, 89, StringUtils.SPACE)
        + StringUtils.rightPad(this.newAddressIndicator, 1, StringUtils.SPACE)
        + StringUtils.rightPad(this.newAddressType, 1, StringUtils.SPACE)
        + StringUtils.rightPad(this.newAddressRaw, 89, StringUtils.SPACE)
        + StringUtils.rightPad(this.newInvalidAddressTag, 1, StringUtils.SPACE)
        + StringUtils.rightPad(this.dateOfAddressChange, 8, StringUtils.SPACE)
        + StringUtils.rightPad(this.citizenshipAttainmentIssueDate, 8, StringUtils.SPACE);
  }
}
