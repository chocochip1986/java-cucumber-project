@development
@change-person-details
@mha
Feature: Data processing for Mha Change in Person Details

  @set_1
  Scenario: Mha sends an empty file for Change in Person details
    Given the mha change in person details file is empty
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status FILE_ERROR
    And the error message contains Must have at least 1 valid body record

  @set_2
  Scenario: Mha sends a Change in Person details file for processing
    Given the mha person details file has the following details:
    | nric  | data_item_change_category | data_item_orig_value | data_item_change_value  | data_item_change_date |
    | valid | S                         | M                    | F                       | valid                 |
    | valid | N                         | Chao Ah Beng         | Tan Ah Beng             | valid                 |
    | valid | B                         | 19860924             | 20000924                | valid                 |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status CLEANUP

  @set_3
  Scenario: Mha sends a Change in Person details file with an invalid data item change category
    Given the mha person details file has the following details:
      | nric  | data_item_change_category | data_item_orig_value | data_item_change_value  | data_item_change_date |
      | valid | S                         | M                    | F                       | valid                 |
      | valid | N                         | Chao Ah Beng         | Tan Ah Beng             | valid                 |
      | valid | X                         | 19860924             | 20000924                | valid                 |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status RAW_DATA_ERROR
    And the error message contains Invalid Person Detail Data Item Changed type

  @set_4
  Scenario: Mha sends a file with repeated partial duplicated name
    Given the mha person details file has the following details:
      | nric      | data_item_change_category | data_item_orig_value | data_item_change_value  | data_item_change_date |
      | S5881915H | N                         | Jay Chou             | Tan Ah Beng             | valid                 |
      | valid     | B                         | 19860924             | 20000924                | valid                 |
      | S5881915H | N                         | Jay Chou             | Tan Tan Ah Beng         | valid                 |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And the error message contains Partially Duplicate Record found

   @set_5
   Scenario: MHa sends a file with repeated partial duplicated gender
     Given the mha person details file has the following details:
       | nric      | data_item_change_category | data_item_orig_value | data_item_change_value  | data_item_change_date |
       | S5881915H | S                         | M                    | F                       | valid                 |
       | valid     | N                         | Chao Ah Beng         | Tan Ah Beng             | valid                 |
       | S5881915H | S                         | U                    | F                       | valid                 |
     When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
     And the Mha Change of Personal Details batch job completes running with status BULK_CHECK_VALIDATION_ERROR
     And the error message contains Partially Duplicate Record found

  @set_6
  Scenario: MHa sends a file with repeated partial duplicated birth date
    Given the mha person details file has the following details:
      | nric      | data_item_change_category | data_item_orig_value | data_item_change_value  | data_item_change_date |
      | S5881915H | B                         | 20180909             | 20191231                | valid                 |
      | valid     | N                         | Chao Ah Beng         | Tan Ah Beng             | valid                 |
      | S5881915H | B                         | 20180919             | 20191231                | valid                 |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And the error message contains Partially Duplicate Record found