package cdit_automation.constants;

import java.time.LocalDate;

public class TestConstants {
    public static final LocalDate DEFAULT_EXTRACTION_DATE = LocalDate.now().minusDays(5);
    public static final LocalDate DEFAULT_CUTOFF_DATE = LocalDate.now().minusDays(5);
}
