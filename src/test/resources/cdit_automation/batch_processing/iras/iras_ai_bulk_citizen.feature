@development
@bulk_citizen
@iras
Feature: Data processing for IRAS bulk citizen file

  @set_1
  Scenario: Datasource service processes a MHA bulk citizen file
    Given there are 10 Singaporeans who passed away in the year 2019
    And the iras ai bulk file is created
    When Datasource is triggered to generate the IRAS AI Bulk file
