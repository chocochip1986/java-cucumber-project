@development
@datasourceui
@truncate
Feature: CDS Authentication

  @set_1
  Scenario: CPF officer attempts to assess previous session by using browser back
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I logout of CDS Intranet
    When I click the browser back button
    Then I should remain as logged out