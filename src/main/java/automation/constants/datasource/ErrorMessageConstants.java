package automation.constants.datasource;

public class ErrorMessageConstants {

    private ErrorMessageConstants() {
        // Not Needed
    }

    public static final String ERROR_TYPE_ERROR = "ERROR";
    public static final String ERROR_TYPE_WARNING = "WARNING";

    // Custom validator default error message
    public static final String SECOND_DATE_IS_NOT_BEFORE_THE_FIRST_DATE =
            "Second date is not before the first date.";
    public static final String SECOND_DATE_IS_NOT_AFTER_OR_EQUAL_TO_FIRST_DATE =
            "Second date is not after or equal to first date.";
    public static final String VALUE_MUST_BE_IN_NUMERIC_FORMAT = "Value must be in numeric format.";
    public static final String MUST_NOT_CONTAIN_ANY_VALUES = "Must not contain any values.";
    public static final String TEXT_MUST_BE_UPPERCASE = "Text must be uppercase.";
    public static final String VALUE_IS_NOT_EQUAL_OR_BEFORE_CURRENT_YEAR =
            "Value is not equal or before Current Year.";
    public static final String VALUE_IS_NOT_AFTER_VALUE_TO_COMPARE =
            "Value is not after 'value to compare'.";
    public static final String INVALID_RECORD_TYPE = "Invalid record type.";
    public static final String
            EITHER_INCOMING_RECORD_OR_VALIDATED_DATA_MUST_EXIST_IS_FILE_ERROR_MUST_BE_TRUE =
            "Either incoming record or validated data must exist / is file error must be true.";
    public static final String NATIONALITY_MUST_NOT_BE_SG_COUNTRY_CODE =
            "Nationality must not be SG country code";
    public static final String WRONG_INVALID_ADDRESS_INDICATOR = "Wrong Invalid Address Indicator.";
    public static final String MUST_BE_INSERT_UPDATE_DELETE =
            "Must be \"I\" - Insert,\"U\" - Update, or \"D\" - Delete";
    public static final String MUST_BE_DBS_POSB_OCB_OR_UOB =
            "Must be \"DBS\",\"POSB\",\"OCB\", or \"UOB\"";
    public static final String MUST_BE_IN_VALID_DATE_FORMAT = "Must be in valid date format.";
    public static final String DATE_OF_BIRTH_MUST_BE_IN_VALID_FORMAT =
            "Date of Birth must be in valid format.";
    public static final String ATTAINMENT_DATE_CANNOT_BE_NULL_WHILE_A_VALID_FIN_IS_PROVIDED =
            "Citizenship Attainment Date cannot be null while a valid FIN is provided";
    public static final String MUST_BE_VALID_FIN_IN_VALID_FORMAT =
            "Must be valid FIN in format [F/G]1234567[A-Z]";
    public static final String MUST_BE_VALID_NRIC_IN_VALID_FORMAT =
            "Must be valid NRIC in format [S/T]1234567[A-Z]";
    public static final String MUST_BE_VALID_ID_IN_VALID_FORMAT =
            "Must be valid ID in format [S/T/F/G]1234567[A-Z]";

    // General spring batch step errors
    public static final String HEADER_LENGTH_ERROR = "Wrong header length.";
    public static final String BODY_LENGTH_ERROR = "Wrong body length.";
    public static final String BODY_MUST_HAVE_ONE_VALID = "Must have at least 1 valid body record.";
    public static final String FOOTER_SIZE_ERROR = "Footer size does not match body size.";
    public static final String FOOTER_NOT_NUMERIC = "Footer record count must be numeric.";
    public static final String FOOTER_BLANK = "Footer record count must not be blank.";
    public static final String FOOTER_LENGTH_EXCEED = "Footer length exceeded expected length.";
    public static final String FOOTER_MUST_HAVE_ONE = "Must have 1 Footer record.";
    public static final String MAP_TO_PREPARED_DATA_ERROR = "Unable to map to prepared data.";

    // SingleDateNoOfRecordHeader errors
    public static final String HEADER_DATE_CONTAINS_WHITESPACE =
            "Header date should only contain 8 numeric characters.";
    public static final String HEADER_DATE_INCORRECT_LENGTH = "Header date length is incorrect.";
    public static final String HEADER_DATE_IS_BLANK = "Header date cannot be blank.";
    public static final String HEADER_NO_OF_RECORD_NOT_NUMERIC =
            "Header record count must be numeric.";
    public static final String HEADER_NO_OF_RECORD_CONTAINS_WHITESPACE =
            "Header record count should only contain 4 numeric characters.";
    public static final String HEADER_NO_OF_RECORD_INVALID = "Header record count is invalid.";
    public static final String HEADER_NO_OF_RECORD_INCORRECT_COUNT =
            "Header record count does not match number of records in body.";
    public static final String HEADER_NO_OF_RECORD_INCORRECT_LENGTH =
            "Header record count length is incorrect.";
    public static final String HEADER_NO_OF_RECORD_IS_BLANK = "Header record count cannot be blank.";
    public static final String HEADER_FILLER_IS_NULL_OR_EMPTY =
            "Header filler cannot be null or empty.";
    public static final String HEADER_FILLER_INCORRECT_LENGTH = "Header filler length is incorrect.";

