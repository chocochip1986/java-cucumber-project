package cdit_automation.constants;

public class ErrorMessageConstants {

    private ErrorMessageConstants() {
        // Not Needed
    }

    public static final String ERROR_TYPE_ERROR = "ERROR";
    public static final String ERROR_TYPE_WARNING = "WARNING";

    // General spring batch step errors
    public static final String HEADER_LENGTH_ERROR = "Wrong header length.";
    public static final String BODY_LENGTH_ERROR = "Wrong body length.";
    public static final String FOOTER_FORMAT_ERROR = "Wrong footer format.";
    public static final String FOOTER_SIZE_ERROR = "Footer size does not match body size.";
    public static final String FOOTER_NOT_NUMERIC = "Footer record count must be numeric.";
    public static final String HEADER_FORMAT_ERROR = "Wrong header format.";
    public static final String MAP_TO_PREPARED_DATA_ERROR = "Unable to map to prepared data.";

    // Annotation messages
    public static final String INVALID_MHA_ADDRESS_TYPE = "Invalid mha address type.";
    public static final String INVALID_NCA_ADDRESS_TYPE = "Invalid nca address type.";
    public static final String INVALID_ADDRESS_INDICATOR = "Invalid address indicator.";
    public static final String INVALID_INVALID_ADDRESS_TAG = "Invalid invalid-address tag.";
    public static final String EXTRACTION_DATE_CUTOFF_DATE_ERROR =
            "Extraction Date must be equal/after Cut-off Date.";
    public static final String MHA_OVERSEAS_INDICATOR_AND_TYPE_ERROR =
            "If MHA address indicator is 'C', the MHA address type must also be 'C'.";
    public static final String INVALID_HOME_TYPE = "Invalid home type.";

    // Common error messages
    public static final String YEAR_CANNOT_BE_LESS_THAN_1800 = "Year value cannot be less than 1800";
    public static final String NRIC_IS_BLANK = "NRIC cannot be null/blank.";
    public static final String INVALID_NRIC_FORMAT = "Must be valid NRIC in format [S or T]1234567[A-Z]";
    public static final String DUPLICATE_NRIC_FOUND_IN_FILE = "Duplicate NRIC found in file.";
    public static final String DUPLICATE_FIN_FOUND_IN_FILE = "Duplicate FIN found in file.";
    public static final String MINIMUM_AGE_NOT_MET =
            "Age as at 31 Dec <File Cut-off Year> must be at least "
                    + Constants.MIN_AGE_AT_CUTOFF_YEAR
                    + ".";

    // Common Header Dates error messages
    public static final String EXTRACTION_DATE_AFTER_FILE_RECEIVED_DATE =
            "Extraction date cannot be after File Received date.";
    public static final String CUTOFF_DATE_AFTER_FILE_RECEIVED_DATE =
            "Cut-off date cannot be after File Received date.";
    public static final String NO_INTERACTION_HEADER_DATE_AFTER_FILE_RECEIVED_DATE =
            "Header date cannot be after File Received date.";
    public static final String SELF_EMPLOYED_TRANS_DATE_AFTER_FILE_RECEIVED_DATE =
            "Trans date cannot be after File Received date.";
    public static final String INVALID_DATE_FORMAT_DDMMYYYY =
            "Must be in " + Constants.DATE_FORMAT_DDMMYYYY + " date format.";

    // HDB Property
    public static final String INVALID_PROPERTY_TYPE = "Invalid property type.";
    public static final String INVALID_ACTION_CODE = "Invalid action code.";
    public static final String INVALID_FLAT_TYPE = "Invalid flat type.";
    public static final String INVALID_SHOP_HOUSE_TAG = "Invalid shop house tag.";
    public static final String INVALID_SHOP_HOUSE_LIVING_QUARTER =
            "Invalid shop house living quarter.";
    public static final String INVALID_RENTAL_FLAT_VALUE = "Invalid rental flat value.";

    // MHA new citizen
    public static final String ACTIVE_PERSON = "NRIC still valid";
    public static final String LATE_CITIZENSHIP_ATTAINMENT =
            "Citizen Attainment more than one month ago";
    public static final String WRONG_DATE_OF_INDIVIDUAL =
            "DOB OR DOD cannot be after the cutoff date";
    public static final String ATTAINMENT_AFTER_CUTOFF =
            "Citizenship attainment date must be equal/before Cut-off date.";
    public static final String NEW_ADDRESS_DATE_AFTER_CUTOFF =
            "New Address Valid From date must be equal/before Cut-off date.";
    public static final String OLD_ADDRESS_DATE_NOT_BEFORE_NEW_ADDRESS_DATE =
            "Previous Address Valid Till date must be before New Address Valid From date.";
    public static final String FOUND_WITH_ACTIVE_NATIONALITY =
            "Found to have active (no renunciation date) Nationality record.";

