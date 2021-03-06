@datasource
@development
@bulk_citizen
@mha
@truncate
Feature: Data processing for MHA bulk citizen file

  @set_1 @GRYFFINDOR-912
  Scenario: Datasource service processes a MHA bulk citizen file
    Given the mha bulk file is being created
    Given the mha bulk file has the following details:
      | Singaporean,M,Name:Dao Sa Pia,Fin:F8100327X,DoD:DeathBeforeBirth,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag: ,Quantity:1 |
      | PermanentResident,DoB:19860901,Name:Roti John,F,DoD:DeathBeforeBirth,Overseas,BlkNo:212,StrtName:faea eae,FlrNo:12,BuildingName:123113afweaafawe aefa121,mhaAddrType:A,Quantity:1               |
      | DualCitizen,M,DoD:DeathBeforeBirth,NCAAddress,BlkNo:212,StrtCode:1h2y3u,LvlNo:131,UnitNo:123ka,Quantity:1                                                                                       |
      | Singaporean,F,Name:Bak Chor Mee,MHAAddress,PostalCode:123456,BuildingName:Shenton way,UnitNo:12,BlkNo:212,StrtName:Kfc street,FlrNo:12,ctzIssDate:        ,Quantity:1                           |
#    | RandomPeople,Quantity:100000                                                                                                                                                                          |
    And the mha bulk file is created
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
#    When the mha bulk file is ran
    And the Mha Bulk batch job completes running with status CLEANUP

  @set_2
  Scenario: Datasource service processes a MHA bulk citizen file
    Given the mha bulk file is being created
    Given the mha bulk file has the following details:
      | Singaporean,M,Name:Laksa Boy,Fin:F8100327X,ctzIssDate:20190325,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag: ,Quantity:1 |
      | PermanentResident,Nric:S3450033I,DoB:19860901,Name:Mee Siam,F,Overseas,BlkNo:212,StrtName:faea eae,FlrNo:12,BuildingName:123113afweaafawe aefa121,mhaAddrType:A,Quantity:1                    |
      | DualCitizen,Name:Wan Mo Peh,Nric:S0743815Z,M,NCAAddress,BlkNo:212,StrtCode:1h2y3u,LvlNo:131,UnitNo:123ka,Quantity:1                                                                           |
