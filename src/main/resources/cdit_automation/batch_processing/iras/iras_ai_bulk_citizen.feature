@development
@ingresss_bulk_citizen
@iras
@truncate
Feature: Data processing for IRAS bulk citizen file

  @set_1 @wip
  Scenario: Datasource service processes a IRAS bulk citizen file
    Given there are 1 Singaporeans
    Given there are 1 Singaporeans who passed away within the year 2019
    Given there are 1 Singaporeans who passed away within the year 2018
    Given there are 1 Singaporeans who passed away within the year 2017
    When Datasource is triggered to generate the IRAS AI Bulk file
    Then the IRAS AI bulk file should be created
    Then I verify that there are no erroneous entries in the IRAS AI bulk file