    // MHA bulk citizen
    public static final String PERSON_EXISTS = "Duplicate NRIC exists in the database";
    public static final String FIN_NOT_FOUND = "FIN not found in prepared database";
    public static final String WRONG_CITIZENSHIP_ATTAINMENT_DATE =
            "Citizenship attainment date should be later than previous citizenship ceased date";
    public static final String ATTAINMENT_AFTER_EXTRACTION =
            "Citizenship attainment date cannot be after extraction date";
    public static final String WRONG_GENDER = "Gender can only be M, F or U";
    public static final String WRONG_ADDRESSINDICATOR =
            "Invalid address indicator. Only space, C or Z";
    public static final String WRONG_DUALNATIONALITY =
            "Invalid dual nationality indicator. Only Y or N";
    public static final String WRONG_INVALIDADDRESSINDICATOR =
            "Wrong Invalid Address Indicator given";
    public static final String PERSON_ISNOTALIVE = "Person is not alive, invalid entry";
    public static final String WRONG_DUAL_NATIONALITY =
            "Person cannot be a foreigner and a SG dual citizen at the same time";

    // MHA dual citizen
    public static final String PERSON_NOT_FOUND = "No such person found in prepared database";
    public static final String PERSON_WITHOUT_NATIONALITY =
            "Person does not have a valid nationality record";
    public static final String PERSON_WITH_MULTIPLE_NATIONALITIES =
            "Person has more than one valid nationalities";

    // MHA address
    public static final String LATE_ADDRESS_CHANGE = "Date of address change more than one month ago";
    public static final String LATE_ADDRESS_CHANGE_DATE =
            "Address change date cannot be more recent than File Cut-off Date";

    // MHA death date
    public static final String DEATH_BEFORE_BIRTH = "Date of death is earlier than Date of birth";
    public static final String LATE_DEATH_DATE = "Date of death change more than one month ago";

    // MHA ceased citizen
    public static final String RENUNCIATION_AFTER_CUTOFF =
            "Renunciation Date is after File Cut-off Date.";
    public static final String NRIC_NOT_FOUND_IN_SYSTEM = "NRIC not found in System.";
    public static final String NO_ACTIVE_NATIONALITY = "No active Nationality record.";
    public static final String NOT_SC_DUALCITIZEN = "Not SC or Dual Citizen currently.";
    public static final String RENUNCIATION_DATE_FOUND =
            "Renunciation Date found in current Nationality record.";
    public static final String RENUNCIATION_NOT_AFTER_ATTAINMENT =
            "Renunciation Date is not after Citizenship Attainment Date.";
    public static final String INVALID_NAME = "Invalid Name.";
    public static final String INVALID_NATIONALITY = "Invalid Ceased Citizen Nationality.";
    public static final String INVALID_RENUNCIATION_DATE = "Invalid Citizen Renunciation Date.";
    public static final String INVALID_NRIC_CANCELLED_STATUS = "Invalid NRIC Cancelled Status.";

    // MHA Person Detail Change (ValidPersonDetailChangeValidator)
    public static final String INVALID_GENDER_MESSAGE =
            "Invalid DataItemNewValue Format - Invalid Gender";
    public static final String INVALID_NAME_MESSAGE =
            "Invalid DataItemNewValue Format - Name should not be empty";
    public static final String INVALID_DOB_MESSAGE =
            "Invalid DataItemNewValue Format - Incorrect Date of Birth format";
    public static final String INVALID_DOD_MESSAGE =
            "Invalid DataItemNewValue Format - Incorrect Date of Death format";
    public static final String NULL_FIELDS =
            "Data Item Changed and Data Item New Value should not be null";

    // MHA Person Detail Change (PersonDetailChangeCheckService)
    public static final String NRIC_NOT_FOUND = "NRIC not found in prepared database";
    public static final String NO_EXISTING_PERSON_DETAIL = "Person Detail not found for this NRIC";
    public static final String NO_EXISTING_PERSON_NAME = "Person Name not found for this NRIC";
    public static final String DATA_ITEM_CHANGED_DATE_AFTER_CUT_OFF =
            "Data item changed date cannot be after cut off date";
    public static final String DUPLICATED_NRIC_AND_DATA_ITEM_CHANGED =
            "Duplicated occurrence of NRIC and Data Item Changed";
    public static final String DOB_NOT_BEFORE_DOD =
            "Provided date of birth should be before provided date of death";
    public static final String DOB_NOT_BEFORE_EXISTING_DOD =
            "Provided date of birth should be before existing date of death";
    public static final String EXISTING_DOB_NOT_BEFORE_DOD =
            "Provided date of death should be after existing date of birth";
    public static final String DOB_BELOW_12 =
            "Date of birth should be at least 12 years old from cut-off date";
    public static final String DATA_ITEM_NEW_VALUE_ALREADY_IN_DB =
            "Data Item New Value should be a value not already in CDIT database";
    public static final String DOB_YEAR_IS_EARLIER_THAN_1800 =
            "Year for date of birth cannot be earlier than 1800";
    public static final String DOD_YEAR_IS_EARLIER_THAN_1800 =
            "Year for date of death cannot be earlier than 1800";

