package automation.data_helpers.datasource.batch_entities;

import automation.data_setup.Phaker;
import automation.utilities.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MhaCeasedCitizenFileEntry {
  private String nric;
  private String name;
  private String nationality;
  private String citizenRenunciationDate;

  public MhaCeasedCitizenFileEntry(Map<String, String> row) {
    this.nric = row.get("NRIC");
    this.name = resolveName(row.get("NAME"));
    this.nationality = resolveNationality(row.get("NATIONALITY"));
    this.citizenRenunciationDate = row.get("CESSATION_DATE");
  }

  private String resolveName(String str) {
    if (str.equalsIgnoreCase("<AUTO>")) {
      return Phaker.validName();
    }
    return str.equalsIgnoreCase("-") ? "" : str;
  }

  private String resolveNationality(String str) {
    if (str.equalsIgnoreCase("<SPACE>")) {
      return "";
    }
    return str;
  }

  public String toString() {
    return StringUtils.rightPad(this.nric, 9)
        + StringUtils.rightPad(this.name, 66)
        + StringUtils.rightPad(this.nationality, 2)
        + StringUtils.rightPad(this.citizenRenunciationDate, 8);
  }
}
