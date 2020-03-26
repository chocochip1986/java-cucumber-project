@development
@death
@mha
Feature: Data processing for MHA death broadcast

  @set_1
  Scenario: MHA sends an empty Death file
    Given the mha death file is empty
    When MHA sends the MHA_DEATH_DATE file to Datasource sftp for processing
    And the Mha Death batch job completes running with status FILE_ERROR
    And the error message contains Must have at least 1 valid body record

  @set_2
  Scenario: MHA sends a Death file for processing
    Given the mha death file has the following details:
    | ValidSCDeathCases | ValidPPDeathCases |
    | 1                 | 1                 |
    When MHA sends the MHA_DEATH_DATE file to Datasource sftp for processing
    And the Mha Death batch job completes running with status CLEANUP
    Then I verify that the people listed in the death file have the correct death dates

  @set_3
  Scenario: MHA sends a Death file with some dude whose birth date is more recent than his corresponding death date
    Given the mha death file has the following details:
      | ValidSCDeathCases | DeathDateEarlierThanBirthDate |
      | 1                 | 1                             |
    When MHA sends the MHA_DEATH_DATE file to Datasource sftp for processing
    And the Mha Death batch job completes running with status ERROR_RATE_ERROR
    Then the error message contains Date of death is earlier than Date of birth

  @set_4
  Scenario: MHA sends a Death file with a FIN for processing
    Given the mha death file has the following details:
    | ValidFRDeathCases |
    | 1                 |
    When MHA sends the MHA_DEATH_DATE file to Datasource sftp for processing
    And the Mha Death batch job completes running with status CLEANUP
    Then I verify that the people listed in the death file have the correct death dates

  @set_5
  Scenario: MHA sends a death date for a person who already is dead
    Given the mha death file has the following details:
      | PplWhoAreAlreadyDead |
      | 1                    |
    When MHA sends the MHA_DEATH_DATE file to Datasource sftp for processing
    And the Mha Death batch job completes running with status CLEANUP
    Then the error message contains Citizen has an existing Death Date

  @set_6
  Scenario: MHA sends a future death date
    Given the mha death file has the following details:
      | PplWithFutureDeathDates |
      | 1                       |
    When MHA sends the MHA_DEATH_DATE file to Datasource sftp for processing
    And the Mha Death batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    Then I verify that there is an error message for future death date case

  @set_7
  Scenario: MHA sends duplicated entries
    Given the mha death file has the following details:
      | DuplicatedEntries |
      | 1                 |
    When MHA sends the MHA_DEATH_DATE file to Datasource sftp for processing
    And the Mha Death batch job completes running with status CLEANUP
    Then the error message contains Completely Duplicate Record found

  @set_8
  Scenario: MHA sends partially duplicated entries
    Given the mha death file has the following details:
      | PartialDuplicates |
      | 1                 |
    When MHA sends the MHA_DEATH_DATE file to Datasource sftp for processing
    And the Mha Death batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then the error message contains Partially Duplicate Record found

  @set_9
  Scenario: MHA sends partially duplicated entries
    Given the mha death file has the following details:
      | PartialDuplicates | ValidSCDeathCases |
      | 1                 | 1                 |
    When MHA sends the MHA_DEATH_DATE file to Datasource sftp for processing
    And the Mha Death batch job completes running with status ERROR_RATE_ERROR
    Then the error message contains Partially Duplicate Record found