#    | RandomPeople,Quantity:100000                                                                                                                                                                          |
    And the mha bulk file is created
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
#    When the mha bulk file is ran
    And the Mha Bulk batch job completes running with status CLEANUP
    And I verify that person with F8100327X is persisted in Datasource
    And I verify that person with S3450033I is persisted in Datasource
    And I verify that person with S0743815Z is persisted in Datasource

  @set_3
  Scenario: Datasource service processes an empty MHA bulk citizen file
    Given the mha bulk file is being created with no records
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    And the Mha Bulk batch job completes running with status FILE_ERROR
    And the error message contains Must have at least 1 valid body record

  @set_4
  Scenario: Datasource service partial duplicated records
    Given the mha bulk file is being created
    Given the mha bulk file has the following details:
      | Singaporean,M,Name:Laksa Girl,Nric:S1412535C,ctzIssDate:20190325,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag:D,Quantity:1 |
      | Singaporean,M,Name:Laksa Boy,Nric:S1412535C,ctzIssDate:20190325,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag:D,Quantity:1  |
    And the mha bulk file is created
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    And the Mha Bulk batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then the error message contains Partially Duplicate Record found

  @set_5
  Scenario: Datasource service partial duplicated records and one valid record
    Given the mha bulk file is being created
    Given the mha bulk file has the following details:
      | Singaporean,M,Name:Laksa Girl,Nric:S1412535C,ctzIssDate:20190325,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag:D,Quantity:1 |
      | Singaporean,M,Name:Laksa Boy,Nric:S1412535C,ctzIssDate:20190325,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag:D,Quantity:1  |
      | PermanentResident,DoB:19860901,Name:Roti John,F,DoD:DeathBeforeBirth,Overseas,BlkNo:212,StrtName:faea eae,FlrNo:12,BuildingName:123113afweaafawe aefa121,mhaAddrType:A,Quantity:1               |
    And the mha bulk file is created
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    And the Mha Bulk batch job completes running with status CLEANUP
    Then the error message contains Partially Duplicate Record found

  @set_6
  Scenario: Datasource service partial duplicated records
    Given the mha bulk file is being created
    Given the mha bulk file has the following details:
      | Singaporean,M,Name:Laksa Boy,Nric:S1412535C,ctzIssDate:20190325,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag:D,Quantity:1 |
      | Singaporean,M,Name:Laksa Boy,Nric:S1412535C,ctzIssDate:20190325,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag:D,Quantity:1 |
    And the mha bulk file is created
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    And the Mha Bulk batch job completes running with status CLEANUP
    Then the error message contains Completely Duplicate Record found

  @set_7
  Scenario Outline: Datasource should not process file(s) with either invalid DateOfRun or CutOffDate
    Given the mha bulk file is created with DateOfRun <DateOfRun> and CutOffDate <CutOffDate> with one record
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    Then the Mha Bulk batch job completes running with status <BatchStatus>
    And the error message contains <ErrorMessage>

    Examples:
      | DateOfRun       | CutOffDate      | BatchStatus                 | ErrorMessage                                                                                        |
      | EMPTY           | CurrentDate     | FILE_ERROR                  | Wrong header length.                                                                                |
      | CurrentDate     | EMPTY           | FILE_ERROR                  | Wrong header length.                                                                                |
      | SPACE           | CurrentDate     | RAW_DATA_ERROR              | size must be between 8 and 8, Must be in yyyyMMdd date format                                       |
      | CurrentDate     | SPACE           | RAW_DATA_ERROR              | size must be between 8 and 8, Must be in yyyyMMdd date format                                       |
      | CurrentDate - 1 | CurrentDate     | RAW_DATA_ERROR              | Extraction Date must be equal/after Cut-off Date                                                    |
      | CurrentDate     | CurrentDate + 1 | RAW_DATA_ERROR              | Extraction Date must be equal/after Cut-off Date                                                    |
      | CurrentDate + 1 | CurrentDate     | BULK_CHECK_VALIDATION_ERROR | Extraction date cannot be after File Received date                                                  |
      | CurrentDate + 1 | CurrentDate + 1 | BULK_CHECK_VALIDATION_ERROR | Extraction date cannot be after File Received date, Cut-off date cannot be after File Received date |

  @set_8
  Scenario Outline: Datasource should not process files with invalid NRIC records
    Given MHA send MHA_BULK_CITIZEN file with the following data:
      | NRIC   | FIN   | NAME   | DOB   | DOD   | GENDER   | ADDR_IND   | ADDR_TYPE   | ADDR   | INVALID_ADDR_TAG   | CTZ_ATT_DATE   |
      | <NRIC> | <FIN> | <NAME> | <DOB> | <DOD> | <GENDER> | <ADDR_IND> | <ADDR_TYPE> | <ADDR> | <INVALID_ADDR_TAG> | <CTZ_ATT_DATE> |
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    Then the Mha Bulk Citizen batch job completes running with status <BATCHSTATUS>
    And the error message contains <ERROR_MESSAGE>

    Examples:
      | NRIC      | FIN | NAME   | DOB      | DOD | GENDER | ADDR_IND | ADDR_TYPE | ADDR   | INVALID_ADDR_TAG | CTZ_ATT_DATE | BATCHSTATUS                 | ERROR_MESSAGE                                           |
      | -         | -   | <AUTO> | 19881003 | -   | M      | C        | C         | <AUTO> | -                | 19881003     | RAW_DATA_ERROR              | NRIC cannot be null/blank, size must be between 9 and 9 |
      | S1234567A | -   | <AUTO> | 19881003 | -   | M      | C        | C         | <AUTO> | -                | 19881003     | BULK_CHECK_VALIDATION_ERROR | Invalid NRIC                                            |
      | T494552B  | -   | <AUTO> | 19881003 | -   | M      | C        | C         | <AUTO> | -                | 19881003     | RAW_DATA_ERROR              | size must be between 9 and 9                            |
      | t4945521B | -   | <AUTO> | 19881003 | -   | M      | C        | C         | <AUTO> | -                | 19881003     | BULK_CHECK_VALIDATION_ERROR | Invalid NRIC                                            |
      | s4945521B | -   | <AUTO> | 19881003 | -   | M      | C        | C         | <AUTO> | -                | 19881003     | BULK_CHECK_VALIDATION_ERROR | Invalid NRIC                                            |
      | S5550000B | -   | <AUTO> | 19881003 | -   | M      | C        | C         | <AUTO> | -                | 19881003     | BULK_CHECK_VALIDATION_ERROR | Invalid NRIC                                            |
      | S8880001Z | -   | <AUTO> | 19881003 | -   | M      | C        | C         | <AUTO> | -                | 19881003     | BULK_CHECK_VALIDATION_ERROR | Invalid NRIC                                            |

  @set_9
  Scenario: Datasource should not process record(s) with same FIN
    Given MHA send MHA_BULK_CITIZEN file with the following data:
      | NRIC      | FIN       | NAME  | DOB      | DOD | GENDER | ADDR_IND | ADDR_TYPE | ADDR           | INVALID_ADDR_TAG | CTZ_ATT_DATE |
      | T7754288J | G2020975K | Marry | 20010719 | -   | F      | C        | C         | Wonderland 456 | -                | 20010719     |
      | T8741641G | G2020975K | Marry | 20010719 | -   | F      | C        | C         | Wonderland 456 | -                | 20010719     |
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    Then the Mha Bulk Citizen batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    And the error message contains Partially Duplicate Record found

  @set_10
  Scenario Outline: Datasource should not process record(s) with invalid FIN format
    Given MHA send MHA_BULK_CITIZEN file with the following data:
      | NRIC   | FIN   | NAME   | DOB   | DOD   | GENDER   | ADDR_IND   | ADDR_TYPE   | ADDR   | INVALID_ADDR_TAG   | CTZ_ATT_DATE   |
      | <NRIC> | <FIN> | <NAME> | <DOB> | <DOD> | <GENDER> | <ADDR_IND> | <ADDR_TYPE> | <ADDR> | <INVALID_ADDR_TAG> | <CTZ_ATT_DATE> |
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    Then the Mha Bulk Citizen batch job completes running with status <BATCH_STATUS>
    And the error message contains <ERROR_MESSAGE>
    Examples:
      | NRIC      | FIN       | NAME   | DOB      | DOD | GENDER | ADDR_IND | ADDR_TYPE | ADDR           | INVALID_ADDR_TAG | CTZ_ATT_DATE | BATCH_STATUS                | ERROR_MESSAGE |
      | T7754288J | F4132581X | Janice | 20010719 | -   | F      | C        | C         | Wonderland 456 | -                | 20010719     | BULK_CHECK_VALIDATION_ERROR | Invalid FIN   |
      | T7754288J | F413258W  | Janice | 20010719 | -   | F      | C        | C         | Wonderland 456 | -                | 20010719     | BULK_CHECK_VALIDATION_ERROR | Invalid FIN   |
      | T7754288J | E4132581W | Janice | 20010719 | -   | F      | C        | C         | Wonderland 456 | -                | 20010719     | BULK_CHECK_VALIDATION_ERROR | Invalid FIN   |
      | T7754288J | H4132581W | Janice | 20010719 | -   | F      | C        | C         | Wonderland 456 | -                | 20010719     | BULK_CHECK_VALIDATION_ERROR | Invalid FIN   |

  @set_1
  Scenario: Datasource should not process record(s) with empty name
    Given MHA send MHA_BULK_CITIZEN file with the following data:
      | NRIC      | FIN       | NAME | DOB      | DOD | GENDER | ADDR_IND | ADDR_TYPE | ADDR           | INVALID_ADDR_TAG | CTZ_ATT_DATE |
      | S4764841F | G4503131X | -    | 19990219 | -   | M      | C        | C         | Wonderland 456 | -                | -            |
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    Then the Mha Bulk Citizen batch job completes running with status RAW_DATA_ERROR
    And the error message contains Name field cannot be empty

  @set_2 @wip
  Scenario Outline: Datasource should not process record(s) with invalid DateOfBirth
    Given MHA send MHA_BULK_CITIZEN file with the following data:
      | NRIC   | FIN   | NAME   | DOB   | DOD   | GENDER   | ADDR_IND   | ADDR_TYPE   | ADDR   | INVALID_ADDR_TAG   | CTZ_ATT_DATE   |
      | <NRIC> | <FIN> | <NAME> | <DOB> | <DOD> | <GENDER> | <ADDR_IND> | <ADDR_TYPE> | <ADDR> | <INVALID_ADDR_TAG> | <CTZ_ATT_DATE> |
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    Then the Mha Bulk Citizen batch job completes running with status <BATCH_STATUS>
    And the error message contains <ERROR_MESSAGE>
    Examples:
      | NRIC      | FIN       | NAME | DOB      | DOD | GENDER | ADDR_IND | ADDR_TYPE | ADDR           | INVALID_ADDR_TAG | CTZ_ATT_DATE | BATCH_STATUS   | ERROR_MESSAGE                         |
      | S4764841F | G4503131X | John | 00000000 | -   | F      | C        | C         | Wonderland 456 | -                | -            | RAW_DATA_ERROR | Invalid Date of Birth                 |
      | S4764841F | G4503131X | John | -        | -   | F      | C        | C         | Wonderland 456 | -                | -            | RAW_DATA_ERROR | Date of Birth must be in valid format |
      | S4764841F | G4503131X | John | 19991301 | -   | F      | C        | C         | Wonderland 456 | -                | -            | RAW_DATA_ERROR | Date of Birth must be in valid format |
      | S4764841F | G4503131X | John | 19991232 | -   | F      | C        | C         | Wonderland 456 | -                | -            | RAW_DATA_ERROR | Date of Birth must be in valid format |
      | S4764841F | G4503131X | John | 20010229 | -   | F      | C        | C         | Wonderland 456 | -                | -            | RAW_DATA_ERROR | Date of Birth must be in valid format |
      | S4764841F | G4503131X | John | 20080101 | -   | F      | C        | C         | Wonderland 456 | -                | -            | RAW_DATA_ERROR | Age must be 12 or older               |
      | S4764841F | G4503131X | John | 00001001 | -   | F      | C        | C         | Wonderland 456 | -                | -            | RAW_DATA_ERROR | Date of Birth must be in valid format |
      | S4764841F | G4503131X | John | 17990101 | -   | F      | C        | C         | Wonderland 456 | -                | -            | RAW_DATA_ERROR | Date of Birth must be in valid format |

  @set_3 @defect @GRYFFINDOR-1290
  Scenario Outline: Datasource should not process record(s) with invalid DateOfDeath
    Given MHA send MHA_BULK_CITIZEN file with the following data:
      | NRIC   | FIN   | NAME   | DOB   | DOD   | GENDER   | ADDR_IND   | ADDR_TYPE   | ADDR   | INVALID_ADDR_TAG   | CTZ_ATT_DATE   |
      | <NRIC> | <FIN> | <NAME> | <DOB> | <DOD> | <GENDER> | <ADDR_IND> | <ADDR_TYPE> | <ADDR> | <INVALID_ADDR_TAG> | <CTZ_ATT_DATE> |
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    Then the Mha Bulk Citizen batch job completes running with status <BATCH_STATUS>
    And the error message contains <ERROR_MESSAGE>
    Examples:
      | NRIC      | FIN       | NAME | DOB      | DOD      | GENDER | ADDR_IND | ADDR_TYPE | ADDR           | INVALID_ADDR_TAG | CTZ_ATT_DATE | BATCH_STATUS   | ERROR_MESSAGE                   |
      | S4764841F | G4503131X | John | 19990516 | 00001010 | M      | C        | C         | Wonderland 456 | -                | -            | RAW_DATA_ERROR | Date of death format is invalid |
      | S4764841F | G4503131X | John | 19990516 | 10102008 | M      | C        | C         | Wonderland 456 | -                | -            | RAW_DATA_ERROR | Date of death format is invalid |
      | S4764841F | G4503131X | John | 19990516 | 00000000 | M      | C        | C         | Wonderland 456 | -                | -            | RAW_DATA_ERROR | Date of death format is invalid |
      | S4764841F | G4503131X | John | 19990516 | 19990516 | M      | C        | C         | Wonderland 456 | -                | -            | RAW_DATA_ERROR | Date of death format is invalid |
      | S4764841F | G4503131X | John | 19990516 | 19901231 | M      | C        | C         | Wonderland 456 | -                | -            | RAW_DATA_ERROR | Date of death format is invalid |

  @set_4
  Scenario Outline: Datasource should not process record(s) with invalid gender
    Given MHA send MHA_BULK_CITIZEN file with the following data:
      | NRIC   | FIN   | NAME   | DOB   | DOD   | GENDER   | ADDR_IND   | ADDR_TYPE   | ADDR   | INVALID_ADDR_TAG   | CTZ_ATT_DATE   |
      | <NRIC> | <FIN> | <NAME> | <DOB> | <DOD> | <GENDER> | <ADDR_IND> | <ADDR_TYPE> | <ADDR> | <INVALID_ADDR_TAG> | <CTZ_ATT_DATE> |
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    Then the Mha Bulk Citizen batch job completes running with status <BATCH_STATUS>
    And the error message contains <ERROR_MESSAGE>
    Examples:
      | NRIC      | FIN       | NAME | DOB      | DOD | GENDER | ADDR_IND | ADDR_TYPE | ADDR           | INVALID_ADDR_TAG | CTZ_ATT_DATE | BATCH_STATUS   | ERROR_MESSAGE                |
      | S4764841F | G4503131X | John | 19990516 | -   | A      | C        | C         | Wonderland 456 | -                | -            | RAW_DATA_ERROR | Gender can only be M, F or U |
      | S4764841F | G4503131X | John | 19990516 | -   | -      | C        | C         | Wonderland 456 | -                | -            | RAW_DATA_ERROR | Gender field cannot be empty |

  @wip
  Scenario: Datasource should not process record(s) with invalid oversea address

  @wip
  Scenario: Datasource should not process record(s) with invalid address tag

  @set_5 @wip @defect @GRYFFINDOR-1292 @GRYFFINDOR-1293
  Scenario Outline: Datasource should not process record(s) with invalid citizen attainment date
    Given MHA send MHA_BULK_CITIZEN file with the following data:
      | NRIC   | FIN   | NAME   | DOB   | DOD   | GENDER   | ADDR_IND   | ADDR_TYPE   | ADDR   | INVALID_ADDR_TAG   | CTZ_ATT_DATE   |
      | <NRIC> | <FIN> | <NAME> | <DOB> | <DOD> | <GENDER> | <ADDR_IND> | <ADDR_TYPE> | <ADDR> | <INVALID_ADDR_TAG> | <CTZ_ATT_DATE> |
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    Then the Mha Bulk Citizen batch job completes running with status <BATCH_STATUS>
    And the error message contains <ERROR_MESSAGE>
    Examples:
      | NRIC      | FIN       | NAME | DOB      | DOD      | GENDER | ADDR_IND | ADDR_TYPE | ADDR           | INVALID_ADDR_TAG | CTZ_ATT_DATE | BATCH_STATUS                | ERROR_MESSAGE                                                                                        |
      | S4764841F | G4503131X | John | 19990516 | -        | U      | C        | C         | Wonderland 456 | -                | 00000000     | RAW_DATA_ERROR              | Must be in yyyyMMdd date format                                                                      |
      | S4764841F | G4503131X | John | 19990516 | -        | U      | C        | C         | Wonderland 456 | -                | 00001015     | RAW_DATA_ERROR              | Must be in yyyyMMdd date format                                                                      |
      | S4764841F | G4503131X | John | 19990516 | -        | U      | C        | C         | Wonderland 456 | -                | 99991015     | BULK_CHECK_VALIDATION_ERROR | Citizenship attainment date must be equal/before Cut-off date OR Year value cannot be less than 1800 |
      | S4764841F | G4503131X | John | 19990516 | -        | U      | C        | C         | Wonderland 456 | -                | 19901015     | RAW_DATA_ERROR              | Citizenship attainment date must be after date of birth                                              |
      | S4764841F | G4503131X | John | 19990516 | -        | U      | C        | C         | Wonderland 456 | -                | 17991015     | BULK_CHECK_VALIDATION_ERROR | Citizenship attainment date must be equal/before Cut-off date OR Year value cannot be less than 1800 |
      | S4764841F | G4503131X | John | 19990516 | 20050406 | U      | C        | C         | Wonderland 456 | -                | 20050406     | RAW_DATA_ERROR              | Must be in yyyyMMdd date format                                                                      |
      | S4764841F | G4503131X | John | 19990516 | 20050406 | U      | C        | C         | Wonderland 456 | -                | 20050425     | RAW_DATA_ERROR              | Must be in yyyyMMdd date format                                                                      |
      | S4764841F | G4503131X | John | 19990516 | 20050406 | U      | C        | C         | Wonderland 456 | -                | -            | RAW_DATA_ERROR              | Must be in yyyyMMdd date format                                                                      |