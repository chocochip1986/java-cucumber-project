@development
@bulk_citizen
@mha
Feature: Data processing for MHA bulk citizen file

  @set_1 @GRYFFINDOR-912
  Scenario: Datasource service processes a MHA bulk citizen file
    Given the mha bulk file is being created
    Given the mha bulk file has the following details:
    | Singaporean,M,Name:Dao Sa Pia,Fin:F8100327X,DoD:DeathBeforeBirth,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag: ,Quantity:1       |
    | PermanentResident,DoB:19860901,Name:Roti John,F,DoD:DeathBeforeBirth,Overseas,BlkNo:212,StrtName:faea eae,FlrNo:12,BuildingName:123113afweaafawe aefa121,mhaAddrType:A,Quantity:1                     |
    | DualCitizen,M,DoD:DeathBeforeBirth,NCAAddress,BlkNo:212,StrtCode:1h2y3u,LvlNo:131,UnitNo:123ka,Quantity:1                                                                                             |
    | Singaporean,F,Name:Bak Chor Mee,MHAAddress,PostalCode:123456,BuildingName:Shenton way,UnitNo:12,BlkNo:212,StrtName:Kfc street,FlrNo:12,ctzIssDate:        ,Quantity:1                                                     |
#    | RandomPeople,Quantity:100000                                                                                                                                                                          |
    And the mha bulk file is created
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
#    When the mha bulk file is ran
    And the Mha Bulk batch job completes running with status CLEANUP

  @set_2
  Scenario: Datasource service processes a MHA bulk citizen file
    Given the mha bulk file is being created
    Given the mha bulk file has the following details:
      | Singaporean,M,Name:Laksa Boy,Fin:F8100327X,ctzIssDate:20190325,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag: ,Quantity:1       |
      | PermanentResident,Nric:S3450033I,DoB:19860901,Name:Mee Siam,F,Overseas,BlkNo:212,StrtName:faea eae,FlrNo:12,BuildingName:123113afweaafawe aefa121,mhaAddrType:A,Quantity:1      |
      | DualCitizen,Name:Wan Mo Peh,Nric:S0743815Z,M,NCAAddress,BlkNo:212,StrtCode:1h2y3u,LvlNo:131,UnitNo:123ka,Quantity:1                                                             |
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
      | Singaporean,M,Name:Laksa Girl,Nric:S1412535C,ctzIssDate:20190325,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag:D,Quantity:1       |
      | Singaporean,M,Name:Laksa Boy,Nric:S1412535C,ctzIssDate:20190325,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag:D,Quantity:1       |
    And the mha bulk file is created
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    And the Mha Bulk batch job completes running with status BULK_CHECK_VALIDATION_ERROR
    Then the error message contains Partially Duplicate Record found

  @set_5
  Scenario: Datasource service partial duplicated records and one valid record
    Given the mha bulk file is being created
    Given the mha bulk file has the following details:
      | Singaporean,M,Name:Laksa Girl,Nric:S1412535C,ctzIssDate:20190325,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag:D,Quantity:1       |
      | Singaporean,M,Name:Laksa Boy,Nric:S1412535C,ctzIssDate:20190325,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag:D,Quantity:1       |
      | PermanentResident,DoB:19860901,Name:Roti John,F,DoD:DeathBeforeBirth,Overseas,BlkNo:212,StrtName:faea eae,FlrNo:12,BuildingName:123113afweaafawe aefa121,mhaAddrType:A,Quantity:1                     |
    And the mha bulk file is created
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    And the Mha Bulk batch job completes running with status CLEANUP
    Then the error message contains Partially Duplicate Record found

  @set_6
  Scenario: Datasource service partial duplicated records
    Given the mha bulk file is being created
    Given the mha bulk file has the following details:
      | Singaporean,M,Name:Laksa Boy,Nric:S1412535C,ctzIssDate:20190325,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag:D,Quantity:1       |
      | Singaporean,M,Name:Laksa Boy,Nric:S1412535C,ctzIssDate:20190325,MHAAddress,BuildingName:123113afweaafawe aefa121,UnitNo:12,BlkNo:212,StrtName:faea eae,FlrNo:12,InvalidAddressTag:D,Quantity:1       |
    And the mha bulk file is created
    When MHA sends the MHA_BULK_CITIZEN file to Datasource sftp for processing
    And the Mha Bulk batch job completes running with status CLEANUP
    Then the error message contains Completely Duplicate Record found
