package cdit_automation.data_helpers.batch_entities;

import cdit_automation.models.PersonDetail;
import cdit_automation.models.PersonId;
import cdit_automation.utilities.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Configurable(autowire = Autowire.BY_NAME)
public class MhaChangePersonDetailsFileEntryCatBirthDate extends MhaChangePersonDetailsFileEntry {
    private String dataItemChangeVal;

    public MhaChangePersonDetailsFileEntryCatBirthDate(String nric, String dataItemChangeVal, String dataItemChangeDate, char personDetailDataItemChanged, PersonId personId) {
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

    public void checkValid() {
        if ( isItemChangeDateOlderThanCurrentPersonDetailRecord() ) {
            errorMessages.add("Item Change Date is older than Person Details record for : "+this.nric);
        }
        if ( isNewBirthDateParsable() ) {
            errorMessages.add("New Birth Date is not parsable for nric: "+this.nric);
        }
        if ( isItemChangeDateOlderThanNewBirthDate() ) {
            errorMessages.add("New Birth Date is older than the Item Change Date for nric "+this.nric);
        }
        super.checkValid();
    }

    @Override
    public boolean isValid() {
        checkValid();
        if ( errorMessages.isEmpty() ) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isNewBirthDateParsable() {
        return isDateParsable(this.dataItemChangeVal);
    }

    private boolean isItemChangeDateOlderThanNewBirthDate() {
        LocalDate birthDate = LocalDate.parse(this.dataItemChangeDate, dateUtils.DATETIME_FORMATTER_YYYYMMDD);
        Timestamp itemChangeDateTime = dateUtils.beginningOfDayToTimestamp(LocalDate.parse(this.dataItemChangeDate, dateUtils.DATETIME_FORMATTER_YYYYMMDD));
        return itemChangeDateTime.before(dateUtils.beginningOfDayToTimestamp(birthDate));
    }

    private boolean isItemChangeDateOlderThanCurrentPersonDetailRecord() {
        PersonDetail personDetail = personDetailRepo.findByPerson(this.personId.getPerson());
        if ( personDetail == null ) {
            return false;
        }
        else {
            Timestamp itemChangeDateTime = dateUtils.beginningOfDayToTimestamp(LocalDate.parse(this.dataItemChangeDate, dateUtils.DATETIME_FORMATTER_YYYYMMDD));
            return itemChangeDateTime.before(personDetail.getBiTemporalData().getBusinessTemporalData().getValidFrom());
        }
    }
}
