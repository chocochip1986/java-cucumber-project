package cdit_automation.data_helpers.batch_entities;

import cdit_automation.data_helpers.factories.PersonFactory;
import cdit_automation.data_setup.Phaker;
import cdit_automation.models.datasource.PersonDetail;
import cdit_automation.models.datasource.PersonId;
import cdit_automation.repositories.datasource.PersonDetailRepo;
import cdit_automation.utilities.DateUtils;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Configurable(autowire = Autowire.BY_NAME)
public class MhaChangePersonDetailsFileEntry {
    @Autowired protected PersonDetailRepo personDetailRepo;
    @Autowired protected PersonFactory personFactory;
    @Autowired protected DateUtils dateUtils;

    protected String nric;
    protected String dataItemChangeDate;
    protected char dataItemCat;
    protected PersonId personId;

    protected List<String> errorMessages;

    public MhaChangePersonDetailsFileEntry(String nric, String dataItemChangeDate, char personDetailDataItemChanged) {
        this.nric = nric;
        this.dataItemChangeDate = dataItemChangeDate;
        this.dataItemCat = personDetailDataItemChanged;
        errorMessages = new ArrayList<>();
    }

    public void checkValid() {
        if ( isPersonIdNull() ) {
            errorMessages.add("No person_id record created!");
        }
        if ( isDataItemChangeDateParsable() ) {
            errorMessages.add("Unable to parse item change date: "+this.dataItemChangeDate);
        }
        if ( isItemChangeDateOlderThanBirthDate() ) {
            errorMessages.add("Item Change Date is older than Person, "+this.nric+", Birth Date ");
        }
    }

    public boolean isValid() {
        checkValid();
        if ( errorMessages.isEmpty() ) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean isPersonIdNull() {
        if ( this.personId == null ) {
            return true;
        } else {
            return false;
        }
    }

    protected  boolean isDataItemChangeDateParsable() {
        return isDateParsable(this.dataItemChangeDate);
    }

    protected boolean isItemChangeDateOlderThanBirthDate() {
        PersonDetail personDetail = personDetailRepo.findByPerson(this.personId.getPerson());
        if ( personDetail == null ) {
            return false;
        } else {
            return LocalDate.parse(this.dataItemChangeDate, dateUtils.DATETIME_FORMATTER_YYYYMMDD).isBefore(personDetail.getDateOfBirth());
        }
    }

    protected boolean isDateParsable(String date) {
        try {
            LocalDate.parse(date, Phaker.DATETIME_FORMATTER_YYYYMMDD);
            return true;
        } catch ( DateTimeParseException e ) {
            return false;
        }
    }

    public String toString() {
        return "Do not use this.";
    }
}
