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
  Scenario: MHA sends file with empty Date of Run
    Given the mha dual citizen file contains invalid date of run and date of run is EMPTY
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status FILE_ERROR
    Then I verify that there is an error message for wrong header length

  @set_3
  Scenario: MHA sends file with empty spaces of Date of Run
    Given the mha dual citizen file contains invalid date of run and date of run is EMPTY_SPACE
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status RAW_DATA_ERROR
    Then I verify that there is an error message for wrong date format

  @set_4
  Scenario: MHA sends file with invalid format for Date of Run
    Given the mha dual citizen file contains invalid date of run and date of run is INVALID_FORMAT
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status RAW_DATA_ERROR
    Then I verify that there is an error message for wrong date format

  @set_5
  Scenario: MHA sends file with future date for Date of Run
    Given the mha dual citizen file contains invalid date of run and date of run is FUTURE_DATE
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then I verify that there is an error message for future date

  @set_6
  Scenario: MHA sends file with empty nric
    Given the mha dual citizen file has EMPTY nric
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status FILE_ERROR
    Then I verify that the is an error message for wrong body length

  @set_7
  Scenario: MHA sends file with empty whitespace nric
    Given the mha dual citizen file has EMPTY_SPACE nric
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status RAW_DATA_ERROR
    Then I verify that the is an error message for null or blank nric

  @set_8
  Scenario: MHA sends file with invalid nric
    Given the mha dual citizen file has INVALID nric
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status RAW_DATA_ERROR
    Then I verify that there is an error message for invalid nric

  @set_9
  Scenario: MHA sends file with shorter nric
    Given the mha dual citizen file has SHORT nric
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status FILE_ERROR
    Then I verify that the is an error message for wrong body length

  @set_10
  Scenario: MHA sends file with S555 nric
    Given the mha dual citizen file has S555 nric
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status RAW_DATA_ERROR
    Then I verify that there is an error message for invalid nric

  @set_1
  Scenario: MHA sends file with S888 nric
    Given the mha dual citizen file has S888 nric
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status RAW_DATA_ERROR
    Then I verify that there is an error message for invalid nric

  @set_2 @defect
  Scenario: MHA sends file with duplicate nric
    Given the mha dual citizen file has duplicate nric record
    | DuplicatedNrics |
    | 2               |
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP

  @set_3
  Scenario: Person nric is not found in CDS
    Given the mha dual citizen file has the following details:
      | NonExistentNrics | NewDualCitizensInFile |
      | 1                | 1                     |
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP
    And no update is done for these nrics
    Then I verify that there are new dual citizen in datasource db

  @set_4
  Scenario: Person nric is a dual citizen and remains as a dual citizen
    Given john who is 13 years old converted to a dual citizen 7 days ago
    And MHA dual citizen file contains john nric
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP
    Then I verify that john nationality was not updated in datasource db

  @set_5
  Scenario: Person nric is a dual citizen and turns sg citizen
    Given john who is 30 years old converted to a dual citizen 100 days ago
    And MHA dual citizen file does not contains person nric
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP
    Then I verify that john nationality was updated in datasource db











  @set_5 @defect
  Scenario: An person who was previously a dual citizen is now a dual citizen again
    Given john was a dual citizen 100 days ago
    And john became a dual citizen 99 days ago
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    Then the Mha Dual Citizen batch job completes running with status CLEANUP
    And I verify that john is a dual citizen 99 days ago







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
  Scenario: Person loses dual citizenship and becomes a singaporean
    Given jane who is 12 years old had her citizenship renounced 365 days ago
    And jane became a singapore citizen 10 days ago
    And mha states that jane is a dual citizen since 8 days ago
    And mha sends a dual citizen file without jane in it 5 days ago
    When MHA sends the MHA_DUAL_CITIZEN file to Datasource sftp for processing
    Then the Mha Dual Citizen batch job completes running with status CLEANUP
    And there are no error messages
    Then jane is a singaporean from 5 days ago

