package cdit_automation.data_helpers.batch_entities;

import cdit_automation.enums.PersonDetailDataItemChangedEnum;
import cdit_automation.utilities.StringUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class MhaChangePersonDetailsFileEntry {
    private String nric;
    private String dataItemOriginalValue;
    private String dataItemChangeVal;
    private String dataItemChangeDate;
    private PersonDetailDataItemChangedEnum dataItemCat;

    public MhaChangePersonDetailsFileEntry(String nric, String dataItemOriginalValue, String dataItemChangeVal, String dataItemChangeDate, PersonDetailDataItemChangedEnum personDetailDataItemChangedEnum) {
        this.nric = nric;
        this.dataItemOriginalValue = dataItemOriginalValue;
        this.dataItemChangeVal = dataItemChangeVal;
        this.dataItemChangeDate = dataItemChangeDate;
        this.dataItemCat = personDetailDataItemChangedEnum;
    }

    public String toString() {
        return StringUtils.leftPad(nric, 9)
                +StringUtils.leftPad(dataItemCat.getValue(), 1)
                +StringUtils.leftPad(dataItemChangeVal, 66)
                +StringUtils.leftPad(dataItemChangeDate, 8);
    }
}
