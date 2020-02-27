@development
@datasourceui
@file_trail
Feature: File Trail

  @set_1
  Scenario: CPF officer accesses file trail page
    Given There are 50 files that were previously processed by Datasource
    And there is a MHA BULK CITIZEN FILE at 
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