    // Annotation messages
    public static final String INVALID_ZERO_ONLY_STRING = "This field should only contain zeroes";
    public static final String INVALID_MHA_ADDRESS_TYPE = "Invalid mha address type.";
    public static final String INVALID_NCA_ADDRESS_TYPE = "Invalid nca address type.";
    public static final String INVALID_ADDRESS_INDICATOR = "Invalid address indicator.";
    public static final String INVALID_INVALID_ADDRESS_TAG = "Invalid invalid-address tag.";
    public static final String EXTRACTION_DATE_CUTOFF_DATE_ERROR =
            "Extraction Date must be equal/after Cut-off Date.";
    public static final String MHA_OVERSEAS_INDICATOR_AND_TYPE_ERROR =
            "If MHA address indicator is 'C', the MHA address type must also be 'C'.";
    public static final String NEW_ADDRESS_FIELD_INVALID =
            "New Address field cannot be blank or New Nca Address type cannot be invalid";
    public static final String INVALID_HOME_TYPE = "Invalid home type.";
    public static final String NRIC_LENGTH_INVALID = "NRIC Size must be exactly 9";
    public static final String DOB_LENGTH_VALID = "Date of birth size must be exactly 8";

    // Common error messages
    public static final String YEAR_CANNOT_BE_LESS_THAN_1800 = "Year value cannot be less than 1800";
    public static final String NRIC_IS_BLANK = "NRIC cannot be null/blank.";
    public static final String ID_IS_BLANK = "ID cannot be null/blank.";
    public static final String DATA_ITEM_NEW_VALUE_BLANK = "DATA ITEM NEW VALUE cannot be null/blank";
    public static final String DATA_ITEM_CHANGED_DATE_BLANK =
            "DATA ITEM CHANGED DATE cannot be null/blank";
    public static final String MINIMUM_AGE_NOT_MET =
            "Age as at 31 Dec <File Cut-off Year> must be at least "
                    + Constants.MIN_AGE_AT_CUTOFF_YEAR
                    + ".";

    // Common Header Dates error messages
    public static final String EXTRACTION_DATE_AFTER_FILE_RECEIVED_DATE =
            "Extraction date cannot be after File Received date.";
    public static final String CUTOFF_DATE_AFTER_FILE_RECEIVED_DATE =
            "Cut-off date cannot be after File Received date.";
    public static final String SELF_EMPLOYED_TRANS_DATE_AFTER_FILE_RECEIVED_DATE =
            "Trans date cannot be after File Received date.";
    public static final String INVALID_DATE_FORMAT_DDMMYYYY =
            "Must be in " + Constants.DATE_FORMAT_DDMMYYYY + " date format.";
    public static final String CURRENT_FILE_DATE_NOT_AFTER_PREV_FILE_DATE =
            "Current file header date must be after previous file header date.";
    public static final String DATE_OF_RUN_AFTER_FILE_RECEIVED_DATE =
            "Date of run cannot be after File Received date.";
    public static final String INVALID_DATE_FORMAT_YYYYMMDD =
            "Must be in " + Constants.DATE_FORMAT_YYYYMMDD + " date format.";

    // HDB Property
    public static final String INVALID_PROPERTY_TYPE = "Invalid property type.";
    public static final String INVALID_ACTION_CODE = "Invalid action code.";
    public static final String INVALID_FLAT_TYPE = "Invalid flat type.";
    public static final String INVALID_SHOP_HOUSE_TAG = "Invalid shop house tag.";
    public static final String INVALID_SHOP_HOUSE_LIVING_QUARTER =
            "Invalid shop house living quarter.";
    public static final String INVALID_RENTAL_FLAT_VALUE = "Invalid rental flat value.";

