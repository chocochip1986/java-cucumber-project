@datasource
@development
@change_person_details
@mha
@truncate
Feature: Data processing for Mha Change in Person Details

  @set_1
  Scenario: Mha sends an empty file for Change in Person details
    Given the mha change in person details file is empty
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status FILE_ERROR
    And the error message contains Must have at least 1 valid body record

  @set_2
  Scenario Outline: Mha sends a valid Change in Person details file for processing
    Given the mha person details file has the following details:
      | nric   | data_item_change_category | data_item_orig_value | data_item_change_value | data_item_change_date |
      | <nric> | <data_item_change_cat>    | <original_value>     | <new_value>            | valid                 |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status CLEANUP
    And the corresponding <Person_Record> record for person with nric <nric> now reflects the new value <new_value>
    Examples:
      | nric      | data_item_change_cat | original_value       | new_value              | Person_Record |
      | S9047674B | B                    | 20010924             | 19940924               | Person_Detail |
      | S3098804C | N                    | Chao Ah Beng         | Tan Ah Beng            | Person_Name   |
      | S5821001C | S                    | M                    | F                      | Person_Gender |

  @set_3
  Scenario: Mha sends a Change in Person details file with an invalid data item change category
    Given the mha person details file has the following details:
      | nric  | data_item_change_category | data_item_orig_value | data_item_change_value  | data_item_change_date |
      | valid | S                         | M                    | F                       | valid                 |
      | valid | N                         | Chao Ah Beng         | Tan Ah Beng             | valid                 |
      | valid | D                         | 20010924             | 20000924                | valid                 |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status CLEANUP
    And the error message contains Invalid Person Detail Data Item Changed type

  @set_4
  Scenario: Mha sends a file with repeated partial duplicated name
    Given the mha person details file has the following details:
      | nric      | data_item_change_category | data_item_orig_value | data_item_change_value  | data_item_change_date |
      | S5881915H | N                         | Jay Chou             | Tan Ah Beng             | valid                 |
      | valid     | B                         | 20010924             | 20000924                | valid                 |
      | S5881915H | N                         | Jay Chou             | Tan Tan Ah Beng         | valid                 |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status ERROR_RATE_ERROR
    And the error message contains Partially Duplicate Record found

   @set_5
   Scenario: Mha sends a file with repeated partial duplicated gender
     Given the mha person details file has the following details:
       | nric      | data_item_change_category | data_item_orig_value | data_item_change_value  | data_item_change_date |
       | S5881915H | S                         | M                    | F                       | valid                 |
       | valid     | N                         | Chao Ah Beng         | Tan Ah Beng             | valid                 |
       | S5881915H | S                         | U                    | F                       | valid                 |
     When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
     And the Mha Change of Personal Details batch job completes running with status ERROR_RATE_ERROR
     And the error message contains Partially Duplicate Record found

  @set_6
  Scenario: Mha sends a file with repeated partial duplicated birth date
    Given the mha person details file has the following details:
      | nric      | data_item_change_category | data_item_orig_value | data_item_change_value  | data_item_change_date |
      | S5881915H | B                         | 20200101             | 20191231                | valid                 |
      | valid     | N                         | Chao Ah Beng         | Tan Ah Beng             | valid                 |
      | S5881915H | B                         | 20200101             | 20191231                | valid                 |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status ERROR_RATE_ERROR
    And the error message contains Partially Duplicate Record found

  @set_7
  Scenario: Mha sends a file with full duplicated records
    Given the mha person details file has the following details:
      | nric      | data_item_change_category | data_item_orig_value | data_item_change_value  | data_item_change_date |
      | S5881915H | N                         | Jay Chou             | Tan Ah Beng             | 20180723              |
      | valid     | B                         | 20010924             | 20000924                | valid                 |
      | S5881915H | N                         | Jay Chou             | Tan Ah Beng             | 20180723              |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status CLEANUP
    And the error message contains Completely Duplicate Record found

  @set_8
  Scenario: Mha sends a file with an item change date after run date (test run date is today minus 5 days)
    Given the mha person details file has the following details:
      | nric      | data_item_change_category | data_item_orig_value | data_item_change_value | data_item_change_date |
      | S5881915H | N                         | Jay Chou             | Tan Ah Beng            | 99991231              |
      | valid     | B                         | 20010924             | 20000924               | valid                 |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status ERROR_RATE_ERROR
    And the error message contains Data item changed date cannot be after run date

  @set_9
  Scenario Outline: Mha sends a file with a detail change for a person with no such existing detail record
    Given the mha person details file has the following details:
      | nric      | data_item_change_category | data_item_orig_value | data_item_change_value | data_item_change_date |
      | S3899408E | <data_item_cat>           | <data_item_orig_val>  | <data_item_new_val>    | valid                 |
    And the person with nric S3899408E does not have an existing <Person_Record> record
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And the error message contains <error_message>
    Examples:
      | data_item_cat | data_item_orig_val | data_item_new_val | Person_Record | error_message           |
      | B             | 00010101           | 20000924          | Person_Detail | Person Detail not found |
      | N             | Non existing name  | xxx               | Person_Name   | Person Name not found   |
      | S             | U                  | F                 | Person_Gender | Person Gender not found |

  @set_10
  Scenario Outline: Mha sends a file with a detail change for a person but new value is already in the database
    Given the mha person details file has the following details:
      | nric      | data_item_change_category | data_item_orig_value | data_item_change_value | data_item_change_date |
      | S3899408E | <data_item_cat>           | <data_item_org_val>  | <data_item_new_val>    | valid                 |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And the error message contains <error_message>
    Examples:
      | data_item_cat | data_item_org_val | data_item_new_val | error_message             |
      | B             | 20000101          | 20000101          | New DOB already exists    |
      | N             | Samantha Nami     | Samantha Nami     | New name already exists   |
      | S             | F                 | F                 | New gender already exists |

  @set_1
  Scenario Outline: Mha sends a file with a detail change for a person but the changed date is outdated (before the latest record's valid from)
    Given the mha person details file has the following details:
      | nric      | data_item_change_category | data_item_orig_value | data_item_change_value | data_item_change_date |
      | S3899408E | <data_item_cat>           | <data_item_org_val>  | <data_item_new_val>    | 19000101              |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And the error message contains <error_message>
    Examples:
      | data_item_cat | data_item_org_val | data_item_new_val | error_message                             |
      | B             | 20000101          | 19900101          | New record is attempting to change a past |
      | N             | Samantha Nami     | Differani Nami    | New record is attempting to change a past |
      | S             | F                 | U                 | New record is attempting to change a past |

  @set_2
  Scenario: Mha sends a valid Change in Person details file for processing which will affect other tables
    Given the mha person details file has the following details:
      | nric      | data_item_change_category | data_item_orig_value | data_item_change_value | data_item_change_date |
      | S3899408E | B                         | 20000101             | 19900101               | valid                 |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status CLEANUP
    And the corresponding records for person with nric S3899408E are now valid from the new value 19900101