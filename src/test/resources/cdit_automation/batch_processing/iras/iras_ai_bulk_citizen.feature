@development
@bulk_citizen
@iras
Feature: Data processing for IRAS bulk citizen file

  @set_1 @wip
  Scenario: Datasource service processes a IRAS bulk citizen file
    Given there are 10 Singaporeans
    Given there are 10 Singaporeans who passed away within the year 2019
    Given there are 10 Singaporeans who passed away within the year 2018
    Given there are 10 Singaporeans who passed away within the year 2017
    Given there are 10 Singaporeans who passed away within the year 2016
    Given there are 10 Singaporeans who passed away within the year 2015
    Given there are 10 Singaporeans who passed away within the year 2014
    When Datasource is triggered to generate the IRAS AI Bulk file
    Then the IRAS AI bulk file should be created
