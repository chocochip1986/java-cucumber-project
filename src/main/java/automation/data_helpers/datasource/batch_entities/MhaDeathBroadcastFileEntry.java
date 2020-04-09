package automation.data_helpers.datasource.batch_entities;

import automation.utilities.StringUtils;
import lombok.Getter;

import java.util.Map;

@Getter
public class MhaDeathBroadcastFileEntry {
  private String uid;
  private String deathDate;

  public MhaDeathBroadcastFileEntry(Map<String, String> row) {
    this.uid = row.get("NATURALID");
    this.deathDate = row.get("DOD");
  }

  public String toRawString() {
    return StringUtils.rightPad(this.uid, 9) + StringUtils.rightPad(this.deathDate, 8);
  }
}
