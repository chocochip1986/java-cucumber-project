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
#      | CurrentDate     | 20191231        | CLEANUP                     |                                                                                                     |

  Scenario: Datasource should not process files(s) with invalid NRIC records
    Given MHA send MHA_BULK_CITIZEN file with the following data:
      | NRIC      | FIN | NAME   | DOB      | DOD      | GENDER | ADDR_IND | ADDR_TYPE | ADDR   | INVALID_ADDR_TAG | CTZ_ATT_DATE |
      | EMPTY     | -   | <AUTO> | 19881003 | 00000000 | M      | C        | C         | <AUTO> | -                | 19881003     |
      | SPACE     | -   | <AUTO> | 19881003 | 00000000 | M      | C        | C         | <AUTO> | -                | 19881003     |
      | S1234567A | -   | <AUTO> | 19881003 | 00000000 | M      | C        | C         | <AUTO> | -                | 19881003     |
      | T494552B  | -   | <AUTO> | 19881003 | 00000000 | M      | C        | C         | <AUTO> | -                | 19881003     |
      | t4945521B | -   | <AUTO> | 19881003 | 00000000 | M      | C        | C         | <AUTO> | -                | 19881003     |
      | S4945521B | -   | <AUTO> | 19881003 | 00000000 | M      | C        | C         | <AUTO> | -                | 19881003     |
      | S5550000B | -   | <AUTO> | 19881003 | 00000000 | M      | C        | C         | <AUTO> | -                | 19881003     |
      | S8880001Z | -   | <AUTO> | 19881003 | 00000000 | M      | C        | C         | <AUTO> | -                | 19881003     |
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    Then the Mha Bulk Citizen batch job completes running with status ERROR_RATE_ERROR
    And I verify that the following error message appeared:
      | Message                                             | Count |
      | Extraction date cannot be after File Received date. | 1     |
      | Cut-off date cannot be after File Received date.    | 1     |

  Scenario: Datasource should not process record(s) with same FIN

  Scenario: Datasource should not process record(s) with invalid FIN

  Scenario: Datsource should not process record(s) with invalid DateOfBirth

  Scenario: Datasource should not process record(s) with invalid DateOfDeath

  Scenario: Datasource should not process record(s) with invalid oversea address

  Scenario: Datasource should not process record(s) with invalid address tag

  Scenario: Datasource should not process record(s) with invalid citizen attainment date