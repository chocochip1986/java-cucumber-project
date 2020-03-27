@development
@datasourceui
@revalidate_file
Feature: Re-validate file

  @set_1
  Scenario: CPF officer overwrites system to load a file that exceeded error threshold
    Given There are 50 files that were previously processed by Datasource
    Given There is a MHA BULK CITIZEN file, processed 40 days ago, exceeded the error threshold by 40%
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    And I search for the file