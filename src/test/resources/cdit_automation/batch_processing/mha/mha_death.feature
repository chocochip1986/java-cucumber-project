@development
@mha_death
Feature: Data processing for MHA death broadcast

  @set_1
  Scenario: MHA sends an empty Death file
    Given the mha death file is empty
    When the mha death job is ran
    And the Mha Death batch job completes running with status FILE_CHECK_AGAINST_PREP_DATA

  @set_2
  Scenario: MHA sends a Death file for processing
    Given the mha death file has the following details:
    | ValidSCDeathCases | ValidPPDeathCases |
    | 1                 | 1                 |
    When the mha death job is ran
    And the Mha Death batch job completes running with status FILE_CHECK_AGAINST_PREP_DATA
    Then I verify that the people listed in the death file have the correct death dates

  @set_3
  Scenario: MHA sends a Death file with some dude whose birth date is more recent than his corresponding death date
    Given the mha death file has the following details:
      | ValidSCDeathCases | DeathDateEarlierThanBirthDate |
      | 1                 | 1                             |
    When the mha death job is ran
    And the Mha Death batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    Then I verify that there is an error message for invalid death dates

  @set_4
  Scenario: MHA sends a Death file with a FIN for processing
    Given the mha death file has the following details:
    | ValidFRDeathCases |
    | 1                 |
    When the mha death job is ran
    And the Mha Death batch job completes running with status RAW_DATA_ERROR
    Then I verify that there is an error message for invalid nric

  @set_5
  Scenario: MHA sends a death date for a person who already is dead
    Given the mha death file has the following details:
      | PplWhoAreAlreadyDead |
      | 1                    |
    When the mha death job is ran
    And the Mha Death batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    Then I verify that there is an error message for existing death case