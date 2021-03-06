@datasource
@development
@ceased_citizen
@mha
@truncate
Feature: Data processing for Mha ceased citizenship

  @set_1
  Scenario: Mha send a ceased citizenship file with a person detail record not found in system
    Given the file has the following details with Header date of run valid
      | CeasedCitizen |
      | 1             |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And I verify that the following error message appeared:
      | Message                   | Count |
      | NRIC not found in System. | 1     |

  @set_2
  Scenario: Mha send a ceased citizenship file with a record that was sent previously 
    Given the database populated with the following data:
      | SingaporeCitizen | DualCitizen | CeasedCitizen |
      | 1                | 0           | 1             |
    And the file has the following details with Header date of run valid
      | RepeatedCeasedCitizen |
      | 1                     |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And I verify that the following error message appeared:
      | Message                           | Count |
      | Not SC or Dual Citizen currently. | 1     |

  @set_3
  Scenario: Mha send a ceased citizenship file with completely duplicate records
    Given the database populated with the following data:
      | SingaporeCitizen | DualCitizen |
      | 1                | 0           |
    And the file has the following details with Header date of run valid
      | CeasedCitizen | NumberOfDuplication |
      | 1             | 3                   |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status CLEANUP
    And I verify that the following error message appeared:
      | Message                            | Count |
      | Completely Duplicate Record found. | 3     |
    And I verify that the correct persons have ceased being singaporean

  @set_4 @defect @GRYFFINDOR-1276 @GRYFFINDOR-1289
  Scenario: Mha send a ceased citizenship file with a record existing in the system and also duplicated in the file
    Given the database populated with the following data:
      | SingaporeCitizen | CeasedCitizen |
      | 1                | 1             |
    And the file has the following details with Header date of run valid
      | RepeatedCeasedCitizen | NumberOfDuplication |
      | 1                     | 2                   |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And I verify that the following error message appeared:
      | Message                            | Count |
      | Not SC or Dual Citizen currently.  | 2     |
      | Completely Duplicate Record found. | 2     |

  @set_5
  Scenario: Mha send a ceased citizenship file with a name length of zero
    Given the file has the following details with Header date of run valid
      | EmptyName |
      | 1         |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status RAW_DATA_ERROR
    And I verify that the following error message appeared:
      | Message       | Count |
      | Invalid Name. | 1     |

  @set_6
  Scenario: Mha send a ceased citizenship file with an renunciation date that is after cut off date
    Given the file has the following details with Header date of run valid
      | RenunciationDateAfterCutOff |
      | 2                           |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And I verify that the following error message appeared:
      | Message                                       | Count |
      | Renunciation Date is after File Date of run.  | 2     |

  @set_7
  Scenario: Mha send a ceased citizenship file for processing
    Given the database populated with the following data:
      | SingaporeCitizen | DualCitizen |
      | 1                | 1           |
    And the file has the following details with Header date of run valid
      | CeasedCitizen |
      | 2             |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status CLEANUP
    And I verify the the people listed in the file have nationality of NON_SINGAPORE_CITIZEN
    And I verify the previous nationality valid till timestamp is the day before renunciation date at 2359HR
    And I verify the supersede nationality valid from timestamp is the day after renunciation date

  @set_8
  Scenario: Mha send a ceased citizenship file with nationality of [SG]

  This is a special case that will never happen because this file is for renunciation of
  singapore citizenship, not award singapore citizenship. Therefore, datasource will
  ignore/skip if come across it.

    Given the database populated with the following data:
      | SingaporeCitizen |
      | 1                |
    And the file has the following details with Header date of run valid
      | AwardedSingaporeCitizen |
      | 1                       |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status RAW_DATA_ERROR

  @set_9
  Scenario: Mha send a empty ceased citizenship file
    Given the file has the following details with Header date of run valid
      | CeasedCitizen |
      | 0             |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status FILE_ERROR
    And the error message contains Must have at least 1 valid body record

  @set_10
  Scenario: Mha sends a ceased citizenship with an SG country code
    Given the file has the following details with Header date of run valid
      | CeasedCitizenWithSGCountryCode |
      | 1                              |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status RAW_DATA_ERROR
    And I verify that the following error message appeared:
      | Message                                 | Count |
      | Nationality must not be SG country code | 1     |

  @set_1
  Scenario: Mha sends a ceased citizenship for a citizen would was previously a dual citizen
    Given that john was convert from a dual citizen to a singaporean 10 days ago
    And john's citizenship ceased 9 days ago
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status CLEANUP
    And I verify that john is not a citizen 9 days ago

  @set_2
  Scenario: Mha sends a ceased citizenship file containing a person who is already non Singaporean
    Given the file has the following details with Header date of run valid
      | CeasedCitizenWhoIsNonSG |
      | 1                       |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And I verify that the following error message appeared:
      | Message                                 | Count |
      | Not SC or Dual Citizen currently.       | 1     |

  @set_3
  Scenario: Mha send a ceased citizenship file with partially duplicate records
    Given the database populated with the following data:
      | SingaporeCitizen | DualCitizen |
      | 1                | 0           |
    And the file has the following details with Header date of run valid
      | CeasedCitizen | NumberOfPartialDuplication |
      | 1             | 3                          |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And I verify that the following error message appeared:
      | Message                            | Count |
      | Partially Duplicate Record found.  | 3     |

  @set_4
  Scenario Outline: Mha send a ceased citizenship file with various types of Header Date of Run
    Given the file has the following details with Header date of run <dateOfRun>
      | CeasedCitizen |
      | 1             |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status <status>
    And I verify that the following error message appeared:
      | Message     | Count   |
      | <message>   | <count> |
    Examples:
      | dateOfRun   | message                                            | count | status                      |
      | blank       | Wrong header length                                | 1     | FILE_ERROR                  |
      | spaces      | Must be in yyyyMMdd date format.                   | 1     | RAW_DATA_ERROR              |
      | invalid     | Must be in yyyyMMdd date format.                   | 1     | RAW_DATA_ERROR              |
      | futureDate  | Extraction date cannot be after File Received date | 1     | BULK_CHECK_VALIDATION_ERROR |
      | 20010229    | Must be in yyyyMMdd date format.                   | 1     | RAW_DATA_ERROR              |
      | 199901 1    | Must be in yyyyMMdd date format.                   | 1     | RAW_DATA_ERROR              |
      | 20011332    | Must be in yyyyMMdd date format.                   | 1     | RAW_DATA_ERROR              |
      | 2001aBcD    | Must be in yyyyMMdd date format.                   | 1     | RAW_DATA_ERROR              |
      | 202010      | Wrong header length                                | 1     | FILE_ERROR                  |

  @set_5
  Scenario Outline: Mha send a ceased citizenship file with various types of nrics
    Given the mha ceased citizen file contains the following details with Header date of run valid
      | Nric        | Name        | Nationality   | Cessation Date   | 
      | <nric>      | valid       | valid         | valid            |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status <status>
    And I verify that the following error message appeared:
      | Message     | Count   |
      | <message>   | <count> |
    Examples:
      | nric        | message                                        | count | status         |
      | spaces      | NRIC cannot be null/blank                      | 1     | RAW_DATA_ERROR |
      | invalid     | Must be valid NRIC in format [S/T]1234567[A-Z] | 1     | RAW_DATA_ERROR |
      | S150163Z    | size must be between 9 and 9                   | 1     | RAW_DATA_ERROR |
      | S5550000B   | Must be valid NRIC in format [S/T]1234567[A-Z] | 1     | RAW_DATA_ERROR |
      | S8880001Z   | Must be valid NRIC in format [S/T]1234567[A-Z] | 1     | RAW_DATA_ERROR |
      | S1501508z   | Must be valid NRIC in format [S/T]1234567[A-Z] | 1     | RAW_DATA_ERROR |
      | s1501508Z   | Must be valid NRIC in format [S/T]1234567[A-Z] | 1     | RAW_DATA_ERROR |
      | s1501508z   | Must be valid NRIC in format [S/T]1234567[A-Z] | 1     | RAW_DATA_ERROR |

  @set_6 @defect @example-4-5
  Scenario Outline: Mha send a ceased citizenship file with various types of nationality
    Given the mha ceased citizen file contains the following details with Header date of run valid
      | Nric        | Name        | Nationality   | Cessation Date   |
      | valid       | valid       | <nationality> | valid            |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status <status>
    And I verify that the following error message appeared:
      | Message     | Count   |
      | <message>   | <count> |
    Examples:
      | nationality | message                                        | count | status         |
      | spaces      | must not be blank                              | 1     | RAW_DATA_ERROR |
      | S           | size must be between 2 and 2                   | 1     | RAW_DATA_ERROR |
      | invalid     | Nationality must not be SG country code        | 1     | RAW_DATA_ERROR |
      | 12          | <Error message>                                | 1     | RAW_DATA_ERROR |
      | !@          | <Error message>                                | 1     | RAW_DATA_ERROR |
    
  @set_7 @defect @example-8
  Scenario Outline: Mha send a ceased citizenship file with various types of cessation date
    Given the mha ceased citizen file contains the following details with Header date of run valid
      | Nric        | Name        | Nationality   | Cessation Date   |
      | valid       | valid       | valid         | <cessation date> |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status <status>
    And I verify that the following error message appeared:
      | Message     | Count   |
      | <message>   | <count> |
    Examples:
      | cessation date | message                                        | count | status                      |
      | invalid        | Invalid Citizen Renunciation Date.             | 1     | RAW_DATA_ERROR              |
      | futureDate     | Renunciation Date is after File Date of run.   | 1     | VALIDATED_TO_PREPARED_ERROR |
      | spaces         | Invalid Citizen Renunciation Date.             | 1     | RAW_DATA_ERROR              |
      | 00000000       | Invalid Citizen Renunciation Date.             | 1     | RAW_DATA_ERROR              |
      | 20020000       | Invalid Citizen Renunciation Date.             | 1     | RAW_DATA_ERROR              |
      | 20010229       | Invalid Citizen Renunciation Date.             | 1     | RAW_DATA_ERROR              |
      | 17990101       | Year value cannot be less than 1800            | 1     | RAW_DATA_ERROR              |
      | onDateOfRun    | <Error message>                                | 1     | RAW_DATA_ERROR              |

  @set_8
  Scenario: Mha send a ceased citizenship file with hybrid (both completely & partially) duplicate records
    Given the file has the following details with Header date of run valid
      | CeasedCitizen | NumberOfDuplication | NumberOfPartialDuplication |
      | 1             | 2                   | 2                          |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And I verify that the following error message appeared:
      | Message                            | Count |
      | Partially Duplicate Record found.  | 3     |

  @set_9
  Scenario Outline: Mha send a ceased citizenship file with various types of name
    Given the database populated with the following data:
      | SingaporeCitizen |
      | 1                |
    Given the mha ceased citizen file contains the following details with Header date of run valid
      | Nric        | Name          | Nationality   | Cessation Date   |
      | existing    | <name>        | valid         | valid            |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status CLEANUP
    And there are no error messages
    Examples:
      | name          |
      | valid         |
      | T3st m@cH!nE  |

  @set_10 @defect
  Scenario: John ceased citizenship on the same day he becomes Singapore Citizen
    Given john who is 50 years old attained his Singapore Citizenship 100 days ago
    And MHA sends a ceased citizenship file stating that john renounced his citizenship 100 days ago
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status CLEANUP
    And there are no error messages
    And john is a non singaporean since 100 days ago

  @set_1
  Scenario: John ceased citizenship after he becomes Singapore Citizen
    Given john who is 50 years old attained his Singapore Citizenship 100 days ago
    And MHA sends a ceased citizenship file stating that john renounced his citizenship 10 days ago
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status CLEANUP
    And there are no error messages
    And john is a non singaporean since 10 days ago

  @set_2
  Scenario: John ceased citizenship before he becomes Singapore Citizen
    Given john who is 50 years old attained his Singapore Citizenship 100 days ago
    And MHA sends a ceased citizenship file stating that john renounced his citizenship 101 days ago
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And I verify that the following error message appeared:
      | Message                                                      | Count |
      | Renunciation Date is not after Citizenship Attainment Date.  | 1     |

  @set_3 @defect @GRYFFINDOR-1276 @GRYFFINDOR-1289
  Scenario: Mha send a ceased citizenship file with completely duplicate records but nric not found in system
    Given the file has the following details with Header date of run valid
      | CeasedCitizen | NumberOfDuplication |
      | 1             | 2                   |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And I verify that the following error message appeared:
      | Message                             | Count |
      | Completely Duplicate Record found.  | 2     |
      | NRIC not found in System.           | 2     |

  @set_4
  Scenario: John ceased citizenship before he started becoming a dual citizen
    Given john who is 13 years old converted to a dual citizen 10 days ago
    And MHA sends a ceased citizenship file stating that john renounced his citizenship 10 days ago
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status CLEANUP
    And there are no error messages
    And john is a non singaporean since 10 days ago

  @set_5 @defect @GRYFFINDOR-1289
  Scenario: Mha send a ceased citizenship file with a combination of partially duplicate and valid records but all nrics not found in system
    Given the mha ceased citizen file contains the following details with Header date of run valid
      | Nric        | Name          | Nationality   | Cessation Date   |
      | S1501508Z   | valid         | CN            | valid            |
      | S1501508Z   | valid         | MY            | valid            |
      | valid       | valid         | valid         | valid            |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And I verify that the following error message appeared:
      | Message                             | Count |
      | Partially Duplicate Record found.   | 2     |
      | NRIC not found in System.           | 1     |

  @set_6
  Scenario Outline: John appears in consecutive ceased citizenship files with different cessation dates
    Given john who is 12 years old had his citizenship renounced <previous_cessation_date> days ago
    And MHA sends a ceased citizenship file stating that john renounced his citizenship <current_cessation_date> days ago
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And the error message contains Not SC or Dual Citizen currently
  Examples:
    | previous_cessation_date | current_cessation_date |
    | 5 | 5 |
    | 5 | 6 |
    | 6 | 5 |

  @set_7
  Scenario: Mha send a ceased citizenship file with no content (i.e totally empty file).
    Given the MHA_CEASED_CITIZEN file is empty
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status FILE_ERROR
    And I verify number of records in Incoming Record table is 0
    And I verify that the following error message appeared:
      | Message                                 | Count |
      | Must have at least 1 valid body record. | 1     |
      | Must have 1 Footer record.              | 1     |
