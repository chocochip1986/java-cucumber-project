package cds_automation.data_helpers.datasource.batch_entities;

import cds_automation.models.datasource.PersonId;
import cds_automation.utilities.StringUtils;
import java.util.ArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Configurable(autowire = Autowire.BY_NAME)
public class MhaChangePersonDetailsFileEntryCatUnsupported extends MhaChangePersonDetailsFileEntry {
    private String dataItemChangeVal;

    public MhaChangePersonDetailsFileEntryCatUnsupported(String nric, String dataItemChangeVal, String dataItemChangeDate, char personDetailDataItemChanged, PersonId personId) {
        this.nric = nric;
        this.dataItemChangeVal = dataItemChangeVal;
        this.dataItemChangeDate = dataItemChangeDate;
        this.dataItemCat = personDetailDataItemChanged;
        this.personId = personId;

        errorMessages = new ArrayList<>();
    }

    @Override
    public String toString() {
        return StringUtils.leftPad(nric, 9)
                +StringUtils.leftPad(String.valueOf(dataItemCat), 1)
                +StringUtils.leftPad(dataItemChangeVal, 66)
                +StringUtils.leftPad(dataItemChangeDate, 8);
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
