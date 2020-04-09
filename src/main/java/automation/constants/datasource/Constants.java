package automation.constants.datasource;

import java.time.LocalDateTime;

public class Constants {

    private Constants() {
        // Not Needed
    }

    public static final String SLAVE_STEPS = "slaveSteps";
    public static final String CREATE_BATCH_STEP = "createBatch";
    public static final String EXTRACT_INCOMING_DATA_STEP = "extractIncomingDataStep";
    public static final String RP_VALIDATE_RAW_DATA_STEP = "rpValidateRawDataStep";
    public static final String VALIDATE_RAW_DATA_STEP = "validateRawDataStep";
    public static final String BULK_CHECK_VALIDATED_DATA_STEP = "bulkCheckValidatedDataStep";
    public static final String RP_CHECK_VALIDATED_DATA_STEP = "rpCheckValidatedDataStep";
    public static final String CHECK_VALIDATED_DATA_STEP = "checkValidatedDataStep";
    public static final String ERROR_RATE_STEP = "errorRateStep";
    public static final String MAP_TO_PREPARED_DATA_STEP = "mapToPreparedDataStep";
    public static final String SEND_DATA_STEP = "sendPreparedDataToMq";
    public static final String BULK_MAP_TO_PREPARED_STEP = "bulkMapToPreparedStep";
    public static final String CLEANUP_STEP = "cleanupStep";
    public static final int CHUNK_SIZE = 100;

    public static final String INFINITE_LOCAL_DATE_TIME_STRING = "9999-12-31 23:59:59";
    public static final LocalDateTime INFINITE_LOCAL_DATE_TIME =
            LocalDateTime.of(9999, 12, 31, 23, 59, 59);
    public static final LocalDateTime DEFAULT_START_LOCAL_DATE_TIME =
            LocalDateTime.of(1, 1, 1, 0, 0, 0);

    public static final String FAILED = "FAILED";
    public static final String SKIP_CHECK_VALIDATED_DATA_PROCESSOR =
            "SKIP_CHECK_VALIDATED_DATA_PROCESSOR";
    public static final String SKIP_MAP_TO_PREPARED_DATA_PROCESSOR =
            "SKIP_MAP_TO_PREPARED_DATA_PROCESSOR";
    public static final String ANY = "*";
    public static final String JOB_CONTEXT_BATCH = "batch";
    public static final String JOB_CONTEXT_IGNORE_ERROR_RATE = "isIgnoreErrorRate";

    public static final String REGEX_DIGITS = "^\\d+$";
    public static final String REGEX_POSITIVE_NEGATIVE_SIGN = "^[+-]$";

    public static final int MIN_AGE_AT_CUTOFF_YEAR = 12;
    public static final int MIN_YEAR_VALUE_FOR_DATE = 1800;

    public static final int RELEVANT_YEAR_YEAR_OF_ASSESSMENT = 1900;

    // For SimpleDateFormat usage.
    public static final String DATE_FORMAT_DDMMYYYY = "ddMMyyyy";
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";

    // For DateTimeFormatter usage.
    public static final String DATE_TIME_FORMATTER_UUUUMMDD = "uuuuMMdd";
}
