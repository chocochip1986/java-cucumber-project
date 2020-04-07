package cds_automation.data_helpers.datasource.batch_entities;

import cds_automation.data_setup.Phaker;
import cds_automation.utilities.StringUtils;
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
  private String citizenshipAttainmentIssueDate;

  public MhaBulkCitizenFileEntry(String nric, String fin, String name, String dateOfBirth, String dateOfDeath,
                                 String gender, String addressIndicator, String addressType, String address,
                                 String invalidAddressTag, String citizenshipAttainmentIssueDate) {
    this.nric = resolveNric(nric);
    this.fin = resolveFin(fin);
    this.name = resolveName(name);
    this.dateOfBirth = resolveDateOfBirth(dateOfBirth);
    this.dateOfDeath = dateOfDeath;
    this.gender = resolveGender(gender);
    this.addressIndicator = resolveAddressIndicator(addressIndicator);
    this.addressType = resolveAddressType(addressType);
    this.address = resolveAddress(address);
    this.invalidAddressTag = resolveInvalidAddressTag(invalidAddressTag);
    this.citizenshipAttainmentIssueDate = resolveCitizenAttainmentIssueDate(citizenshipAttainmentIssueDate);
  }

  public MhaBulkCitizenFileEntry(Map<String, String> row) {
    this.nric = resolveNric(row.get("NRIC"));
    this.fin = resolveFin(row.get("FIN"));
    this.name = resolveName(row.get("NAME"));
    this.dateOfBirth = resolveDateOfBirth(row.get("DOB"));
    this.dateOfDeath = resolveDateOfDeath(row.get("DOD"));
    this.gender = resolveGender(row.get("GENDER"));
    this.addressIndicator = resolveAddressIndicator(row.get("ADDR_IND"));
    this.addressType = resolveAddressType(row.get("ADDR_TYPE"));
    this.address = resolveAddress(row.get("ADDR"));
    this.invalidAddressTag = resolveInvalidAddressTag(row.get("INVALID_ADDR_TAG"));
    this.citizenshipAttainmentIssueDate =
        resolveCitizenAttainmentIssueDate(row.get("CTZ_ATT_DATE"));
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

  private String resolveDateOfBirth(String str) {
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

  private String resolveCitizenAttainmentIssueDate(String str) {
    return str.equalsIgnoreCase("-") ? "" : str;
  }

  public String toRawString() {
    return StringUtils.rightPad(this.nric, 9, StringUtils.SPACE)
        + StringUtils.rightPad(this.fin, 9, StringUtils.SPACE)
        + StringUtils.rightPad(this.name, 66, StringUtils.SPACE)
        + StringUtils.rightPad(this.dateOfBirth, 8)
        + StringUtils.rightPad(this.dateOfDeath, 8, StringUtils.SPACE)
        + StringUtils.rightPad(this.gender, 1)
        + StringUtils.rightPad(this.addressIndicator, 1)
        + StringUtils.rightPad(this.addressType, 1)
        + StringUtils.rightPad(this.address, 89, StringUtils.SPACE)
        + StringUtils.rightPad(this.invalidAddressTag, 1, StringUtils.SPACE)
        + StringUtils.rightPad(this.citizenshipAttainmentIssueDate, 8);
  }
}
