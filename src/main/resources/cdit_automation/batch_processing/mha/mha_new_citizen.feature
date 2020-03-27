@development
@new_citizen
@mha
@truncate
Feature: Data processing for MHA new citizen file

  @set_1
  Scenario: Datasource service processes a MHA new citizen file with complete duplicate records
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN       | NAME      | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S3450033I | F1701510U | Person A  | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
      | S3450033I | F1701510U | Person A  | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status CLEANUP
    And I verify that person with S3450033I is persisted in Datasource
    And the error message contains Completely Duplicate Record found.

  @set_2
  Scenario: Datasource service processes a MHA new citizen file with partial duplicate records
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN       | NAME      | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S1501502J |           | Person A  | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
      | S1501502J |           | Person B  | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And I verify that person with S1501502J is not persisted in Datasource  
    And the error message contains Partially Duplicate Record found.

  @set_3
  Scenario: Datasource service processes a MHA new citizen file with hybrid (both completed & partial) duplicate records
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN       | NAME      | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S1501626D | G1801520Q | Person A  | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
      | S1501626D | G1801520Q | Person A  | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
      | S1501626D | G1801520Q | Person A  | 19001231 | M      | Z            | A             | 118       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And I verify that person with S1501626D is not persisted in Datasource  
    And the error message contains Partially Duplicate Record found.

  @set_4
  Scenario: Datasource service processes a MHA new citizen file with a mixture of valid and complete duplicate records
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN       | NAME      | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | S1501634E | F8100327X | Person A  | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
      | S1501501B |           | Person B  | 18881231 | F      | Z            | A             | 999       High Rise  | Z            | A             | 001       Low Rise     | D                    | 20200301            |              |
      | S1501634E | F8100327X | Person A  | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status CLEANUP
    And I verify that person with S1501634E is persisted in Datasource
    And I verify that person with S1501501B is persisted in Datasource
    And the error message contains Completely Duplicate Record found.

  @set_5
  Scenario: Datasource service processes a MHA new citizen file with a mixture of valid and partial duplicate records
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN       | NAME      | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR             | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR               | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | T1601511Z | G1801510U | Person A  | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            | 19001231     |
      | T1601518G |           | Person C  | 19001231 | M      | Z            | A             | 117       Vista Rise | Z            | A             | 888       Diamond Rise | D                    | 20200301            |              |
      | T1601511Z | G1801510U | Person A  | 19990101 | M      | Z            | A             | 999       High Rise  | Z            | A             | 001       Low Rise     | D                    | 20200301            | 19990101     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha New Citizen batch job completes running with status ERROR_RATE_ERROR
    And I verify that person with T1601518G is not persisted in Datasource
    And I verify that person with T1601511Z is not persisted in Datasource
    And the error message contains Partially Duplicate Record found.
