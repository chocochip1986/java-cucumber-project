@development
@datasourceui
@file_trail
Feature: File Trail

  @set_1
  Scenario: CPF officer accesses file trail page
    Given There are 50 files that were previously processed by Datasource
    And there is a MHA BULK CITIZEN file at Load step with Urgent Action status processed 40 days ago
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    And I search for the file
    Then I verify that I see the file trail page
    And I logout of CDS Intranet
