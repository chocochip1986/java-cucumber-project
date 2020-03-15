package cdit_automation.data_helpers;

import cdit_automation.constants.TestConstants;
import cdit_automation.utilities.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.time.LocalDate;

@Slf4j
@Component
public class MhaChangeAddressDataPrep extends BatchFileDataPrep {
    public String generateSingleDateNoOfRecordsHeader(@Nullable LocalDate dateOfRun, int recordCount) {
        if ( dateOfRun == null ) {
            dateOfRun = TestConstants.DEFAULT_EXTRACTION_DATE;
        }
        return StringUtils.rightPad(dateOfRun.format(dateUtils.DATETIME_FORMATTER_YYYYMMDD)+StringUtils.leftPad(String.valueOf(recordCount), 4, "0"), 12);
    }

    public String generateSingleDateNoOfRecordsHeader(int recordCount) {
        return generateSingleDateNoOfRecordsHeader(null, recordCount);
    }
}
