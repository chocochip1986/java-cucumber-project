package automation.data_helpers.datasource.batch_entities;

import automation.models.datasource.PersonId;
import automation.models.datasource.PersonName;
import automation.repositories.datasource.PersonNameRepo;
import automation.utilities.StringUtils;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Slf4j
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Configurable(autowire = Autowire.BY_NAME)
public class MhaChangePersonDetailsFileEntryCatName extends MhaChangePersonDetailsFileEntry {
    @Autowired
    private PersonNameRepo personNameRepo;

    private String dataItemChangeVal;

    public MhaChangePersonDetailsFileEntryCatName(String nric, String dataItemChangeVal, String dataItemChangeDate, char personDetailDataItemChanged, PersonId personId) {
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
                +StringUtils.leftPad(dataItemChangeVal == null? " " : dataItemChangeVal, 66)
                +StringUtils.leftPad(dataItemChangeDate == null? " " : dataItemChangeDate, 8);
    }

    public void checkValid() {
        if ( isItemChangeDateOlderThanCurrentPersonNameRecord() ) {
            errorMessages.add("Item Change Date is older than Person Details record for : "+this.nric);
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

    private boolean isItemChangeDateOlderThanCurrentPersonNameRecord() {
        PersonName personName = personNameRepo.findByPerson(personId.getPerson());
        if ( personName == null ) {
            return false;
        }
        else {
            Timestamp itemChangeDateTime = dateUtils.beginningOfDayToTimestamp(LocalDate.parse(this.dataItemChangeDate, dateUtils.DATETIME_FORMATTER_YYYYMMDD));
            return itemChangeDateTime.before(personName.getBiTemporalData().getBusinessTemporalData().getValidFrom());
        }
    }
}
