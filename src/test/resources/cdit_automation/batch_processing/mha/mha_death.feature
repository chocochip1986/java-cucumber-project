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