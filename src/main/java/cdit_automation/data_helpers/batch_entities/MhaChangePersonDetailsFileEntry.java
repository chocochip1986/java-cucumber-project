package cdit_automation.data_helpers.batch_entities;

import cdit_automation.enums.PersonDetailDataItemChangedEnum;
import cdit_automation.utilities.StringUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MhaChangePersonDetailsFileEntry {
    private String nric;
    private String dataItemOriginalValue;
    private String dataItemChangeVal;
    private String dataItemChangeDate;
    private PersonDetailDataItemChangedEnum dataItemCat;

    @Override
    public String toString() {
        return StringUtils.leftPad(nric, 9)
                +StringUtils.leftPad(dataItemCat.getValue(), 1)
                +StringUtils.leftPad(dataItemChangeVal, 66)
                +StringUtils.leftPad(dataItemChangeDate, 8);
    }
}
