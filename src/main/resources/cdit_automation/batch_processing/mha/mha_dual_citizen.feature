@development
@dual_citizen
@mha
Feature: Data processing for MHA dual citizenship

  @Set_1 @GRYFFINDOR-877 @defect
  Scenario: Dual Citizen batch job processing runs successfully
    Given the mha dual citizen file has the following details:
    | NewDualCitizensInFile | ExistingDualCitizensInFile | ExpiredDualCitizens |
    | 1                     | 1                          | 1                   |
    When the mha dual citizen job is ran
    And the Mha Dual Citizen batch job completes running with status FILE_CHECK_AGAINST_PREP_DATA
    Then I verify that there are new dual citizen in datasource db
    Then I verify that no changes were made to existing dual citizens
    Then I verify that the dual citizens who are not in the file will be Singaporeans

  @Set_2
  Scenario: Dual Citizen batch job fails when there is an invalid nric
    Given the mha dual citizen file has an invalid nric
    When the mha dual citizen job is ran
    And the Mha Dual Citizen batch job completes running with status RAW_DATA_ERROR
    Then I verify that there is an error message for invalid nric

  @Set_3 @defect
  Scenario: Duplicate nric in dual citizen file
    Given the mha dual citizen file have duplicate nric record
    When the mha dual citizen job is ran
    And the Mha Dual Citizen batch job completes running with status RAW_DATA_ERROR

  @Set_4
  Scenario: Test scenario
    Given the mha dual citizen file has the following details:
    | NewDualCitizensInFile | InvalidNrics | ExistingDualCitizensInFile |
    | 1                     | 1            | 1                          |
    When the mha dual citizen job is ran
    And the Mha Dual Citizen batch job completes running with status RAW_DATA_ERROR
    Then I verify that there is an error message for invalid nric
    
  @Set_4
  Scenario: MHA sends an empty Dual Citizen file
    Given the mha dual citizen file is empty
    When the mha dual citizen job is ran
    And the Mha Dual Citizen batch job completes running with status FILE_CHECK_AGAINST_PREP_DATA

  @Set_5
  Scenario: MHA sends a file with a cut-off date after that file recevied date
    Given the mha dual citizen file has a cut-off date in the future
    When the mha dual citizen job is ran
    And the Mha Dual Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And the error message is Cut-off date cannot be after File Received date.