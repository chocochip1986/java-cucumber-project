@development
@ceased-citizen
@mha
Feature: Data processing for Mha ceased citizenship

  @set_1
  Scenario: Mha send a ceased citizenship file with a person detail record not found in system
    Given the file has the following details:
      | PresentSingaporeCitizen | PresentDualCitizen | CeasedSingaporeCitizen | CeasedDualCitizen |
      | 0                       | 0                  | 0                      | 1                 |
    When the mha ceased sc job is ran
    Then the Mha Ceased Citizen batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And I verify the batch error message is NRIC not found in System.

  @set_2 @defect @GRYFFINDOR-908
  Scenario: Mha send a ceased citizenship file with a record that is already exist in system
    Given the file contain a record that is already exist in the system
    When the mha ceased sc job is ran
    Then the Mha Ceased Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR

  @set_3 @defect @GRYFFINDOR-908
  Scenario: Mha send a ceased citizenship file with duplicate record
    Given the file contain duplicate record
    When the mha ceased sc job is ran
    Then the Mha Ceased Citizen batch job completes running with status RAW_DATA_ERROR

  @set_4
  Scenario: Mha send a ceased citizenship file with a name length of zero
    Given the file has the following details:
      | EmptyName |
      | 1         |
    When the mha ceased sc job is ran
    Then the Mha Ceased Citizen batch job completes running with status RAW_DATA_ERROR
    And I verify the batch error message is Invalid Name.

  @set_5
  Scenario: Mha send a ceased citizenship file with an renunciation date that is after cut off date
    Given the file has the following details:
      | RenunciationDateAfterCutOff |
      | 1                           |
    When the mha ceased sc job is ran
    Then the Mha Ceased Citizen batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And I verify the batch error message is Renunciation Date is after File Cut-off Date.

  @set_6
  Scenario: Mha send a ceased citizenship file for processing
    Given the file has the following details:
      | PresentSingaporeCitizen | PresentDualCitizen | CeasedSingaporeCitizen | CeasedDualCitizen |
      | 3                       | 3                  | 2                      | 2                 |
    When the mha ceased sc job is ran
    Then the Mha Ceased Citizen batch job completes running with status FILE_CHECK_AGAINST_PREP_DATA
    And I verify the the people listed in the file have nationality of NON_SINGAPORE_CITIZEN
    And I verify the the people listed in the file have NRIC_CANCELLED_STATUS of 1
    And I verify the old nationality [VALID_TILL] timestamp is a day before cut off date at 2359HR
    And I verify the supersede nationality [VALID_FROM] date is cut off date at 0000HR

  @set_7
  Scenario: Mha send a ceased citizenship file with nric cancelled status of [Y] and nationality of [SG]

  This is a special case that will never happen because this file is for renunciation of
  singapore citizenship, not award singapore citizenship. Therefore, datasource will
  ignore/skip if come across it.

    Given the file has the following details:
      | PresentSingaporeCitizen | AwardedSingaporeCitizen |
      | 1                       | 1                       |
    When the mha ceased sc job is ran
    Then the Mha Ceased Citizen batch job completes running with status FILE_CHECK_AGAINST_PREP_DATA
    And I verify the the people listed in the file have nationality of SINGAPORE_CITIZEN
    And I verify the the people listed in the file have NRIC_CANCELLED_STATUS of 0

  @set_8
  Scenario: Mha send a empty ceased citizenship file
    Given the file has the following details:
      | PresentSingaporeCitizen | PresentDualCitizen | CeasedSingaporeCitizen | CeasedDualCitizen |
      | 0                       | 0                  | 0                      | 0                 |
    When the mha ceased sc job is ran
    Then the Mha Ceased Citizen batch job completes running with status FILE_CHECK_AGAINST_PREP_DATA