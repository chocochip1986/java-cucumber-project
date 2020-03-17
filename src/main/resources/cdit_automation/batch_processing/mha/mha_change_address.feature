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
    | john   | Existing:abd,AddrType:C                                                                                    | IndType:Z,AddrType:C,Block:12B,Street:16 Sheraton Street,Unit:13,Floor:12,Building:The Clive,Postal:232902 | 20190909           |
    | john   | Existing:abc,AddrType:C                                                                                    | IndType:Z,AddrType:C,Block:12B,Street:16 Sheraton Street,Unit:13,Floor:12,Building:The Clive,Postal:232902 | 20190909           |
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status CLEANUP
    And the error message contains Must have at least 1 valid body record

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
    And the Mha Change Address batch job completes running with status CLEANUP
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
    And the Mha Change Address batch job completes running with status CLEANUP
    And the error message contains Invalid validity of old address
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
    And the Mha Change Address batch job completes running with status CLEANUP
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
    And the Mha Change Address batch job completes running with status CLEANUP

  @set_8
  Scenario: A person changes address every day
    Given A 60 year old singaporean person john owns a landed property abc
    And john owns a landed property abd
    And john resides in a condo property abe
    And mha change address file states that john moved from abe to abd 6 days ago
    And the mha change address file contains information that john changed from (mha_z)abd to (mha_z)abc 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status CLEANUP
    And there are no error messages
    Then john does not reside in abd since 6 days ago
    And john resides in abc from 5 days ago

  @set_9
  Scenario: John moves to an existing address that Jane is living in
    Given A 30 year old singaporean person john owns a landed property abc
    And john resides in a hdb property abd
    And A 28 year old singaporean person jane resides a condo property abe
    And the mha change address file contains information that john changed from (mha_z)abd to (mha_z)abe 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status CLEANUP
    And there are no error messages
    Then john does not reside in abd since 6 days ago
    And john resides in abe from 5 days ago

  @set_10
  Scenario: John moves back to the same address
    Given A 60 year old singaporean person john owns a hdb property abc
    And john resides in a condo property abd
    And mha change address file states that john moved from abd to abc 6 days ago
    And the mha change address file contains information that john changed from (mha_z)abc to (mha_z)abd 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status CLEANUP
    And there are no error messages
    Then john does not reside in abc since 6 days ago
    And john resides in abd from 5 days ago

  @set_11
  Scenario: Person has changed his address many times in the past and mha tells us that he change address again
    Given A 30 year old singaporean person john resides a hdb property abc
    And john had lived in a hdb property abd from 12 Jan 2000 to 12 Jan 2015
    And john had lived in a hdb property abf from 13 Feb 2015 to 12 Jan 2019
    And the mha change address file contains information that john changed from (mha_z)abc to a new (mha_z)landed property 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status CLEANUP
    And there are no error messages
    Then john does not reside in abc since 6 days ago

  @set_12
  Scenario: John moves to an existing special property address
    Given A 60 year old singaporean person john resides a hdb property abc
    And A 40 year old singaporean person jane resides in a lorong_buangkok property abe
    And the mha change address file contains information that john changed from (mha_z)abc to (mha_z)abe 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status CLEANUP
    And there are no error messages
    Then john does not reside in abc since 6 days ago
    And john resides in the island special property

  @set_13
  Scenario Outline: John moves to a new special property address
    Given A 60 year old singaporean person john resides a hdb property abc
    And the mha change address file contains information that john changed from (<prev_address_indicator>)abc to a new (<cur_address_indicator>)lorong_buangkok property 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status CLEANUP
    And there are no error messages
    Then john does not reside in abc since 6 days ago
    And john resides in the island special property
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