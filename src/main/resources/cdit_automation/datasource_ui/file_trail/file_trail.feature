@development
@datasourceui
@file_trail
Feature: File Trail

  @set_1
  Scenario Outline: CPF officer accesses file trail page for various files
    Given There are 50 files that were previously processed by Datasource
    Given There is a MHA BULK CITIZEN file at <currentStep> step with <currentStatus> status processed 40 days ago
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
    Given There is a MHA BULK CITIZEN file at Load step with Pending status processed 40 days ago
    And the below MHA BULK CITIZEN reasonableness statistics with CLEANUP status inserted previously with year - 1 month + 0 day + 0
      | data_item             | data_item_value |
      | No of new 13 year old | 100             |
    And the below MHA BULK CITIZEN reasonableness statistics with CLEANUP status inserted previously with year - 2 month + 0 day + 0
      | data_item             | data_item_value |
      | No of new 13 year old | 200             |
    And the below MHA BULK CITIZEN reasonableness statistics with CLEANUP status inserted previously with year - 3 month + 0 day + 0
      | data_item             | data_item_value |
      | No of new 13 year old | 300             |
    And the below MHA BULK CITIZEN reasonableness statistics with CLEANUP status inserted previously with year - 4 month + 0 day + 0
      | data_item             | data_item_value |
      | No of new 13 year old | 400             |
    Given I am a CPF officer
    And I login to CDS Intranet as a CPF officer
    And I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    And I search for the file
    Then I verify that I see the file trail page
    And I click on the reasonableness trending link
    And I logout of CDS Intranet

  @set_3
  Scenario: CPF officer observe the mha new citizen file ingested successfully
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN       | NAME   | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR            | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | T4669435F | G4562923L | I AM A | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | S7536226I | G4345241Q | I AM B | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | T2146457G | F5520667T | I AM C | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | S1039739A | F3703726P | I AM D | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | S1630092F | F0075695N | I AM E | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | S1019101G | G6307824M | I AM F | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | T0575876E | F3170574Q | I AM G | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | T4893862G | F3424242U | I AM H | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | T5013175G | G2772450M | I AM I | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | S1366411J | G7491416R | I AM J | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And I login to CDS Intranet as a CPF officer
    Then I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    Then the Mha new citizen batch job completes running with status CLEANUP
    And I search for the file
    Then I verify that I see the file trail page
    Then I verify 12 records passed format validation
    Then I verify 12 records passed content validation
    And I logout of CDS Intranet

  @set_4
  Scenario: CPF officer observe the mha new citizen file has full format error
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC | FIN       | NAME   | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR            | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | T    | G4562923L | I AM A | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | S    | G4345241Q | I AM B | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | T    | F5520667T | I AM C | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | S    | F3703726P | I AM D | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | S    | F0075695N | I AM E | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | S    | G6307824M | I AM F | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | T    | F3170574Q | I AM G | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | T    | F3424242U | I AM H | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | T    | G2772450M | I AM I | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | S    | G7491416R | I AM J | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And I login to CDS Intranet as a CPF officer
    Then I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    Then the Mha new citizen batch job completes running with status RAW_DATA_ERROR
    And I search for the file
    Then I verify that I see the file trail page
    Then I verify 10 records failed format validation
    And I logout of CDS Intranet

  @set_5
  Scenario: CPF officer observe the mha new citizen file has full content error
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN       | NAME   | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR            | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | T4669435F | G4562923L | I AM A | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 99990115     |
      | S7536226I | G4345241Q | I AM B | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 99990115     |
      | T2146457G | F5520667T | I AM C | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 99990115     |
      | S1039739A | F3703726P | I AM D | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 99990115     |
      | S1630092F | F0075695N | I AM E | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 99990115     |
      | S1019101G | G6307824M | I AM F | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 99990115     |
      | T0575876E | F3170574Q | I AM G | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 99990115     |
      | T4893862G | F3424242U | I AM H | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 99990115     |
      | T5013175G | G2772450M | I AM I | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 99990115     |
      | S1366411J | G7491416R | I AM J | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 99990115     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And I login to CDS Intranet as a CPF officer
    Then I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    Then the Mha new citizen batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And I search for the file
    Then I verify that I see the file trail page
    Then I verify 12 records passed format validation
    Then I verify 10 records failed content validation
    And I logout of CDS Intranet

  @set_6
  Scenario: CPF officer observe the mha new citizen file has partial format and content errors
    Given a MHA_NEW_CITIZEN file with the following details
      | NRIC      | FIN       | NAME   | DOB      | GENDER | OLD_ADDR_IND | OLD_ADDR_TYPE | OLD_ADDR | NEW_ADDR_IND | NEW_ADDR_TYPE | NEW_ADDR            | NEW_INVALID_ADDR_TAG | DATE_OF_ADDR_CHANGE | CTZ_ATT_DATE |
      | T         | G4562923L | I AM A | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | S         | G4345241Q | I AM B | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | T         | F5520667T | I AM C | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | S         | F3703726P | I AM D | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | S1630092F | F0075695N | I AM E | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 99990115     |
      | S1019101G | G6307824M | I AM F | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 99990115     |
      | T0575876E | F3170574Q | I AM G | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 99990115     |
      | T4893862G | F3424242U | I AM H | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 99990115     |
      | T5013175G | G2772450M | I AM I | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
      | S1366411J | G7491416R | I AM J | 20200101 | F      | C            | C             | Hotel 81 | C            | C             | The Fullerton Hotel |                      | 20200201            | 20200115     |
    When MHA sends the MHA_NEW_CITIZEN file to Datasource sftp for processing
    And I login to CDS Intranet as a CPF officer
    Then I access Datasource UI Files Dashboard function
    Then I should see that there are files displayed
    Then the Mha new citizen batch job completes running with status CLEANUP
    And I search for the file
    Then I verify that I see the file trail page
    Then I verify 8 records passed format validation
    Then I verify 4 records failed format validation
    Then I verify 4 records passed content validation
    Then I verify 4 records failed content validation
    And I logout of CDS Intranet