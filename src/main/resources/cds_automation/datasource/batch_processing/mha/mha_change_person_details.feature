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

  #UAT Scenarios: 1, 5, 13, 16, 17, 18
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

  #UAT Scenario: 14
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

  #UAT Scenario: 11
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

  #UAT Scenario: 11
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

  #UAT Scenario: 11
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

  #UAT Scenario: 10
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

  #UAT Scenario: 12
  @set_8
  Scenario: Mha sends a file with both full duplicated and partial duplicated records
    Given the mha person details file has the following details:
      | nric      | data_item_change_category | data_item_orig_value | data_item_change_value  | data_item_change_date |
      | S5881915H | N                         | Jay Chou             | Tan Ah Beng             | 20180723              |
      | S5881915H | N                         | Jay Chou             | Tan Ah Lian             | 20180723              |
      | S5881915H | N                         | Jay Chou             | Tan Ah Beng             | 20180723              |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And the error message contains Partially Duplicate Record found

  #UAT Scenario: 32
  @set_9
  Scenario: Mha sends a file with an item change date after run date (test run date is today minus 5 days)
    Given the mha person details file has the following details:
      | nric      | data_item_change_category | data_item_orig_value | data_item_change_value | data_item_change_date |
      | S5881915H | N                         | Jay Chou             | Tan Ah Beng            | 99991231              |
      | valid     | B                         | 20010924             | 20000924               | valid                 |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status ERROR_RATE_ERROR
    And the error message contains Data item changed date cannot be after run date

  #UAT Scenario: 37
  @set_10
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

  #UAT Scenario: 37
  @set_11
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

  #Custom Scenario: 4
  @set_12
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

  #UAT Scenario: 39
  @set_13
  Scenario: Mha sends a valid Change in Person details file for processing which will affect other tables
    Given the mha person details file has the following details:
      | nric      | data_item_change_category | data_item_orig_value | data_item_change_value | data_item_change_date |
      | S3899408E | B                         | 20000101             | 19900101               | valid                 |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status CLEANUP
    And the corresponding records for person with nric S3899408E are now valid from the new value 19900101

  #UAT Scenario: 40, 41
  @set_14
  Scenario Outline: Mha sends a valid Change in Person details file for processing with Gender and Name change
    Given the mha person details file has the following details:
      | nric   | data_item_change_category | data_item_orig_value | data_item_change_value | data_item_change_date |
      | <nric> | <data_item_change_cat>    | <original_value>     | <new_value>            | 20100101              |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status CLEANUP
    And the corresponding <Person_Record> record for person with nric <nric> now reflects the new value <new_value>
    And the corresponding <Person_Record> record for person with nric <nric> now reflects new valid_from value 20100101
    Examples:
      | nric      | data_item_change_cat | original_value       | new_value              | Person_Record |
      | S3098804C | N                    | Chao Ah Beng         | Tan Ah Beng            | Person_Name   |
      | S5821001C | S                    | M                    | F                      | Person_Gender |

  #UAT Scenario: 42
  #Custom Scenario: 1
  @set_15
  Scenario Outline: Mha sends a valid Change in Person details file containing changes that are already in database (since 5 days ago)
    Given the mha person details file has the following details:
      | nric      | data_item_change_category | data_item_orig_value | data_item_change_value | data_item_change_date |
      | S3899408E | N                         | Name One             | Name Two               | 20180101              |
      | S3098804C | S                         | M                    | F                      | 20180101              |
      | S5821001C | B                         | 20000101             | 19900101               | 20180101              |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status CLEANUP
    Given the mha person details file is of the following contents and with header date of 2 days ago:
      | nric      | data_item_change_category | data_item_change_value | data_item_change_date |
      | S3899408E | N                         | Name Two               | 20180101              |
      | S3098804C | S                         | F                      | 20180101              |
      | S5821001C | B                         | 19900101               | 20180101              |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And the error message contains <error_message>
    Examples:
      | error_message             |
      | New DOB already exists    |
      | New name already exists   |
      | New gender already exists |

  #Custom Scenario: 2
  @set_16
  Scenario Outline: Mha sends a valid Change in Person details file from 4 days ago containing changes that are already in database (since 5 days ago)
    Given the mha person details file has the following details:
      | nric      | data_item_change_category | data_item_orig_value | data_item_change_value | data_item_change_date |
      | S3899408E | N                         | Name One             | Name Three             | 20180201              |
      | S3098804C | S                         | M                    | U                      | 20180201              |
      | S5821001C | B                         | 20000101             | 19900101               | 20180201              |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status CLEANUP
    Given the mha person details file is of the following contents and with header date of 4 days ago:
      | nric      | data_item_change_category | data_item_change_value | data_item_change_date |
      | S3899408E | N                         | Name Two               | 20180101              |
      | S3098804C | S                         | F                      | 20180101              |
      | S5821001C | B                         | 19950101               | 20180101              |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And the error message contains <error_message>
    Examples:
      | error_message                                                                       |
      | Manual update required as new DOB is after existing DOB and may result in conflicts |
      | New record is attempting to change a past (not current) record                      |
      | New record is attempting to change a past (not current) record                      |

  #UAT Scenario: 19, 20, 21, 22, 25
  @set_17
  Scenario Outline: Mha sends a file with a detail change for a person with erroneous Gender, DOB and Name values
    Given the mha person details file has the following details:
      | nric      | data_item_change_category | data_item_orig_value | data_item_change_value | data_item_change_date |
      | S3899408E | S                         | U                    | A                      | valid                 |
      | S3899408E | N                         | Robert Chan          |                        | valid                 |
      | S3899408E | B                         | 19900101             | 31121989               | valid                 |
      | S5821001C | S                         | F                    | 2                      | valid                 |
      | S3098804C | S                         | M                    | B                      | valid                 |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status RAW_DATA_ERROR
    And the error message contains <error_message>
    Examples:
      | error_message                            |
      | Invalid Gender                           |
      | Incorrect Date of Birth format           |
      | DATA ITEM NEW VALUE cannot be null/blank |
      | Invalid Gender                           |
      | Invalid Gender                           |

  #UAT Scenario: 23, 24
  @set_18 @defect @gryffindor-1281
  Scenario Outline: Mha sends a file with a detail change for a person with erroneous Name values
    Given the mha person details file has the following details:
      | nric      | data_item_change_category | data_item_orig_value | data_item_change_value | data_item_change_date |
      | S3899408E | N                         | Robert Chan          | 19900612               | valid                 |
      | S3098804C | N                         | Margaret Chan        | M                      | valid                 |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status RAW_DATA_ERROR
    And the error message contains <error_message>
    Examples:
      | error_message |
      | Invalid Name  |
      | Invalid Name  |

  #UAT Scenario: 26, 27
  @set_18
  Scenario Outline: Mha sends a file with a detail change for a person with erroneous DOB values
    Given the mha person details file has the following details:
      | nric      | data_item_change_category | data_item_orig_value | data_item_change_value | data_item_change_date |
      | S3899408E | B                         | 19900101             | 99991231               | valid                 |
      | S3098804C | B                         | 19910101             | M                      | valid                 |
    When MHA sends the MHA_PERSON_DETAIL_CHANGE file to Datasource sftp for processing
    And the Mha Change of Personal Details batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And the error message contains <error_message>
    Examples:
      | error_message |
      | New DOB is a future date and is invalid |
      | Incorrect Date of Birth format          |
