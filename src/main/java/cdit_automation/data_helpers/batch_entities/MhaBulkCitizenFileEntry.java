package cdit_automation.data_helpers.batch_entities;

import cdit_automation.data_setup.Phaker;
import cdit_automation.utilities.StringUtils;
import lombok.Getter;

import java.util.Map;

@Getter
public class MhaBulkCitizenFileEntry {
  private String nric;
  private String fin;
  private String name;
  private String dateOfBirth;
  private String dateOfDeath;
  private String gender;
  private String addressIndicator;
  private String addressType;
  private String address;
  private String invalidAddressTag;
  private String nationality;
  private String citizenshipAttainmentIssueDate;
  private String dualCitizen;

  public MhaBulkCitizenFileEntry(Map<String, String> row) {
    this.nric = resolveNric(row.get("NRIC"));
    this.fin = resolveFin(row.get("FIN"));
    this.name = resolveName(row.get("NAME"));
    this.dateOfBirth = resolveDateOFBirth(row.get("DOB"));
    this.dateOfDeath = resolveDateOfDeath(row.get("DOD"));
    this.gender = resolveGender(row.get("GENDER"));
    this.addressIndicator = resolveAddressIndicator(row.get("ADDR_IND"));
    this.addressType = resolveAddressType(row.get("ADDR_TYPE"));
    this.address = resolveAddress(row.get("ADDR"));
    this.invalidAddressTag = resolveInvalidAddressTag(row.get("INVALID_ADDR_TAG"));
    this.nationality = resolveNationality(row.get("NATIONALITY"));
    this.citizenshipAttainmentIssueDate =
        resolveCitizenAttainmentIssueDate(row.get("CTZ_ATT_DATE"));
    this.dualCitizen = resolveDualCitizen(row.get("DC"));
  }

  private String resolveNric(String str) {
    return str.equalsIgnoreCase("-") ? "" : str;
  }

  private String resolveFin(String str) {
    return str.equalsIgnoreCase("-") ? "" : str;
  }

  private String resolveName(String str) {
    if (str.equalsIgnoreCase("<AUTO>")) {
      return Phaker.validName();
    }
    return str.equalsIgnoreCase("-") ? "" : str;
  }

  private String resolveDateOFBirth(String str) {
    return str.equalsIgnoreCase("-") ? "" : str;
  }

  private String resolveDateOfDeath(String str) {
    return str.equalsIgnoreCase("-") ? "" : str;
  }

  private String resolveGender(String str) {
    return str.equalsIgnoreCase("-") ? "" : str;
  }

  private String resolveAddressIndicator(String str) {
    return str.equalsIgnoreCase("-") ? "" : str;
  }

  private String resolveAddressType(String str) {
    return str.equalsIgnoreCase("-") ? "" : str;
  }

  private String resolveAddress(String str) {
    if (str.equalsIgnoreCase("<AUTO>")) {
      return Phaker.validBlockNo()
          + Phaker.validStreetName()
          + Phaker.validFloorNo()
          + "-"
          + Phaker.validUnitNo();
    }
    return str.equalsIgnoreCase("-") ? "" : str;
  }

  private String resolveInvalidAddressTag(String str) {
    return str.equalsIgnoreCase("-") ? "" : str;
  }

  private String resolveNationality(String str) {
    return str.equalsIgnoreCase("-") ? "" : str;
  }

  private String resolveCitizenAttainmentIssueDate(String str) {
    return str.equalsIgnoreCase("-") ? "" : str;
  }

  private String resolveDualCitizen(String str) {
    return str.equalsIgnoreCase("-") ? "" : str;
  }

  public String toRawString() {
    return StringUtils.rightPad(this.nric, 9, StringUtils.SPACE)
        + StringUtils.rightPad(this.fin, 9, StringUtils.SPACE)
        + StringUtils.rightPad(this.name, 66, StringUtils.SPACE)
        + StringUtils.rightPad(this.dateOfBirth, 8)
        + StringUtils.rightPad(this.dateOfDeath, 8)
        + StringUtils.rightPad(this.gender, 1)
        + StringUtils.rightPad(this.addressIndicator, 1)
        + StringUtils.rightPad(this.addressType, 1)
        + StringUtils.rightPad(this.address, 89, StringUtils.SPACE)
        + StringUtils.rightPad(this.invalidAddressTag, 1)
        + StringUtils.rightPad(this.nationality, 2, StringUtils.SPACE)
        + StringUtils.rightPad(this.citizenshipAttainmentIssueDate, 8)
        + StringUtils.rightPad(this.dualCitizen, 1);
  }
}
