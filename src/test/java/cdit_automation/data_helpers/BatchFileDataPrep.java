package cdit_automation.data_helpers;

import cdit_automation.data_setup.Phaker;
import cdit_automation.repositories.PersonDetailRepo;
import cdit_automation.utilities.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BatchFileDataPrep {

    @Autowired protected DateUtils dateUtils;
    @Autowired protected PersonIdService personIdService;

    @Autowired protected PersonDetailRepo personDetailRepo;

    protected int parseStringSize(String size) {
        try {
            return Integer.valueOf(size);
        } catch ( NumberFormatException e ) {
            //Fail silently
        }
        return 0;
    }

    public List<String> createListOfInvalidNrics(int size) {
        List<String> listOfInvalidNrics = new ArrayList<>();
        for ( int i = 0 ; i < size ; i++ ) {
            listOfInvalidNrics.add(Phaker.invalidNric());
        }

        return listOfInvalidNrics;
    }

    public String createInvalidNric() {
        return Phaker.invalidNric();
    }

    public String generateDoubleHeader(@Nullable LocalDate extractionDate, @Nullable LocalDate cutOffDate) {
        if ( extractionDate == null ) {
            extractionDate = dateUtils.daysBeforeToday(5);
        }
        if ( cutOffDate == null ) {
            cutOffDate = dateUtils.daysBeforeToday(5);
        }

        return extractionDate.format(dateUtils.DATETIME_FORMATTER_YYYYMMDD)+cutOffDate.format(dateUtils.DATETIME_FORMATTER_YYYYMMDD);
    }

    public String generateDoubleHeader() {
        return generateDoubleHeader(null, null);
    }
}
