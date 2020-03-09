@development
@change_address
@mha
Feature: MHA Change address

  @set_1
  Scenario: MHA sends an empty change address file
    Given the mha change address file is empty
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status FILE_ERROR
    And the error message contains Must have at least 1 valid body record

  @set_2
  Scenario: CDS successfully processes a MHA change address file
    Given A singaporean person john owns a landed property abc
    And john owns a landed property abd
    And john resides in a condo property abe
    And the mha change address file contains the following details:
    | person | previous_address                                                                                           | current_address                                                                                            | address_change_dte |
    | john   | abc                                                                                                        | IndType:Mha, Block:12B, Street:16 Sheraton Street, Unit:13, Floor:12, Building:The Clive, Singapore:232902 | 20190909 |
    | john   | IndType:Mha, Block:12B, Street:16 Sheraton Street, Unit:13, Floor:12, Building:The Clive, Singapore:232902 | IndType:Mha, Block:12B, Street:16 Sheraton Street, Unit:13, Floor:12, Building:The Clive, Singapore:232902 | 20190909 |
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP

  @set_3
  Scenario: CDS successfully updates address information to a non existent address
#    Given A singaporean person john owns a landed property abc
#    And john owns a landed property abd
#    And john resides in a condo property abe
#    And the mha change address file contains information that john changed from (nca)abe to (mha_z)new hdb property 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP

  @set_4
  Scenario: CDS successfully updates address information to an existing address
    Given A singaporean person john owns a landed property abc
    And john owns a landed property abd
    And john resides in a condo property abe
    And the mha change address file contains information that john changed from (mha_z)abe to (mha_z)abd 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP

  @set_5
  Scenario: CDS is unable to map a person's previous address if it does not exist in the system
    Given A singaporean person john owns a landed property abc
    And john owns a landed property abd
    And john resides in a condo property abe
    And the mha change address file contains information that john changed from (nca)abc to a new (nca)landed property 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP

  @set_5
  Scenario: Change address file states a previous address that a person once lived in
    Given A singaporean person john owns a landed property abc
    And john owns a landed property abd
    And john resides in a condo property abe
#    And the mha change address file contains information that john changed from abc to a new executive_condo property
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP