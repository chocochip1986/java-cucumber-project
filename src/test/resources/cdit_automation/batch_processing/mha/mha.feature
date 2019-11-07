@development
@mha_dual_citizen
Feature: Mha batch job processing end to end feature

  @Set_1
  Scenario: Dual Citizen batch job processing runs successfully
    Given the mha dual citizen file has the following details:
    | NewDualCitizensInFile | ExistingDualCitizensInFile | ExpiredDualCitizens |
    | 1                     | 1                          | 1                   |
    When the mha dual citizen job is ran
    And the batch job completes running with status FILE_CHECK_AGAINST_PREP_DATA
    Then I verify that there are new dual citizen in datasource db
    Then I verify that no changes were made to existing dual citizens
    Then I verify that the dual citizens who are not in the file will be Singaporeans

  @Set_2
  Scenario: Dual Citizen batch job fails when there is an invalid nric
    Given the mha dual citizen file has an invalid nric
    When the mha dual citizen job is ran
    And the batch job completes running with status RAW_DATA_ERROR
    Then I verify that there is an error message for invalid nric
