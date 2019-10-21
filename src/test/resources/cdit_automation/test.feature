Feature: This is a test feature

  @Set_1
  Scenario: This is a test scenario
    Given I love "vanilla" ice cream
    And I have 2 of them
    And Each cost $25.50
    Then My identifier is H&^&^*&

  @Set_2
  Scenario: This is a test scenario using a table
    Given the following animals:
    | cow   |
    | horse |
    | tiger |
    | rat   |

  @Set_3
  Scenario: Googling stuff
    Given I access the google search engine
    And I type "vanilla ice cream" into the search bar
    And I begin searching
    And I verify that the search is done

  @Set_4
  Scenario: Tab navigation
    Given I access the google search engine
    And I open a new tab
    And I close current tab

  @Set_5
  Scenario: An SC person
    Given There is an SC person
    Given There is a FR person
    Given There is a PR person