@development
@datasourceui
Feature: Datasource UI file received dashboard

  Scenario: CPF officer accesses the file received dashboard
    Given There are 10 files that were previously processed by Datasource
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
