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
    And the Mha Death batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    Then I verify that there is an error message for invalid death dates

  @set_4
  Scenario: MHA sends a Death file with a FIN for processing
    Given the mha death file has the following details:
    | ValidFRDeathCases |
    | 1                 |
    When MHA sends the MHA_DEATH_DATE file to Datasource sftp for processing
    And the Mha Death batch job completes running with status RAW_DATA_ERROR
    Then I verify that there is an error message for invalid nric

  @set_5
  Scenario: MHA sends a death date for a person who already is dead
    Given the mha death file has the following details:
      | PplWhoAreAlreadyDead |
      | 1                    |
    When MHA sends the MHA_DEATH_DATE file to Datasource sftp for processing
    And the Mha Death batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    Then I verify that there is an error message for existing death case

  @set_6 @defect @GRYFFINDOR-897
  Scenario: MHA sends a future death date
    Given the mha death file has the following details:
      | PplWithFutureDeathDates |
      | 1                       |
    When MHA sends the MHA_DEATH_DATE file to Datasource sftp for processing
    And the Mha Death batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    Then I verify that there is an error message for future death date case