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
    Given the mha dual citizen file has duplicate nric record
    | DuplicatedNrics |
    | 2               |
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
    | NonExistentNrics | NewDualCitizensInFile |
    | 1                | 1                     |
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP
    And no update is done for these nrics
    Then I verify that there are new dual citizen in datasource db

  @set_8 @defect
  Scenario: Person has a ceased citizenship date that is after the dual citizen date of run
    Given jane who is 45 years old had her citizenship renounced 4 days ago
    And the mha dual citizen file sends information that jane is a dual citizen 3 days ago
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP
    Then jane remains a non singaporean
    And the error message contains Person has already ceased Singapore citizenship

  @set_9
  Scenario: Person who previously ceased citizenship becomes a dual citizen
    Given jane who is 12 years old had her citizenship renounced 365 days ago
    And jane became a singapore citizen 10 days ago
    And the mha dual citizen file sends information that jane is a dual citizen 5 days ago
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    Then the Mha Dual Citizen batch job completes running with status CLEANUP
    And there are no error messages
    And jane is a dual citizen with a citizenship attainment date dating 10 days ago

  @set_10
  Scenario: Person loses dual citizenship and becomes a non singaporean
    Given jane who is 12 years old had her citizenship renounced 365 days ago
    And jane became a singapore citizen 10 days ago
    And mha states that jane is a dual citizen since 8 days ago
    And mha sends a dual citizen file without jane in it 5 days ago
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    Then the Mha Dual Citizen batch job completes running with status CLEANUP
    And there are no error messages
    Then jane is a singaporean from 5 days ago
