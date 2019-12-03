@development
@bulk_citizen
@iras
Feature: Data processing for IRAS bulk citizen file

  @set_1
  Scenario: Datasource service processes a MHA bulk citizen file
    Given the iras assessable income bulk file has the following details:
    | |
    And the iras ai bulk file is created
    When Datasource is triggered to generate the IRAS AI Bulk file
