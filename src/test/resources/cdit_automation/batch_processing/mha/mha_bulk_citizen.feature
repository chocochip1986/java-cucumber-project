@development
@bulk_citizen
@mha
Feature: Data processing for MHA bulk citizen file

  @set_1
  Scenario: Datasource service processes a MHA bulk citizen file
    Given the mha bulk file has the following details:
    | Singaporean,Male,DoD:DeathBeforeBirth |
    And the mha bulk file is created
    When the mha bulk file is ran
