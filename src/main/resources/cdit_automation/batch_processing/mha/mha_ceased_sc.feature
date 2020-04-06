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

  @set_2 @GRYFFINDOR-908 @defect
  Scenario: Mha send a ceased citizenship file with a record that is already exist in system
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

  @set_4 @GRYFFINDOR-908 @defect
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
      | Message                           | Count |
      | Not SC or Dual Citizen currently. | 2     |
      | Duplicate NRIC found in file.     | 2     |

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
      | Renunciation Date is after File Date of run. | 2     |

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

  @set_9 @GRYFFINDOR-908 @defect
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
    And the error message contains <message>
    Examples:
      | dateOfRun   | message                                            | status                      |
      | blank       | Wrong header length                                | FILE_ERROR                  |
      | spaces      | Must be in yyyyMMdd date format.                   | RAW_DATA_ERROR              |
      | 12312020    | Must be in yyyyMMdd date format.                   | RAW_DATA_ERROR              |
      | futureDate  | Extraction date cannot be after File Received date | BULK_CHECK_VALIDATION_ERROR |
      | 20010229    | Must be in yyyyMMdd date format.                   | RAW_DATA_ERROR              |
      | 199901 1    | Must be in yyyyMMdd date format.                   | RAW_DATA_ERROR              |
      | 20011332    | Must be in yyyyMMdd date format.                   | RAW_DATA_ERROR              |
      | 2001aBcD    | Must be in yyyyMMdd date format.                   | RAW_DATA_ERROR              |
      | 202010      | Wrong header length                                | FILE_ERROR                  |

#  @set_5 @wip
#  Scenario Outline: Mha send a ceased citizenship file with various types of nrics
#    Given the mha ceased citizen file contains the following details with Header date of run valid
#      | Nric      | Name              | Nationality | Renunciation Date |
#      | <nric>    |                   |             |                   |
#    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
#    Then the Mha Ceased Citizen batch job completes running with status <status>
#    And the error message contains <message>
#    Examples:
#      | nric        | message                                            | status                      |
#      | blank       | Must be in yyyyMMdd date format.                   | RAW_DATA_ERROR              |
#      | invalid     | Must be in yyyyMMdd date format.                   | RAW_DATA_ERROR              |
#      | S150163Z    | Extraction date cannot be after File Received date | BULK_CHECK_VALIDATION_ERROR |
#      | S5550000B   | Must be in yyyyMMdd date format.                   | RAW_DATA_ERROR              |
#      | S8880001Z   | Must be in yyyyMMdd date format.                   | RAW_DATA_ERROR              |
      
  @set_4 @wip
  Scenario: John ceased citizenship before he started becoming a dual citizen
    Given john who is 13 years old converted to a dual citizen 10 days ago
    And MHA sends a ceased citizenship file stating that john renounced his citizenship 10 days ago
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status CLEANUP
    And there are no error messages
    And john is a non singaporean since 10 days ago