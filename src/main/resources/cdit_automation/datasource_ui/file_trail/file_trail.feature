@development
@datasourceui
@file_trail
Feature: File Trail

  @set_1
  Scenario Outline: CPF officer accesses file trail page for various files
    Given There are 50 files that were previously processed by Datasource
    And there is a MHA BULK CITIZEN file at <currentStep> step with <currentStatus> status processed 40 days ago
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    And I search for the file
    Then I verify that I see the file trail page
    And I logout of CDS Intranet
    Examples:
      | currentStep | currentStatus |
      | Format      | Pending       |
      | Format      | Follow-up     |
      | Format      | Urgent Action |
      | Content     | Pending       |
      | Content     | Follow-up     |
      | Load        | Pending       |
      | Load        | Follow-up     |
      | Load        | Urgent Action |

  @set_2
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