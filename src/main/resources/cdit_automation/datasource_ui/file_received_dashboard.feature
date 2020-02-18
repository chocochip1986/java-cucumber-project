@development
@datasourceui
Feature: Datasource UI file received dashboard

  Scenario: CPF officer accesses the file received dashboard
    Given There are 10 files that were previously processed by Datasource
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    When I click on a file to access it's file trail
    Then I see the File Trail of the file
    When I click on the back button
    Then I should see the Files Dashboard
