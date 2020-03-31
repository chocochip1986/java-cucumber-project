@development
@new_citizen
@mha
@truncate
Feature: Data processing for MHA new citizen file

  @set_1
  Scenario: Datasource service processes a MHA new citizen file with complete duplicate records
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN       | NAME     | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S3450033I | F1701510U | Person A | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
      | S3450033I | F1701510U | Person A | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status CLEANUP
    And I verify that person with S3450033I is persisted in Datasource
    And the error message contains Completely Duplicate Record found.

  @set_2
  Scenario: Datasource service processes a MHA new citizen file with partial duplicate records
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN | NAME     | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S1501502J |     | Person A | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
      | S1501502J |     | Person B | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And I verify that person with S1501502J is not persisted in Datasource
    And the error message contains Partially Duplicate Record found.

  @set_3
  Scenario: Datasource service processes a MHA new citizen file with hybrid (both completed & partial) duplicate records
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN       | NAME     | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S1501626D | G1801520Q | Person A | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
      | S1501626D | G1801520Q | Person A | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
      | S1501626D | G1801520Q | Person A | 19001231 | M      | Z            | A             | 118       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And I verify that person with S1501626D is not persisted in Datasource
    And the error message contains Partially Duplicate Record found.

  @set_4
  Scenario: Datasource service processes a MHA new citizen file with a mixture of valid and complete duplicate records
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN       | NAME     | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S1501634E | F8100327X | Person A | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
      | S1501501B |           | Person B | 18881231 | F      | Z            | A             | 999       High Rise  | Z            | A             | 001       Low Rise     | D                    | 20200301            |              |
      | S1501634E | F8100327X | Person A | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status CLEANUP
    And I verify that person with S1501634E is persisted in Datasource
    And I verify that person with S1501501B is persisted in Datasource
    And the error message contains Completely Duplicate Record found.

  @set_5
  Scenario: Datasource service processes a MHA new citizen file with a mixture of valid and partial duplicate records
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN       | NAME     | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | T1601511Z | G1801510U | Person A | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
      | T1601518G |           | Person C | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            |              |
      | T1601511Z | G1801510U | Person A | 19990101 | M      | Z            | A             | 999       High Rise  | Z            | A             | 001       Low Rise     | D                    | 20200301            | 19990101     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status ERROR_RATE_ERROR
    And I verify that person with T1601518G is not persisted in Datasource
    And I verify that person with T1601511Z is not persisted in Datasource
    And the error message contains Partially Duplicate Record found.

  @set_6
  Scenario: Datasource service processes a MHA new citizen file with correct date format and value
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN | NAME     | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S1501634E |     | Person A | 18000000 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 18000101     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status CLEANUP
    And I verify that person with S1501634E is persisted in Datasource

  @set_7
  Scenario Outline: New Citizen file contains invalid date of run
    Given a MHA_NEW_CITIZEN file contains a <problematic> invalid date of run
      | NRIC      | FIN       | NAME     | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S1501634E | F8100327X | Person A | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status <status>
    And the error message contains <message>
    Examples:
      | problematic | message                                            | status                      |
      |             | Wrong header length                                | FILE_ERROR                  |
      | 99999       | Wrong header length                                | FILE_ERROR                  |
      | FutureDate  | Extraction date cannot be after File Received date | BULK_CHECK_VALIDATION_ERROR |

  @set_8
  Scenario: Datasource service processes a MHA new citizen file with blank nric
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC | FIN | NAME     | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      |      |     | Person A | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status RAW_DATA_ERROR
    And the error message contains NRIC cannot be null/blank

  @set_9
  Scenario: Datasource service processes a MHA new citizen file with incorrect check digit
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN | NAME     | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S1501645E |     | Person A | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status RAW_DATA_ERROR
    And the error message contains Must be valid NRIC in format [S/T]1234567[A-Z]

  @set_10
  Scenario: Datasource service processes a MHA new citizen file with only 8 alpha-numeric nric
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC     | FIN | NAME     | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S1501634 |     | Person A | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status RAW_DATA_ERROR
    And the error message contains NRIC Size must be exactly 9

  @set_11
  Scenario: Datasource service processes a MHA new citizen file with with NRIC S888
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN | NAME     | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S8881634E |     | Person A | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status RAW_DATA_ERROR
    And the error message contains Must be valid NRIC

  @set_12
  Scenario: Datasource service processes a MHA new citizen file with with NRIC S555
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN | NAME     | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S5551634E |     | Person A | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status RAW_DATA_ERROR
    And the error message contains Must be valid NRIC

  @set_13
  Scenario: Datasource service processes a MHA new citizen file with DOB DD = 00, and MM = 00 , Invalid year = 0000
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN | NAME     | DOB | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S1501634E |     | Person A |     | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 18000101     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status RAW_DATA_ERROR
    And the error message contains Date of Birth must be in valid format

  @set_14
  Scenario: Datasource service processes a MHA new citizen file with DOB field is empty / space
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN | NAME     | DOB | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S1501634E |     | Person A |     | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 18000101     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status RAW_DATA_ERROR
    And the error message contains Date of Birth must be in valid format

  @set_15
  Scenario: Datasource service processes a MHA new citizen file with DOB incorrect date format
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN | NAME     | DOB       | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S1501634E |     | Person A | 190012321 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 18000101     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status RAW_DATA_ERROR
    And the error message contains Date of Birth must be in valid format

  @set_16
  Scenario: Datasource service processes a MHA new citizen file with DOB 20010229
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN | NAME     | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S1501634E |     | Person A | 20010229 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 18000101     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status RAW_DATA_ERROR
    And the error message contains Date of Birth must be in valid format

  @set_17
  Scenario: Datasource service processes a MHA new citizen file with DOB YYYY = 1799
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN | NAME     | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S1501634E |     | Person A | 17991231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 18000101     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status RAW_DATA_ERROR
    And the error message contains Year value cannot be less than 1800