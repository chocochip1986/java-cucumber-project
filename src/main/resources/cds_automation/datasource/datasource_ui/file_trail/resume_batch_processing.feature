@datasource
@development
@datasourceui
@revalidate_file
@truncate
Feature: Re-validate file

#  @set_1
#  Scenario: CPF officer overwrites system to load a file that exceeded error threshold
#    Given There are 50 files that were previously processed by Datasource
#    Given There is a MHA BULK CITIZEN file, processed 40 days ago, exceeded the error threshold by 40%
#    Given I am a CPF officer
#    And I login to CDS Intranet as a CPF officer
#    And I access Datasource UI Files Dashboard function
#    Then I should see that there are files displayed
#    And the current status of the file in the Dashboard is Urgent Action : Exceeded Error Rate
#    And I search for the file
#    Then I verify that I see the file trail page
#    Then I verify that I see the revalidate without error rate button
#    When I click on the revalidate without error rate button
#    Then I should see the Files Dashboard
#    Then I should see that there are files displayed
#    And the current status of the file in the Dashboard is Pending : Processing
#    And I logout of CDS Intranet

  @set_1
  Scenario: CPF officer overwrites system to load a file that exceeded error threshold
    Given There are 20 files that were previously processed by Datasource
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN       | NAME   | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR            | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | T         | G4562923L | I AM A | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | S         | G4345241Q | I AM B | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | S1630092F | F0075695N | I AM C | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 99990115     |
      | S1019101G | G6307824M | I AM D | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 99990115     |
      | T0575876E | F3170574Q | I AM E | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 99990115     |
      | T4893862G | F3424242U | I AM F | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 99990115     |
      | T5013175G | G2772450M | I AM G | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | S1366411J | G7491416R | I AM H | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And the Mha new citizen batch job completes running with status ERROR_RATE_ERROR
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    And I search for the file
    Then I verify that I see the file trail page
    Then I verify that I see the revalidate without error rate button
    When I click on the revalidate without error rate button
    Then I should see the Files Dashboard
    Then I should see that there are files displayed
    And the current status of the file in the Dashboard is Pending : Processing
    And I logout of CDS Intranet