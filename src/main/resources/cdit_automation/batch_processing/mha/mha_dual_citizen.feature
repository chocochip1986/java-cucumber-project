@development
@dual_citizen
@mha
@truncate
Feature: Data processing for MHA dual citizenship

  @set_1
  Scenario: Dual Citizen batch job processing runs successfully
    Given the mha dual citizen file has the following details:
    | NewDualCitizensInFile | ExistingDualCitizensInFile | ExpiredDualCitizens |
    | 1                     | 1                          | 1                   |
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP
    Then I verify that there are new dual citizen in datasource db
    Then I verify that no changes were made to existing dual citizens
    Then I verify that the dual citizens who are not in the file will be Singaporeans

  @set_2
  Scenario: Dual Citizen batch job fails when there is an invalid nric
    Given the mha dual citizen file has an invalid nric
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status RAW_DATA_ERROR
    Then I verify that there is an error message for invalid nric

  @set_3 @defect
  Scenario: Duplicate nric in dual citizen file
    Given the mha dual citizen file have duplicate nric record
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status RAW_DATA_ERROR

  @set_4
  Scenario: Test scenario
    Given the mha dual citizen file has the following details:
    | NewDualCitizensInFile | InvalidNrics | ExistingDualCitizensInFile |
    | 1                     | 1            | 1                          |
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP
    Then I verify that there is an error message for invalid nric
    
  @set_4
  Scenario: MHA sends an empty Dual Citizen file
    Given the mha dual citizen file is empty
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status FILE_ERROR
    And the error message contains Must have at least 1 valid body record

  @set_5
  Scenario: MHA sends a file with a cut-off date after that file recevied date
    Given the mha dual citizen file has a run date in the future
      | NewDualCitizensInFile |
      | 1                     |
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And the error message contains Extraction date cannot be after File Received date

  @set_6
  Scenario: An person who was previously a dual citizen is now a dual citizen again
    Given john was a dual citizen 100 days ago
    And john became a dual citizen 99 days ago
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    Then the Mha Dual Citizen batch job completes running with status CLEANUP
    And I verify that john is a dual citizen 99 days ago

  @set_7
  Scenario: Person nric is not found in CDS
    Given the mha dual citizen file has the following details:
    | NonExistentNrics |
    | 1                |
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP