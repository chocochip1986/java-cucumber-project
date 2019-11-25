@development
Feature: Data processing for MHA ceased sc

  @set_1
  Scenario: Mha send a ceased sc file that contain a person detail record not found in system
    Given the ceased sc file contain a person detail record not found in system
    When the mha ceased sc job is ran
    Then the Mha Ceased Citizen batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And the batch error message is NRIC not found in System.

  @set_2 @defect
  Scenario: Mha send a ceased sc file that contain a record that is already exist in system
  GRYFFINDOR-908
    Given the ceased sc file contain a record that is already exist in the system
    When the mha ceased sc job is ran
    Then the Mha Ceased Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR

  @set_3 @defect
  Scenario: Mha send a ceased sc file that contain duplicate record
  GRYFFINDOR-908
    Given the ceased sc file contain duplicate record
    When the mha ceased sc job is ran
    Then the Mha Ceased Citizen batch job completes running with status RAW_DATA_ERROR

  @set_4
  Scenario: Mha send a ceased sc file that contain a name length of zero
    Given the ceased sc file contain an invalid name length of zero
    When the mha ceased sc job is ran
    Then the Mha Ceased Citizen batch job completes running with status RAW_DATA_ERROR
    And the batch error message is Invalid Name.

  @set_5
  Scenario: Mha send a ceased sc file that contain an renunciation date that is after cut off date with no attainment date
    Given the ceased sc file contain an invalid renunciation date that is after cut off date with no attainment date
    When the mha ceased sc job is ran
    Then the Mha Ceased Citizen batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And the batch error message is Renunciation Date is after File Cut-off Date.

  @set_6 @defect
  Scenario: Mha send a ceased sc file has the following details:
    Given the ceased sc file has the following details:
      | SINGAPORE CITIZEN | DUAL CITIZEN |
      | 0                 | 1            |
    When the mha ceased sc job is ran
    Then the Mha Ceased Citizen batch job completes running with status FILE_CHECK_AGAINST_PREP_DATA
    And nationality of all person should change to NON_SINGAPORE_CITIZEN
    And nric cancelled status change to 1