    // IRAS Assessable Income
    public static final String INVALID_HEADER_RECORD_TYPE =
            "Wrong RecordType for header. Should be 0";
    public static final String INVALID_BODY_RECORD_TYPE = "Wrong RecordType for body. Should be 1";
    public static final String INVALID_FOOTER_RECORD_TYPE =
            "Wrong RecordType for footer. Should be 2";
    public static final String INVALID_RESULT_INDICATOR = "Result indicator provided is invalid.";
    public static final String INVALID_ASSESSABLE_INCOME =
            "Result indicator provided is not 01, AI should be 0 but is not.";
    public static final String INVALID_ASSESSMENT_YEAR =
            "Result indicator provided is 06, but Assessment Year is less than current +2 years back.";
    public static final String ID_NOT_EXISTS = "ID does not exist in IRAS database";
    public static final String NO_ASSESSMENT_RECORD_FOUND_RETURN_ISSUED =
            "No assessment record found and Return was issued";
    public static final String NO_ASSESSMENT_RECORD_FOUND_NO_RETURN_ISSUED =
            "No assessment record found and NO Return was issued";
    public static final String INVALID_ID = "INVALID NRIC or FIN provided";
    public static final String YEAR_OF_ASSESSMENT_BEFORE_EXTRACTION_FOR_EXTRACTION_BEFORE_JUN =
            "Year of assessment out of range for Extraction before June; YA >3 BEFORE Extraction Year";
    public static final String YEAR_OF_ASSESSMENT_AFTER_EXTRACTION_FOR_EXTRACTION_BEFORE_JUN =
            "Year of assessment out of range for Extraction before June; YA AFTER Extraction Year";
    public static final String YEAR_OF_ASSESSMENT_EQUAL_EXTRACTION_FOR_EXTRACTION_BEFORE_JUN =
            "Year of assessment out of range for Extraction before June; YA EQUAL Extraction Year";
    public static final String YEAR_OF_ASSESSMENT_AFTER_EXTRACTION_FOR_EXTRACTION_AFTER_JUN =
            "Year of assessment out of range for Extraction on/after June; YA AFTER Extraction Year";
    public static final String YEAR_OF_ASSESSMENT_BEFORE_EXTRACTION_FOR_EXTRACTION_AFTER_JUN =
            "Year of assessment out of range for Extraction on/after June; YA >2 BEFORE Extraction Year";
    public static final String ASSESSABLE_INCOME_OUT_OF_RANGE =
            "Amount is greater than 120k but not mapped properly";
    public static final String COMPLETELY_DUPLICATE_RECORD_FOUND_ERROR_MESSAGE =
            "Completely Duplicate Record found.";
    public static final String PARTIALLY_DUPLICATE_RECORD_FOUND_ERROR_MESSAGE =
            "Partially Duplicate Record found.";
    public static final String RECORDS_WITH_RESULT_INDICATOR_03_OR_06 =
            "Record found with resultIndicator 03 or 06 found in return file: ";

    // Lorong Buangkok
    public static final String POSTAL_CODE_CANNOT_BE_EMPTY = "Postal code cannot be empty";

    // Singpost Self-Employed
    public static final String INVALID_FOOTER_HIGH_VALUE = "Wrong High Value for footer. Should be 2";
    public static final String INVALID_YES_NO_VALUE = "Invalid Yes/No value.";
    public static final String INVALID_SIGN_VALUE = "Invalid (+/-) sign.";
    public static final String INVALID_BODY_RECORD_TYPE_02 =
            "Wrong Value for body 'record type 2'. Should be 02.";
    public static final String RELEVANT_YEAR_AFTER_ERROR =
            "Relevant Year must be after " + Constants.RELEVANT_YEAR_YEAR_OF_ASSESSMENT + ".";
    public static final String RELEVANT_YEAR_EQUAL_BEFORE_CURRENT_YEAR =
            "Relevant Year must be equal/before Current Year.";

    // Declared NTI
    public static final String YEAR_OF_ASSESSMENT_AFTER_ERROR =
            "Year Of Assessment must be after " + Constants.RELEVANT_YEAR_YEAR_OF_ASSESSMENT + ".";

    // CPFB Nursing Home
    public static final String NURSING_HOME_COMPLETE_DUPLICATED_RECORD =
            "Duplicated records found with same Postal, Block, Floor, Unit and Home type.";

    public static final String NURSING_HOME_DUPLICATED_RECORD_DIFFERENT_HOMETYPE =
            "Duplicated records found with same Postal, Block, Floor and Unit but different Home type.";

    public static final String NURSING_HOME_POSTAL_MATCH_BUT_BLOCK_NOT_MATCH_WITH_PROPERTYDETAIL =
            "Block number does not match prepared records given valid Postal Code";
}
