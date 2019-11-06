Feature: Mha batch job processing end to end feature

  Scenario: Dual Citizen batch job processing runs successfully
    Given there are 2 existing dual citizens
    And I verify that the dual citizens exists

  Scenario: Dual Citizen batch job processing runs successfully
    Given the mha dual citizen file has the following details:
    | NewDualCitizensInFile | ExistingDualCitizensInFile | ExpiredDualCitizens |
    | 1                     | 1                          | 1                   |
    When the mha dual citizen job is ran
    And the batch job completes running
    Then I verify that there are new dual citizen in datasource db
    Then I verify that no changes were made to existing dual citizens
    Then I verify that the dual citizens who are not in the file will be Singaporeans
