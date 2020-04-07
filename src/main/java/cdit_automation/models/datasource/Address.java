package cdit_automation.models.datasource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(includeFieldNames = true)
public class Address {

  private String address;
  private String blockNo;
  private String buildingName;
  private String postal;
  private String roadName;

  @JsonCreator
  public Address(
      @JsonProperty("ADDRESS") String address,
      @JsonProperty("BLK_NO") String blockNo,
      @JsonProperty("BUILDING") String buildingName,
      @JsonProperty("POSTAL") String postal,
      @JsonProperty("ROAD_NAME") String roadName) {

    this.address = address;
    this.blockNo = blockNo;
    this.buildingName = buildingName;
    this.postal = postal;
    this.roadName = roadName;
  }

  @Override
  public Object clone() {
    try {
      return (Address) super.clone();
    } catch (CloneNotSupportedException e) {
      return new Address(this.address, this.blockNo, this.buildingName, this.postal, this.roadName);
    }
  }
}
