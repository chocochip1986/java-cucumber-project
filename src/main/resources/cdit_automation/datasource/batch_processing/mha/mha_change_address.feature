@datasource
@development
@change_address
@mha
@truncate
Feature: MHA Change address

  @set_1
  Scenario: MHA sends an empty change address file
    Given the mha change address file is empty
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status FILE_ERROR
    And the error message contains Must have at least 1 valid body record

  @set_2
  Scenario: CDS fails to processes a MHA change address file with partial duplicated records of same NRIC but different current address
    Given A singaporean person john resides in a landed property abc
    Given A singaporean person johnson resides in a hdb property def
    And the mha change address file contains the following details:
    | person | previous_address        | current_address                                                                                            | address_change_dte |
    | john   | Existing:abc,AddrType:C | IndType:Z,AddrType:C,Block:12B,Street:16 Sheraton Street,Unit:13,Floor:12,Building:The Clive,Postal:232902 | 20190909           |
    | john   | Existing:abc,AddrType:C | IndType:Z,AddrType:C,Block:14B,Street:22 Jalan Besar,Unit:14,Floor:40,Building:City View,Postal:232902     | 20190909           |
    | johnson| Existing:def,AddrType:C | IndType:Z,AddrType:C,Block:123,Street:22 Jalan Jalan,Unit:12,Floor:20,Building:City View,Postal:452932     | 20190909           |
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status ERROR_RATE_ERROR
    And the error message contains Partially Duplicate Record found.

  @set_3
  Scenario: CDS processes a MHA change address file with full duplicated records of same NRIC and current address
    Given A singaporean person john resides in a landed property abc
    And A singaporean person jane resides in a condo property efg
    And the mha change address file contains the following details:
      | person | previous_address        | current_address                                                                                            | address_change_dte |
      | jane   | Existing:efg,AddrType:C | IndType:Z,AddrType:C,Block:13C,Street:22 Hilton Street,Unit:22,Floor:32,Building:The Sail,Postal:232903    | 20190909           |
      | john   | Existing:abc,AddrType:C | IndType:Z,AddrType:C,Block:12B,Street:16 Sheraton Street,Unit:13,Floor:12,Building:The Clive,Postal:232902 | 20190909           |
      | john   | Existing:abc,AddrType:C | IndType:Z,AddrType:C,Block:12B,Street:16 Sheraton Street,Unit:13,Floor:12,Building:The Clive,Postal:232902 | 20190909           |
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status CLEANUP
    And the error message contains Completely Duplicate Record found.

  @set_4
  Scenario: CDS fails to processes a MHA change address file with both full and partial duplicated records for same NRIC
    Given A singaporean person john resides in a landed property abc
    Given A singaporean person ahmeng resides in a landed property abc
    And the mha change address file contains the following details:
      | person | previous_address        | current_address                                                                                            | address_change_dte |
      | john   | Existing:abc,AddrType:C | IndType:Z,AddrType:C,Block:12B,Street:16 Sheraton Street,Unit:13,Floor:12,Building:The Clive,Postal:232902 | 20190909           |
      | john   | Existing:abc,AddrType:C | IndType:Z,AddrType:C,Block:12B,Street:16 Sheraton Street,Unit:13,Floor:12,Building:The Clive,Postal:232902 | 20190909           |
      | john   | Existing:abc,AddrType:C | IndType:Z,AddrType:C,Block:14B,Street:22 Jalan Besar,Unit:14,Floor:40,Building:City View,Postal:232902     | 20190909           |
      | ahmeng | Existing:abc,AddrType:C | IndType:Z,AddrType:C,Block:14B,Street:22 Jalan Besar,Unit:14,Floor:40,Building:City View,Postal:232902     | 20190909           |
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status ERROR_RATE_ERROR
    And the error message contains Partially Duplicate Record found.

  @set_5 @defect @RVC-148
  Scenario Outline: CDS successfully updates address information to a non existent address
    Given A 60 year old singaporean person john owns a condo property abc
    And john owns a landed property abd
    And john resides in a condo property abe
    And the mha change address file contains information that john changed from (<prev_address_indicator>)abe to a new (<cur_address_indicator>)condo property 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status CLEANUP
    And there are no error messages
    Then john does not reside in abe since 6 days ago
    And john resides in the new condo from 5 days ago
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

  @set_6 @Defect @Rvc-110
  Scenario Outline: CDS successfully updates address information to an existing address
    Given A singaporean person john owns and resides a landed property abc
    And john owns a landed property abd
