package cdit_automation.data_helpers;

import cdit_automation.utilities.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.time.LocalDate;

public class BatchFileDataPrep {

    @Autowired protected DateUtils dateUtils;

    protected int parseStringSize(String size) {
        try {
            return Integer.valueOf(size);
        } catch ( NumberFormatException e ) {
            //Fail silently
        }
        return 0;
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
