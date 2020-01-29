@development
@change-person-details
@mha
Feature: Data processing for Mha Change in Person Details

  @set_1
  Scenario: Mha sends an empty file for Change in Person details
    Given the mha change in person details file is empty
    When the mha person details job is ran
    And the Mha Change of Personal Details batch job completes running with status CLEANUP

  @set_2
  Scenario: Mha sends a Change in Person details file for processing
    Given the mha person details file has the following details:
    | nric  | data_item_change_category | data_item_orig_value | data_item_change_value  | data_item_change_date |
    | valid | S                         | M                    | F                       | valid                 |
    | valid | N                         | Chao Ah Beng         | Tan Ah Beng             | valid                 |
    | valid | B                         | 19860924             | 20000924                | valid                 |
    | valid | D                         | 20180909             | 20190909                | valid                 |
    When the mha person details job is ran
    And the Mha Change of Personal Details batch job completes running with status CLEANUP