#    And john resides in a condo property abe
    And the mha change address file contains information that john changed from (<prev_address_indicator>)abc to (<cur_address_indicator>)abd 5 days ago
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

  @set_6
  Scenario Outline: MHA claims john changes address from an existing property which he does not live in
    Given A singaporean person john owns a landed property abc
    Given A singaporean person jane owns a landed property abf
    And john owns a landed property abd
    And john resides in a condo property abe
    And the mha change address file contains information that john changed from (<prev_address_indicator>)abf to (<cur_address_indicator>)abd 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And the error message contains Invalid Person Property association
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
  Scenario Outline: CDS is unable to map a person's previous address if it does not exist in the system
    Given A singaporean person john owns a landed property abc
    And john owns a landed property abd
    And john resides in a condo property abe
    And the mha change address file contains information that john changed from a new (<prev_address_indicator>)hdb property to (<cur_address_indicator>)abc 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status VALIDATED_TO_PREPARED_ERROR
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

  @set_8
  Scenario Outline: CDS is unable to map a persons' addresses when both are non-existent
    Given A singaporean person john owns a landed property abc
    And john owns a landed property abd
    And john resides in a condo property abe
    And the mha change address file contains information that john changed from a new (<prev_address_indicator>)condo property to a new (<cur_address_indicator>)landed property 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status VALIDATED_TO_PREPARED_ERROR
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

  @set_9
  Scenario: Change address file states a previous address that a person once lived in
    Given A 60 year old singaporean person john owns a landed property abc
    And john owns a landed property abd
    And john resides in a condo property abe
    And john resided in a hdb property xyz 100 days ago
    And the mha change address file contains information that john changed from (mha_z)xyz to (mha_z)abd 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status CLEANUP

  @set_10
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

  @set_1
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

  @set_2
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

  @set_3
  Scenario: Person has changed his address many times in the past and mha tells us that he change address again
    Given A 30 year old singaporean person john resides a hdb property abc
    And john had lived in a hdb property abd from 12 Jan 2000 to 12 Jan 2015
    And john had lived in a hdb property abf from 13 Feb 2015 to 12 Jan 2019
    And the mha change address file contains information that john changed from (mha_z)abc to a new (mha_z)landed property 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status CLEANUP
    And there are no error messages
    Then john does not reside in abc since 6 days ago

  @set_4
  Scenario: John moves to an existing special property address
    Given A 60 year old singaporean person john resides a hdb property abc
    And A 40 year old singaporean person jane resides in a lorong_buangkok property abe
    And the mha change address file contains information that john changed from (mha_z)abc to (mha_z)abe 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status CLEANUP
    And there are no error messages
    Then john does not reside in abc since 6 days ago
    And john resides in the lorong buangkok special property

  @set_5 @defect @RVC115
  Scenario Outline: John moves to a new special property address
    Given A 60 year old singaporean person john resides a hdb property abc
    And the mha change address file contains information that john changed from (<prev_address_indicator>)abc to a new (<cur_address_indicator>)lorong_buangkok property 5 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status CLEANUP
    And there are no error messages
    Then john does not reside in abc since 6 days ago
    And john resides in the new lorong buangkok special property
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
    Scenario: john has same old and new address
      Given A singaporean person john owns a landed property abc
      And john resides in a condo property abe
      And the mha change address file contains information that john changed from (mha_z)abe to (mha_z)abe 5 days ago
      When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
      And the Mha Change Address batch job completes running with status CLEANUP
      And the error message contains Old and new address are identical

    @set_7
    Scenario: john move back to his first residence address
      Given A singaporean person john resides a landed property abc
      And john had lived in a hdb property abd from 12 Jan 2000 to 12 Jan 2015
      And the mha change address file contains information that john changed from (mha_z)abc to (mha_z)abd 5 days ago
      When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
      And the Mha Change Address batch job completes running with status CLEANUP
      And there are no error messages


  @set_8
  Scenario: MHA sends two files which claims John moved from bbb to ccc and subsequently aaa to bbb
    Given A 30 year old singaporean person john resides a hdb property aaa
    Given A 30 year old singaporean person brandon resides a hdb property bbb
    Given A 30 year old singaporean person charlie resides a hdb property ccc
    And the mha change address file with date of run 20200101 contains information that john changed from (mha_z)bbb to (mha_z)ccc 365 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And the error message contains Invalid Person Property association.
    And I will receive another file
    And the mha change address file with date of run 20200102 contains information that john changed from (mha_z)aaa to (mha_z)bbb 367 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status CLEANUP
    Then john does not reside in aaa since 368 days ago

  @set_9
  Scenario: MHA sends two files which claims John moved from bbb to ccc and subsequently aaa to bbb
    Given A 30 year old singaporean person john resides a hdb property aaa
    Given A 30 year old singaporean person brandon resides a hdb property bbb
    And the mha change address file with date of run 20200101 contains information that john changed from (mha_z)bbb to (mha_z)ccc 365 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And the error message contains Invalid Person Property association.
    And I will receive another file
    And the mha change address file with date of run 20200102 contains information that john changed from (mha_z)aaa to (mha_z)bbb 367 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status CLEANUP
    Then john does not reside in aaa since 368 days ago


  @set_10
  Scenario: MHA sends two files which claims John moved from aaa to bbb and subsequently bbb to ccc at an earlier date
    Given A 30 year old singaporean person john resides a hdb property aaa
    Given A 30 year old singaporean person brandon resides a hdb property bbb
    Given A 30 year old singaporean person charlie resides a hdb property ccc
    And the mha change address file with date of run 20200101 contains information that john changed from (mha_z)aaa to (mha_z)bbb 365 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status CLEANUP
    And I will receive another file
    And the mha change address file with date of run 20200102 contains information that john changed from (mha_z)bbb to (mha_z)ccc 367 days ago
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status MAPPED_DATA

  @set_1
  Scenario: Given address indicator is MHA but Address format is NCA, and vice versa
    Given A singaporean person john resides in a landed property abc
    Given A singaporean person jane resides in a landed property def
    And the mha change address file contains the following details:
      | person | previous_address        | current_address                                                                                            | address_change_dte |
      | john   | Existing:abc,AddrType:C | IndType:Z,AddrType:S,Block:13C,Street:22 Hilton Street,Unit:22,Floor:32,Building:The Sail,Postal:232903    | 20190909           |
      | jane   | Existing:def,AddrType:C | IndType: ,AddrType:C,Block:13C,Street:22 Hilton Street,Unit:22,Floor:32,Building:The Sail,Postal:232903    | 20190909           |
    When MHA sends the MHA_CHANGE_ADDRESS file to Datasource sftp for processing
    And the Mha Change Address batch job completes running with status RAW_DATA_ERROR