    // MHA new citizen
    public static final String INVALID_ATTAINMENT_BEFORE_DOB =
            "Citizenship attainment date must be on or after date of birth";
    public static final String INVALID_ATTAINMENT_ON_OR_AFTER_DATE_OF_RUN =
            "Citizenship attainment date must be before dateOfRun";
    public static final String INVALID_DATE_OF_ADDRESS_CHANGE_ON_OR_AFTER_DATE_OF_RUN =
            "Date of address change must be before Date of Run.";
    public static final String INVALID_FIN_AND_ATTAINMENT_DATE =
            "FIN is of wrong format, attainment date should not be empty where a FIN is provided.";
    public static final String INVALID_ATTAINMENT_DATE_WHILE_VALID_FIN =
            "FIN is valid, attainment date cannot be blank";
    public static final String INVALID_BLANK_NEW_NCA_ADDRESS =
            "New Address Indicator is empty, New NCA AddressType is null but dateOfAddressChange Or NewInvalidAddressTag is not null. "
                    + "The new NCA address is only partially blank.";
    public static final String INVALID_DATE_OF_ADDRESS_CHANGE_NCA =
            "New Address Indicator is SPACE, New NCA AddressType is valid but DateOfAddressChange is null."
                    + "The new NCA address is only partially blank.";
    public static final String INVALID_DATE_OF_ADDRESS_CHANGE_MHA =
            "New Address Indicator is SPACE, New NCA AddressType is valid but DateOfAddressChange is null."
                    + "The new NCA address is only partially blank.";
    public static final String INVALID_DATE_OF_BIRTH =
            "DateOfBirth cannot be equal/after the job dateOfRun";
    public static final String INVALID_NEW_INVALID_ADDRESS_TAG =
            "New Invalid Address tag must be uppercase.";
    public static final String INVALID_NRIC_PERSONID_FOUND =
            "RECORD NEEDS TO BE PATCHED: PersonId found for the NRIC of this record and Nationality is also found for this NRIC record.";
    public static final String INVALID_FIN_PERSONID_FOUND =
            "RECORD NEEDS TO BE PATCHED: PersonId found for the FIN of this record and Nationality found for the given FIN in the record";

    // MHA bulk citizen
    public static final String NAME_EMPTY = "Name field cannot be empty";
    public static final String GENDER_EMPTY = "Gender field cannot be empty";
    public static final String PERSON_EXISTS = "Duplicate NRIC exists in the database";
    public static final String FIN_NOT_FOUND = "FIN not found in prepared database";
    public static final String WRONG_GENDER = "Gender can only be M, F or U";
    public static final String WRONG_ADDRESS_INDICATOR =
            "Invalid address indicator. Only space, C or Z";
    public static final String WRONG_BIRTH_DATE_OF_INDIVIDUAL =
            "DOB cannot be after the cutoff date OR Individual "
                    + "must be at least 12 years old as of Cutoff year end";
    public static final String ATTAINMENT_AFTER_CUTOFF =
            "Citizenship attainment date must be equal/before Cut-off date.";
    public static final String INVALID_DATE_OF_DEATH_FORMAT = "Date of death format is invalid";
    public static final String DOD_AFTER_CUTOFF = "Date of death must be equal/before Cut-off date.";
    public static final String INVALID_NRIC = "Invalid NRIC";
    public static final String INVALID_FIN = "Invalid FIN";
    public static final String ATTAINMENT_AFTER_CUTOFF_OR_BEFORE_1800 =
            "Citizenship attainment date must be equal/before Cut-off date OR Year value cannot be less than 1800";

    // MHA dual citizen
    public static final String PERSON_WITH_MULTIPLE_NATIONALITIES =
            "Person has more than one valid nationalities";
    public static final String PERSON_HAS_CEASED_SC =
            "Person has already ceased Singapore citizenship";

    // MHA changed address
    public static final String LATE_ADDRESS_CHANGE_DATE =
            "Address change date cannot be more recent than File Cut-off Date";
    public static final String ADDRESS_CHANGED_DATE_BLANK = "Address Changed Date cannot be blank.";

    // MHA death date
    public static final String DEATH_BEFORE_BIRTH = "Date of death is earlier than Date of birth";
    public static final String UID_CANNOT_BE_EMPTY =
            "This cannot be blank. It must be a valid FIN or NRIC.";
    public static final String UNABLE_TO_DETERMINE_IF_DEATH_DATE_IS_AFTER_BIRTHDAY =
            "Unable to determine if death date is on or after birth date because there is not record of birthday";
    public static final String INVALID_VALIDITY_OF_OLD_ADDRESS = "Invalid validity of old address";
    public static final String NO_EXISTING_PERSON_DETAIL_FOR_CHANGE_ADDRESS =
            "Person Detail not found for this Id Number";
    public static final String INVALID_PERSON_PROPERTY = "Invalid Person Property association";
    public static final String EXISTING_PERSON_PROPERTY_LINK =
            "Existing Person Property with matching old and new property";
    public static final String OLD_AND_NEW_PROPERTY_IDENTICAL = "Old and new address are identical";

