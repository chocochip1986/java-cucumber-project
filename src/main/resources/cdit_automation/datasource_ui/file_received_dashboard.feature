@development
@datasourceui
Feature: Datasource UI file received dashboard

  @set_1
  Scenario: CPF officer accesses the file received dashboard
    Given There are 64 files that were previously processed by Datasource
    And There is a MHA DEATH DATE file at Load step with Follow-up status processed 40 days ago
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    When I search for the file
    Then I verify that I see the file trail page
    When I click on the back button
    Then I should see the Files Dashboard
    Then I logout of CDS Intranet

  @set_2
  Scenario: CPF officer should see correct number of files on each page when accessing the file received dashboard
    Given There are 50 files that were previously processed by Datasource
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    Then I should see that there are 10 rows displayed

  @set_3
  Scenario: CPF officer changes the number of items on page
    Given There are 50 files that were previously processed by Datasource
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    Then I select items per page to be 3
    Then I should see that there are 3 rows displayed

  @set_4
  Scenario: CPF officer accesses file received dashboard containing records of all current status type
    Given There is a file of each current status type processed by Datasource
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    Then I should see that there are 6 rows displayed
    Then The records should be displayed with the correct current status

  @set_5
  Scenario: CPF officer views file dashboard when it is empty
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I verify that there are no files displayed