@development
@datasourceui
Feature: Datasource UI file received dashboard

  @set_1
  Scenario: CPF officer accesses the file received dashboard
    Given There are 50 files that were previously processed by Datasource
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    When I click on a file to access it's file trail
    Then I see the File Trail of the file
    When I click on the back button
    Then I should see the Files Dashboard

  Scenario: CPF officer should see correct number of files on each page when accessing the file received dashboard
    Given There are 50 files that were previously processed by Datasource
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    Then I should see that there are 10 rows displayed

  Scenario: CPF officer changes the number of items on page
    Given There are 50 files that were previously processed by Datasource
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    Then I select items per page to be 3
    Then I should see that there are 3 rows displayed

  Scenario: CPF officer accesses file received dashboard containing records of all current status type
    Given There is a file of each current status type processed by Datasource
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    Then I should see that there are 6 rows displayed
    Then The records should be displayed with the correct current status