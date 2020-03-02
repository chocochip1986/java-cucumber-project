@development
@datasourceui
@trending_records
Feature: Details of Trending Records for files

  @set_1
  Scenario: CPF officer accesses file trail page
    Given There are 50 files that were previously processed by Datasource
    And there is a MHA BULK CITIZEN file at Load step with Pending status processed 40 days ago
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    And I search for the file
    Then I verify that I see the file trail page
    And I click on the reasonableness trending link
    And I logout of CDS Intranet