package automation.constants;

import java.time.LocalDate;

public class TestConstants {
    public static final LocalDate DEFAULT_EXTRACTION_DATE = LocalDate.now().minusDays(5);
    public static final LocalDate DEFAULT_CUTOFF_DATE = LocalDate.now().minusDays(5);
    
    public static final String OPTION_VALID = "VALID";
    public static final String OPTION_INVALID = "INVALID";
    public static final String OPTION_BLANK = "BLANK";
    public static final String OPTION_SPACES = "SPACES";
    public static final String OPTION_FUTURE_DATE = "FUTUREDATE";
    
    // Ceased Citizen specific options
    public static final String OPTION_ON_DATE_OF_RUN = "ONDATEOFRUN";
    public static final String OPTION_EXISTING = "EXISTING";
}
