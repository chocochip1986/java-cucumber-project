@development
@ceased-citizen
@mha
Feature: Data processing for Mha ceased citizenship

  @set_1
  Scenario: Mha send a ceased citizenship file with a person detail record not found in system
    Given the file has the following details:
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
    And the file has the following details:
      | RepeatedCeasedCitizen |
      | 1                     |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And I verify that the following error message appeared:
      | Message                           | Count |
      | Not SC or Dual Citizen currently. | 1     |

  @set_3 @GRYFFINDOR-908 @defect
  Scenario: Mha send a ceased citizenship file with duplicate record
    Given the database populated with the following data:
      | SingaporeCitizen | DualCitizen |
      | 1                | 0           |
    And the file has the following details:
      | CeasedCitizen | NumberOfDuplication |
      | 1             | 3                   |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And I verify that the following error message appeared:
      | Message                       | Count |
      | Duplicate NRIC found in file. | 3     |

  @set_4 @GRYFFINDOR-908 @defect
  Scenario: Mha send a ceased citizenship file with a record existing in the system and also duplicated in the file
    Given the database populated with the following data:
      | SingaporeCitizen | CeasedCitizen |
      | 1                | 1             |
    And the file has the following details:
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
    Given the file has the following details:
      | EmptyName |
      | 1         |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status RAW_DATA_ERROR
    And I verify that the following error message appeared:
      | Message       | Count |
      | Invalid Name. | 1     |

  @set_6
  Scenario: Mha send a ceased citizenship file with an renunciation date that is after cut off date
    Given the file has the following details:
      | RenunciationDateAfterCutOff |
      | 2                           |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status VALIDATED_TO_PREPARED_ERROR
    And I verify that the following error message appeared:
      | Message                                       | Count |
      | Renunciation Date is after File Cut-off Date. | 2     |

  @set_7
  Scenario: Mha send a ceased citizenship file for processing
    Given the database populated with the following data:
      | SingaporeCitizen | DualCitizen |
      | 1                | 1           |
    And the file has the following details:
      | CeasedCitizen |
      | 2             |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status CLEANUP
    And I verify the the people listed in the file have nationality of NON_SINGAPORE_CITIZEN
    And I verify the previous nationality valid till timestamp is the renunciation date at 2359HR
    And I verify the supersede nationality valid from timestamp is the day after renunciation date
    And I verify the previous person detail valid till timestamp is the renunciation date at 2359HR
    And I verify the supersede person detail valid from timestamp is the day after renunciation date

  @set_8
  Scenario: Mha send a ceased citizenship file with nationality of [SG]

  This is a special case that will never happen because this file is for renunciation of
  singapore citizenship, not award singapore citizenship. Therefore, datasource will
  ignore/skip if come across it.

    Given the database populated with the following data:
      | SingaporeCitizen |
      | 1                |
    And the file has the following details:
      | AwardedSingaporeCitizen |
      | 1                       |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status RAW_DATA_ERROR

  @set_9 @GRYFFINDOR-908 @defect
  Scenario: Mha send a empty ceased citizenship file
    Given the file has the following details:
      | CeasedCitizen |
      | 0             |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status CLEANUP

  @set_10
  Scenario: Mha sends a ceased citizenship with an SG country code
    Given the file has the following details:
      | CeasedCitizenWithSGCountryCode |
      | 1                              |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status RAW_DATA_ERROR
    And I verify that the following error message appeared:
      | Message                                 | Count |
      | Nationality must not be SG country code | 1     |

  @set_10
  Scenario: Mha sends a ceased citizenship file containing a person who is already non Singaporean
    Given the file has the following details:
      | CeasedCitizenWhoIsNonSG |
      | 1                       |
    When MHA sends the MHA_CEASED_CITIZEN file to Datasource sftp for processing
    Then the Mha Ceased Citizen batch job completes running with status RAW_DATA_ERROR
    And I verify that the following error message appeared:
      | Message                                 | Count |
      | Not SC or Dual Citizen currently.       | 1     |