    // MHA ceased citizen
    public static final String RENUNCIATION_AFTER_DATE_OF_RUN =
            "Renunciation Date is after File Date of run.";
    public static final String NRIC_NOT_FOUND_IN_SYSTEM = "NRIC not found in System.";
    public static final String NO_ACTIVE_NATIONALITY = "No active Nationality record.";
    public static final String NOT_SC_DUAL_CITIZEN = "Not SC or Dual Citizen currently.";
    public static final String RENUNCIATION_DATE_FOUND =
            "Renunciation Date found in current Nationality record.";
    public static final String RENUNCIATION_NOT_AFTER_ATTAINMENT =
            "Renunciation Date is not after Citizenship Attainment Date.";
    public static final String INVALID_NAME = "Invalid Name.";
    public static final String INVALID_RENUNCIATION_DATE = "Invalid Citizen Renunciation Date.";

    // MHA person detail change (ValidPersonDetailChangeValidator)
    public static final String INVALID_GENDER_MESSAGE = "Invalid Gender";
    public static final String INVALID_GENDER_LENGTH = "Gender must be at exactly 1 character size";
    public static final String INVALID_NAME_MESSAGE = "Name should not be empty";
    public static final String INVALID_DOB_MESSAGE = "Incorrect Date of Birth format";
    public static final String NULL_FIELDS =
            "Data Item Changed and Data Item New Value should not be null";

    // MHA person detail change (PersonDetailChangeCheckService)
    public static final String NRIC_NOT_FOUND = "NRIC not found in prepared database";
    public static final String NO_EXISTING_PERSON_DETAIL = "Person Detail not found for this NRIC";
    public static final String NO_EXISTING_PERSON_DOB = "Person DOB not found for this NRIC";
    public static final String NO_EXISTING_PERSON_NAME = "Person Name not found for this NRIC";
    public static final String NO_EXISTING_PERSON_GENDER = "Person Gender not found for this NRIC";
    public static final String DATA_ITEM_CHANGED_DATE_AFTER_RUN =
            "Data item changed date cannot be after run date";
    public static final String DATA_ITEM_CHANGED_DATE_INVALID_SIZE =
            "DATA ITEM CHANGED DATE field size must be exactly 8";
    public static final String DOB_NOT_BEFORE_EXISTING_DOD =
            "Provided date of birth should be before existing date of death";
    public static final String DOB_YEAR_IS_EARLIER_THAN_1800 =
            "Year for date of birth cannot be earlier than 1800";
    public static final String DATA_ITEM_CHANGED_INVALID =
            "Invalid Person Detail Data Item Changed type.";
    public static final String GENDER_UNCHANGED = "New gender already exists in CD database";
    public static final String NAME_UNCHANGED = "New name already exists in CD database";
    public static final String DOB_UNCHANGED = "New DOB already exists in CD database";
    public static final String DATA_ITEM_CHANGED_DATE_OUTDATED =
            "New record is attempting to change a past (not current) record";
    public static final String DOB_AFTER_EXISTING_DOB =
            "Manual update required, new DOB is after existing DOB and may result in conflicts";

    // MHA Death Broadcast
    public static final String CITIZEN_HAS_AN_EXISTING_DEATH_DATE =
            "Citizen has an existing Death Date";

    // IRAS assessable income
    public static final String ID_NOT_EXIST = "NRIC does not exist in CD database";
    public static final String INVALID_HEADER_RECORD_TYPE =
            "Wrong RecordType for header. Should be 0";
    public static final String INVALID_BODY_RECORD_TYPE = "Wrong RecordType for body. Should be 1";
    public static final String INVALID_FOOTER_RECORD_TYPE =
            "Wrong RecordType for footer. Should be 2";
    public static final String INVALID_RESULT_INDICATOR = "Result indicator provided is invalid.";
    public static final String INVALID_ASSESSABLE_INCOME =
            "Result indicator provided is not 01, AI should be 0 but is not.";
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
            "Record found with resultIndicator 03 or 06 found in return file.";

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
    public static final String NURSING_HOME_DUPLICATED_RECORD_DIFFERENT_HOME_TYPE =
            "Duplicated records found with same Postal, Block, Floor and Unit but different Home type.";
    public static final String NURSING_HOME_POSTAL_MATCH_BUT_BLOCK_NOT_MATCH_WITH_PROPERTY_DETAIL =
            "Block number does not match prepared records given valid Postal Code";

    // File Upload Error Message
    public static final String FILE_UPLOAD_INTERNAL_SERVER_ERROR =
            "An Error has occurred. Failed to upload file.";
    public static final String FILE_UPLOAD_NO_FILE = "Please select a file to upload.";
    public static final String FILE_UPLOAD_NO_FILE_TYPE =
            "Please select a valid file type to upload.";
    public static final String FILE_UPLOAD_INVALID_FILE_EXTENSION =
            "Invalid file type uploaded! Please retry.";
}
