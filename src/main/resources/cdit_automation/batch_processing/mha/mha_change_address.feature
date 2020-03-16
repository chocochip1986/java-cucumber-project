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
  Scenario Outline: CDS successfully updates address information to a non existent address
    Given A 60 year old singaporean person john owns a landed property abc
    And john owns a landed property abd
    And john resides in a condo property abe
    And the mha change address file contains information that john changed from (<prev_address_indicator>)abe to a new (<cur_address_indicator>)hdb property 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status CLEANUP
    And there are no error messages
    Then john does not reside in abe since 6 days ago
  Examples:
    | prev_address_indicator | cur_address_indicator |
    | mha_z                  | mha_z                 |
    | mha_z                  | mha_c                 |
    | mha_z                  | nca                   |
    | mha_c                  | mha_z                 |
    | mha_c                  | mha_c                 |
    | mha_c                  | nca                   |
    | nca                    | mha_z                 |
    | nca                    | mha_c                 |
    | nca                    | nca                   |

  @set_4
  Scenario Outline: CDS successfully updates address information to an existing address
    Given A singaporean person john owns a landed property abc
    And john owns a landed property abd
    And john resides in a condo property abe
    And the mha change address file contains information that john changed from (<prev_address_indicator>)abe to (<cur_address_indicator>)abd 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP
    And there are no error messages
    Then john resides in abd from 5 days ago
  Examples:
    | prev_address_indicator | cur_address_indicator |
    | mha_z                  | mha_z                 |
    | mha_z                  | mha_c                 |
    | mha_z                  | nca                   |
    | mha_c                  | mha_z                 |
    | mha_c                  | mha_c                 |
    | mha_c                  | nca                   |
    | nca                    | mha_z                 |
    | nca                    | mha_c                 |
    | nca                    | nca                   |

  @set_5
  Scenario Outline: CDS is unable to map a person's previous address if it does not exist in the system
    Given A singaporean person john owns a landed property abc
    And john owns a landed property abd
    And john resides in a condo property abe
    And the mha change address file contains information that john changed from a new (<prev_address_indicator>)hdb property to (<cur_address_indicator>)abc 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP
    And the error message contains <any>
    Examples:
      | prev_address_indicator | cur_address_indicator |
      | mha_z                  | mha_z                 |
      | mha_z                  | mha_c                 |
      | mha_z                  | nca                   |
      | mha_c                  | mha_z                 |
      | mha_c                  | mha_c                 |
      | mha_c                  | nca                   |
      | nca                    | mha_z                 |
      | nca                    | mha_c                 |
      | nca                    | nca                   |

  @set_6
  Scenario Outline: CDS is unable to map a persons' addresses when both are non-existent
    Given A singaporean person john owns a landed property abc
    And john owns a landed property abd
    And john resides in a condo property abe
    And the mha change address file contains information that john changed from a new (<prev_address_indicator>)abc property to a new (<cur_address_indicator>)landed property 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP
    And the error message contains <any>
    Examples:
      | prev_address_indicator | cur_address_indicator |
      | mha_z                  | mha_z                 |
      | mha_z                  | mha_c                 |
      | mha_z                  | nca                   |
      | mha_c                  | mha_z                 |
      | mha_c                  | mha_c                 |
      | mha_c                  | nca                   |
      | nca                    | mha_z                 |
      | nca                    | mha_c                 |
      | nca                    | nca                   |

  @set_7
  Scenario: Change address file states a previous address that a person once lived in
    Given A 60 year old singaporean person john owns a landed property abc
    And john owns a landed property abd
    And john resides in a condo property abe
    And john resided in a hdb property xyz 100 days ago
    And the mha change address file contains information that john changed from (mha_z)xyz to (mha_z)abd 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP

  @set_9
  Scenario: A person changes address every day
    Given A 60 year old singaporean person john owns a landed property abc
    And john owns a landed property abd
    And john resides in a condo property abe
    And mha change address file states that john moved from abe to abd 6 days ago
    And the mha change address file contains information that john changed from (mha_z)abd to (mha_z)abc 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Dual Citizen batch job completes running with status CLEANUP
    And there are no error messages
    And john resides in abc from 5 days ago